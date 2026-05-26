package com.pgii.model.patterns.behavioral;
import com.pgii.model.interfaces.IPagoStrategy;

/**
 * estrategia de pago que procesa pagos con tarjeta de credito o debito.
 * implementa el patron Strategy (IPagoStrategy) para el metodo de pago con tarjeta.
 * el pago se considera exitoso si el monto es mayor a cero.
 */
public class PagoTarjeta implements IPagoStrategy {

    /** tipo de tarjeta utilizada (ej. "Visa", "Mastercard"). */
    private String tipoTarjeta;

    public PagoTarjeta(String tipo) { tipoTarjeta = tipo; }

    /**
     * procesa el pago con tarjeta validando que el monto sea positivo.
     * @param monto Valor a cobrar en pesos colombianos.
     * @return true si el monto es mayor a cero, false en caso contrario.
     */
    @Override public boolean procesarPago(double monto) {
        System.out.println("Pago " + tipoTarjeta + " $" + monto);
        return monto > 0;
    }

    /**
     * retorna la descripcion del metodo de pago indicando el tipo de tarjeta.
     * @return cadena describiendo la tarjeta utilizada.
     */
    @Override public String getDescripcion() { return tipoTarjeta; }

    /** retorna el tipo de tarjeta (por ejemplo: "Visa", "Mastercard"). */
    public String getTipoTarjeta() { return tipoTarjeta; }
}
