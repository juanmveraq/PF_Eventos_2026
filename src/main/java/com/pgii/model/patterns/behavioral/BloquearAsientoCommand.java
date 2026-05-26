package com.pgii.model.patterns.behavioral;
import com.pgii.model.entities.Asiento;
import com.pgii.model.enums.EstadoAsiento;
import com.pgii.model.interfaces.ICommand;

/**
 * comando que bloquea un asiento y permite deshacer la operacion restaurando su estado previo.
 * Implementa el patron Command (ICommand) para encapsular la accion de bloqueo
 * con soporte de deshacer (undo).
 */
public class BloquearAsientoCommand implements ICommand {

    /** el asiento sobre el que se ejecuta el comando de bloqueo. */
    private Asiento theAsiento;

    /** estaod previo del asiento antes de ejecutar el comando, para permitir el deshacer. */
    private EstadoAsiento estadoAnterior;

    public BloquearAsientoCommand(Asiento a) { theAsiento=a; }

    /**
     * ejecuta el comando guardando el estado actual y bloqueando el asiento.
     * no hace nada si el asiento es nulo.
     */
    @Override public void ejecutar() { if(theAsiento==null) return; estadoAnterior=theAsiento.getEstado(); theAsiento.setEstado(EstadoAsiento.BLOQUEADO); }

    /**
     * deshace el comando restaurando el estado previo del asiento.
     * no hace nada si el asiento o el estado anterior son nulos.
     */
    @Override public void deshacer() { if(theAsiento!=null && estadoAnterior!=null) theAsiento.setEstado(estadoAnterior); }
}
