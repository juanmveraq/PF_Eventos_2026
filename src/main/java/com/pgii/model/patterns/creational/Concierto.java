package com.pgii.model.patterns.creational;
import com.pgii.model.entities.Evento;
import com.pgii.model.enums.CategoriaEvento;

/** Evento de tipo CONCIERTO. TODO: completar getInfoEspecifica() y getters/setters. */
public class Concierto extends Evento {
    private String artista;
    private String generoMusical;
    public Concierto(int id, String nombre, String desc, String ciudad, String fecha, String artista, String genero) {
        super(id, nombre, CategoriaEvento.CONCIERTO, desc, ciudad, fecha);
        this.artista = artista; this.generoMusical = genero;
    }
    @Override public String getInfoEspecifica() { return "TODO"; }
    // TODO: getters y setters
}
