package com.pgii.view.viewcontroller;

import com.pgii.Main;
import com.pgii.model.entities.Evento;
import com.pgii.model.entities.Zona;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class EventoDetalleViewController {

    private Main  app;
    private Evento evento;

    @FXML private Label   lblTitulo, lblCategoria, lblCiudad, lblFecha,
                           lblEstado, lblRecinto, lblDireccion, lblInfoExtra,
                           lblPolCancelacion, lblPolReembolso;
    @FXML private TextArea txtDescripcion;

    @FXML private TableView<Zona>             tablaZonas;
    @FXML private TableColumn<Zona,String>    colZonaNombre, colZonaCapacidad,
                                               colZonaDisp, colZonaPrecio;

    private ObservableList<Zona> zonasDatos = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colZonaNombre.setCellValueFactory(   c -> new SimpleStringProperty(c.getValue().getNombre()));
        colZonaCapacidad.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getCapacidad())));
        colZonaDisp.setCellValueFactory(     c -> new SimpleStringProperty(String.valueOf(c.getValue().getCapacidadDisponible())));
        colZonaPrecio.setCellValueFactory(   c -> new SimpleStringProperty("$" + (long)c.getValue().getPrecioBase()));
        tablaZonas.setItems(zonasDatos);
    }

    /** llaamado desde Main.java justo después de load() */
    public void setEvento(Evento e) {
        this.evento = e;
        refrescarUI();
    }

    private void refrescarUI() {
        if (evento == null) return;
        lblTitulo.setText(evento.getNombre());
        lblCategoria.setText(evento.getCategoria().name());
        lblCiudad.setText(evento.getTheRecinto() != null
            ? evento.getTheRecinto().getCiudad()
            : evento.getCiudad());
        lblFecha.setText(evento.getFechaHora());
        lblEstado.setText(evento.getEstado().name());
        txtDescripcion.setText(evento.getDescripcion());
        lblPolCancelacion.setText(evento.getPoliticaCancelacion());
        lblPolReembolso.setText(evento.getPoliticaReembolso());
        lblInfoExtra.setText(evento.getInfoEspecifica());

        if (evento.getTheRecinto() != null) {
            lblRecinto.setText(evento.getTheRecinto().getNombre());
            lblDireccion.setText(evento.getTheRecinto().getDireccion() + " - " + evento.getTheRecinto().getCiudad());
            // Cargar zonas con auto-refresh desde la lista viva del recinto
            zonasDatos.setAll(evento.getTheRecinto().getListaZonas());
        }
    }

    @FXML private void onVolver()  { app.mostrarListaEventos(); }
    @FXML private void onVerMapa() { app.mostrarSeleccionAsiento(evento); }

    public void setApp(Main app) { this.app = app; }
}
