package com.pgii.model.interfaces;

/**
 * interfaz que crea un contrato de implementar del patron Command que encapsula una accion ejecutable y reversible.
 * las clases clases que implementen esta interfaz representan comandos especificos
 * como bloquear/liberar asientos o cancelar compras.
 */
public interface ICommand {

    /**
     * ejecuta la accion encapsulada por el comando.
     */
    public void ejecutar();

    /**
     * revisa la accion ejecutada, restaurando el estado anterior.
     */
    public void deshacer();
}
