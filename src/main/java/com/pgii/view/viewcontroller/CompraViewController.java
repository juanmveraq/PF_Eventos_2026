package com.pgii.view.viewcontroller;

import com.pgii.Main;
import com.pgii.controller.AsientoController;
import com.pgii.controller.CompraController;
import com.pgii.controller.UsuarioController;
import com.pgii.model.entities.Asiento;
import com.pgii.model.entities.Compra;
import com.pgii.model.entities.Entrada;
import com.pgii.model.entities.Evento;
import com.pgii.model.entities.Zona;
import com.pgii.model.enums.EstadoCompra;
import com.pgii.model.enums.TipoServicio;
import com.pgii.model.interfaces.IPagoStrategy;
import com.pgii.model.patterns.behavioral.PagoEfectivo;
import com.pgii.model.patterns.behavioral.PagoTarjeta;
import com.pgii.model.entities.Incidencia;
import com.pgii.model.interfaces.IObserver;
import com.pgii.model.patterns.behavioral.NotificacionObserver;
import com.pgii.model.patterns.creational.GestorDatos;
import com.pgii.model.patterns.structural.EntradaSimple;
import com.pgii.model.patterns.structural.ServicioAdicional;
import com.pgii.model.patterns.structural.VIPDecorador;
import com.pgii.model.patterns.structural.SeguroDecorador;
import com.pgii.model.patterns.structural.MerchandisingDecorador;
import com.pgii.model.patterns.structural.ParqueaderoDecorador;
import com.pgii.model.patterns.structural.AccesoPreferencialDecorador;
import com.pgii.view.AppState;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CompraViewController {

    private Main              app;
    private Evento            evento;
    private List<Asiento>     asientos;
    private List<Zona>        zonas;
    private CompraController  compraCtrl;
    private UsuarioController usuarioCtrl;
    private List<Compra>      comprasPendientes;

    private static final NumberFormat NF = NumberFormat.getInstance(new Locale("es", "CO"));

    @FXML private Label            lblEvento;
    @FXML private Label            lblAsientosCount;
    @FXML private ListView<String> listAsientos;
    @FXML private Label            lblTotal;
    @FXML private Label            lblResultado;
    @FXML private CheckBox         chkVip, chkSeg, chkMer, chkPar, chkAcc;
    @FXML private ComboBox<String> cbMetodoPago;
    @FXML private VBox             panelResultado;

    // Campos de detalle de pago
    @FXML private Label     lblSaldoUsuario;
    @FXML private Label     lblDetallePago;
    @FXML private TextField txtDetallesPago;
    @FXML private Label     lblCambio;

    @FXML
    public void initialize() {
        compraCtrl        = new CompraController();
        usuarioCtrl       = new UsuarioController();
        comprasPendientes = new ArrayList<>();
        panelResultado.setVisible(false);

        cbMetodoPago.setItems(FXCollections.observableArrayList(
                "Efectivo", "Tarjeta Credito Visa", "Tarjeta Debito Mastercard",
                "Tarjeta Credito Mastercard", "PSE"
        ));

        // Listener para calcular cambio en vivo cuando es efectivo
        txtDetallesPago.textProperty().addListener((obs, oldV, newV) -> {
            if ("Efectivo".equals(cbMetodoPago.getValue())) calcularCambioEnVivo();
        });

        AppState state = AppState.getInstancia();
        this.evento   = state.getEventoSeleccionado();
        this.asientos = new ArrayList<>(state.getAsientosSeleccionados());
        this.zonas    = new ArrayList<>(state.getZonasDeAsientos());

        if (evento != null) lblEvento.setText(evento.getNombre());

        lblAsientosCount.setText(asientos.size() + " asiento(s) — categoria: " +
                (state.getCategoriaSeleccionada() != null ? state.getCategoriaSeleccionada() : "—"));

        ObservableList<String> items = FXCollections.observableArrayList();
        for (int i = 0; i < asientos.size(); i++) {
            Asiento a = asientos.get(i);
            Zona    z = zonas.get(i);
            items.add("Fila " + a.getFila() + "  Asiento " + a.getNumero()
                    + "   |   " + z.getNombre()
                    + "   |   $" + NF.format((long) z.getPrecioBase()));
        }
        listAsientos.setItems(items);
        listAsientos.setMouseTransparent(true);
        listAsientos.setFocusTraversable(false);

        if (state.haySesion() && evento != null && !asientos.isEmpty()) {
            String nombreUsuario = state.getUsuarioActual().getNombre();
            boolean yaRegistrado = evento.getListaObservers().stream()
                    .anyMatch(o -> o instanceof NotificacionObserver
                            && ((NotificacionObserver) o).getNombre().equals(nombreUsuario));
            if (!yaRegistrado)
                evento.agregarObserver(new NotificacionObserver(nombreUsuario));
            for (int i = 0; i < asientos.size(); i++) {
                Asiento a = asientos.get(i);
                Zona    z = zonas.get(i);
                Compra c = compraCtrl.crearCompra(
                        state.getUsuarioActual().getIdUsuario(),
                        evento.getIdEvento(),
                        z.getIdZona(),
                        a.getIdAsiento()
                );
                if (c != null) {
                    comprasPendientes.add(c);
                } else {
                    registrarIncidencia(
                        "Asiento no disponible",
                        "Intento de compra del asiento fila " + a.getFila() + " num " + a.getNumero()
                            + " en zona '" + z.getNombre() + "' — asiento ya reservado o vendido",
                        "ASIENTO", a.getIdAsiento()
                    );
                }
            }
        }
        actualizarTotal();
    }

    // Llamado por los checkboxes de servicios
    @FXML private void onServicioChanged() { actualizarTotal(); }

    // Llamado cuando cambia el metodo de pago
    @FXML
    private void onMetodoPagoChanged() {
        actualizarTotal();
        String metodo = cbMetodoPago.getValue();
        lblCambio.setText("");
        lblSaldoUsuario.setText("");
        if (metodo == null) {
            txtDetallesPago.setDisable(true);
            txtDetallesPago.clear();
            return;
        }
        txtDetallesPago.clear();
        if ("Efectivo".equals(metodo)) {
            // Pago con saldo en efectivo: mostrar saldo disponible y deshabilitar campo manual
            double saldo = AppState.getInstancia().haySesion()
                ? AppState.getInstancia().getUsuarioActual().getSaldo() : 0;
            lblSaldoUsuario.setText("Saldo efectivo disponible: $" + NF.format((long) saldo));
            lblDetallePago.setText("El pago se descontara de su saldo en efectivo:");
            txtDetallesPago.setDisable(true);
            txtDetallesPago.setPromptText("Pago automatico desde saldo");
        } else {
            // Pago con tarjeta: mostrar saldo de tarjeta y pedir numero
            double saldoT = AppState.getInstancia().haySesion()
                ? AppState.getInstancia().getUsuarioActual().getSaldoTarjeta() : 0;
            lblSaldoUsuario.setText("Saldo tarjeta disponible: $" + NF.format((long) saldoT));
            lblDetallePago.setText("Numero de tarjeta registrado:");
            txtDetallesPago.setPromptText("Ingrese su numero de tarjeta exacto");
            txtDetallesPago.setDisable(false);
        }
    }

    private void calcularCambioEnVivo() {
        String raw = txtDetallesPago.getText().replaceAll("[^0-9]", "");
        if (raw.isEmpty()) { lblCambio.setText(""); return; }
        try {
            long recibido = Long.parseLong(raw);
            long total    = (long) calcularTotalDouble();
            if (recibido < total) {
                lblCambio.setText("Falta: $" + NF.format(total - recibido));
                lblCambio.setStyle("-fx-font-weight:bold; -fx-text-fill:#d32f2f;");
            } else {
                lblCambio.setText("Cambio: $" + NF.format(recibido - total));
                lblCambio.setStyle("-fx-font-weight:bold; -fx-text-fill:#388e3c;");
            }
        } catch (NumberFormatException e) {
            lblCambio.setText("");
        }
    }

    /**
     * calcula el total usando el patron Decorator: encadena los servicios seleccionados
     * sobre un EntradaSimple base y suma el precio acumulado por cada asiento.
     */
    private double calcularTotalDouble() {
        double base = zonas.stream().mapToDouble(Zona::getPrecioBase).sum();
        // Patron Decorator: se encadenan los servicios sobre la entrada base
        ServicioAdicional servicios = new EntradaSimple(0);
        if (chkVip.isSelected()) servicios = new VIPDecorador(servicios);
        if (chkSeg.isSelected()) servicios = new SeguroDecorador(servicios);
        if (chkMer.isSelected()) servicios = new MerchandisingDecorador(servicios);
        if (chkPar.isSelected()) servicios = new ParqueaderoDecorador(servicios);
        if (chkAcc.isSelected()) servicios = new AccesoPreferencialDecorador(servicios);
        double extraPorAsiento = servicios.getPrecioExtra();
        return base + extraPorAsiento * Math.max(1, asientos.size());
    }

    private void actualizarTotal() {
        lblTotal.setText("$" + NF.format((long) calcularTotalDouble()));
    }

    @FXML
    private void onConfirmar() {
        if (comprasPendientes.isEmpty()) { mostrarError("No hay tiene compras pendientes."); return; }
        String metodo = cbMetodoPago.getValue();
        if (metodo == null) { mostrarError("Seleccione un metodo de pago."); return; }

        String detalle = txtDetallesPago.getText().trim();
        double saldoAntes = 0;

        // Validaciones segun metodo
        if ("Efectivo".equals(metodo)) {
            // Validar saldo en efectivo del usuario
            if (!AppState.getInstancia().haySesion()) { mostrarError("No hay sesion activa."); return; }
            saldoAntes = AppState.getInstancia().getUsuarioActual().getSaldo();
            double totalReq = calcularTotalDouble();
            if (saldoAntes < totalReq) {
                mostrarError("Saldo insuficiente. Disponible: $" + NF.format((long) saldoAntes)
                        + " | Requerido: $" + NF.format((long) totalReq));
                return;
            }
        } else {
            // Validar que se ingreso el numero de tarjeta
            if (detalle.isEmpty()) {
                mostrarError("Ingrese su numero de tarjeta."); return;
            }
            // Validar que el numero ingresado coincide exactamente con el registrado
            if (!AppState.getInstancia().haySesion()) { mostrarError("No hay sesion activa."); return; }
            String numeroRegistrado = AppState.getInstancia().getUsuarioActual().getNumeroTarjeta();
            if (!detalle.equals(numeroRegistrado)) {
                mostrarError("ese numero no lo registro. Verifique e intente de nuevo."); return;
            }
            // Validar saldo de tarjeta
            double saldoTarjeta = AppState.getInstancia().getUsuarioActual().getSaldoTarjeta();
            double totalReq = calcularTotalDouble();
            if (saldoTarjeta < totalReq) {
                mostrarError("no tiene el saldo suficiente. Disponible: $" + NF.format((long) saldoTarjeta)
                        + " | Requerido: $" + NF.format((long) totalReq));
                return;
            }
        }

        IPagoStrategy estrategia = resolverEstrategia(metodo, detalle);
        int exitosas = 0;
        double totalFinal = 0;
        StringBuilder ids = new StringBuilder();

        for (Compra c : comprasPendientes) {
            if (chkVip.isSelected()) compraCtrl.agregarServicio(c.getIdCompra(), TipoServicio.VIP);
            if (chkSeg.isSelected()) compraCtrl.agregarServicio(c.getIdCompra(), TipoServicio.SEGURO);
            if (chkMer.isSelected()) compraCtrl.agregarServicio(c.getIdCompra(), TipoServicio.MERCHANDISING);
            if (chkPar.isSelected()) compraCtrl.agregarServicio(c.getIdCompra(), TipoServicio.PARQUEADERO);
            if (chkAcc.isSelected()) compraCtrl.agregarServicio(c.getIdCompra(), TipoServicio.ACCESO_PREFERENCIAL);

            boolean pagado = compraCtrl.pagarCompra(c.getIdCompra(), estrategia);
            if (!pagado) {
                registrarIncidencia(
                    "Fallo en el pago",
                    "El pago de la compra #" + c.getIdCompra() + " no pudo procesarse"
                        + " (metodo: " + metodo + ")",
                    "COMPRA", c.getIdCompra()
                );
            }
            boolean confirmado = pagado && compraCtrl.confirmarCompra(c.getIdCompra());

            if (pagado && confirmado) {
                exitosas++;
                totalFinal += c.getTotal();
                if (ids.length() > 0) ids.append(", ");
                ids.append("#").append(c.getIdCompra());
            }
        }

        panelResultado.setVisible(true);
        if (exitosas > 0) {
            StringBuilder res = new StringBuilder();
            if ("Efectivo".equals(metodo) && AppState.getInstancia().haySesion()) {
                // Descontar del saldo en efectivo
                int uid = AppState.getInstancia().getUsuarioActual().getIdUsuario();
                usuarioCtrl.cobrar(uid, totalFinal);
                double saldoRestante = AppState.getInstancia().getUsuarioActual().getSaldo();
                lblSaldoUsuario.setText("Saldo efectivo restante: $" + NF.format((long) saldoRestante));
                res.append(exitosas).append(" ticket(s) confirmado(s)\n");
                res.append("IDs: ").append(ids).append("\n");
                res.append("Total cobrado: $").append(NF.format((long) totalFinal)).append("\n");
                res.append("Metodo: ").append(metodo);
                res.append("\nNuevo saldo efectivo: $").append(NF.format((long) saldoRestante));
            } else if (AppState.getInstancia().haySesion()) {
                // Descontar del saldo de tarjeta
                int uid = AppState.getInstancia().getUsuarioActual().getIdUsuario();
                usuarioCtrl.cobrarTarjeta(uid, totalFinal);
                double saldoTarjeta = AppState.getInstancia().getUsuarioActual().getSaldoTarjeta();
                lblSaldoUsuario.setText("Saldo tarjeta restante: $" + NF.format((long) saldoTarjeta));
                res.append(exitosas).append(" ticket(s) confirmado(s)\n");
                res.append("IDs: ").append(ids).append("\n");
                res.append("Total cobrado: $").append(NF.format((long) totalFinal)).append("\n");
                res.append("Metodo: ").append(metodo);
                res.append("\nNuevo saldo tarjeta: $").append(NF.format((long) saldoTarjeta));
            }
            lblResultado.setText(res.toString());
            lblResultado.setStyle("-fx-text-fill:#388e3c;");
            evento.notificarObservers(exitosas + " entrada(s) confirmada(s) para "
                    + evento.getNombre() + " — IDs: " + ids
                    + " | Total: $" + NF.format((long) totalFinal));
            Main.mostrarNotificaciones(evento);
        } else {
            mostrarError("Error al procesar el pago. Intente de nuevo.");
        }
    }

    private IPagoStrategy resolverEstrategia(String metodo, String detalle) {
        if (metodo.contains("Visa"))       return new PagoTarjeta("Visa");
        if (metodo.contains("Mastercard")) return new PagoTarjeta("Mastercard");
        if (metodo.contains("PSE"))        return new PagoEfectivo("PSE - Ref: " + detalle);
        return new PagoEfectivo("Efectivo");
    }

    private void mostrarError(String msg) {
        panelResultado.setVisible(true);
        lblResultado.setText(msg);
        lblResultado.setStyle("-fx-text-fill:#d32f2f;");
    }

    private void registrarIncidencia(String tipo, String desc, String entidad, int idEntidad) {
        GestorDatos g = GestorDatos.getInstancia();
        g.getListaIncidencias().add(new Incidencia(
            g.generarIdIncidencia(), tipo, desc,
            LocalDate.now().toString(), entidad, idEntidad
        ));
    }

    @FXML
    private void onVolver() {
        AsientoController asientoCtrl = new AsientoController();
        for (Compra c : comprasPendientes) {
            if (c.getEstadoActual() != EstadoCompra.CONFIRMADA) {
                for (Entrada entrada : c.getListaEntradas())
                    if (entrada.getTheAsiento() != null)
                        asientoCtrl.liberarAsiento(entrada.getTheAsiento().getIdAsiento());
                compraCtrl.cancelarCompra(c.getIdCompra());
            }
        }
        AppState st = AppState.getInstancia();
        if (st.haySesion() && st.getUsuarioActual().getRol() == com.pgii.model.enums.RolUsuario.ADMINISTRADOR) {
            app.mostrarAdminPanel();
        } else if (evento != null) {
            app.mostrarSeleccionAsiento(evento);
        } else {
            app.mostrarListaEventos();
        }
    }

    public void setApp(Main app) { this.app = app; }
}
