package com.pgii.model.patterns.structural;
import com.pgii.model.enums.TipoServicio;

/**
 * decorado de servicio que agrega el servicio VIP a una entrada.
 * Aplica el patron Decorator sobre ServicioAdicional.
 */
public class VIPDecorador extends ServicioDecorador {

    public VIPDecorador(ServicioAdicional s) { super(s); }

    @Override public String getDescripcion() { return theServicio.getDescripcion() + " + Trato VIP en el evento"; }

    @Override public double getPrecioExtra() { return theServicio.getPrecioExtra() + TipoServicio.VIP.getPrecio(); }
}
