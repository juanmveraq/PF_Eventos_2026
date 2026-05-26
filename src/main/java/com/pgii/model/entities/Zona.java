package com.pgii.model.entities;
import com.pgii.model.enums.EstadoAsiento;
import java.util.ArrayList;
import java.util.List;

public class Zona {
    private int idZona;
    private String nombre;
    private int capacidad;
    private double precioBase;
    private List<Asiento> listaAsientos;
    private Recinto ownedByRecinto;

    public Zona(int idZona, String nombre, int capacidad, double precioBase, Recinto ownedByRecinto) {
        this.idZona = idZona;
        this.nombre = nombre;
        this.capacidad = capacidad;
        this.precioBase = precioBase;
        this.listaAsientos = new ArrayList<>();
        this.ownedByRecinto = ownedByRecinto;
    }

    // TODO: agregarAsiento(), eliminarAsiento(), buscarAsientoPorId(), getCapacidadDisponible()
    // TODO: getters, setters y toString
}
