package com.pgii.model.entities;

/**
 * es el registro de pago asociado a una compra en la plataforma.
 * aqui almacenamos el monto, metodo de pago utilizado y si la transaccion fue exitosa.
 */
public class Pago {

    /** identificador unico del pago generado por GestorDatos. */
    private int idPago;

    /** monto total cobrado en este pago (en pesos colombianos). */
    private double monto;

    /** fecha en que se realizo el pago (formato ISO: "yyyy-MM-dd"). */
    private String fechaPago;

    /** descripcion del metodo de pago utilizado (ej. "Visa terminada en 1234"). */
    private String metodoPago;

    /** indica si la transaccion de pago fue procesada exitosamente. */
    private boolean exitoso;

    /** indica si el pago fue realizado con tarjeta (true) o en efectivo (false). */
    private boolean esTarjeta;

    /** compra a la que pertenece este pago. */
    private Compra ownedByCompra;

    public Pago(int idPago, double monto, String metodoPago, Compra ownedByCompra) {
        this.idPago=idPago; this.monto=monto; this.metodoPago=metodoPago;
        this.ownedByCompra=ownedByCompra; this.exitoso=false; this.fechaPago=""; this.esTarjeta=false;
    }

    /** retorna el id del pago. */
    public int getIdPago() { return idPago; }
    /** retorna el monto del pago. */
    public double getMonto() { return monto; }
    /** retorna la fecha del pago. */
    public String getFechaPago() { return fechaPago; }
    /** asigna la fecha del pago. */
    public void setFechaPago(String v) { fechaPago=v; }
    /** retorna el metodo de pago utilizado. */
    public String getMetodoPago() { return metodoPago; }
    /** retorna true si el pago fue exitoso. */
    public boolean isExitoso() { return exitoso; }
    /** asigna el resultado de la transaccion de pago. */
    public void setExitoso(boolean v) { exitoso=v; }
    /** retorna true si el pago fue con tarjeta. */
    public boolean isEsTarjeta() { return esTarjeta; }
    /** asigna si el pago fue con tarjeta. */
    public void setEsTarjeta(boolean v) { esTarjeta=v; }
    /** retorna la compra a la que pertenece este pago. */
    public Compra getOwnedByCompra() { return ownedByCompra; }
}
