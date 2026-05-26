package com.pgii.model.patterns.structural;

/**
 * interfaz base del patron Decorator para los servicios adicionales de la plataforma.
 * define el contrato que deben cumplir tanto el componente base (EntradaSimple)
 * como todos los decoradores de servicio (VIPDecorador, SeguroDecorador, etc.).
 */
public interface ServicioAdicional {

    /**
     * retorna la descripcion acumulada de los servicios aplicados.
     * @return cadena con la descripcion del servicio o combinacion de servicios.
     */
    public String getDescripcion();

    /**
     * retorna el precio extra acumulado por todos los servicios aplicados.
     * @return precio adicional total en pesos colombianos.
     */
    public double getPrecioExtra();
}
