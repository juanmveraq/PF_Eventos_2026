package com.pgii.model.patterns.structural;

/**
 * clase abstracta base del patron Decorator para los servicios adicionales.
 * envulve un objeto ServicioAdicional y delega las llamadas al componente interno.
 * las subclases concretas agregan su descripcion y precio especifico al resultado.
 */
public abstract class ServicioDecorador implements ServicioAdicional {

    /** el servicio o decorador envuelto por este decorador. */
    protected ServicioAdicional theServicio;

    public ServicioDecorador(ServicioAdicional s) { theServicio=s; }

    /**
     * retorna la descripcion del servicio envuelto. Las subclases la extienden.
     * @return descripcion del servicio.
     */
    @Override public String getDescripcion() { return theServicio.getDescripcion(); }

    /**
     * retorna el precio extra del servicio envuelto. Las subclases suman su costo.
     * @return precio extra acumulado.
     */
    @Override public double getPrecioExtra() { return theServicio.getPrecioExtra(); }
}
