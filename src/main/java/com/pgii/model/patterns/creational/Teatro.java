package com.pgii.model.patterns.creational;
import com.pgii.model.entities.Evento;
import com.pgii.model.enums.CategoriaEvento;

/** Evento de tipo TEATRO. TODO: completar getInfoEspecifica() y getters/setters. */
public class Teatro extends Evento {
    private String obra;
    private String compania;
    public Teatro(int id, String nombre, String desc, String ciudad, String fecha, String obra, String comp) {
        super(id, nombre, CategoriaEvento.TEATRO, desc, ciudad, fecha);
        this.obra = obra; this.compania = comp;
    }
    @Override public String getInfoEspecifica() { return "TODO"; }
    // TODO: getters y setters
}
