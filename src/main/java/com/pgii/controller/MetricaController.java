package com.pgii.controller;
import com.pgii.model.entities.*;
import com.pgii.model.enums.*;
import com.pgii.model.patterns.creational.GestorDatos;
import java.util.ArrayList;
import java.util.List;

/**
 * controlador que se encargado de calcular y exponer las metricas de la plataforma.
 * este proporciona totales de ventas, compras, cancelaciones y conteo de servicios.
 * entonces accede a los datos directamente desde GestorDatos (patron Singleton).
 */
public class MetricaController {

    /** instancia del gestor de datos (Singleton) que contiene todas las compras y eventos. */
    private GestorDatos gestor;

    public MetricaController() { gestor = GestorDatos.getInstancia(); }

    /**
     * calcular el total de dinero vendido sumando las compras en estado PAGADA o CONFIRMADA.
     * @return total de ventas en pesos colombianos.
     */
    public double getTotalVentas() {
        double t=0; List<Compra> cs=gestor.getListaCompras();
        for(int i=0;i<cs.size();i++) {
            EstadoCompra est=cs.get(i).getEstadoActual();
            if(est==EstadoCompra.PAGADA||est==EstadoCompra.CONFIRMADA) t+=cs.get(i).getTotal();
        } return t;
    }

    /**
     * retorna el  numero total de compras registradas en el sistema (todos los estados).
     * @return cantidad total de compras.
     */
    public int getTotalCompras() { return gestor.getListaCompras().size(); }

    /**
     * cuenta cuantas compras tienen estado CANCELADA en el sistema.
     * @return numero de compras canceladas.
     */
    public int getTotalCancelaciones() {
        int n=0; List<Compra> cs=gestor.getListaCompras();
        for(int i=0;i<cs.size();i++) if(cs.get(i).getEstadoActual()==EstadoCompra.CANCELADA) n++;
        return n;
    }

    /**
     * cuenta cuantas veces fue contratado un servicio adicional especifico.
     * @param s Tipo de servicio a contabilizar.
     * @return numero de veces que el servicio aparece en todas las compras.
     */
    public int getConteoServicio(TipoServicio s) {
        int n=0; List<Compra> cs=gestor.getListaCompras();
        for(int i=0;i<cs.size();i++) { List<TipoServicio> ss=cs.get(i).getListaServicios();
            for(int j=0;j<ss.size();j++) if(ss.get(j)==s) n++;
        } return n;
    }

    /**
     * esto identifica el evento con mayor numero de compras registradas.
     * @return Cadena con el nombre del evento y la cantidad de compras, o "Sin datos" si no hay eventos.
     */
    public String getTopEvento() {
        List<Evento> ev=gestor.getListaEventos(); Evento top=null; int max=0;
        for(int i=0;i<ev.size();i++) { int n=ev.get(i).getListaCompras().size(); if(n>max) { max=n; top=ev.get(i); } }
        return top!=null ? top.getNombre()+" ("+max+" compras)" : "Sin datos";
    }

    /**
     * retorna la lista completa de eventos para consultar sus compras asociadas.
     * @return lista de todos los eventos del sistema.
     */
    public List<Evento> getListaEventosConCompras() { return gestor.getListaEventos(); }

    /**
     * suma el total de entradas en compras con estado PAGADA o CONFIRMADA.
     * @return cantidad total de entradas vendidas.
     */
    public int getTotalEntradasVendidas() {
        int total = 0;
        for (Compra c : gestor.getListaCompras()) {
            EstadoCompra est = c.getEstadoActual();
            if (est == EstadoCompra.PAGADA || est == EstadoCompra.CONFIRMADA)
                total += c.getListaEntradas().size();
        }
        return total;
    }

    /**
     * calcula el ingreso promedio por compra considerando solo compras PAGADA o CONFIRMADA.
     * @return promedio en pesos colombianos, o 0 si no hay compras validas.
     */
    public double getIngresosPromedioPorCompra() {
        double suma = 0; int count = 0;
        for (Compra c : gestor.getListaCompras()) {
            EstadoCompra est = c.getEstadoActual();
            if (est == EstadoCompra.PAGADA || est == EstadoCompra.CONFIRMADA) {
                suma += c.getTotal(); count++;
            }
        }
        return count > 0 ? suma / count : 0;
    }
}
