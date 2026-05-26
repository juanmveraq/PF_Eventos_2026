package com.pgii.model.patterns.behavioral;
import com.pgii.model.interfaces.IObserver;
import java.util.ArrayList;
import java.util.List;

/**
 * implementa del patron Observer que recibe y almacena notificaciones de eventos.
 * cada instancia representa un receptor de notificaciones identificado por un nombre.
 * se suscribe a un Evento (IObservable) para ser notificado de cambios de estado.
 */
public class NotificacionObserver implements IObserver {

    /** nombre del receptor de notificaciones (por ej: nombre del usuario suscrito). */
    private String nombre;

    /** historial de notificaciones recibidas por este observer. */
    private List<String> listaNotificaciones;

    public NotificacionObserver(String nombre) { this.nombre=nombre; listaNotificaciones=new ArrayList<>(); }

    /**
     * recibe una notificacion, la almacena en el historial y la imprime en consola.
     * @param msg Mensaje de notificacion enviado por el objeto observable.
     */
    @Override public void actualizar(String msg) { listaNotificaciones.add("["+nombre+"] "+msg); System.out.println("NOTIF: "+msg); }

    /**
     * retorna el historial completo de notificaciones recibidas por este observer.
     * @return lista de mensajes de notificacion almacenados.
     */
    public List<String> getListaNotificaciones() { return listaNotificaciones; }

    /**
     * retorna el nombre del receptor de notificaciones.
     * @return nombre del observer.
     */
    public String getNombre() { return nombre; }
}
