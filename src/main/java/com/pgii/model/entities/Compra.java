package com.pgii.model.entities;
import com.pgii.model.enums.EstadoCompra;
import com.pgii.model.enums.TipoServicio;
import com.pgii.model.interfaces.IEstadoCompra;
import com.pgii.model.interfaces.IPagoStrategy;
import java.util.ArrayList;
import java.util.List;

public class Compra {
    private int idCompra;
    private String fechaCreacion;
    private double total;
    private IEstadoCompra theEstado;
    private Usuario theUsuario;
    private Evento theEvento;
    private List<Entrada> listaEntradas;
    private List<TipoServicio> listaServicios;
    private Pago thePago;

    public Compra(int idCompra, String fechaCreacion, Usuario theUsuario, Evento theEvento) {
        this.idCompra = idCompra;
        this.fechaCreacion = fechaCreacion;
        this.theUsuario = theUsuario;
        this.theEvento = theEvento;
        this.total = 0.0;
        this.listaEntradas = new ArrayList<>();
        this.listaServicios = new ArrayList<>();
    }

    // TODO: agregarEntrada(), agregarServicio(), recalcularTotal()
    // TODO: pagar(), getEstadoActual()
    // TODO: getters, setters y toString
}
