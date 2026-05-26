package com.pgii.model.patterns.structural;
public class EntradaSimple implements ServicioAdicional {
    private double precioBase;
    public EntradaSimple(double p) { precioBase=p; }
    @Override public String getDescripcion() { return "Una entrada base"; }
    @Override public double getPrecioExtra() { return precioBase; }
}
