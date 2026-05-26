package com.pgii.view.viewcontroller;

import com.pgii.Main;
import com.pgii.controller.EventoController;
import com.pgii.model.entities.Evento;
import com.pgii.model.enums.CategoriaEvento;
import com.pgii.model.enums.EstadoEvento;
import com.pgii.view.AppState;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class EventoListViewController {

    private Main app;
    private EventoController eventoCtrl;
    private ObservableList<Evento> datosTabla;
    private SortedList<Evento> sortedEvt;

    @FXML private TextField              txtBuscar;
    @FXML private ComboBox<String>       cbCategoria;
    @FXML private TableView<Evento>      tablaEventos;
    @FXML private TableColumn<Evento,String> colId, colNombre, colCategoria,
                                             colCiudad, colFecha, colEstado, colPrecio;
    @FXML private Label   lblConteo;
    @FXML private Label   lblBienvenida;
    @FXML private Button  btnDetalle;
    @FXML private Button  btnAdminPanel;
    @FXML private Button  btnMisCompras;

    @FXML
    public void initialize() {
        eventoCtrl = new EventoController();
        datosTabla = FXCollections.observableArrayList();
        sortedEvt  = new SortedList<>(datosTabla);
        sortedEvt.comparatorProperty().bind(tablaEventos.comparatorProperty());

        // Columnas
        colId.setCellValueFactory(      c -> new SimpleStringProperty(String.valueOf(c.getValue().getIdEvento())));
        colNombre.setCellValueFactory(  c -> new SimpleStringProperty(c.getValue().getNombre()));
        colCategoria.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCategoria().name()));
        colCiudad.setCellValueFactory(  c -> new SimpleStringProperty(
            c.getValue().getTheRecinto() != null
                ? c.getValue().getTheRecinto().getCiudad()
                : c.getValue().getCiudad()));
        colFecha.setCellValueFactory(   c -> new SimpleStringProperty(c.getValue().getFechaHora()));
        colEstado.setCellValueFactory(  c -> new SimpleStringProperty(c.getValue().getEstado().name()));
        colPrecio.setCellValueFactory(  c -> {
            String precio = "–";
            if (c.getValue().getTheRecinto() != null &&
                !c.getValue().getTheRecinto().getListaZonas().isEmpty()) {
                double min = c.getValue().getTheRecinto().getListaZonas().stream()
                        .mapToDouble(z -> z.getPrecioBase()).min().orElse(0);
                precio = "$" + (long) min;
            }
            return new SimpleStringProperty(precio);
        });

        tablaEventos.setItems(sortedEvt);

        // Habilitar botón al seleccionar fila
        tablaEventos.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldV, newV) -> btnDetalle.setDisable(newV == null)
        );

        // ComboBox categorías
        cbCategoria.setItems(FXCollections.observableArrayList(
            "Todos", "CONCIERTO", "CONFERENCIA", "TEATRO"
        ));
        cbCategoria.setValue("Todos");

        // Info de usuario
        AppState state = AppState.getInstancia();
        if (state.haySesion()) {
            lblBienvenida.setText("Hola, " + state.getUsuarioActual().getNombre());
            boolean esAdmin = state.getUsuarioActual().getRol().name().equals("ADMINISTRADOR");
            btnAdminPanel.setVisible(esAdmin);
            btnMisCompras.setVisible(!esAdmin);
        }

        cargarEventosPublicados();
    }

    private void cargarEventosPublicados() {
        List<Evento> todos = eventoCtrl.listarEventos();
        datosTabla.clear();
        for (Evento e : todos) {
            if (e.getEstado() == EstadoEvento.PUBLICADO) datosTabla.add(e);
        }
        actualizarConteo();
    }

    @FXML private void onBuscar() {
        String texto = txtBuscar.getText().trim().toLowerCase();
        String cat   = cbCategoria.getValue();
        List<Evento> todos = eventoCtrl.listarEventos();
        datosTabla.clear();
        for (Evento e : todos) {
            if (e.getEstado() != EstadoEvento.PUBLICADO) continue;
            boolean matchTxt = texto.isEmpty()
                || e.getNombre().toLowerCase().contains(texto)
                || e.getCiudad().toLowerCase().contains(texto);
            boolean matchCat = "Todos".equals(cat) || e.getCategoria().name().equals(cat);
            if (matchTxt && matchCat) datosTabla.add(e);
        }
        actualizarConteo();
    }

    @FXML private void onMostrarTodos() {
        txtBuscar.clear();
        cbCategoria.setValue("Todos");
        cargarEventosPublicados();
    }

    @FXML private void onVerDetalle() {
        Evento sel = tablaEventos.getSelectionModel().getSelectedItem();
        if (sel != null) {
            AppState.getInstancia().setEventoSeleccionado(sel);
            app.mostrarDetalleEvento(sel);
        }
    }

    @FXML private void onAbrirAdmin()    { app.mostrarAdminPanel(); }
    @FXML private void onMisCompras()   { app.mostrarMisCompras(); }
    @FXML private void onCerrarApp()    { app.mostrarPantallaFinal(); }
    @FXML private void onCerrarSesion() {
        AppState.getInstancia().cerrarSesion();
        app.mostrarLogin();
    }

    private void actualizarConteo() {
        lblConteo.setText(datosTabla.size() + " eventos encontrados");
    }

    public void setApp(Main app) { this.app = app; }
}
