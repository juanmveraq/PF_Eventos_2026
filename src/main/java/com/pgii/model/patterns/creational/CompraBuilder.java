package com.pgii.model.patterns.creational;
import com.pgii.model.entities.*;

/** Builder para construir objetos Compra. TODO: completar build() con estado inicial. */
public class CompraBuilder {
    private int idCompra;
    private String fechaCreacion;
    private Usuario theUsuario;
    private Evento theEvento;
    public CompraBuilder setIdCompra(int v)         { idCompra=v; return this; }
    public CompraBuilder setFechaCreacion(String v) { fechaCreacion=v; return this; }
    public CompraBuilder setUsuario(Usuario v)       { theUsuario=v; return this; }
    public CompraBuilder setEvento(Evento v)         { theEvento=v; return this; }
    public Compra build() {
        if (theUsuario==null) throw new IllegalStateException("hace falta usuario");
        if (theEvento==null)  throw new IllegalStateException("hace falta evento");
        // TODO: asignar estado inicial (EstadoCreada) al patron State
        return new Compra(idCompra, fechaCreacion, theUsuario, theEvento);
    }
}
