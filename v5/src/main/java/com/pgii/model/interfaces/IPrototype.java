package com.pgii.model.interfaces;

/**
 * interfaz del patron Prototype que permite clonar objetos del dominio.
 * las clases que implementen esta interfaz (Asiento, Entrada) pueden crear copias
 * exactas de si mismas sin depender de su clase concreta.
 */
public interface IPrototype {

    /**
     * crea y retorna una copia del objeto que implementa esta interfaz.
     * @return una copia del objeto original con los mismos atributos.
     */
    public Object clonar();
}
