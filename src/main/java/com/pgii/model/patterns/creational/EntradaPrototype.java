package com.pgii.model.patterns.creational;
import com.pgii.model.entities.Entrada;
import com.pgii.model.interfaces.IPrototype;

/**
 * clase que encapsula una Entrada y permite clonarla usando el patron Prototype.
 * este actua como wrapper para delegar la clonacion al metodo clonar() de la entrada.
 * es util cuando se necesita duplicar entradas sin conocer los detalles de su construccion.
 */
public class EntradaPrototype implements IPrototype {

    /** entrada original que se usara como plantilla para las clonaciones. */
    private Entrada theEntrada;

    public EntradaPrototype(Entrada e) { theEntrada=e; }

    /**
     * crea y retorna una copia de la entrada almacenada en este prototipo.
     * @return copia de la Entrada original con los mismos atributos.
     */
    @Override public Entrada clonar() { return theEntrada.clonar(); }

    /** retorna la entrada original almacenada en el prototipo. */
    public Entrada getTheEntrada() { return theEntrada; }

    /** asigna la entrada a usar como plantilla para la clonacion. */
    public void setTheEntrada(Entrada v) { theEntrada=v; }
}
