package com.pgii.view.viewcontroller;

import com.pgii.Main;
import com.pgii.controller.*;
import com.pgii.model.entities.*;
import com.pgii.model.enums.*;
import com.pgii.model.patterns.creational.GestorDatos;
import com.pgii.model.patterns.structural.PlataformaFacade;
import com.pgii.view.AppState;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import com.pgii.model.interfaces.ICommand;
import com.pgii.model.interfaces.IObserver;
import com.pgii.model.patterns.behavioral.CancelarCompraCommand;
import com.pgii.model.patterns.behavioral.NotificacionObserver;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import javafx.util.StringConverter;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

public class AdminPanelViewController {

    private Main app;

    // Controllers
    private EventoController   eventoCtrl;
    private UsuarioController  usuarioCtrl;
    private RecintoController  recintoCtrl;
    private CompraController   compraCtrl;
    private MetricaController  metricaCtrl;
    private ReporteController  reporteCtrl;
    private PlataformaFacade   facade;

    // -- Eventos Tab
    @FXML private TextField    txtEvNombre, txtEvInfo1, txtEvInfo2;
    @FXML private DatePicker   dpEvFecha;
    @FXML private Spinner<Integer> spHora, spMinuto;
    @FXML private ComboBox<CategoriaEvento> cbEvCategoria;
    @FXML private ComboBox<Recinto>         cbEvRecinto;
    @FXML private Label      lblEvMsg;
    @FXML private TableView<Evento>  tablaEventos;
    @FXML private TableColumn<Evento,String> colEvId, colEvNombre, colEvCat, colEvCiudad,
                                              colEvFecha, colEvEstado, colEvRecinto, colEvCompras;

    private ObservableList<Evento> eventosData = FXCollections.observableArrayList();
    private SortedList<Evento>     sortedEventos;

    // Usuarios Tab
    @FXML private TextField  txtUsNombre, txtUsCorreo, txtUsTelefono, txtUsContrasena, txtUsSaldo;
    @FXML private TextField  txtUsSaldoTarjeta, txtUsNumeroTarjeta;
    @FXML private ComboBox<RolUsuario> cbUsRol;
    @FXML private Label      lblUsMsg;
    @FXML private TableView<Usuario>  tablaUsuarios;
    @FXML private TableColumn<Usuario,String> colUsId, colUsNombre, colUsCorreo, colUsTel, colUsRol,
                                              colUsSaldo, colUsSaldoTarjeta, colUsNumeroTarjeta, colUsCompras;

    private ObservableList<Usuario> usuariosData = FXCollections.observableArrayList();
    private SortedList<Usuario>     sortedUsuarios;

    private static final Map<String, Double> PRECIOS_ZONA = new LinkedHashMap<>();
    static {
        PRECIOS_ZONA.put("VIP",          150_000.0);
        PRECIOS_ZONA.put("Preferencial",  80_000.0);
        PRECIOS_ZONA.put("General",       50_000.0);
    }

    // -- Recintos Tab
    @FXML private TextField        txtReNombre, txtReDireccion, txtReCiudad;
    @FXML private ComboBox<String> cbZoTipo;
    @FXML private TextField        txtZoSeccion;
    @FXML private TextField        txtZoCapacidad;
    @FXML private Label      lblReMsg;
    @FXML private TableView<Recinto>  tablaRecintos;
    @FXML private TableColumn<Recinto,String> colReId, colReNombre, colReDireccion, colReCiudad, colReZonas, colReCap;

    private ObservableList<Recinto> recintosData = FXCollections.observableArrayList();
    private SortedList<Recinto>     sortedRecintos;

    // -- Compras Tab
    @FXML private Label      lblCoMsg;
    @FXML private Label      lblResMsg;
    @FXML private ComboBox<Usuario> cmbResUsuario;
    @FXML private ComboBox<Evento>  cmbResEvento;
    @FXML private ComboBox<Zona>    cmbResZona;
    @FXML private ComboBox<Asiento> cmbResAsiento;
    @FXML private TableView<Compra>  tablaCompras;
    @FXML private TableColumn<Compra,String> colCoId, colCoUsuario, colCoEvento, colCoTotal,
                                              colCoEstado, colCoFecha, colCoServ;

    private ObservableList<Compra> comprasData = FXCollections.observableArrayList();
    private SortedList<Compra>     sortedCompras;

    // Reportes Tab
    @FXML private ComboBox<String> cbTipoReporte;
    @FXML private ComboBox<Compra> cbCompraReporte;
    @FXML private CheckBox         chkEncabezado, chkFirma;
    @FXML private TextArea         areaReporte;
    @FXML private Label            lblRepMsg;

    // Métricas Tab
    @FXML private HBox      panelKpis, panelKpis2;
    @FXML private BarChart<String,Number>  barChart;
    @FXML private PieChart  pieChart;

    // Incidencias Tab
    @FXML private TextField  txtIncTipo, txtIncDesc, txtIncEntidad, txtIncIdEnt;
    @FXML private Label      lblIncMsg;
    @FXML private TableView<Incidencia>  tablaIncidencias;
    @FXML private TableColumn<Incidencia,String> colIncId, colIncTipo, colIncDesc, colIncEntidad, colIncFecha;

    private ObservableList<Incidencia> incidenciasData = FXCollections.observableArrayList();
    private SortedList<Incidencia>     sortedIncidencias;

    // Labels misc
    @FXML private Label   lblAdminNombre;
    @FXML private TabPane tabPane;

    /** Pila de comandos ejecutados, permite deshacer la ultima accion (patron Command). */
    private final Stack<ICommand> historialComandos = new Stack<>();


    @FXML
    public void initialize() {
        eventoCtrl  = new EventoController();
        usuarioCtrl = new UsuarioController();
        recintoCtrl = new RecintoController();
        compraCtrl  = new CompraController();
        metricaCtrl = new MetricaController();
        reporteCtrl = new ReporteController();
        facade      = new PlataformaFacade(eventoCtrl, compraCtrl, usuarioCtrl, recintoCtrl);

        spHora.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 20));
        spMinuto.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0, 5));

        configurarTablaEventos();
        configurarTablaUsuarios();
        configurarTablaRecintos();
        configurarTablaCompras();
        configurarTablaIncidencias();
        configurarReportes();
        configurarCombos();

        cargarTodosDatos();

        if (AppState.getInstancia().haySesion())
            lblAdminNombre.setText("Admin: " + AppState.getInstancia().getUsuarioActual().getNombre());

        // Auto-refresh al cambiar de tab
        tabPane.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldT, newT) -> cargarTodosDatos()
        );
    }

    // -- Configurar columnas
    private void configurarTablaEventos() {
        colEvId.setCellValueFactory(      c -> new SimpleStringProperty(String.valueOf(c.getValue().getIdEvento())));
        colEvNombre.setCellValueFactory(  c -> new SimpleStringProperty(c.getValue().getNombre()));
        colEvCat.setCellValueFactory(     c -> new SimpleStringProperty(c.getValue().getCategoria().name()));
        colEvCiudad.setCellValueFactory(  c -> new SimpleStringProperty(
            c.getValue().getTheRecinto() != null
                ? c.getValue().getTheRecinto().getCiudad()
                : c.getValue().getCiudad()));
        colEvFecha.setCellValueFactory(   c -> new SimpleStringProperty(c.getValue().getFechaHora()));
        colEvEstado.setCellValueFactory(  c -> new SimpleStringProperty(c.getValue().getEstado().name()));
        colEvRecinto.setCellValueFactory( c -> new SimpleStringProperty(
            c.getValue().getTheRecinto() != null ? c.getValue().getTheRecinto().getNombre() : "—"));
        colEvCompras.setCellValueFactory( c -> new SimpleStringProperty(
            String.valueOf(c.getValue().getListaCompras().size())));
        sortedEventos = new SortedList<>(eventosData);
        sortedEventos.comparatorProperty().bind(tablaEventos.comparatorProperty());
        tablaEventos.setItems(sortedEventos);
    }

    private void configurarTablaUsuarios() {
        colUsId.setCellValueFactory(      c -> new SimpleStringProperty(String.valueOf(c.getValue().getIdUsuario())));
        colUsNombre.setCellValueFactory(  c -> new SimpleStringProperty(c.getValue().getNombre()));
        colUsCorreo.setCellValueFactory(  c -> new SimpleStringProperty(c.getValue().getCorreo()));
        colUsTel.setCellValueFactory(     c -> new SimpleStringProperty(c.getValue().getTelefono()));
        colUsRol.setCellValueFactory(     c -> new SimpleStringProperty(c.getValue().getRol().name()));
        colUsSaldo.setCellValueFactory(   c -> new SimpleStringProperty(
            "$" + NumberFormat.getInstance(new java.util.Locale("es","CO"))
                .format((long) c.getValue().getSaldo())));
        colUsSaldoTarjeta.setCellValueFactory(c -> new SimpleStringProperty(
            "$" + NumberFormat.getInstance(new java.util.Locale("es","CO"))
                .format((long) c.getValue().getSaldoTarjeta())));
        colUsNumeroTarjeta.setCellValueFactory(c -> new SimpleStringProperty(
            c.getValue().getNumeroTarjeta().isEmpty() ? "—" : c.getValue().getNumeroTarjeta()));
        colUsCompras.setCellValueFactory( c -> new SimpleStringProperty(
            String.valueOf(c.getValue().getListaCompras().size())));
        sortedUsuarios = new SortedList<>(usuariosData);
        sortedUsuarios.comparatorProperty().bind(tablaUsuarios.comparatorProperty());
        tablaUsuarios.setItems(sortedUsuarios);
    }

    private void configurarTablaRecintos() {
        colReId.setCellValueFactory(        c -> new SimpleStringProperty(String.valueOf(c.getValue().getIdRecinto())));
        colReNombre.setCellValueFactory(    c -> new SimpleStringProperty(c.getValue().getNombre()));
        colReDireccion.setCellValueFactory( c -> new SimpleStringProperty(c.getValue().getDireccion()));
        colReCiudad.setCellValueFactory(    c -> new SimpleStringProperty(c.getValue().getCiudad()));
        colReZonas.setCellValueFactory(     c -> new SimpleStringProperty(
            String.valueOf(c.getValue().getListaZonas().size())));
        colReCap.setCellValueFactory(       c -> new SimpleStringProperty(
            String.valueOf(c.getValue().getCapacidadTotal())));
        sortedRecintos = new SortedList<>(recintosData);
        sortedRecintos.comparatorProperty().bind(tablaRecintos.comparatorProperty());
        tablaRecintos.setItems(sortedRecintos);
    }

    private void configurarTablaCompras() {
        colCoId.setCellValueFactory(      c -> new SimpleStringProperty(String.valueOf(c.getValue().getIdCompra())));
        colCoUsuario.setCellValueFactory( c -> new SimpleStringProperty(c.getValue().getTheUsuario().getNombre()));
        colCoEvento.setCellValueFactory(  c -> new SimpleStringProperty(c.getValue().getTheEvento().getNombre()));
        colCoTotal.setCellValueFactory(   c -> new SimpleStringProperty("$" + (long)c.getValue().getTotal()));
        colCoEstado.setCellValueFactory(  c -> new SimpleStringProperty(
            c.getValue().getEstadoActual() != null ? c.getValue().getEstadoActual().name() : "—"));
        colCoFecha.setCellValueFactory(   c -> new SimpleStringProperty(c.getValue().getFechaCreacion()));
        colCoServ.setCellValueFactory(    c -> new SimpleStringProperty(
            String.valueOf(c.getValue().getListaServicios().size())));
        sortedCompras = new SortedList<>(comprasData);
        sortedCompras.comparatorProperty().bind(tablaCompras.comparatorProperty());
        tablaCompras.setItems(sortedCompras);
    }

    private void configurarTablaIncidencias() {
        colIncId.setCellValueFactory(     c -> new SimpleStringProperty(String.valueOf(c.getValue().getIdIncidencia())));
        colIncTipo.setCellValueFactory(   c -> new SimpleStringProperty(c.getValue().getTipo()));
        colIncDesc.setCellValueFactory(   c -> new SimpleStringProperty(c.getValue().getDescripcion()));
        colIncEntidad.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTipoEntidadAfectada()));
        colIncFecha.setCellValueFactory(  c -> new SimpleStringProperty(c.getValue().getFecha()));
        sortedIncidencias = new SortedList<>(incidenciasData);
        sortedIncidencias.comparatorProperty().bind(tablaIncidencias.comparatorProperty());
        tablaIncidencias.setItems(sortedIncidencias);
    }

    private void configurarReportes() {
        cbTipoReporte.setItems(FXCollections.observableArrayList(
            "Reporte de Compra", "Reporte de Eventos"
        ));
        cbTipoReporte.setValue("Reporte de Compra");

        // Mostrar/ocultar selector de compra segun tipo
        cbTipoReporte.valueProperty().addListener((obs, oldV, newV) ->
            cbCompraReporte.setDisable(!"Reporte de Compra".equals(newV))
        );

        cbCompraReporte.setConverter(new StringConverter<Compra>() {
            @Override public String toString(Compra c) {
                if (c == null) return "— Seleccione una compra —";
                String usuario = c.getTheUsuario() != null ? c.getTheUsuario().getNombre() : "?";
                String evento  = c.getTheEvento()  != null ? c.getTheEvento().getNombre()  : "?";
                String estado  = c.getEstadoActual() != null ? c.getEstadoActual().name()  : "?";
                return "#" + c.getIdCompra() + "  " + usuario + "  |  " + evento
                        + "  |  $" + (long)c.getTotal() + "  [" + estado + "]";
            }
            @Override public Compra fromString(String s) { return null; }
        });
    }

    private void configurarCombos() {
        cbEvCategoria.setItems(FXCollections.observableArrayList(CategoriaEvento.values()));
        cbUsRol.setItems(FXCollections.observableArrayList(RolUsuario.values()));
        cbUsRol.setValue(RolUsuario.USUARIO);
        cbZoTipo.setItems(FXCollections.observableArrayList("VIP", "Preferencial", "General"));
        recargarComboRecintos();
        configurarCombosReserva();
    }

    private void configurarCombosReserva() {
        cmbResUsuario.setConverter(new StringConverter<Usuario>() {
            @Override public String toString(Usuario u) {
                return u == null ? "— Usuario —" : u.getNombre() + "  ·  " + u.getCorreo();
            }
            @Override public Usuario fromString(String s) { return null; }
        });
        cmbResEvento.setConverter(new StringConverter<Evento>() {
            @Override public String toString(Evento e) {
                return e == null ? "— Evento —" : e.getNombre();
            }
            @Override public Evento fromString(String s) { return null; }
        });
        cmbResZona.setConverter(new StringConverter<Zona>() {
            @Override public String toString(Zona z) {
                return z == null ? "— Zona —" : z.getNombre() + "  $" + (long) z.getPrecioBase();
            }
            @Override public Zona fromString(String s) { return null; }
        });
        cmbResAsiento.setConverter(new StringConverter<Asiento>() {
            @Override public String toString(Asiento a) {
                return a == null ? "— Asiento —" : "Fila " + a.getFila() + " N°" + a.getNumero();
            }
            @Override public Asiento fromString(String s) { return null; }
        });

        cmbResEvento.valueProperty().addListener((obs, oldV, newV) -> {
            cmbResZona.setValue(null);   cmbResZona.getItems().clear();
            cmbResAsiento.setValue(null); cmbResAsiento.getItems().clear();
            if (newV != null && newV.getTheRecinto() != null)
                cmbResZona.setItems(FXCollections.observableArrayList(newV.getTheRecinto().getListaZonas()));
        });

        cmbResZona.valueProperty().addListener((obs, oldV, newV) -> {
            cmbResAsiento.setValue(null); cmbResAsiento.getItems().clear();
            if (newV != null) {
                List<Asiento> disponibles = new java.util.ArrayList<>();
                for (Asiento a : newV.getListaAsientos())
                    if (a.getEstado() == EstadoAsiento.DISPONIBLE) disponibles.add(a);
                cmbResAsiento.setItems(FXCollections.observableArrayList(disponibles));
            }
        });
    }

    private void recargarComboRecintos() {
        cbEvRecinto.setItems(FXCollections.observableArrayList(recintoCtrl.listarRecintos()));
    }

    // Cargar datos (auto-refresh)
    private void cargarTodosDatos() {
        eventosData.setAll(eventoCtrl.listarEventos());
        usuariosData.setAll(usuarioCtrl.listarUsuarios());
        recintosData.setAll(recintoCtrl.listarRecintos());
        comprasData.setAll(compraCtrl.listarCompras());
        incidenciasData.setAll(GestorDatos.getInstancia().getListaIncidencias());

        Compra selActual = cbCompraReporte.getValue();
        cbCompraReporte.setItems(FXCollections.observableArrayList(compraCtrl.listarCompras()));
        if (selActual != null) cbCompraReporte.setValue(selActual);

        // Refresh reserva combos (keep user/event selection, cascade updates the rest)
        cmbResUsuario.setItems(FXCollections.observableArrayList(usuarioCtrl.listarUsuarios()));
        cmbResEvento.setItems(FXCollections.observableArrayList(eventoCtrl.listarEventos()));

        actualizarMetricas();
    }

    // EVENTOS
    @FXML
    private void onCrearEvento() {
        String nombre = txtEvNombre.getText().trim();
        CategoriaEvento cat = cbEvCategoria.getValue();
        Recinto rec = cbEvRecinto.getValue();
        if (nombre.isEmpty() || dpEvFecha.getValue() == null || cat == null || rec == null) {
            setMsg(lblEvMsg, "Complete todos los campos (nombre, fecha, categoria y recinto).", false); return;
        }
        String fecha = String.format("%s %02d:%02d",
                dpEvFecha.getValue().toString(),
                spHora.getValue(), spMinuto.getValue());
        // ciudad viene del recinto, no del evento
        String ciudad = rec.getCiudad();
        Evento e = eventoCtrl.crearEvento(nombre, cat, "Descripcion pendiente",
            ciudad, fecha, rec.getIdRecinto(),
            txtEvInfo1.getText().trim(), txtEvInfo2.getText().trim());
        if (e != null) {
            setMsg(lblEvMsg, "Evento '" + nombre + "' creado en " + ciudad + ".", true);
            eventosData.setAll(eventoCtrl.listarEventos());
            limpiarFormEvento();
        } else {
            setMsg(lblEvMsg, "Error al crear evento.", false);
        }
    }

    @FXML private void onPublicarEvento() { cambiarEstadoEvento(e -> eventoCtrl.publicarEvento(e), "publicado"); }
    @FXML private void onPausarEvento()   { cambiarEstadoEvento(e -> eventoCtrl.pausarEvento(e),   "pausado"); }

    @FXML
    private void onCancelarEvento() {
        Evento sel = tablaEventos.getSelectionModel().getSelectedItem();
        if (sel == null) { setMsg(lblEvMsg, "Seleccione un evento.", false); return; }
        if (eventoCtrl.cancelarEvento(sel.getIdEvento())) {
            int numCompras = sel.getListaCompras().size();
            if (numCompras > 0) {
                registrarIncidencia(
                    "Cancelacion masiva de evento",
                    "este evento: " + sel.getNombre() + ",  fue cancelado con "
                        + numCompras + " compra(s) registrada(s)",
                    "EVENTO", sel.getIdEvento()
                );
            }
            setMsg(lblEvMsg, "Evento cancelado.", true);
            eventosData.setAll(eventoCtrl.listarEventos());
        } else {
            setMsg(lblEvMsg, "operacion no permitida en estado actual.", false);
        }
    }
    @FXML private void onEliminarEvento()  {
        Evento sel = tablaEventos.getSelectionModel().getSelectedItem();
        if (sel == null) { lblEvMsg.setText("selecciona un evento."); return; }
        if (eventoCtrl.eliminarEvento(sel.getIdEvento())) {
            lblEvMsg.setText("evento eliminado.");
            lblEvMsg.setStyle("-fx-text-fill:#2e7d32;");
            eventosData.setAll(eventoCtrl.listarEventos());
        }
    }
    @FXML private void onActualizarPolitica() {
        Evento sel = tablaEventos.getSelectionModel().getSelectedItem();
        if (sel == null) { lblEvMsg.setText("selecciones un evento."); return; }
        sel.setPoliticaCancelacion(txtEvInfo1.getText().isEmpty() ? sel.getPoliticaCancelacion() : txtEvInfo1.getText());
        sel.setPoliticaReembolso(txtEvInfo2.getText().isEmpty() ? sel.getPoliticaReembolso() : txtEvInfo2.getText());
        lblEvMsg.setText("política actualizada.");
        lblEvMsg.setStyle("-fx-text-fill:#2e7d32;");
    }

    @FunctionalInterface interface EventoAction { boolean run(int id); }
    private void cambiarEstadoEvento(EventoAction action, String nombreAccion) {
        Evento sel = tablaEventos.getSelectionModel().getSelectedItem();
        if (sel == null) { lblEvMsg.setText("seleccione un evento."); return; }
        if (action.run(sel.getIdEvento())) {
            lblEvMsg.setText("evento " + nombreAccion + ".");
            lblEvMsg.setStyle("-fx-text-fill:#2e7d32;");
            eventosData.setAll(eventoCtrl.listarEventos());
        } else {
            lblEvMsg.setText("operación no permitida en estado actual.");
            lblEvMsg.setStyle("-fx-text-fill:#c62828;");
        }
    }

    private void limpiarFormEvento() {
        txtEvNombre.clear();
        dpEvFecha.setValue(null);
        spHora.getValueFactory().setValue(20);
        spMinuto.getValueFactory().setValue(0);
        txtEvInfo1.clear(); txtEvInfo2.clear();
        cbEvCategoria.setValue(null); cbEvRecinto.setValue(null);
    }

    // USUARIOS
    @FXML
    private void onRegistrarUsuario() {
        String nombre = txtUsNombre.getText().trim();
        String correo = txtUsCorreo.getText().trim();
        String tel    = txtUsTelefono.getText().trim();
        RolUsuario rol = cbUsRol.getValue();
        if (nombre.isEmpty() || correo.isEmpty()) {
            setMsg(lblUsMsg, " Nombre y correo son obligatorios.", false); return;
        }
        String contrasena = txtUsContrasena != null && !txtUsContrasena.getText().trim().isEmpty()
                ? txtUsContrasena.getText().trim() : "1234";
        Usuario u = usuarioCtrl.registrar(nombre, correo, contrasena, tel, rol != null ? rol : RolUsuario.USUARIO);
        if (u != null) {
            // asignar saldo inicial en efectivo si se ingreso un valor
            if (txtUsSaldo != null && !txtUsSaldo.getText().trim().isEmpty()) {
                try {
                    double saldo = Double.parseDouble(txtUsSaldo.getText().trim().replace(",","").replace("$",""));
                    usuarioCtrl.asignarSaldo(u.getIdUsuario(), saldo);
                } catch (NumberFormatException ignored) {}
            }
            // asignar saldo de tarjeta si se ingreso un valor
            if (txtUsSaldoTarjeta != null && !txtUsSaldoTarjeta.getText().trim().isEmpty()) {
                try {
                    double saldoT = Double.parseDouble(txtUsSaldoTarjeta.getText().trim().replace(",","").replace("$",""));
                    usuarioCtrl.asignarSaldoTarjeta(u.getIdUsuario(), saldoT);
                } catch (NumberFormatException ignored) {}
            }
            // asignar numero de tarjeta si se ingreso
            if (txtUsNumeroTarjeta != null && !txtUsNumeroTarjeta.getText().trim().isEmpty()) {
                usuarioCtrl.asignarNumeroTarjeta(u.getIdUsuario(), txtUsNumeroTarjeta.getText().trim());
            }
            setMsg(lblUsMsg, "Usuario '" + nombre + "' registrado.", true);
            usuariosData.setAll(usuarioCtrl.listarUsuarios());
            limpiarFormUsuario();
        } else {
            setMsg(lblUsMsg, "✖ Correo ya registrado.", false);
        }
    }

    @FXML
    private void onActualizarUsuario() {
        Usuario sel = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (sel == null) { setMsg(lblUsMsg, "seleccione un usuario.", false); return; }

        // Nombre y telefono (correo no se modifica)
        usuarioCtrl.actualizar(sel.getIdUsuario(),
            txtUsNombre.getText().trim(), "", txtUsTelefono.getText().trim());

        // Contrasena
        if (txtUsContrasena != null && !txtUsContrasena.getText().trim().isEmpty())
            usuarioCtrl.actualizarContrasena(sel.getIdUsuario(), txtUsContrasena.getText().trim());

        // Saldo efectivo
        if (txtUsSaldo != null && !txtUsSaldo.getText().trim().isEmpty()) {
            try {
                double saldo = Double.parseDouble(
                    txtUsSaldo.getText().trim().replace(",", "").replace("$", ""));
                usuarioCtrl.asignarSaldo(sel.getIdUsuario(), saldo);
            } catch (NumberFormatException e) {
                setMsg(lblUsMsg, "Saldo efectivo invalido: ingrese solo numeros.", false); return;
            }
        }

        // Saldo tarjeta
        if (txtUsSaldoTarjeta != null && !txtUsSaldoTarjeta.getText().trim().isEmpty()) {
            try {
                double saldoT = Double.parseDouble(
                    txtUsSaldoTarjeta.getText().trim().replace(",", "").replace("$", ""));
                usuarioCtrl.asignarSaldoTarjeta(sel.getIdUsuario(), saldoT);
            } catch (NumberFormatException e) {
                setMsg(lblUsMsg, "Saldo tarjeta invalido: ingrese solo numeros.", false); return;
            }
        }

        // Numero de tarjeta
        if (txtUsNumeroTarjeta != null && !txtUsNumeroTarjeta.getText().trim().isEmpty())
            usuarioCtrl.asignarNumeroTarjeta(sel.getIdUsuario(), txtUsNumeroTarjeta.getText().trim());

        // Rol
        if (cbUsRol.getValue() != null)
            usuarioCtrl.actualizarRol(sel.getIdUsuario(), cbUsRol.getValue());

        setMsg(lblUsMsg, "usuario actualizado.", true);
        usuariosData.setAll(usuarioCtrl.listarUsuarios());
    }

    @FXML
    private void onEliminarUsuario() {
        Usuario sel = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (sel == null) { setMsg(lblUsMsg, "seleccione un usuario.", false); return; }
        usuarioCtrl.eliminar(sel.getIdUsuario());
        setMsg(lblUsMsg, "usuario eliminado.", true);
        usuariosData.setAll(usuarioCtrl.listarUsuarios());
    }

    private void limpiarFormUsuario() {
        txtUsNombre.clear(); txtUsCorreo.clear(); txtUsTelefono.clear();
        if (txtUsContrasena    != null) txtUsContrasena.clear();
        if (txtUsSaldo         != null) txtUsSaldo.clear();
        if (txtUsSaldoTarjeta  != null) txtUsSaldoTarjeta.clear();
        if (txtUsNumeroTarjeta != null) txtUsNumeroTarjeta.clear();
    }

    // RECINTOS
    @FXML
    private void onCrearRecinto() {
        String nombre = txtReNombre.getText().trim();
        String dir    = txtReDireccion.getText().trim();
        String ciudad = txtReCiudad.getText().trim();
        if (nombre.isEmpty() || ciudad.isEmpty()) {
            setMsg(lblReMsg, " nombre y ciudad son obligatorios.", false); return;
        }
        Recinto r = recintoCtrl.crearRecinto(nombre, dir, ciudad);
        if (r != null) {
            setMsg(lblReMsg, " recinto '" + nombre + "' creado.", true);
            recintosData.setAll(recintoCtrl.listarRecintos());
            recargarComboRecintos();
            txtReNombre.clear(); txtReDireccion.clear(); txtReCiudad.clear();
        }
    }

    @FXML
    private void onAgregarZona() {
        Recinto sel = tablaRecintos.getSelectionModel().getSelectedItem();
        if (sel == null) { setMsg(lblReMsg, "seleccione un recinto primero.", false); return; }
        String tipo   = cbZoTipo.getValue();
        String capStr = txtZoCapacidad.getText().trim();
        if (tipo == null || capStr.isEmpty()) {
            setMsg(lblReMsg, "seleccione tipo y cantidad de asientos.", false); return;
        }
        try {
            int cap = Integer.parseInt(capStr);
            if (cap <= 0) { setMsg(lblReMsg, "la capacidad debe ser mayor a 0.", false); return; }
            double precio = PRECIOS_ZONA.getOrDefault(tipo, 50_000.0);
            String seccion = txtZoSeccion.getText().trim();
            String nombreZona = seccion.isEmpty() ? tipo : tipo + " " + seccion;
            recintoCtrl.agregarZona(sel.getIdRecinto(), nombreZona, cap, precio);
            // Auto-generar asientos para la zona recien creada
            List<Zona> zonas = sel.getListaZonas();
            if (!zonas.isEmpty()) {
                generarAsientos(zonas.get(zonas.size() - 1), cap);
            }
            setMsg(lblReMsg, "zona '" + nombreZona + "' creada con " + cap + " asientos ($" + (long)precio + " c/u).", true);
            recintosData.setAll(recintoCtrl.listarRecintos());
            cbZoTipo.setValue(null);
            txtZoSeccion.clear();
            txtZoCapacidad.clear();
        } catch (NumberFormatException e) {
            setMsg(lblReMsg, "la cantidad de asientos debe ser un numero entero.", false);
        }
    }

    private void generarAsientos(Zona zona, int capacidad) {
        AsientoController asientoCtrl = new AsientoController();
        String[] filas = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T"};
        int porFila = 10;
        int restantes = capacidad;
        int filaIdx = 0;
        while (restantes > 0 && filaIdx < filas.length) {
            int enFila = Math.min(porFila, restantes);
            for (int num = 1; num <= enFila; num++) {
                asientoCtrl.crearAsiento(zona.getIdZona(), filas[filaIdx], num);
            }
            restantes -= enFila;
            filaIdx++;
        }
    }

    @FXML
    private void onEliminarRecinto() {
        Recinto sel = tablaRecintos.getSelectionModel().getSelectedItem();
        if (sel == null) { setMsg(lblReMsg, "seleccione un recinto.", false); return; }
        recintoCtrl.eliminarRecinto(sel.getIdRecinto());
        setMsg(lblReMsg, " recinto eliminado.", true);
        recintosData.setAll(recintoCtrl.listarRecintos());
        recargarComboRecintos();
    }

    // COMPRAS
    @FXML private void onCrearReserva() {
        Usuario u  = cmbResUsuario.getValue();
        Evento  ev = cmbResEvento.getValue();
        Zona    z  = cmbResZona.getValue();
        Asiento a  = cmbResAsiento.getValue();
        if (u == null || ev == null || z == null || a == null) {
            setMsg(lblResMsg, "seleccione usuario, evento, zona y asiento.", false); return;
        }
        Compra compra = compraCtrl.crearCompra(
            u.getIdUsuario(), ev.getIdEvento(), z.getIdZona(), a.getIdAsiento());
        if (compra == null) {
            setMsg(lblResMsg, "no se pudo crear la reserva (asiento ya no disponible).", false); return;
        }
        setMsg(lblResMsg, "reserva #" + compra.getIdCompra() + " creada — estado CREADA.", true);
        cmbResAsiento.getItems().remove(a);
        cmbResAsiento.setValue(null);
        comprasData.setAll(compraCtrl.listarCompras());
    }

    @FXML private void onPagarCompra() {
        Compra sel = tablaCompras.getSelectionModel().getSelectedItem();
        if (sel == null) { setMsg(lblCoMsg, "seleccione una compra.", false); return; }
        if (sel.getEstadoActual() != EstadoCompra.CREADA) {
            setMsg(lblCoMsg, "solo se pueden pagar compras en estado CREADA.", false); return;
        }
        Usuario u = sel.getTheUsuario();
        if (u == null) { setMsg(lblCoMsg, "la compra no tiene usuario asociado.", false); return; }
        if (u.getSaldo() < sel.getTotal()) {
            setMsg(lblCoMsg, "saldo insuficiente del usuario ($"
                    + NumberFormat.getInstance(new java.util.Locale("es","CO")).format((long) u.getSaldo())
                    + " disponible, $"
                    + NumberFormat.getInstance(new java.util.Locale("es","CO")).format((long) sel.getTotal())
                    + " requerido).", false);
            return;
        }
        boolean ok = compraCtrl.pagarCompra(sel.getIdCompra(),
                new com.pgii.model.patterns.behavioral.PagoEfectivo("Admin-Pago"));
        if (!ok) { setMsg(lblCoMsg, "no se pudo procesar el pago.", false); return; }
        usuarioCtrl.cobrar(u.getIdUsuario(), sel.getTotal());
        setMsg(lblCoMsg, "pago procesado. La compra ahora esta en PAGADA — puede confirmarla.", true);
        comprasData.setAll(compraCtrl.listarCompras());
        usuariosData.setAll(usuarioCtrl.listarUsuarios());
        tablaCompras.refresh();
    }

    @FXML private void onConfirmarCompra() {
        Compra sel = tablaCompras.getSelectionModel().getSelectedItem();
        if (sel == null) { setMsg(lblCoMsg, "seleccione una compra.", false); return; }
        boolean ok = compraCtrl.confirmarCompra(sel.getIdCompra());
        if (ok) setMsg(lblCoMsg, "compra confirmada.", true);
        else    setMsg(lblCoMsg, "no se puede confirmar (la compra debe estar en estado PAGADA).", false);
        comprasData.setAll(compraCtrl.listarCompras());
        tablaCompras.refresh();
    }

    @FXML private void onCancelarCompra() {
        Compra sel = tablaCompras.getSelectionModel().getSelectedItem();
        if (sel == null) { setMsg(lblCoMsg, "seleccione una compra.", false); return; }
        EstadoCompra estadoPrevio = sel.getEstadoActual();
        // Patron Command: ejecutar primero para verificar si el estado permite cancelar
        ICommand cmd = new CancelarCompraCommand(sel);
        cmd.ejecutar();
        if (sel.getEstadoActual() == EstadoCompra.CANCELADA) {
            // Liberar asientos solo si la cancelacion fue exitosa
            AsientoController aCtrl = new AsientoController();
            for (Entrada entrada : sel.getListaEntradas())
                if (entrada.getTheAsiento() != null)
                    aCtrl.liberarAsiento(entrada.getTheAsiento().getIdAsiento());
            historialComandos.push(cmd);
            setMsg(lblCoMsg, "compra cancelada. Puede deshacer con 'Deshacer'.", true);
        } else {
            setMsg(lblCoMsg, "no se puede cancelar en estado " + estadoPrevio + ".", false);
        }
        comprasData.setAll(compraCtrl.listarCompras());
        tablaCompras.refresh();
    }

    @FXML private void onDeshacerUltimo() {
        if (historialComandos.isEmpty()) { setMsg(lblCoMsg, "no hay acciones para deshacer.", false); return; }
        historialComandos.pop().deshacer();
        setMsg(lblCoMsg, "ultima accion deshecha.", true);
        comprasData.setAll(compraCtrl.listarCompras());
        tablaCompras.refresh();
    }

    @FXML private void onReembolsarCompra() {
        Compra sel = tablaCompras.getSelectionModel().getSelectedItem();
        if (sel == null) { setMsg(lblCoMsg, "seleccione una compra.", false); return; }
        boolean ok = compraCtrl.reembolsarCompra(sel.getIdCompra());
        if (!ok) { setMsg(lblCoMsg, "operacion no permitida en estado actual.", false); return; }
        // Observer: notificar al usuario que realizo la compra
        Evento ev = sel.getTheEvento();
        if (ev != null && sel.getTheUsuario() != null) {
            String nombreComprador = sel.getTheUsuario().getNombre();
            boolean yaRegistrado = ev.getListaObservers().stream()
                    .anyMatch(o -> o instanceof NotificacionObserver
                            && ((NotificacionObserver) o).getNombre().equals(nombreComprador));
            if (!yaRegistrado)
                ev.agregarObserver(new NotificacionObserver(nombreComprador));
            ev.notificarObservers("Reembolso procesado por administrador: tu compra #"
                    + sel.getIdCompra() + " por $"
                    + NumberFormat.getInstance(new java.util.Locale("es","CO")).format((long) sel.getTotal())
                    + " ha sido devuelta a tu cuenta.");
            Main.mostrarNotificaciones(ev);
        }
        setMsg(lblCoMsg, "reembolso procesado.", true);
        comprasData.setAll(compraCtrl.listarCompras());
        usuariosData.setAll(usuarioCtrl.listarUsuarios());
        tablaCompras.refresh();
    }

    // REPORTES (patrón Decorator igual que JavaCourse)
    @FXML
    private void onGenerarReporte() {
        String tipo = cbTipoReporte.getValue();
        boolean conEnc  = chkEncabezado.isSelected();
        boolean conFirma = chkFirma.isSelected();
        String reporte;
        if ("Reporte de Compra".equals(tipo)) {
            Compra sel = cbCompraReporte.getValue();
            if (sel == null) { setMsg(lblRepMsg, "Seleccione una compra.", false); return; }
            reporte = facade.generarReporteCompra(sel.getIdCompra(), conEnc, conFirma);
        } else {
            reporte = facade.generarReporteEventos(conEnc, conFirma);
        }
        areaReporte.setText(reporte);
        setMsg(lblRepMsg, " Reporte generado.", true);
    }

    @FXML
    private void onExportarTXT() {
        String contenido = areaReporte.getText();
        if (contenido.isEmpty()) { setMsg(lblRepMsg, "Genere un reporte primero.", false); return; }
        String footer = "\n\n-----------\n"
                      + "  TuBoletaRobinson - Sistema de Gestion de Eventos\n"
                      + "-----------\n";
        String archivo = "reporte_pgii_" + System.currentTimeMillis() + ".txt";
        if (reporteCtrl.exportarTXT(contenido + footer, archivo)) {
            copiarLogoJunto(archivo.replace(".txt", "_logo.png"));
            setMsg(lblRepMsg, "TXT guardado: " + archivo, true);
        } else {
            setMsg(lblRepMsg, "error al exportar TXT.", false);
        }
    }

    @FXML
    private void onExportarCSV() {
        String tipo    = cbTipoReporte.getValue();
        String archivo = "reporte_pgii_" + System.currentTimeMillis() + ".csv";
        StringBuilder csv = new StringBuilder();

        if ("Reporte de Compra".equals(tipo)) {
            Compra sel = cbCompraReporte.getValue();
            if (sel == null) { setMsg(lblRepMsg, "Seleccione una compra.", false); return; }

            csv.append("Campo,Valor\n");
            csv.append("ID Compra,").append(sel.getIdCompra()).append("\n");
            csv.append("Usuario,").append(csvCampo(sel.getTheUsuario() != null ? sel.getTheUsuario().getNombre() : "")).append("\n");
            csv.append("Evento,").append(csvCampo(sel.getTheEvento() != null ? sel.getTheEvento().getNombre() : "")).append("\n");
            csv.append("Fecha,").append(sel.getFechaCreacion()).append("\n");
            csv.append("Total,$").append((long) sel.getTotal()).append("\n");
            csv.append("Estado,").append(sel.getEstadoActual() != null ? sel.getEstadoActual().name() : "").append("\n");
            csv.append("Metodo de pago,").append(csvCampo(sel.getThePago() != null ? sel.getThePago().getMetodoPago() : "")).append("\n");
            csv.append("\n");
            csv.append("ID Entrada,Zona,Fila,Asiento,Precio\n");
            for (Entrada e : sel.getListaEntradas()) {
                csv.append(e.getIdEntrada()).append(",");
                csv.append(csvCampo(e.getTheZona() != null ? e.getTheZona().getNombre() : "")).append(",");
                csv.append(e.getTheAsiento() != null ? e.getTheAsiento().getFila() : "").append(",");
                csv.append(e.getTheAsiento() != null ? e.getTheAsiento().getNumero() : "").append(",");
                csv.append("$").append((long) e.getPrecioFinal()).append("\n");
            }
            if (!sel.getListaServicios().isEmpty()) {
                csv.append("\nLos servicios adicionales\n");
                for (TipoServicio s : sel.getListaServicios())
                    csv.append(s.name()).append("\n");
            }
        } else {
            csv.append("ID,Nombre,Categoria,Ciudad,Fecha,Estado,Recinto,Compras,Precio Min\n");
            for (Evento e : eventoCtrl.listarEventos()) {
                String ciudad  = e.getTheRecinto() != null ? e.getTheRecinto().getCiudad() : e.getCiudad();
                String recinto = e.getTheRecinto() != null ? e.getTheRecinto().getNombre() : "";
                double min     = e.getTheRecinto() != null
                    ? e.getTheRecinto().getListaZonas().stream().mapToDouble(Zona::getPrecioBase).min().orElse(0)
                    : 0;
                csv.append(e.getIdEvento()).append(",");
                csv.append(csvCampo(e.getNombre())).append(",");
                csv.append(e.getCategoria().name()).append(",");
                csv.append(csvCampo(ciudad)).append(",");
                csv.append(e.getFechaHora()).append(",");
                csv.append(e.getEstado().name()).append(",");
                csv.append(csvCampo(recinto)).append(",");
                csv.append(e.getListaCompras().size()).append(",");
                csv.append("$").append((long) min).append("\n");
            }
        }

        csv.append("\n# TuBoletaRobinson - Sistema de Gestion de Eventos\n");

        try (java.io.FileWriter fw = new java.io.FileWriter(archivo)) {
            fw.write(csv.toString());
            copiarLogoJunto(archivo.replace(".csv", "_logo.png"));
            // Usar ExportadorCSV (patron Adapter) para dump completo de compras junto al reporte
            String dump = "compras_dump_" + System.currentTimeMillis() + ".csv";
            reporteCtrl.exportarCSV(dump);
            setMsg(lblRepMsg, "CSV guardado: " + archivo + " | Dump: " + dump, true);
        } catch (Exception e) {
            setMsg(lblRepMsg, "Error al exportar CSV: " + e.getMessage(), false);
        }
    }

    private String csvCampo(String s) {
        if (s == null || s.isEmpty()) return "";
        if (s.contains(",") || s.contains("\"") || s.contains("\n"))
            return "\"" + s.replace("\"", "\"\"") + "\"";
        return s;
    }

    private void copiarLogoJunto(String destino) {
        try (java.io.InputStream is = getClass().getResourceAsStream("/com/pgii/logo.png")) {
            if (is != null)
                java.nio.file.Files.copy(is, java.nio.file.Paths.get(destino),
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception ignored) {}
    }

    @FXML
    private void onExportarPDF() {
        String contenido = areaReporte.getText();
        if (contenido.isEmpty()) { setMsg(lblRepMsg, "genera un reporte primero.", false); return; }
        String archivo = "reporte TuBoletaRobinson" + System.currentTimeMillis() + ".pdf";
        try {
            org.apache.pdfbox.pdmodel.PDDocument doc = new org.apache.pdfbox.pdmodel.PDDocument();
            String[] lineas = contenido.split("\n");
            float margenX = 40, margenY = 750, espaciado = 14, limiteY = 180;
            float y = margenY;
            org.apache.pdfbox.pdmodel.PDPage pag = new org.apache.pdfbox.pdmodel.PDPage();
            doc.addPage(pag);
            org.apache.pdfbox.pdmodel.PDPageContentStream cs =
                new org.apache.pdfbox.pdmodel.PDPageContentStream(doc, pag);
            cs.setFont(org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA, 10);
            cs.beginText();
            cs.newLineAtOffset(margenX, y);

            for (String linea : lineas) {
                if (y <= limiteY) {
                    cs.endText(); cs.close();
                    pag = new org.apache.pdfbox.pdmodel.PDPage();
                    doc.addPage(pag);
                    cs = new org.apache.pdfbox.pdmodel.PDPageContentStream(doc, pag);
                    cs.setFont(org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA, 10);
                    cs.beginText();
                    y = margenY;
                    cs.newLineAtOffset(margenX, y);
                }
                String ascii = linea
                    .replace("á","a").replace("é","e").replace("í","i")
                    .replace("ó","o").replace("ú","u").replace("ñ","n")
                    .replace("Á","A").replace("É","E").replace("Í","I")
                    .replace("Ó","O").replace("Ú","U").replace("Ñ","N")
                    .replaceAll("[^\\x20-\\x7E]", "");
                cs.showText(ascii);
                cs.newLineAtOffset(0, -espaciado);
                y -= espaciado;
            }
            cs.endText(); cs.close();

            // Agregar logo al final (en la misma pagina si hay espacio, si no en pagina nueva)
            java.io.InputStream imgStream = getClass().getResourceAsStream("/com/pgii/logo.png");
            if (imgStream != null) {
                byte[] imgBytes = imgStream.readAllBytes();
                imgStream.close();
                org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject logo =
                    org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject
                        .createFromByteArray(doc, imgBytes, "logo");

                float pageW = pag.getMediaBox().getWidth();
                float maxImgW = pageW - 80;
                float scale  = Math.min(1f, maxImgW / logo.getWidth());
                float imgW   = logo.getWidth()  * scale;
                float imgH   = logo.getHeight() * scale;
                float imgX   = (pageW - imgW) / 2f;
                // Si no hay suficiente espacio en la ultima pagina, agregar pagina nueva
                org.apache.pdfbox.pdmodel.PDPage logoPage;
                float imgY;
                if (y > imgH + 30) {
                    logoPage = pag;
                    imgY = y - imgH - 10;
                } else {
                    logoPage = new org.apache.pdfbox.pdmodel.PDPage();
                    doc.addPage(logoPage);
                    imgY = (logoPage.getMediaBox().getHeight() - imgH) / 2f;
                }
                org.apache.pdfbox.pdmodel.PDPageContentStream logoCs =
                    new org.apache.pdfbox.pdmodel.PDPageContentStream(
                        doc, logoPage,
                        org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode.APPEND,
                        true, false
                    );
                logoCs.drawImage(logo, imgX, imgY, imgW, imgH);
                logoCs.close();
            }

            doc.save(archivo); doc.close();
            // Usar ExportadorPDF (patron Adapter) para dump completo de compras junto al reporte
            String dump = "compras_dump_" + System.currentTimeMillis() + ".pdf";
            reporteCtrl.exportarPDF(dump);
            setMsg(lblRepMsg, "PDF guardado: " + archivo + " | Dump: " + dump, true);
        } catch (Exception e) {
            setMsg(lblRepMsg, "Error al exportar PDF: " + e.getMessage(), false);
        }
    }

    @FXML private void onLimpiarReporte() { areaReporte.clear(); lblRepMsg.setText(""); }

    // MÉTRICAS
    private void actualizarMetricas() {
        // Fila 1 de KPI cards
        panelKpis.getChildren().clear();
        panelKpis.getChildren().addAll(
            kpiCard("Total Ventas",    "$" + NumberFormat.getInstance(new java.util.Locale("es","CO")).format((long)metricaCtrl.getTotalVentas())),
            kpiCard("Compras",         String.valueOf(metricaCtrl.getTotalCompras())),
            kpiCard("Cancelaciones",   String.valueOf(metricaCtrl.getTotalCancelaciones())),
            kpiCard("Top Evento",      metricaCtrl.getTopEvento()),
            kpiCard("Servicios VIP",   String.valueOf(metricaCtrl.getConteoServicio(TipoServicio.VIP)))
        );

        // Fila 2 de KPI cards con nuevas metricas
        panelKpis2.getChildren().clear();
        panelKpis2.getChildren().addAll(
            kpiCard("Entradas Vendidas", String.valueOf(metricaCtrl.getTotalEntradasVendidas())),
            kpiCard("Promedio por Compra", "$" + NumberFormat.getInstance(new java.util.Locale("es","CO")).format((long)metricaCtrl.getIngresosPromedioPorCompra()))
        );

        // Bar chart: compras por evento
        barChart.getData().clear();
        XYChart.Series<String,Number> serie = new XYChart.Series<>();
        serie.setName("Compras");
        List<Evento> evs = metricaCtrl.getListaEventosConCompras();
        for (Evento e : evs)
            serie.getData().add(new XYChart.Data<>(
                e.getNombre().length()>20 ? e.getNombre().substring(0,20)+"…" : e.getNombre(),
                e.getListaCompras().size()));
        barChart.getData().add(serie);

        // Pie chart: servicios adicionales
        pieChart.getData().clear();
        for (TipoServicio s : TipoServicio.values()) {
            int n = metricaCtrl.getConteoServicio(s);
            if (n > 0) pieChart.getData().add(new PieChart.Data(s.name(), n));
        }
    }

    private VBox kpiCard(String titulo, String valor) {
        Label vLabel = new Label(valor);
        vLabel.setStyle("-fx-text-fill:#c62828; -fx-font-size:20; -fx-font-weight:bold;");
        Label tLabel = new Label(titulo);
        tLabel.setStyle("-fx-text-fill:#a8a8b3; -fx-font-size:11;");
        VBox card = new VBox(4, vLabel, tLabel);
        card.setStyle("-fx-background-color:#16213e; -fx-background-radius:10; -fx-padding:14 20; -fx-alignment:center;");
        card.setPrefWidth(170);
        return card;
    }

    // INCIDENCIAS
    @FXML
    private void onRegistrarIncidencia() {
        String tipo    = txtIncTipo.getText().trim();
        String desc    = txtIncDesc.getText().trim();
        String entidad = txtIncEntidad.getText().trim();
        String idEnt   = txtIncIdEnt.getText().trim();
        if (tipo.isEmpty() || desc.isEmpty()) {
            setMsg(lblIncMsg, "Tipo y descripción son obligatorios.", false); return;
        }
        int idEntidad = 0;
        try { idEntidad = Integer.parseInt(idEnt); } catch (Exception e) {}
        GestorDatos gestor = GestorDatos.getInstancia();
        Incidencia inc = new Incidencia(
            gestor.generarIdIncidencia(), tipo, desc,
            LocalDate.now().toString(), entidad, idEntidad);
        gestor.getListaIncidencias().add(inc);
        setMsg(lblIncMsg, "Se registro correctamente la incidencia registrada.", true);
        incidenciasData.setAll(GestorDatos.getInstancia().getListaIncidencias());
        txtIncTipo.clear(); txtIncDesc.clear(); txtIncEntidad.clear(); txtIncIdEnt.clear();
    }

    // Misc
    @FXML private void onVerEventos()  { app.mostrarListaEventos(); }
    @FXML private void onCerrarApp()   { app.mostrarPantallaFinal(); }
    @FXML private void onCerrarSesion() {
        AppState.getInstancia().cerrarSesion();
        app.mostrarLogin();
    }

    private void setMsg(Label lbl, String texto, boolean ok) {
        lbl.setText(texto);
        lbl.setStyle(ok ? "-fx-text-fill:#2e7d32;" : "-fx-text-fill:#c62828;");
    }

    private void registrarIncidencia(String tipo, String desc, String entidad, int idEntidad) {
        GestorDatos g = GestorDatos.getInstancia();
        g.getListaIncidencias().add(new Incidencia(
            g.generarIdIncidencia(), tipo, desc,
            LocalDate.now().toString(), entidad, idEntidad
        ));
    }

    public void setApp(Main app) { this.app = app; }
}
