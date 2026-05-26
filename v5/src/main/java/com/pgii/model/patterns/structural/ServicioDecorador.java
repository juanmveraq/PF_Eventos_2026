package com.pgii.model.patterns.structural;
public abstract class ServicioDecorador implements ServicioAdicional {
    protected ServicioAdicional theServicio;
    public ServicioDecorador(ServicioAdicional s) { theServicio=s; }
    @Override public String getDescripcion() { return theServicio.getDescripcion(); }
    @Override public double getPrecioExtra() { return theServicio.getPrecioExtra(); }
}
