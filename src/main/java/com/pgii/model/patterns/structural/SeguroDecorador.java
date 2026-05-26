package com.pgii.model.patterns.structural;
import com.pgii.model.enums.TipoServicio;

/**
 * decordaor de servicio que agrega el seguro de cancelacion a una entrada.
 * aplica el patron Decorator sobre ServicioAdicional.
 */
public class SeguroDecorador extends ServicioDecorador {

    public SeguroDecorador(ServicioAdicional s) { super(s); }

    @Override public String getDescripcion() { return theServicio.getDescripcion() + " + Seguro de cancelación"; }

    @Override public double getPrecioExtra() { return theServicio.getPrecioExtra() + TipoServicio.SEGURO.getPrecio(); }
}
