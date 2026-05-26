package com.pgii.model.patterns.creational;
import com.pgii.model.entities.Evento;
import com.pgii.model.enums.CategoriaEvento;

/** Factory Method para crear eventos segun categoria. TODO: completar el switch. */
public class EventoFactory {
    private EventoFactory() {}
    public static Evento crear(int id, CategoriaEvento cat, String nombre, String desc,
                               String ciudad, String fecha, String info1, String info2) {
        if (cat == null) throw new IllegalArgumentException("categoria nula");
        // TODO: switch (cat) { case CONCIERTO: ... case CONFERENCIA: ... case TEATRO: ... }
        return null;
    }
}
