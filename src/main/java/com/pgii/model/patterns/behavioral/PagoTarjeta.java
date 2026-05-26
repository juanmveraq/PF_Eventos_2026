package com.pgii.model.patterns.behavioral;
import com.pgii.model.interfaces.IPagoStrategy;

/** Estrategia pago tarjeta. TODO: completar procesarPago(). */
public class PagoTarjeta implements IPagoStrategy {
    private String tipoTarjeta;
    public PagoTarjeta(String tipo) { tipoTarjeta=tipo; }
    @Override public boolean procesarPago(double monto) { return false; /* TODO: return monto>0 */ }
    @Override public String getDescripcion() { return tipoTarjeta; }
    public String getTipoTarjeta() { return tipoTarjeta; }
}
