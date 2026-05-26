package com.pgii.model.interfaces;

/**
 * interfaz del patron Strategy para el procesamiento de pagos.
 * cada implementacion concreta (PagoEfectivo, PagoTarjeta) define como se procesa
 * el pago segun el metodo elegido por el usuario.
 */
public interface IPagoStrategy {

    /**
     * procesa el pago del monto indicado segun el metodo de pago de la estrategia.
     * @param monto Valvor total a cobrar en la transaccion.
     * @return verdadero (true) si el pago fue procesado exitosamente, falso (false) en caso contrario.
     */
    public boolean procesarPago(double monto);

    /**
     * se retorna una descripcion textual del metodo de pago utilizado.
     * @return descripcion del metodo de pago (un ejemplo practico: "Visa terminada en 1234").
     */
    public String getDescripcion();
}
