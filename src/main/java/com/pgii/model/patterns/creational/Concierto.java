package com.pgii.model.patterns.creational;
import com.pgii.model.entities.Evento;
import com.pgii.model.enums.CategoriaEvento;

/**
 * implementacion concreta de Evento para eventos de tipo musical en vivo.
 * extiende  Evento con informacion especifica del concierto: artista y genero musical.
 * es creada por EventoFactory cuando la categoria es CONCIERTO.
 */
public class Concierto extends Evento {

    /** nombre del artista o banda principal del concierto. */
    private String artista;

    /** genero musical del concierto (por ejemplo "Pop Latino", "Electronica"). */
    private String generoMusical;

    public Concierto(int id, String nombre, String desc, String ciudad, String fecha, String artista, String genero) {
        super(id, nombre, CategoriaEvento.CONCIERTO, desc, ciudad, fecha);
        this.artista = artista; this.generoMusical = genero;
    }

    /**
     * retorna la informacion especifica del concierto: artista y genero musical.
     * @return una cadena de texto con el artista y genero del concierto.
     */
    @Override public String getInfoEspecifica() { return "El artista: " + artista + " | Genero: " + generoMusical; }

    /** retorna el nombre del artista del concierto. */
    public String getArtista() { return artista; }
    /** asigna el nombre del artista del concierto. */
    public void setArtista(String v) { artista=v; }
    /** retorna el genero musical del concierto. */
    public String getGeneroMusical() { return generoMusical; }
    /** asigna el genero musical del concierto. */
    public void setGeneroMusical(String v) { generoMusical=v; }
}
