package com.pgii.model.patterns.creational;
import com.pgii.model.entities.Entrada;
import com.pgii.model.interfaces.IPrototype;

/** Prototype para clonar Entradas. TODO: implementar clonar(). */
public class EntradaPrototype implements IPrototype {
    private Entrada theEntrada;
    public EntradaPrototype(Entrada e) { theEntrada=e; }
    @Override public Entrada clonar() { return null; /* TODO: return theEntrada.clonar() */ }
    public Entrada getTheEntrada() { return theEntrada; }
    public void setTheEntrada(Entrada v) { theEntrada=v; }
}
