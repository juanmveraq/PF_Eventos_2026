package com.pgii.model.patterns.behavioral;
import com.pgii.model.interfaces.IPagoStrategy;

/**
 * estrategia  de pago que procesa pagos en efectivo con una referencia de transaccion.
 * aqui implementa el patron Strategy (IPagoStrategy) para el metodo de pago en efectivo.
 * el  pago se considera exitoso si el monto es mayor a cero.
 */
public class PagoEfectivo implements IPagoStrategy {

    /** referencia de la transaccion en efectivo proporcionada por el cajero o sistema. */
    private String referencia;

    public PagoEfectivo(String ref) { this.referencia=ref; }

    /**
     * procesa el pago en efectivo validando que el monto sea positivo.
     * @param monto Valor a cobrar en pesos colombianos.
     * @return verdadero (true) si el monto es mayor a cero, falso (false) en caso contrario.
     */
    @Override public boolean procesarPago(double monto) { System.out.println("pago en efectivo $"+monto+" de referencia:"+referencia); return monto>0; }

    /**
     * retorna la descripcion del metodo de pago incluyendo la referencia de transaccion.
     * @return cadena describiendo el pago en efectivo con su referencia.
     */
    @Override public String getDescripcion() { return "efectivo (Ref: "+referencia+")"; }

    /** retorna la referencia de la transaccion en efectivo. */
    public String getReferencia() { return referencia; }
}
