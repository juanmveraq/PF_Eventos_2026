package com.pgii.model.entities;
import com.pgii.model.enums.EstadoEntrada;
import com.pgii.model.interfaces.IPrototype;

public class Entrada implements IPrototype {
    private int idEntrada;
    private Zona theZona;
    private Asiento theAsiento;
    private double precioFinal;
    private EstadoEntrada estado;
    private Compra ownedByCompra;

    public Entrada(int idEntrada, Zona theZona, Asiento theAsiento, double precioFinal, Compra ownedByCompra) {
        this.idEntrada = idEntrada;
        this.theZona = theZona;
        this.theAsiento = theAsiento;
        this.precioFinal = precioFinal;
        this.estado = EstadoEntrada.ACTIVA;
        this.ownedByCompra = ownedByCompra;
    }

    // TODO: implementar clonar() del patron Prototype
    @Override public Object clonar() { return null; }
    // TODO: getters y setters
}
