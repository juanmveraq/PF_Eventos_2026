package com.pgii.model.patterns.structural;
import com.pgii.model.enums.TipoServicio;

/** Decorador MerchandisingDecorador. TODO: implementar getDescripcion() y getPrecioExtra() con el costo correcto. */
public class MerchandisingDecorador extends ServicioDecorador {
    public MerchandisingDecorador(ServicioAdicional s) { super(s); }
    @Override public String getDescripcion() { return theServicio.getDescripcion() + " + TODO"; }
    @Override public double getPrecioExtra()  { return theServicio.getPrecioExtra(); /* TODO: + TipoServicio.X.getPrecio() */ }
}
