package com.pgii.view.viewcontroller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class NotificacionViewController {

    @FXML private Label lblNombre;
    @FXML private Label lblMensaje;

    @FXML
    private void onCerrar() {
        ((Stage) lblNombre.getScene().getWindow()).close();
    }

    public void setDatos(String nombre, String mensaje) {
        lblNombre.setText(nombre);
        lblMensaje.setText(mensaje);
    }
}
