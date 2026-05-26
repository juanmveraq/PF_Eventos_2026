package com.pgii.model.patterns.creational;
import com.pgii.model.entities.Evento;
import com.pgii.model.enums.CategoriaEvento;

/** Evento de tipo CONFERENCIA. TODO: completar getInfoEspecifica() y getters/setters. */
public class Conferencia extends Evento {
    private String ponente;
    private String tematica;
    public Conferencia(int id, String nombre, String desc, String ciudad, String fecha, String ponente, String tema) {
        super(id, nombre, CategoriaEvento.CONFERENCIA, desc, ciudad, fecha);
        this.ponente = ponente; this.tematica = tema;
    }
    @Override public String getInfoEspecifica() { return "TODO"; }
    // TODO: getters y setters
}
