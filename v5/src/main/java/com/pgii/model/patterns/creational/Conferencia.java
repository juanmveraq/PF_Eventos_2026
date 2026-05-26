package com.pgii.model.patterns.creational;
import com.pgii.model.entities.Evento;
import com.pgii.model.enums.CategoriaEvento;

/**
 * Implementacion concreta de Evento para eventos de tipo conferencia academica o tecnologica.
 * Extiende Evento con informacion especifica de la conferencia: ponente y tematica.
 * Es creada por EventoFactory cuando la categoria es CONFERENCIA.
 */
public class Conferencia extends Evento {

    /** Nombre del ponente o expositor principal de la conferencia. */
    private String ponente;

    /** Tematica o eje tematico central de la conferencia. */
    private String tematica;

    public Conferencia(int id, String nombre, String desc, String ciudad, String fecha, String ponente, String tema) {
        super(id, nombre, CategoriaEvento.CONFERENCIA, desc, ciudad, fecha);
        this.ponente = ponente; this.tematica = tema;
    }

    /**
     * Retorna la informacion especifica de la conferencia: ponente y tematica.
     * @return Cadena con el ponente y la tematica de la conferencia.
     */
    @Override public String getInfoEspecifica() { return "Ponente: " + ponente + " | Tematica: " + tematica; }

    /** retorna el nombre del ponente de la conferencia. */
    public String getPonente() { return ponente; }
    /** asigna el nombre del ponente de la conferencia. */
    public void setPonente(String v) { ponente=v; }
    /** retorna la tematica de la conferencia. */
    public String getTematica() { return tematica; }
    /** asigna la tematica de la conferencia. */
    public void setTematica(String v) { tematica=v; }
}
