package com.pgii.model.patterns.structural;

/**
 * componente que es base del patron Decorator para los servicios adicionales.
 * representa una entrada sin ningun servicio adicional, con solo el precio base.
 * es el objeto que los decoradores de servicio envuelven para agregar funcionalidad.
 */
public class EntradaSimple implements ServicioAdicional {

    /** el precio base de la entrada sin servicios adicionales. */
    private double precioBase;

    public EntradaSimple(double p) { precioBase=p; }

    /** retornamos la descripcion base de la entrada simple. */
    @Override public String getDescripcion() { return "Una entrada base"; }

    /** retorna el precio base de la entrada sin servicios adicionales. */
    @Override public double getPrecioExtra() { return precioBase; }
}
