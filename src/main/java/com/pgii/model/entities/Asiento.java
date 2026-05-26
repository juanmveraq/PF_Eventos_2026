package com.pgii.model.entities;
import com.pgii.model.enums.EstadoAsiento;
import com.pgii.model.interfaces.IPrototype;

public class Asiento implements IPrototype {
    private int idAsiento;
    private String fila;
    private int numero;
    private EstadoAsiento estado;
    private Zona ownedByZona;

    public Asiento(int idAsiento, String fila, int numero, Zona ownedByZona) {
        this.idAsiento = idAsiento;
        this.fila = fila;
        this.numero = numero;
        this.estado = EstadoAsiento.DISPONIBLE;
        this.ownedByZona = ownedByZona;
    }

    // TODO: implementar clonar() del patron Prototype
    @Override public Object clonar() { return null; }
    // TODO: getters, setters y toString
}
