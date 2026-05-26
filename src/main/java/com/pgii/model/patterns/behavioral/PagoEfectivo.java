package com.pgii.model.patterns.behavioral;
import com.pgii.model.interfaces.IPagoStrategy;

/** Estrategia pago efectivo. TODO: completar procesarPago(). */
public class PagoEfectivo implements IPagoStrategy {
    private String referencia;
    public PagoEfectivo(String ref) { this.referencia=ref; }
    @Override public boolean procesarPago(double monto) { return false; /* TODO: return monto>0 */ }
    @Override public String getDescripcion() { return "efectivo (Ref: "+referencia+")"; }
    public String getReferencia() { return referencia; }
}
