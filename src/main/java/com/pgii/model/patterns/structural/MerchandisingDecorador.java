package com.pgii.model.patterns.structural;
import com.pgii.model.enums.TipoServicio;

/**
 * decorador de servicio que agrega el paquete de merchandising a una entrada.
 * aplica el patron Decorator sobre ServicioAdicional.
 */
public class MerchandisingDecorador extends ServicioDecorador {

    public MerchandisingDecorador(ServicioAdicional s) { super(s); }

    @Override public String getDescripcion() { return theServicio.getDescripcion() + " + Merch"; }

    @Override public double getPrecioExtra() { return theServicio.getPrecioExtra() + TipoServicio.MERCHANDISING.getPrecio(); }
}
