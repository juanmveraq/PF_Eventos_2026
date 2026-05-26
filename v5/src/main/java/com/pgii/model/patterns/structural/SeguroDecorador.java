package com.pgii.model.patterns.structural;
import com.pgii.model.enums.TipoServicio;

/** Decorador SeguroDecorador. TODO: implementar getDescripcion() y getPrecioExtra() con el costo correcto. */
public class SeguroDecorador extends ServicioDecorador {
    public SeguroDecorador(ServicioAdicional s) { super(s); }
    @Override public String getDescripcion() { return theServicio.getDescripcion() + " + TODO"; }
    @Override public double getPrecioExtra()  { return theServicio.getPrecioExtra(); /* TODO: + TipoServicio.X.getPrecio() */ }
}
