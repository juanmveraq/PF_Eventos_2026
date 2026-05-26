package com.pgii.model.patterns.behavioral;
import com.pgii.model.interfaces.IObserver;
import java.util.ArrayList;
import java.util.List;

/** Observer que recibe notificaciones. TODO: completar actualizar(). */
public class NotificacionObserver implements IObserver {
    private String nombre;
    private List<String> listaNotificaciones;
    public NotificacionObserver(String nombre) { this.nombre=nombre; listaNotificaciones=new ArrayList<>(); }
    @Override public void actualizar(String msg) { /* TODO: agregar a lista y printear */ }
    public List<String> getListaNotificaciones() { return listaNotificaciones; }
    public String getNombre() { return nombre; }
}
