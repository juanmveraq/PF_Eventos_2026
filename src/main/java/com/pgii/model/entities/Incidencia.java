package com.pgii.model.entities;

public class Incidencia {
    private int idIncidencia;
    private String tipo;
    private String descripcion;
    private String fecha;
    private String tipoEntidadAfectada;
    private int idEntidadAfectada;

    public Incidencia(int id, String tipo, String desc, String fecha, String entidad, int idEntidad) {
        this.idIncidencia = id;
        this.tipo = tipo;
        this.descripcion = desc;
        this.fecha = fecha;
        this.tipoEntidadAfectada = entidad;
        this.idEntidadAfectada = idEntidad;
    }

    // TODO: getters y setters
}
