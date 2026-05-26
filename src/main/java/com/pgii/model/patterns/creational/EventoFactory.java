package com.pgii.model.patterns.creational;
import com.pgii.model.entities.Evento;
import com.pgii.model.enums.CategoriaEvento;

/**
 * fabrica de eventos que implementa el patron Factory Method.
 * este centraliza la creacion de instancias de las subclases concretas de Evento
 * (que tenemos : Concierto, Conferencia, Teatro) segun la categoria indicada.
 * Es una clase de utilidad y no puede ser instanciada.
 */
public class EventoFactory {

    private EventoFactory() {}

    /**
     * crea y retorna una instancia concreta de Evento segun la categoria especificada.
     * @param id Identificador unico del evento.
     * @param cat Categoria del evento que determina el tipo a crear.
     * @param nombre Nombre del evento.
     * @param desc Descripcion del evento.
     * @param ciudad Ciudad donde se realizara el evento.
     * @param fecha Fecha y hora del evento.
     * @param info1 Informacion adicional 1 especifica del tipo (ej. artista en concierto).
     * @param info2 Informacion adicional 2 especifica del tipo (ej. genero musical en concierto).
     * @return instancia concreta de Evento (Concierto, Conferencia o Teatro).
     * @throws IllegalArgumentException si es que  la categoria es nula o no esta soportada.
     */
    public static Evento crear(int id, CategoriaEvento cat, String nombre, String desc,
                               String ciudad, String fecha, String info1, String info2) {
        if (cat == null) throw new IllegalArgumentException("categoria en nula (como es que la quiere instanciar?????)");
        switch (cat) {
            case CONCIERTO:   return new Concierto(id, nombre, desc, ciudad, fecha, info1, info2);
            case CONFERENCIA: return new Conferencia(id, nombre, desc, ciudad, fecha, info1, info2);
            case TEATRO:      return new Teatro(id, nombre, desc, ciudad, fecha, info1, info2);
            default: throw new IllegalArgumentException("categoria no soportada: " + cat);
        }
    }
}
