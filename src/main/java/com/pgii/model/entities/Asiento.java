package com.pgii.model.entities;
import com.pgii.model.enums.EstadoAsiento;
import com.pgii.model.interfaces.IPrototype;

/**
 * representa un asiento fisico dentro de una zona de un recinto.
 * almacenamiento la fila, numero y estado actual del asiento.
 * implementa el patron Prototype (IPrototype) para permitir la clonacion de asientos.
 */
public class Asiento implements IPrototype {

    /** identificador unico del asiento generado por GestorDatos. */
    private int idAsiento;

    /** fila donde se ubica el asiento (ej. "A", "B"). */
    private String fila;

    /** numero del asiento dentro de la fila. */
    private int numero;

    /** estado actual del asiento (DISPONIBLE, RESERVADO, VENDIDO, BLOQUEADO). */
    private EstadoAsiento estado;

    /** zona a la que pertenece este asiento. */
    private Zona ownedByZona;

    public Asiento(int idAsiento, String fila, int numero, Zona ownedByZona) {
        this.idAsiento = idAsiento;
        this.fila = fila;
        this.numero = numero;
        this.estado = EstadoAsiento.DISPONIBLE;
        this.ownedByZona = ownedByZona;
    }

    /**
     * crea y retorna una copia exacta de este asiento con los mismos atributos.
     * @return nuevo objeto Asiento con los mismos datos que el original.
     */
    @Override public Asiento clonar() {
        Asiento c = new Asiento(idAsiento, fila, numero, ownedByZona);
        c.setEstado(estado);
        return c;
    }

    /** retorna el id del asiento. */
    public int getIdAsiento() { return idAsiento; }
    /** asigna el id del asiento. */
    public void setIdAsiento(int v) { idAsiento = v; }
    /** retorna la fila del asiento. */
    public String getFila() { return fila; }
    /** asigna la fila del asiento. */
    public void setFila(String v) { fila = v; }
    /** retorna el numero del asiento. */
    public int getNumero() { return numero; }
    /** asigna el numero del asiento. */
    public void setNumero(int v) { numero = v; }
    /** retorna el estado actual del asiento. */
    public EstadoAsiento getEstado() { return estado; }
    /** asigna el estado del asiento. */
    public void setEstado(EstadoAsiento v) { estado = v; }
    /** retorna la zona a la que pertenece el asiento. */
    public Zona getOwnedByZona() { return ownedByZona; }
    /** asigna la zona propietaria del asiento. */
    public void setOwnedByZona(Zona v) { ownedByZona = v; }

    @Override public String toString() {
        return "la fila " + fila + " - n°" + numero;
    }
}
