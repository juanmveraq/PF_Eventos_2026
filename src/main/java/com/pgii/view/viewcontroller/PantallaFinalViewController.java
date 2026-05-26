package com.pgii.view.viewcontroller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.InputStream;

public class PantallaFinalViewController {

    @FXML private ImageView imgFinal;

    @FXML
    public void initialize() {
        InputStream is = getClass().getResourceAsStream("/com/pgii/falto_singleton.png");
        if (is != null) {
            imgFinal.setImage(new Image(is));
        }
    }

    @FXML
    private void onCerrar() {
        Platform.exit();
    }
}
