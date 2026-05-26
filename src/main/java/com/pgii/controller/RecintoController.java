package com.pgii.controller;
import com.pgii.model.entities.*;
import com.pgii.model.interfaces.RecintoService;
import com.pgii.model.patterns.creational.*;
import java.util.List;

/**
 * controlador que implementa la logica de negocio para la gestion de recintos.
 * este usa RecintoBuilder (patron Builder) para construir los recintos.
 * entonces delega el almacenamiento en GestorDatos (patron Singleton).
 * implementa RecintoService.
 */
public class RecintoController implements RecintoService {

    /** instancia del gestor de datos (Singleton) que almacena todos los recintos. */
    private GestorDatos gestor;

    public RecintoController() { gestor = GestorDatos.getInstancia(); }

    /**
     * crea un nuevo recinto usando el patron Builder y lo registra en el sistema.
     * @param nombre Nombre del recinto.
     * @param dir Direccion del recinto.
     * @param ciudad Ciudad del recinto.
     * @return el recinto creado y registrado.
     */
    @Override public Recinto crearRecinto(String nombre, String dir, String ciudad) {
        int id = gestor.generarIdRecinto();
        Recinto r = new RecintoBuilder().setIdRecinto(id).setNombre(nombre).setDireccion(dir).setCiudad(ciudad).iniciarRecinto().build();
        gestor.getListaRecintos().add(r); return r;
    }

    /**
     * actualizar los datos de un recinto existente. Solo actualiza campos no vacios.
     * @param id Identificador del recinto a actualizar.
     * @param nombre Nuevo nombre del recinto.
     * @param dir Nueva direccion del recinto.
     * @param ciudad Nueva ciudad del recinto.
     * @return verdadero (true) si el recinto fue encontrado y actualizado, falso (false) en caso contrario.
     */
    @Override public boolean actualizarRecinto(int id, String nombre, String dir, String ciudad) {
        Recinto r=gestor.buscarRecintoPorId(id); if(r==null) return false;
        if(nombre!=null&&!nombre.isEmpty()) r.setNombre(nombre);
        if(dir!=null&&!dir.isEmpty()) r.setDireccion(dir);
        if(ciudad!=null&&!ciudad.isEmpty()) r.setCiudad(ciudad);
        return true;
    }

    /**
     * elimina un recinto del sistema por su identificador.
     * @param id Identificador del recinto a eliminar.
     * @return verdadero (true) si el recinto fue eliminado, falso (false) en caso contrario.
     */
    @Override public boolean eliminarRecinto(int id) {
        List<Recinto> l=gestor.getListaRecintos();
        for(int i=0;i<l.size();i++) if(l.get(i).getIdRecinto()==id) { l.remove(i); return true; }
        return false;
    }

    /** retorna el recinto con el id indicado, o null si no existe. */
    @Override public Recinto buscarPorId(int id) { return gestor.buscarRecintoPorId(id); }

    /** retorna la lista de todos los recintos registrados. */
    @Override public List<Recinto> listarRecintos() { return gestor.getListaRecintos(); }

    /**
     * con esto agrego una nueva zona con sus asientos a un recinto existente.
     * @param idRec Identificador del recinto donde se agrega la zona.
     * @param nom Nombre de la zona.
     * @param cap Capacidad maxima de asientos de la zona.
     * @param precio Precio base por entrada en la zona.
     * @return verdadero (true) si el recinto fue encontrado y la zona agregada, falso (false) en caso contrario.
     */
    @Override public boolean agregarZona(int idRec, String nom, int cap, double precio) {
        Recinto r=gestor.buscarRecintoPorId(idRec); if(r==null) return false;
        r.agregarZona(new Zona(gestor.generarIdZona(), nom, cap, precio, r)); return true;
    }
}
