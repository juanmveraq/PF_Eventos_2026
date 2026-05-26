package com.pgii.model.patterns.structural;
import com.pgii.model.enums.TipoServicio;

/**
 * decordaro de servicio que agrega la reserva de parqueadero a una entrada.
 * aplica el patron Decorator sobre ServicioAdicional.
 */
public class ParqueaderoDecorador extends ServicioDecorador {

    public ParqueaderoDecorador(ServicioAdicional s) { super(s); }

    @Override public String getDescripcion() { return theServicio.getDescripcion() + " + Parqueadero"; }

    @Override public double getPrecioExtra() { return theServicio.getPrecioExtra() + TipoServicio.PARQUEADERO.getPrecio(); }
}
