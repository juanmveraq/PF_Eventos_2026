package com.pgii.view.viewcontroller;

import com.pgii.Main;
import com.pgii.model.entities.Asiento;
import com.pgii.model.entities.Evento;
import com.pgii.model.entities.Recinto;
import com.pgii.model.entities.Zona;
import com.pgii.model.enums.EstadoAsiento;
import com.pgii.view.AppState;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SeleccionAsientoViewController {

    private Main  app;
    private Evento evento;

    // Local multi-select state — pushed to AppState only on confirm
    private final LinkedHashMap<Integer, Button>  selectedButtons  = new LinkedHashMap<>();
    private final LinkedHashMap<Integer, Asiento> selectedAsientos = new LinkedHashMap<>();
    private final LinkedHashMap<Integer, Zona>    selectedZonas    = new LinkedHashMap<>();
    private String selectedCategory = null;

    @FXML private VBox   contenedorMapa;
    @FXML private Label  lblNombreEvento;
    @FXML private Label  lblNombreRecinto;
    @FXML private Label  lblAsientoSeleccionado;
    @FXML private Label  lblZonaSeleccionada;
    @FXML private Label  lblPrecioSeleccionado;
    @FXML private Button btnContinuar;

    @FXML
    public void initialize() {}

    public void setEvento(Evento e) {
        this.evento = e;
        AppState.getInstancia().limpiarSeleccionAsientos();
        lblNombreEvento.setText(e.getNombre());
        if (e.getTheRecinto() != null) {
            lblNombreRecinto.setText("Recinto: " + e.getTheRecinto().getNombre()
                    + " — " + e.getTheRecinto().getCiudad());
            construirMapaAsientos(e.getTheRecinto());
        }
    }

    private void construirMapaAsientos(Recinto recinto) {
        List<Zona> zonasVIP   = new ArrayList<>();
        List<Zona> zonasPref  = new ArrayList<>();
        List<Zona> zonasGen   = new ArrayList<>();
        List<Zona> zonasOtras = new ArrayList<>();

        for (Zona z : recinto.getListaZonas()) {
            String n = z.getNombre().toUpperCase();
            if      (n.contains("VIP"))     zonasVIP.add(z);
            else if (n.contains("PREFER"))  zonasPref.add(z);
            else if (n.contains("GENERAL")) zonasGen.add(z);
            else                            zonasOtras.add(z);
        }

        if (!zonasVIP.isEmpty())
            contenedorMapa.getChildren().add(crearFilaZonas("VIP",          zonasVIP,  "#f9a825"));
        if (!zonasPref.isEmpty())
            contenedorMapa.getChildren().add(crearFilaZonas("Preferencial", zonasPref, "#6a1b9a"));
        if (!zonasGen.isEmpty())
            contenedorMapa.getChildren().add(crearFilaZonas("General",      zonasGen,  "#1565c0"));
        for (Zona z : zonasOtras) {
            List<Zona> una = new ArrayList<>(); una.add(z);
            contenedorMapa.getChildren().add(crearFilaZonas(z.getNombre(), una, "#1565c0"));
        }
    }

    private HBox crearFilaZonas(String tipoLabel, List<Zona> zonas, String color) {
        HBox fila = new HBox(20);
        fila.setAlignment(Pos.CENTER);
        fila.setPadding(new Insets(6, 12, 6, 12));
        fila.setStyle("-fx-background-color:#f0f0f0; -fx-background-radius:6; -fx-border-color:#e0e0e0; -fx-border-radius:6;");

        Label lblTipo = new Label(tipoLabel);
        lblTipo.setMinWidth(110);
        lblTipo.setPrefWidth(110);
        lblTipo.setAlignment(Pos.CENTER_RIGHT);
        lblTipo.setStyle("-fx-text-fill:" + color + "; -fx-font-weight:bold; -fx-font-size:14;");
        fila.getChildren().add(lblTipo);

        for (Zona zona : zonas) {
            fila.getChildren().add(crearBloqueZona(zona, color));
        }
        return fila;
    }

    private VBox crearBloqueZona(Zona zona, String color) {
        VBox bloque = new VBox(4);
        bloque.setAlignment(Pos.CENTER);

        GridPane grid = new GridPane();
        grid.setHgap(3);
        grid.setVgap(3);
        grid.setAlignment(Pos.CENTER);

        List<Asiento> asientos = zona.getListaAsientos();
        int cols = 6;
        for (int i = 0; i < asientos.size(); i++) {
            Asiento a = asientos.get(i);
            grid.add(crearBotonAsiento(a, zona, color), i % cols, i / cols);
        }

        String seccion = extraerSeccion(zona.getNombre());
        Label lblSec = new Label(seccion);
        lblSec.setStyle("-fx-text-fill:#9090a8; -fx-font-size:10;");
        lblSec.setAlignment(Pos.CENTER);
        lblSec.setMaxWidth(Double.MAX_VALUE);

        bloque.getChildren().addAll(grid, lblSec);
        return bloque;
    }

    private Button crearBotonAsiento(Asiento asiento, Zona zona, String colorZona) {
        Button btn = new Button(String.format("%02d", asiento.getNumero()));
        btn.setMinWidth(30); btn.setPrefWidth(30);
        btn.setMinHeight(26); btn.setPrefHeight(26);
        btn.setFont(Font.font("System", FontWeight.BOLD, 9));

        EstadoAsiento estado = asiento.getEstado();
        switch (estado) {
            case DISPONIBLE -> {
                btn.setStyle("-fx-background-color:" + colorZona +
                        "; -fx-text-fill:white; -fx-background-radius:4; -fx-cursor:hand;");
                btn.setOnAction(ev -> toggleAsiento(btn, asiento, zona));
            }
            case RESERVADO, VENDIDO ->  {
                btn.setStyle("-fx-background-color:#bdbdbd; -fx-text-fill:#757575; -fx-background-radius:3;");
                btn.setDisable(true);
            }
            case BLOQUEADO -> {
                btn.setStyle("-fx-background-color:#ef9a9a; -fx-text-fill:#c62828; -fx-background-radius:3;");
                btn.setDisable(true);
            }
        }
        return btn;
    }

    private void toggleAsiento(Button btn, Asiento asiento, Zona zona) {
        String cat = categoriaDe(zona.getNombre());
        int id = asiento.getIdAsiento();
        String colorZona = colorPorZona(zona.getNombre());

        if (selectedButtons.containsKey(id)) {
            // Deselect
            selectedButtons.remove(id);
            selectedAsientos.remove(id);
            selectedZonas.remove(id);
            if (selectedButtons.isEmpty()) selectedCategory = null;
            btn.setStyle("-fx-background-color:" + colorZona +
                    "; -fx-text-fill:white; -fx-background-radius:4; -fx-cursor:hand;");
        } else {
            if (selectedCategory != null && !selectedCategory.equals(cat)) {
                lblAsientoSeleccionado.setText(
                        "solo puede seleccionar asientos de la misma categoria (" + selectedCategory + ")");
                return;
            }
            selectedCategory = cat;
            selectedButtons.put(id, btn);
            selectedAsientos.put(id, asiento);
            selectedZonas.put(id, zona);
            btn.setStyle("-fx-background-color:" + colorZona +
                    "; -fx-text-fill:white; -fx-background-radius:4; -fx-cursor:hand;" +
                    "-fx-border-color:#c0d0f0; -fx-border-width:2; -fx-border-radius:4;");
        }
        actualizarInfoBar();
    }

    private void actualizarInfoBar() {
        int count = selectedButtons.size();
        if (count == 0) {
            lblAsientoSeleccionado.setText("Haga clic en un asiento");
            lblZonaSeleccionada.setText("—");
            lblPrecioSeleccionado.setText("—");
            btnContinuar.setDisable(true);
        } else {
            lblAsientoSeleccionado.setText(count + " asiento(s) seleccionado(s)");
            lblZonaSeleccionada.setText(selectedCategory != null ? selectedCategory : "—");
            double total = selectedZonas.values().stream().mapToDouble(Zona::getPrecioBase).sum();
            lblPrecioSeleccionado.setText(
                    "$" + NumberFormat.getInstance(new Locale("es", "CO")).format((long) total));
            btnContinuar.setDisable(false);
        }
    }

    private String categoriaDe(String nombreZona) {
        String n = nombreZona.toUpperCase();
        if (n.contains("VIP"))     return "VIP";
        if (n.contains("PREFER"))  return "Preferencial";
        if (n.contains("GENERAL")) return "General";
        return "Otra";
    }

    private String colorPorZona(String nombreZona) {
        String n = nombreZona.toUpperCase();
        if (n.contains("VIP"))     return "#f9a825";
        if (n.contains("PREFER"))  return "#6a1b9a";
        if (n.contains("GENERAL")) return "#1565c0";
        return "#1565c0";
    }

    private String extraerSeccion(String nombre) {
        String[] partes = nombre.split(" ", 2);
        return partes.length > 1 ? partes[1] : nombre;
    }

    @FXML
    private void onContinuar() {
        if (selectedAsientos.isEmpty()) return;
        AppState state = AppState.getInstancia();
        state.limpiarSeleccionAsientos();
        for (Map.Entry<Integer, Asiento> entry : selectedAsientos.entrySet()) {
            state.agregarAsiento(entry.getValue(), selectedZonas.get(entry.getKey()), selectedCategory);
        }
        app.mostrarCompra(evento);
    }

    @FXML
    private void onVolver() { app.mostrarDetalleEvento(evento); }

    public void setApp(Main app) { this.app = app; }
}
