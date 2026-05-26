/**
 * Open module: JavaFX FXML puede acceder a todos los paquetes
 * sin necesidad de declarar opens individuales.
 */
open module com.pgii {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.pdfbox;
    requires java.logging;

    exports com.pgii;
    exports com.pgii.model.entities;
    exports com.pgii.model.enums;
}
