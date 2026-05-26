package com.pgii.model.entities;

public class Pago {
    private int idPago;
    private double monto;
    private String fechaPago;
    private String metodoPago;
    private boolean exitoso;
    private boolean esTarjeta;
    private Compra ownedByCompra;

    public Pago(int idPago, double monto, String metodoPago, Compra ownedByCompra) {
        this.idPago = idPago;
        this.monto = monto;
        this.metodoPago = metodoPago;
        this.ownedByCompra = ownedByCompra;
        this.exitoso = false;
        this.fechaPago = "";
        this.esTarjeta = false;
    }

    // TODO: getters y setters
}
