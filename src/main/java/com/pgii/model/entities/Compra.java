package com.pgii.model.entities;
import com.pgii.model.enums.EstadoCompra;
import com.pgii.model.enums.TipoServicio;
import com.pgii.model.interfaces.IEstadoCompra;
import com.pgii.model.interfaces.IPagoStrategy;
import java.util.ArrayList;
import java.util.List;

/**
 * esta representa una compra realizada por un usuario para un evento especifico.
 * gestiona las entradas, servicios adicionales, el pago y el estado del ciclo de vida.
 * entonces, aplica el patron State (IEstadoCompra) para controlar las transiciones de estado
 * y el patron Strategy (IPagoStrategy) para el procesamiento del pago.
 * es construida mediante el patron Builder (CompraBuilder).
 */
public class Compra {

    /** identificador unico de la compra generado por GestorDatos. */
    private int idCompra;

    /** fecha en que se creo la compra (formato ISO: "yyyy-MM-dd"). */
    private String fechaCreacion;

    /** total calculado de la compra incluyendo entradas y servicios adicionales. */
    private double total;

    /** estado actual de la compra en su ciclo de vida (patron State). */
    private IEstadoCompra theEstado;

    /** usuario que realizo la compra. */
    private Usuario theUsuario;

    /** evento al que corresponde esta compra. */
    private Evento theEvento;

    /** lista de entradas incluidas en la compra. */
    private List<Entrada> listaEntradas;

    /** lista de servicios adicionales contratados en la compra (VIP, Seguro, etc.). */
    private List<TipoServicio> listaServicios;

    /** informacion del pago asociado a esta compra. */
    private Pago thePago;

    public Compra(int idCompra, String fechaCreacion, Usuario theUsuario, Evento theEvento) {
        this.idCompra = idCompra; this.fechaCreacion = fechaCreacion;
        this.theUsuario = theUsuario; this.theEvento = theEvento;
        this.total = 0.0;
        this.listaEntradas = new ArrayList<>();
        this.listaServicios = new ArrayList<>();
    }

    /**
     * agregar una entrada a la compra y recalcula el total automaticamente.
     * @param e Entrada a agregar. Se ignora si es nula.
     */
    public void agregarEntrada(Entrada e) { if (e != null) { listaEntradas.add(e); recalcularTotal(); } }

    /**
     * agregar un servicio adicional a la compra y recalcula el total automaticamente.
     * @param s Tipo de servicio a agregar. Se ignora si es nulo.
     */
    public void agregarServicio(TipoServicio s) { if (s != null) { listaServicios.add(s); recalcularTotal(); } }

    /**
     * recalcualar el total de la compra sumando precios de entradas y servicios adicionales.
     * los precios fijos de los servicios estan definidos internamente.
     */
    private void recalcularTotal() {
        double suma = 0.0;
        for (int i = 0; i < listaEntradas.size(); i++) suma += listaEntradas.get(i).getPrecioFinal();
        for (int i = 0; i < listaServicios.size(); i++) suma += listaServicios.get(i).getPrecio();
        this.total = suma;
    }

    /**
     * procesar el pago de la compra usando la estrategia de pago indicada.
     * si el pago es exitoso, transiciona el estado de la compra a PAGADA.
     * @param estrategia Estrategia de pago a usar (efectivo, tarjeta, etc.).
     * @return verdadero (true) si el pago fue procesado correctamente, falso (false) en caso contrario.
     */
    public boolean pagar(IPagoStrategy estrategia) {
        if (estrategia == null || theEstado == null) return false;
        boolean ok = estrategia.procesarPago(total);
        if (ok) return theEstado.pagar(this);
        return false;
    }

    /**
     * retorna el estado actual de la compra como enum EstadoCompra.
     * @return estado actual, o null si no hay estado asignado.
     */
    public EstadoCompra getEstadoActual() { return theEstado != null ? theEstado.getEstadoEnum() : null; }

    /** retorna el id de la compra. */
    public int getIdCompra() { return idCompra; }
    /** asigna el id de la compra. */
    public void setIdCompra(int v) { idCompra = v; }
    /** retorna la fecha de creacion de la compra. */
    public String getFechaCreacion() { return fechaCreacion; }
    /** asigna la fecha de creacion de la compra. */
    public void setFechaCreacion(String v) { fechaCreacion = v; }
    /** retorna el total de la compra. */
    public double getTotal() { return total; }
    /** asigna el total de la compra manualmente. */
    public void setTotal(double v) { total = v; }
    /** retorna el estado actual de la compra (objeto estado del patron State). */
    public IEstadoCompra getTheEstado() { return theEstado; }
    /** asigna el estado de la compra (usado por las transiciones del patron State). */
    public void setTheEstado(IEstadoCompra v) { theEstado = v; }
    /** retorna el usuario que realizo la compra. */
    public Usuario getTheUsuario() { return theUsuario; }
    /** asigna el usuario de la compra. */
    public void setTheUsuario(Usuario v) { theUsuario = v; }
    /** retorna el evento de la compra. */
    public Evento getTheEvento() { return theEvento; }
    /** asigna el evento de la compra. */
    public void setTheEvento(Evento v) { theEvento = v; }
    /** retorna la lista de entradas de la compra. */
    public List<Entrada> getListaEntradas() { return listaEntradas; }
    /** retorna la lista de servicios adicionales de la compra. */
    public List<TipoServicio> getListaServicios() { return listaServicios; }
    /** retorna el pago asociado a la compra. */
    public Pago getThePago() { return thePago; }
    /** asigna el pago asociado a la compra. */
    public void setThePago(Pago v) { thePago = v; }

    @Override public String toString() { return "compra#" + idCompra + " $" + total; }
}
