package com.pgii;

import com.pgii.model.entities.Evento;
import com.pgii.model.interfaces.IObserver;
import com.pgii.model.patterns.behavioral.NotificacionObserver;
import com.pgii.view.AppState;
import com.pgii.view.viewcontroller.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.List;

/**
 * Clase principal de la aplicacion TuBoletaRobinson que extiende Application de JavaFX.
 * Es el punto de entrada del sistema y gestiona la navegacion entre las distintas pantallas
 * cargando los archivos FXML correspondientes a cada vista.
 */
public class  Main extends Application {

    /** ventana principal de la aplicacion JavaFX. */
    private Stage primaryStage;

    /** se fijo el ancho de la ventana de la aplicacion en pixeles. */
    private static final double ANCHO = 1100;

    /** se fijo el alto fijo de la ventana de la aplicacion en pixeles. */
    private static final double ALTO  = 640;

    /**
     * metodo de inicio de JavaFX que inicializa los datos y muestra la pantalla de login.
     * @param primaryStage Ventana principal proporcionada por JavaFX.
     */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("TuBoletaRobinson");
        DataInitializer.inicializar();
        mostrarLogin();
    }

    /**
     * navega y muestra la pantalla de inicio de sesion de la plataforma.
     */
    public void mostrarLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pgii/login.fxml"));
            Parent root = loader.load();
            LoginViewController ctrl = loader.getController();
            ctrl.setApp(this);
            primaryStage.setScene(new Scene(root, ANCHO, ALTO));
            primaryStage.setTitle("TuBoletaRobinson — Ingreso");
            primaryStage.show();
        } catch (Exception e) { e.printStackTrace(); }
    }

    /**
     * Navega y muestra la pantalla con el listado de eventos disponibles.
     */
    public void mostrarListaEventos() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pgii/eventoList.fxml"));
            Parent root = loader.load();
            EventoListViewController ctrl = loader.getController();
            ctrl.setApp(this);
            primaryStage.setScene(new Scene(root, ANCHO, ALTO));
            primaryStage.setTitle("Explorar Eventos");
        } catch (Exception e) { e.printStackTrace(); }
    }

    /**
     * Navega y muestra la pantalla con el detalle de un evento especifico.
     * @param evento Evento cuyo detalle se desea mostrar.
     */
    public void mostrarDetalleEvento(Evento evento) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pgii/eventoDetalle.fxml"));
            Parent root = loader.load();
            EventoDetalleViewController ctrl = loader.getController();
            ctrl.setApp(this);
            ctrl.setEvento(evento);
            primaryStage.setScene(new Scene(root, ANCHO, ALTO));
            primaryStage.setTitle("Evento: " + evento.getNombre());
        } catch (Exception e) { e.printStackTrace(); }
    }

    /**
     * naveega y muestra la pantalla de seleccion de asientos para un evento.
     * @param evento Evento para el que se seleccionaran los asientos.
     */
    public void mostrarSeleccionAsiento(Evento evento) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pgii/seleccionAsiento.fxml"));
            Parent root = loader.load();
            SeleccionAsientoViewController ctrl = loader.getController();
            ctrl.setApp(this);
            ctrl.setEvento(evento);
            primaryStage.setScene(new Scene(root, ANCHO, ALTO));
            primaryStage.setTitle("Selección de Asientos - " + evento.getNombre());
        } catch (Exception e) { e.printStackTrace(); }
    }

    /**
     * navega y muestra la pantalla de proceso de compra para el evento seleccionado.
     * @param evento Evento para el que se realizara la compra.
     */
    public void mostrarCompra(Evento evento) {
        try {
            AppState.getInstancia().setEventoSeleccionado(evento);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pgii/compra.fxml"));
            Parent root = loader.load();
            CompraViewController ctrl = loader.getController();
            ctrl.setApp(this);
            primaryStage.setScene(new Scene(root, ANCHO, ALTO));
            primaryStage.setTitle("Compra de Entrada");
        } catch (Exception e) { e.printStackTrace(); }
    }

    /**
     * navega y muestra la pantalla con el historial de compras del usuario en sesion.
     */
    public void mostrarMisCompras() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pgii/misCompras.fxml"));
            Parent root = loader.load();
            MisComprasViewController ctrl = loader.getController();
            ctrl.setApp(this);
            primaryStage.setScene(new Scene(root, ANCHO, ALTO));
            primaryStage.setTitle("Mis Compras");
        } catch (Exception e) { e.printStackTrace(); }
    }

    /**
     * navega y muestra la pantalla final de confirmacion de compra exitosa.
     */
    public void mostrarPantallaFinal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pgii/pantallaFinal.fxml"));
            Parent root = loader.load();
            primaryStage.setScene(new Scene(root, ANCHO, ALTO));
            primaryStage.setTitle("TuBoletaRobinson");
        } catch (Exception e) { e.printStackTrace(); }
    }

    /**
     * navega y muestra el panel de administracion para usuarios con rol ADMINISTRADOR.
     */
    public void mostrarAdminPanel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pgii/adminPanel.fxml"));
            Parent root = loader.load();
            AdminPanelViewController ctrl = loader.getController();
            ctrl.setApp(this);
            primaryStage.setScene(new Scene(root, ANCHO, ALTO));
            primaryStage.setTitle("Panel Administrador - PGII");
        } catch (Exception e) { e.printStackTrace(); }
    }

    /**
     * abre una ventana emergente de notificacion para cada observer registrado en el evento.
     * metodo estatico reutilizable desde cualquier vista (SRP / DRY).
     * @param evento Evento cuyos observers seran notificados visualmente.
     */
    public static void mostrarNotificaciones(Evento evento) {
        for (IObserver obs : evento.getListaObservers()) {
            if (!(obs instanceof NotificacionObserver)) continue;
            NotificacionObserver no = (NotificacionObserver) obs;
            List<String> notifs = no.getListaNotificaciones();
            if (notifs.isEmpty()) continue;
            try {
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("/com/pgii/notificacion.fxml"));
                Parent root = loader.load();
                NotificacionViewController ctrl = loader.getController();
                ctrl.setDatos(no.getNombre(), notifs.get(notifs.size() - 1));
                Stage stage = new Stage();
                stage.setTitle("Notificacion — TuBoletaRobinson");
                stage.setScene(new Scene(root, 420, 280));
                stage.show();
            } catch (Exception e) { e.printStackTrace(); }
        }
    }

    /**
     * retorna la ventana principal de la aplicacion.
     * @return el Stage principal de JavaFX.
     */
    public Stage getPrimaryStage() { return primaryStage; }

    /**
     * metodo main que lanza la aplicacion JavaFX.
     * @param args Argumentos de linea de comandos (no se usan).
     */
    public static void main(String[] args) { launch(args); }
}
