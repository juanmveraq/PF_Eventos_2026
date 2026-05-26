package com.pgii.view.viewcontroller;

import com.pgii.Main;
import com.pgii.controller.UsuarioController;
import com.pgii.model.entities.Usuario;
import com.pgii.model.enums.RolUsuario;
import com.pgii.view.AppState;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class LoginViewController {

    private Main app;
    private UsuarioController usuarioCtrl;

    @FXML private VBox          panelLogin;
    @FXML private TextField     txtCorreo;
    @FXML private PasswordField txtPassword;
    @FXML private Label         lblError;

    @FXML private VBox      panelRegistro;
    @FXML private TextField txtRegNombre;
    @FXML private TextField txtRegCorreo;
    @FXML private TextField txtRegTelefono;
    @FXML private TextField txtRegTarjeta;
    @FXML private Label     lblRegMsg;

    // Campos de contrasena en el registro
    @FXML private PasswordField txtRegContrasena;
    @FXML private PasswordField txtRegContrasena2;

    @FXML
    public void initialize() {
        usuarioCtrl = new UsuarioController();
        lblError.setText("");
    }

    // ══ LOGIN ══════════════════════════════════════════════════════
    @FXML
    private void onIngresar() {
        String correo = txtCorreo.getText().trim();
        String pass   = txtPassword.getText();

        if (correo.isEmpty()) { lblError.setText("Ingresa tu correo."); return; }
        if (pass.isEmpty())   { lblError.setText("Ingresa tu contrasena."); return; }

        if (!usuarioCtrl.verificarContrasena(correo, pass)) {
            lblError.setText("Correo o contrasena incorrectos.");
            return;
        }

        Usuario u = usuarioCtrl.buscarPorCorreo(correo);
        AppState.getInstancia().setUsuarioActual(u);

        switch (u.getRol()) {
            case ADMINISTRADOR -> app.mostrarAdminPanel();
            default            -> app.mostrarListaEventos();
        }
    }

    // ══ REGISTRO (solo clientes — admins solo por DataInitializer) ═
    @FXML
    private void onRegistrar() {
        String nombre   = txtRegNombre.getText().trim();
        String correo   = txtRegCorreo.getText().trim();
        String telefono = txtRegTelefono.getText().trim();
        // Contrasena viene de los campos si existen en FXML; si no, asignar "1234"
        String contrasena  = txtRegContrasena  != null ? txtRegContrasena.getText()  : "1234";
        String contrasena2 = txtRegContrasena2 != null ? txtRegContrasena2.getText() : "1234";

        String numTarjeta = txtRegTarjeta != null ? txtRegTarjeta.getText().trim() : "";

        if (nombre.isEmpty() || correo.isEmpty()) {
            setRegMsg("Nombre y correo son obligatorios.", false); return;
        }
        if (!correo.contains("@")) {
            setRegMsg("Correo invalido.", false); return;
        }
        if (contrasena.length() < 4) {
            setRegMsg("La contrasena debe tener al menos 4 caracteres.", false); return;
        }
        if (!contrasena.equals(contrasena2)) {
            setRegMsg("Las contrasenas no coinciden.", false); return;
        }
        if (!numTarjeta.isEmpty() && !numTarjeta.matches("\\d{16}")) {
            setRegMsg("El numero de tarjeta debe tener exactamente 16 digitos numericos.", false); return;
        }

        // Solo rol USUARIO — admins unicamente via DataInitializer y creandolo desde la pantalla admin
        Usuario nuevo = usuarioCtrl.registrar(nombre, correo, contrasena, telefono, RolUsuario.USUARIO);
        if (nuevo == null) {
            setRegMsg("Ese correo ya esta registrado.", false);
        } else {
            if (!numTarjeta.isEmpty()) usuarioCtrl.asignarNumeroTarjeta(nuevo.getIdUsuario(), numTarjeta);
            setRegMsg("Cuenta creada. Ya puedes iniciar sesion.", true);
            txtRegNombre.clear(); txtRegCorreo.clear(); txtRegTelefono.clear();
            if (txtRegContrasena  != null) txtRegContrasena.clear();
            if (txtRegContrasena2 != null) txtRegContrasena2.clear();
            if (txtRegTarjeta     != null) txtRegTarjeta.clear();
            txtCorreo.setText(correo);
        }
    }

    @FXML private void onMostrarRegistro() {
        panelLogin.setVisible(false);
        panelRegistro.setVisible(true);
        lblRegMsg.setText("");
    }

    @FXML private void onVolverLogin() {
        panelRegistro.setVisible(false);
        panelLogin.setVisible(true);
        lblError.setText("");
    }

    private void setRegMsg(String msg, boolean ok) {
        lblRegMsg.setText(msg);
        lblRegMsg.setStyle(ok ? "-fx-text-fill:#4ecca3;" : "-fx-text-fill:#e94560;");
    }

    public void setApp(Main app) { this.app = app; }
}
