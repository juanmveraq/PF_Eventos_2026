package com.pgii.controller;
import com.pgii.model.entities.*;
import com.pgii.model.enums.EstadoAsiento;
import com.pgii.model.interfaces.AsientoService;
import com.pgii.model.patterns.behavioral.*;
import com.pgii.model.patterns.creational.GestorDatos;
import java.util.ArrayList;
import java.util.List;

/**
 * controlador que se encarga de implementa la logica de negocio para poder gestion de asientos.
 * usamos los comandos BloquearAsientoCommand y LiberarAsientoCommand (patron comand) para cambiar en las operaciones de cambio de estado.
 * se delega el almacenamiento en GestorDatos (patron singleton).
 * implementa AsientoService.
 */
public class AsientoController implements AsientoService {

    /** instancia del gestor de datos (en singleton por la propia clase) que permite buscar asientos y zonas. */
    private GestorDatos gestor;

    public AsientoController() { gestor = GestorDatos.getInstancia(); }

    /**
     * crea un nuevo asiento en la zona indicada y lo registra en ella.
     * @param idZona Identificador de la zona donde se crea el asiento.
     * @param fila Letra de la fila del asiento.
     * @param num Numero del asiento en la respectiva fila.
     * @return el asiento creado, o null si la zona no existe de momento.
     */


    @Override public Asiento crearAsiento(int idZona, String fila, int num) {
        Zona z=gestor.buscarZonaPorId(idZona); if(z==null) return null;
        Asiento a=new Asiento(gestor.generarIdAsiento(), fila, num, z);
        z.agregarAsiento(a); return a;
    }

    /**
     * habilita un asiento poniendolo en estado DISPONIBLE (con nuestro enumm) usando el comando LiberarAsientoCommand.
     * @param id identificador del asiento a poner en habilitacion.
     * @return verdadero si el asiento fue encontrado y puesto en habilitacion, falso (false) en caso contrario.
     */
    @Override public boolean habilitarAsiento(int id) {
        Asiento a=gestor.buscarAsientoPorId(id); if(a==null) return false;
        new LiberarAsientoCommand(a).ejecutar(); return true;
    }

    /**
     * bloqueo un asiento usando el comando BloquearAsientoCommand (patron comand).
     * @param id identificador respectivo del asiento a bloquear.
     * @return verdadero (true) si el asiento fue encontrado y puesto en estado bloqueado, falso (false) en caso contrario.
     */
    @Override public boolean bloquearAsiento(int id) {
        Asiento a=gestor.buscarAsientoPorId(id); if(a==null) return false;
        new BloquearAsientoCommand(a).ejecutar(); return true;
    }

    /**
     * liberar un asiento directamente cambiando su estado a DISPONIBLE (con enum).
     * @param id identificador del asiento que queremos liberar.
     * @return verdadero (true) si el asiento fue encontrado y liberado, falso (false) en caso contrario.
     */
    @Override public boolean liberarAsiento(int id) {
        Asiento a=gestor.buscarAsientoPorId(id);
        if(a==null) return false;
        a.setEstado(EstadoAsiento.DISPONIBLE);
        return true;
    }

    /** retorna el asiento con el id que fue indicado, o null si no existe. */
    @Override public Asiento buscarPorId(int id) { return gestor.buscarAsientoPorId(id); }

    /**
     * retorna todos los asientos de la zona indicada.
     * @param idZona para identificar la zona.
     * @return lista de asientos de la zona, o lista vacia (en empty) si la zona no existe.
     */
    @Override public List<Asiento> listarPorZona(int idZona) {
        Zona z=gestor.buscarZonaPorId(idZona);
        if(z==null) return new ArrayList<>();
        return z.getListaAsientos();
    }
}
