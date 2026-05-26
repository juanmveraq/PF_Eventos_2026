package com.pgii.view;

import com.pgii.model.entities.Asiento;
import com.pgii.model.entities.Evento;
import com.pgii.model.entities.Usuario;
import com.pgii.model.entities.Zona;

import java.util.ArrayList;
import java.util.List;

/**
 * clase Singleton que mantiene el estado global de la sesion activa en la vista.
 * almacena el usuario en sesion, el evento seleccionado y los asientos escogidos
 * para la compra en proceso. Permite la comunicacion de estado entre controladores de vista.
 */
public class AppState {

    /** unica instancia de AppState (patron Singleton). */
    private static AppState instancia;

    /** usuario que tiene sesion activa en la aplicacion. */
    private Usuario usuarioActual;

    /** evento que el usuario tiene seleccionado actualmente para explorar o comprar. */
    private Evento  eventoSeleccionado;

    /** lista de asientos seleccionados por el usuario para la compra en proceso. */
    private final List<Asiento> asientosSeleccionados = new ArrayList<>();

    /** lista de zonas correspondientes a cada asiento seleccionado (indices paralelos). */
    private final List<Zona>    zonasDeAsientos        = new ArrayList<>();

    /** categoria de zona de los asientos seleccionados (todos deben ser de la misma categoria). */
    private String categoriaSeleccionada = null;

    private AppState() {}

    /**
     * retorna la unica instancia de AppState creandola si no existe.
     * metodo sincronizado para garantizar seguridad en entornos multi-hilo.
     * @return la instancia unica de AppState.
     */
    public static synchronized AppState getInstancia() {
        if (instancia == null) instancia = new AppState();
        return instancia;
    }

    /** retorna el usuario con sesion activa. */
    public Usuario getUsuarioActual()       { return usuarioActual; }
    /** asigna el usuario de la sesion activa. */
    public void    setUsuarioActual(Usuario v) { usuarioActual = v; }

    /**
     * indica si hay un usuario con sesion activa en la aplicacion.
     * @return verdadero si hay usuario en sesion, falso si no.
     */
    public boolean haySesion()              { return usuarioActual != null; }

    /**
     * cierre la sesion activa limpiando el usuario, el evento y los asientos seleccionados.
     */
    public void cerrarSesion() {
        usuarioActual        = null;
        eventoSeleccionado   = null;
        limpiarSeleccionAsientos();
    }

    /** retorna el evento actualmente seleccionado por el usuario. */
    public Evento getEventoSeleccionado()      { return eventoSeleccionado; }
    /** asigna el evento seleccionado para la sesion actual. */
    public void   setEventoSeleccionado(Evento v) { eventoSeleccionado = v; }

    /** retorna la lista de asientos seleccionados para la compra en proceso. */
    public List<Asiento> getAsientosSeleccionados() { return asientosSeleccionados; }
    /** retorna la lista de zonas correspondientes a los asientos seleccionados. */
    public List<Zona>    getZonasDeAsientos()        { return zonasDeAsientos; }
    /** retorna la categoria de zona de los asientos seleccionados. */
    public String        getCategoriaSeleccionada()  { return categoriaSeleccionada; }

    /**
     * agregar un asiento y su zona a la seleccion actual y registra la categoria.
     * @param a Asiento a agregar a la seleccion.
     * @param z Zona a la que pertenece el asiento.
     * @param categoria Nombre de la categoria de zona del asiento seleccionado.
     */
    public void agregarAsiento(Asiento a, Zona z, String categoria) {
        asientosSeleccionados.add(a);
        zonasDeAsientos.add(z);
        categoriaSeleccionada = categoria;
    }

    /**
     * elimina un asiento de la seleccion actual y su zona correspondiente.
     * si la seleccion queda vacia, limpia la categoria seleccionada.
     * @param a Asiento a quitar de la seleccion.
     */
    public void quitarAsiento(Asiento a) {
        int idx = asientosSeleccionados.indexOf(a);
        if (idx >= 0) {
            asientosSeleccionados.remove(idx);
            zonasDeAsientos.remove(idx);
        }
        if (asientosSeleccionados.isEmpty()) categoriaSeleccionada = null;
    }

    /**
     * verifica si un asiento ya fue seleccionado por el usuario para la compra.
     * @param a Asiento a verificar.
     * @return verdadero si el asiento ya esta en la seleccion, falso en caso contrario.
     */
    public boolean contieneAsiento(Asiento a) {
        return asientosSeleccionados.contains(a);
    }

    /**
     * limpia toda la seleccion de asientos, zonas y categoria para una nueva seleccion.
     */
    public void limpiarSeleccionAsientos() {
        asientosSeleccionados.clear();
        zonasDeAsientos.clear();
        categoriaSeleccionada = null;
    }

    /**
     * retorna el primer asiento seleccionado (conveniencia para seleccion de un solo asiento).
     * @return el primer asiento seleccionado, o null si no hay ninguno.
     */
    public Asiento getAsientoSeleccionado() {
        return asientosSeleccionados.isEmpty() ? null : asientosSeleccionados.get(0);
    }

    /**
     * retorna la zona del primer asiento seleccionado (conveniencia para seleccion simple).
     * @return la zona del primer asiento, o null si no hay ninguno.
     */
    public Zona getZonaSeleccionada() {
        return zonasDeAsientos.isEmpty() ? null : zonasDeAsientos.get(0);
    }
}
