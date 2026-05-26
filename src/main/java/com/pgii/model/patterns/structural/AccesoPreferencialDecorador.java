package com.pgii.model.patterns.structural;
import com.pgii.model.enums.TipoServicio;

/**
 * decorador de servicio que agrega el acceso preferencial al evento a una entrada.
 * este aplica el patron Decorator sobre ServicioAdicional.
 */
public class AccesoPreferencialDecorador extends ServicioDecorador {

    public AccesoPreferencialDecorador(ServicioAdicional s) { super(s); }

    @Override public String getDescripcion() { return theServicio.getDescripcion() + " + Acceso preferencial"; }

    @Override public double getPrecioExtra() { return theServicio.getPrecioExtra() + TipoServicio.ACCESO_PREFERENCIAL.getPrecio(); }
}
