package com.pgii.model.entities;

/**
 * representa las inicidencias y problemas que surjan, que quedan registrados en la plataforma
 * permite llevar un historial de eventos anomalos asociados a entidades del sistema
 * como compras, eventos o usuarios.
 */
public class Incidencia {

    /** identificador unico de la incidencia generado por GestorDatos. */
    private int idIncidencia;

    /** tipo de incidencia (como pueden ser "FRAUDE", "ERROR_PAGO", "RECLAMO"). */
    private String tipo;

    /** descripcion detallada del problema registrado. */
    private String descripcion;

    /** fecha en que ocurrio la incidencia. */
    private String fecha;

    /** nombre del tipo de entidad afectada (en esta puede poner cosas como "Compra", "Usuario"). */
    private String tipoEntidadAfectada;

    /** identificador de la entidad especifica que fue afectada por la incidencia. */
    private int idEntidadAfectada;

    public Incidencia(int id, String tipo, String desc, String fecha, String entidad, int idEntidad) {
        this.idIncidencia=id; this.tipo=tipo; this.descripcion=desc;
        this.fecha=fecha; this.tipoEntidadAfectada=entidad; this.idEntidadAfectada=idEntidad;
    }

    /** retorna el id de la incidencia. */
    public int getIdIncidencia() { return idIncidencia; }
    /** retorna el tipo de incidencia. */
    public String getTipo() { return tipo; }
    /** asigna el tipo de incidencia. */
    public void setTipo(String v) { tipo=v; }
    /** retorna la descripcion de la incidencia. */
    public String getDescripcion() { return descripcion; }
    /** asigna la descripcion de la incidencia. */
    public void setDescripcion(String v) { descripcion=v; }
    /** retorna la fecha de la incidencia. */
    public String getFecha() { return fecha; }
    /** retorna el tipo de entidad afectada. */
    public String getTipoEntidadAfectada() { return tipoEntidadAfectada; }
    /** retorna el id de la entidad afectada. */
    public int getIdEntidadAfectada() { return idEntidadAfectada; }
}
