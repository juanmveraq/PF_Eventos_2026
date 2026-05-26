package com.pgii.controller;
import com.pgii.model.entities.*;
import com.pgii.model.enums.*;
import com.pgii.model.interfaces.EventoService;
import com.pgii.model.patterns.creational.*;
import java.util.ArrayList;
import java.util.List;

/**
 * controlador que se encarga implementa la logica de negocio para la gestion de eventos.
 * este usa EventoFactory (patron Factory Method) para crear eventos segun su categoria.
 * por lo tanto, delega el almacenamiento en GestorDatos (patron Singleton).
 * implementa EventoService.
 */
public class EventoController implements EventoService {

    /** instancia del gestor de datos (Singleton) que almacena todos los eventos. */
    private GestorDatos gestor;

    public EventoController() { gestor = GestorDatos.getInstancia(); }

    /**
     * crea un nuevo evento segun la categoria usando la fabrica de eventos.
     * @param nombre Nombre del evento.
     * @param cat Categoria del evento (CONCIERTO, TEATRO, CONFERENCIA).
     * @param desc Descripcion del evento.
     * @param ciudad Ciudad donde se realizara el evento.
     * @param fechaHora Fecha y hora del evento.
     * @param idRecinto Identificador del recinto donde ocurrira el evento.
     * @param info1 Informacion adicional 1 segun el tipo de evento.
     * @param info2 Informacion adicional 2 segun el tipo de evento.
     * @return el evento creado, o null si el recinto no existe.
     */

    @Override public Evento crearEvento(String nombre, CategoriaEvento cat, String desc,
                              String ciudad, String fechaHora, int idRecinto, String info1, String info2) {
        Recinto rec = gestor.buscarRecintoPorId(idRecinto);
        if (rec == null) return null;
        int id = gestor.generarIdEvento();
        Evento e = EventoFactory.crear(id, cat, nombre, desc, ciudad, fechaHora, info1, info2);
        e.setTheRecinto(rec);
        gestor.getListaEventos().add(e);
        return e;
    }

    /**
     * actualizar los datos de un evento existente. Solo actualiza campos no vacios.
     * @param id Identificador del evento a actualizar.
     * @param nombre Nuevo nombre del evento.
     * @param desc Nueva descripcion del evento.
     * @param ciudad Nueva ciudad del evento.
     * @param fecha Nueva fecha y hora del evento.
     * @return verdadero (true) si el evento fue encontrado y actualizado, falso (false) en caso contrario.
     */

    @Override public boolean actualizarEvento(int id, String nombre, String desc, String ciudad, String fecha) {
        Evento e = gestor.buscarEventoPorId(id); if(e==null) return false;
        if(nombre!=null&&!nombre.isEmpty()) e.setNombre(nombre);
        if(desc!=null&&!desc.isEmpty()) e.setDescripcion(desc);
        if(ciudad!=null&&!ciudad.isEmpty()) e.setCiudad(ciudad);
        if(fecha!=null&&!fecha.isEmpty()) e.setFechaHora(fecha);
        return true;
    }

    /**
     * eliminar un evento del sistema por su identificador.
     * @param id Identificador del evento a eliminar.
     * @return true si el evento fue eliminado, false en caso contrario.
     */
    @Override public boolean eliminarEvento(int id) {
        List<Evento> l = gestor.getListaEventos();
        for(int i=0;i<l.size();i++) if(l.get(i).getIdEvento()==id) { l.remove(i); return true; }
        return false;
    }

    /** retorna la lista de todos los eventos registrados. */
    @Override public List<Evento> listarEventos() { return gestor.getListaEventos(); }

    /** retorna el evento con el id indicado, o null si no existe. */
    @Override public Evento buscarPorId(int id) { return gestor.buscarEventoPorId(id); }

    /**
     * Cambia el estado del evento a PUBLICADO si estaba en BORRADOR o PAUSADO.
     * @param id Identificador del evento a publicar.
     * @return verdadero (true) si la transicion fue valida, falso (false) en caso contrario.
     */
    @Override public boolean publicarEvento(int id) {
        Evento e = gestor.buscarEventoPorId(id); if(e==null) return false;
        if(e.getEstado()!=EstadoEvento.BORRADOR && e.getEstado()!=EstadoEvento.PAUSADO) return false;
        e.cambiarEstado(EstadoEvento.PUBLICADO); return true;
    }

    /**
     * Cambia el estado del evento a PAUSADO si estaba PUBLICADO.
     * @param id Identificador del evento a pausar.
     * @return verdadero (true) si el evento fue pausado, falso (false) en caso contrario.
     */
    @Override public boolean pausarEvento(int id) {
        Evento e = gestor.buscarEventoPorId(id); if(e==null||e.getEstado()!=EstadoEvento.PUBLICADO) return false;
        e.cambiarEstado(EstadoEvento.PAUSADO); return true;
    }

    /**
     * cambia el estado del evento a CANCELADO.
     * @param id Identificador del evento a cancelar.
     * @return verdaero (true) si el evento fue cancelado, falso ((false) en caso contrario.
     */
    @Override public boolean cancelarEvento(int id) {
        Evento e = gestor.buscarEventoPorId(id); if(e==null) return false;
        e.cambiarEstado(EstadoEvento.CANCELADO); return true;
    }

    /**
     * filtra y retorna los eventos que pertenecen a la categoria indicada.
     * @param cat Categoria de evento a filtrar.
     * @return lista de eventos de esa categoria.
     */
    @Override public List<Evento> filtrarPorCategoria(CategoriaEvento cat) {
        List<Evento> r=new ArrayList<>(); List<Evento> t=gestor.getListaEventos();
        for(int i=0;i<t.size();i++) if(t.get(i).getCategoria()==cat) r.add(t.get(i)); return r;
    }

    /**
     * filtro y retorna los eventos que se realizan en la ciudad indicada.
     * @param ciudad Ciudad por la que filtrar (sin distinguir mayusculas).
     * @return lista de eventos en esa ciudad.
     */
    @Override public List<Evento> filtrarPorCiudad(String ciudad) {
        List<Evento> r=new ArrayList<>(); List<Evento> t=gestor.getListaEventos();
        for(int i=0;i<t.size();i++) if(t.get(i).getCiudad().equalsIgnoreCase(ciudad)) r.add(t.get(i)); return r;
    }
}
