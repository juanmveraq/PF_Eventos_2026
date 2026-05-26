package com.pgii.model.entities;
import java.util.ArrayList;
import java.util.List;

public class Recinto {
    private int idRecinto;
    private String nombre;
    private String direccion;
    private String ciudad;
    private List<Zona> listaZonas;

    public Recinto(int idRecinto, String nombre, String direccion, String ciudad) {
        this.idRecinto = idRecinto;
        this.nombre = nombre;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.listaZonas = new ArrayList<>();
    }

    // TODO: agregarZona(), eliminarZona(), buscarZonaPorId(), getCapacidadTotal()
    // TODO: getters, setters y toString
}
