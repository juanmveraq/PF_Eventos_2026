package com.pgii.model.entities;
import com.pgii.model.enums.EstadoEntrada;
import com.pgii.model.interfaces.IPrototype;

/**
 * es una entrada individual asociada a una compra en la plataforma.
 * esta vincula una zona y un asiento especifico con su precio final y estado.
 * estamos implementamos el patron Prototype (IPrototype) para permitir la clonacion de entradas.
 */
public class Entrada implements IPrototype {

    /** identificador unico de la entrada generado por GestorDatos. */
    private int idEntrada;

    /** zona del recinto a la que da acceso esta entrada. */
    private Zona theZona;

    /** asiento especifico asignado a esta entrada (puede ser null en zonas sin numeracion). */
    private Asiento theAsiento;

    /** precio final pagado por esta entrada incluyendo decoradores de servicios. */
    private double precioFinal;

    /** estado actual de la entrada (que pueden ser ACTIVA, USADA, ANULADA). */
    private EstadoEntrada estado;

    /** compra a la que pertenece esta entrada. */
    private Compra ownedByCompra;

    public Entrada(int idEntrada, Zona theZona, Asiento theAsiento, double precioFinal, Compra ownedByCompra) {
        this.idEntrada = idEntrada; this.theZona = theZona; this.theAsiento = theAsiento;
        this.precioFinal = precioFinal; this.estado = EstadoEntrada.ACTIVA; this.ownedByCompra = ownedByCompra;
    }

    /**
     * crea y retorna una copia exacta de esta entrada con los mismos atributos.
     * @return nuevo objeto Entrada con los mismos datos que el original.
     */
    @Override public Entrada clonar() {
        Entrada c = new Entrada(idEntrada, theZona, theAsiento, precioFinal, ownedByCompra);
        c.setEstado(estado); return c;
    }

    /** retorna el id de la entrada. */
    public int getIdEntrada() { return idEntrada; }
    /** asigna el id de la entrada. */
    public void setIdEntrada(int v) { idEntrada = v; }
    /** retorna la zona asociada a la entrada. */
    public Zona getTheZona() { return theZona; }
    /** asigna la zona de la entrada. */
    public void setTheZona(Zona v) { theZona = v; }
    /** retorna el asiento asignado a la entrada. */
    public Asiento getTheAsiento() { return theAsiento; }
    /** asigna el asiento de la entrada. */
    public void setTheAsiento(Asiento v) { theAsiento = v; }
    /** retorna el precio final de la entrada. */
    public double getPrecioFinal() { return precioFinal; }
    /** asigna el precio final de la entrada. */
    public void setPrecioFinal(double v) { precioFinal = v; }
    /** retorna el estado de la entrada. */
    public EstadoEntrada getEstado() { return estado; }
    /** asigna el estado de la entrada. */
    public void setEstado(EstadoEntrada v) { estado = v; }
    /** retorna la compra a la que pertenece la entrada. */
    public Compra getOwnedByCompra() { return ownedByCompra; }
    /** asigna la compra propietaria de la entrada. */
    public void setOwnedByCompra(Compra v) { ownedByCompra = v; }
}
