package com.pgii.view.viewcontroller;

import com.pgii.Main;
import com.pgii.controller.AsientoController;
import com.pgii.controller.CompraController;
import com.pgii.controller.UsuarioController;
import com.pgii.model.entities.Compra;
import com.pgii.model.entities.Entrada;
import com.pgii.model.entities.Evento;
import com.pgii.model.entities.Usuario;
import com.pgii.model.enums.EstadoCompra;
import com.pgii.model.interfaces.ICommand;
import com.pgii.model.interfaces.IObserver;
import com.pgii.model.patterns.behavioral.CancelarCompraCommand;
import com.pgii.model.patterns.behavioral.NotificacionObserver;
import com.pgii.model.patterns.creational.EntradaPrototype;
import com.pgii.view.AppState;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.FileWriter;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

public class MisComprasViewController {

    private Main app;
    private CompraController  compraCtrl;
    private UsuarioController usuarioCtrl;
    private ObservableList<Compra> datosTabla;
    private FilteredList<Compra>   filteredList;

    private static final NumberFormat NF = NumberFormat.getInstance(new Locale("es", "CO"));

    @FXML private Label  lblUsuario;
    @FXML private Label  lblMensaje;
    @FXML private Button btnCancelar;
    @FXML private Button btnReembolsar;

    // Filtros RF-010
    @FXML private TextField        txtFiltroEvento;
    @FXML private ComboBox<String> cbFiltroEstado;
    @FXML private TextField        txtFiltroFecha;

    @FXML private TableView<Compra>          tablaCompras;
    @FXML private TableColumn<Compra,String> colId, colEvento, colFecha, colTotal, colEstado, colEntradas;

    @FXML
    public void initialize() {
        compraCtrl  = new CompraController();
        usuarioCtrl = new UsuarioController();
        datosTabla  = FXCollections.observableArrayList();
        filteredList = new FilteredList<>(datosTabla, p -> true);
        SortedList<Compra> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tablaCompras.comparatorProperty());

        AppState state = AppState.getInstancia();
        if (state.haySesion())
            lblUsuario.setText("Historial de compras de: " + state.getUsuarioActual().getNombre());

        colId.setCellValueFactory(      c -> new SimpleStringProperty(String.valueOf(c.getValue().getIdCompra())));
        colEvento.setCellValueFactory(  c -> {
            var ev = c.getValue().getTheEvento();
            return new SimpleStringProperty(ev != null ? ev.getNombre() : "—");
        });
        colFecha.setCellValueFactory(   c -> new SimpleStringProperty(c.getValue().getFechaCreacion()));
        colTotal.setCellValueFactory(   c -> new SimpleStringProperty(
                "$" + NF.format((long) c.getValue().getTotal())));
        colEstado.setCellValueFactory(  c -> {
            var est = c.getValue().getEstadoActual();
            return new SimpleStringProperty(est != null ? est.name() : "—");
        });
        colEntradas.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getListaEntradas().size() + " entrada(s)"));

        tablaCompras.setItems(sortedList);
        cbFiltroEstado.setItems(FXCollections.observableArrayList(
            "Todos", "CREADA", "PAGADA", "CONFIRMADA", "CANCELADA", "REEMBOLSADA"));
        cbFiltroEstado.setValue("Todos");
        tablaCompras.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldV, newV) -> { actualizarBotones(newV); lblMensaje.setText(""); }
        );

        btnCancelar.setDisable(true);
        btnReembolsar.setDisable(true);
        cargarCompras();
    }

    private void cargarCompras() {
        datosTabla.clear();
        AppState state = AppState.getInstancia();
        if (!state.haySesion()) return;
        List<Compra> compras = compraCtrl.listarComprasPorUsuario(
                state.getUsuarioActual().getIdUsuario());
        datosTabla.addAll(compras);
    }

    private void actualizarBotones(Compra compra) {
        if (compra == null) { btnCancelar.setDisable(true); btnReembolsar.setDisable(true); return; }
        EstadoCompra estado = compra.getEstadoActual();
        boolean tieneSeguro = compra.getListaServicios().contains(com.pgii.model.enums.TipoServicio.SEGURO);
        boolean pagada = estado == EstadoCompra.PAGADA || estado == EstadoCompra.CONFIRMADA;
        btnCancelar.setDisable(estado != EstadoCompra.CREADA && !(pagada && !tieneSeguro));
        btnReembolsar.setDisable(!pagada || !tieneSeguro);
    }

    @FXML
    private void onCancelar() {
        Compra sel = tablaCompras.getSelectionModel().getSelectedItem();
        if (sel == null) return;
        AsientoController asientoCtrl = new AsientoController();
        for (Entrada entrada : sel.getListaEntradas())
            if (entrada.getTheAsiento() != null)
                asientoCtrl.liberarAsiento(entrada.getTheAsiento().getIdAsiento());
        ICommand cmd = new CancelarCompraCommand(sel);
        cmd.ejecutar();
        if (sel.getEstadoActual() != EstadoCompra.CANCELADA) {
            mostrarMensaje("No se puede cancelar en estado " + sel.getEstadoActual() + ".", false);
            cargarCompras();
            return;
        }
        mostrarMensaje("Compra #" + sel.getIdCompra() + " cancelada. El valor no es reembolsable.", true);
        cargarCompras();
        tablaCompras.getSelectionModel().clearSelection();
    }

    @FXML
    private void onReembolsar() {
        Compra sel = tablaCompras.getSelectionModel().getSelectedItem();
        if (sel == null) return;
        if (!sel.getListaServicios().contains(com.pgii.model.enums.TipoServicio.SEGURO)) {
            mostrarMensaje("Solo se puede reembolsar si la compra incluye el Seguro de Reembolso.", false);
            return;
        }
        boolean ok = compraCtrl.reembolsarCompra(sel.getIdCompra());
        if (ok) {
            // Observer: notificar al usuario mediante ventana emergente
            Evento evReembolso = sel.getTheEvento();
            if (evReembolso != null && AppState.getInstancia().haySesion()) {
                String nombreUsuario = AppState.getInstancia().getUsuarioActual().getNombre();
                boolean yaRegistrado = evReembolso.getListaObservers().stream()
                        .anyMatch(o -> o instanceof NotificacionObserver
                                && ((NotificacionObserver) o).getNombre().equals(nombreUsuario));
                if (!yaRegistrado)
                    evReembolso.agregarObserver(new NotificacionObserver(nombreUsuario));
                evReembolso.notificarObservers("Reembolso procesado: tu compra #" + sel.getIdCompra()
                        + " por $" + NF.format((long) sel.getTotal()) + " ha sido devuelta a tu cuenta.");
                Main.mostrarNotificaciones(evReembolso);
            }
            // El dinero ya fue devuelto por CompraController; solo mostramos el saldo actualizado
            String saldoMsg = "";
            if (AppState.getInstancia().haySesion()) {
                Usuario u = AppState.getInstancia().getUsuarioActual();
                boolean pagoTarjeta = sel.getThePago() != null && sel.getThePago().isEsTarjeta();
                saldoMsg = pagoTarjeta
                        ? "Saldo tarjeta: $" + NF.format((long) u.getSaldoTarjeta())
                        : "Saldo efectivo: $" + NF.format((long) u.getSaldo());
            }
            mostrarMensaje("Reembolso procesado. " + saldoMsg, true);
        } else {
            mostrarMensaje("No se pudo procesar el reembolso.", false);
        }
        cargarCompras();
        tablaCompras.getSelectionModel().clearSelection();
    }

    @FXML
    private void onVerComprobante() {
        Compra sel = tablaCompras.getSelectionModel().getSelectedItem();
        if (sel == null) { mostrarMensaje("Seleccione una compra.", false); return; }

        TextArea area = new TextArea(textoComprobante(sel));
        area.setEditable(false);
        area.setPrefHeight(340);
        area.setPrefWidth(440);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Comprobante");
        alert.setHeaderText("Compra #" + sel.getIdCompra());
        alert.getDialogPane().setContent(area);
        alert.showAndWait();
    }

    @FXML
    private void onDescargarComprobante() {
        Compra sel = tablaCompras.getSelectionModel().getSelectedItem();
        if (sel == null) { mostrarMensaje("Seleccione una compra para descargar.", false); return; }
        String archivo = "comprobante_" + sel.getIdCompra() + ".txt";
        try (FileWriter fw = new FileWriter(archivo)) {
            fw.write(textoComprobante(sel));
            mostrarMensaje("Comprobante guardado: " + archivo, true);
        } catch (Exception e) {
            mostrarMensaje("Error al guardar: " + e.getMessage(), false);
        }
    }

    private String textoComprobante(Compra sel) {
        StringBuilder sb = new StringBuilder();
        sb.append("===== COMPROBANTE DE COMPRA =====\n");
        sb.append("ID Compra : #").append(sel.getIdCompra()).append("\n");
        sb.append("Fecha     : ").append(sel.getFechaCreacion()).append("\n");

        if (sel.getTheEvento() != null) {
            sb.append("Evento    : ").append(sel.getTheEvento().getNombre()).append("\n");
            String ciudad = sel.getTheEvento().getTheRecinto() != null
                ? sel.getTheEvento().getTheRecinto().getCiudad()
                : sel.getTheEvento().getCiudad();
            sb.append("Ciudad    : ").append(ciudad).append("\n");
            sb.append("Fecha ev. : ").append(sel.getTheEvento().getFechaHora()).append("\n");
        }

        sb.append("\nEntradas:\n");
        for (Entrada e : sel.getListaEntradas()) {
            // Patron Prototype: se clona la entrada para no exponer el objeto original
            EntradaPrototype proto = new EntradaPrototype(e);
            Entrada copia = proto.clonar();
            sb.append("  - Zona: ").append(copia.getTheZona() != null ? copia.getTheZona().getNombre() : "—");
            if (copia.getTheAsiento() != null)
                sb.append("  |  Fila ").append(copia.getTheAsiento().getFila())
                  .append("  Asiento ").append(copia.getTheAsiento().getNumero());
            sb.append("  |  $").append(NF.format((long) copia.getPrecioFinal())).append("\n");
        }

        if (!sel.getListaServicios().isEmpty()) {
            sb.append("\nServicios: ");
            sel.getListaServicios().forEach(s -> sb.append(s.name()).append(" "));
            sb.append("\n");
        }

        sb.append("\nTotal     : $").append(NF.format((long) sel.getTotal())).append("\n");
        sb.append("Estado    : ").append(sel.getEstadoActual() != null ? sel.getEstadoActual().name() : "—").append("\n");
        if (sel.getThePago() != null)
            sb.append("Metodo    : ").append(sel.getThePago().getMetodoPago()).append("\n");
        sb.append("=================================\n");
        sb.append("TuBoletaRobinson - Sistema de Gestion de Eventos\n");
        return sb.toString();
    }

    private void mostrarMensaje(String msg, boolean ok) {
        lblMensaje.setText(msg);
        lblMensaje.setStyle(ok ? "-fx-text-fill:#388e3c;" : "-fx-text-fill:#d32f2f;");
    }

    @FXML
    private void onFiltrar() {
        String eventoFiltro = txtFiltroEvento.getText().trim().toLowerCase();
        String estadoFiltro = cbFiltroEstado.getValue();
        String fechaFiltro  = txtFiltroFecha.getText().trim();
        filteredList.setPredicate(c -> {
            if (!eventoFiltro.isEmpty()) {
                Evento ev = c.getTheEvento();
                if (ev == null || !ev.getNombre().toLowerCase().contains(eventoFiltro)) return false;
            }
            if (estadoFiltro != null && !"Todos".equals(estadoFiltro)) {
                if (c.getEstadoActual() == null || !c.getEstadoActual().name().equals(estadoFiltro)) return false;
            }
            if (!fechaFiltro.isEmpty() && !c.getFechaCreacion().startsWith(fechaFiltro)) return false;
            return true;
        });
    }

    @FXML
    private void onLimpiarFiltro() {
        txtFiltroEvento.clear();
        cbFiltroEstado.setValue("Todos");
        txtFiltroFecha.clear();
        filteredList.setPredicate(p -> true);
    }

    @FXML private void onVolver() { app.mostrarListaEventos(); }

    public void setApp(Main app) { this.app = app; }
}
