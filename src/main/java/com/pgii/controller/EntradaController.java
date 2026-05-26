package com.pgii.controller;
import com.pgii.model.entities.*;
import com.pgii.model.enums.*;
import com.pgii.model.interfaces.EntradaService;
import com.pgii.model.patterns.creational.GestorDatos;
import java.util.ArrayList;
import java.util.List;

/**
 * controlador que implementa la logica de negocio para la gestion de entradas.
 * con esto permite generar entradas individuales, anularlas y marcarlas como usadas.
 * delega el almacenamiento en GestorDatos (patron Singleton).
 * implementa el EntradaService.
 */
public class EntradaController implements EntradaService {

    /** instacia del gestor de datos (Singleton) para buscar compras, zonas y asientos. */
    private GestorDatos gestor;

    public EntradaController() { gestor = GestorDatos.getInstancia(); }

    /**
     * genera una nueva entrada y la agrega a una compra existente.
     * si el asiento tiene id positivo, lo marca como VENDIDO.
     * @param idCompra Identificador de la compra a la que se agrega la entrada.
     * @param idZona Identificador de la zona de la entrada.
     * @param idAsiento Identificador del asiento; si es positivo se marca VENDIDO.
     * @param precio Precio final de la entrada.
     * @return la entrada generada, o null si la compra o zona no existen.
     */

    @Override public Entrada generarEntrada(int idCompra, int idZona, int idAsiento, double precio) {
        Compra c=gestor.buscarCompraPorId(idCompra); Zona z=gestor.buscarZonaPorId(idZona);
        if(c==null||z==null) return null;
        Asiento a=null;
        if(idAsiento>0) { a=z.buscarAsientoPorId(idAsiento); if(a!=null) a.setEstado(EstadoAsiento.VENDIDO); }
        Entrada e=new Entrada(gestor.generarIdEntrada(), z, a, precio, c);
        c.agregarEntrada(e); return e;
    }

    /**
     * anula una entrada y libera el asiento asociado si existe.
     * @param id Identificador de la entrada a anular.
     * @return true si la entrada fue encontrada y anulada, false en caso contrario.
     */
    @Override public boolean anularEntrada(int id) {
        Entrada e=buscarPorId(id); if(e==null) return false;
        e.setEstado(EstadoEntrada.ANULADA);
        if(e.getTheAsiento()!=null) e.getTheAsiento().setEstado(EstadoAsiento.DISPONIBLE);
        return true;
    }

    /**
     * marca una entrada como usada luego del ingreso al evento.
     * @param id Identificador de la entrada a marcar.
     * @return verdadero (true) si la entrada fue encontrada y marcada, falso (false) en caso contrario.
     */
    @Override public boolean marcarUsada(int id) {
        Entrada e=buscarPorId(id); if(e==null) return false; e.setEstado(EstadoEntrada.USADA); return true;
    }

    /**
     * Busca una entrada por su id recorriendo todas las compras del sistema.
     * @param id Identificador de la entrada.
     * @return La entrada encontrada, o null si no existe en ninguna compra.
     */
    @Override public Entrada buscarPorId(int id) {
        List<Compra> cs=gestor.getListaCompras();
        for(int i=0;i<cs.size();i++) { List<Entrada> es=cs.get(i).getListaEntradas();
            for(int j=0;j<es.size();j++) if(es.get(j).getIdEntrada()==id) return es.get(j);
        } return null;
    }

    /**
     * retorna todas las entradas de una compra especifica.
     * @param id Identificador de la compra.
     * @return lista de entradas de la compra, o lista vacia si la compra no existe.
     */
    @Override public List<Entrada> listarPorCompra(int id) {
        Compra c=gestor.buscarCompraPorId(id); if(c==null) return new ArrayList<>(); return c.getListaEntradas();
    }
}
