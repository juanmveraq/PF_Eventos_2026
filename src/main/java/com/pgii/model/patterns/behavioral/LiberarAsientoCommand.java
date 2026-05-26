package com.pgii.model.patterns.behavioral;
import com.pgii.model.entities.Asiento;
import com.pgii.model.enums.EstadoAsiento;
import com.pgii.model.interfaces.ICommand;

/**
 * comand que libera un asiento poniendolo en estado DISPONIBLE y permite deshacer la operacion.
 * implementa el patron Command (ICommand) para encapsular la accion de liberacion
 * con lo que damos soporte de deshacer (undo).
 */
public class LiberarAsientoCommand implements ICommand {

    /** el asiento sobre el que se ejecuta el comando de liberacion. */
    private Asiento theAsiento;

    /** estado previo del asiento antes de ejecutar el comando, para permitir el deshacer. */
    private EstadoAsiento estadoAnterior;

    public LiberarAsientoCommand(Asiento a) { theAsiento=a; }

    /**
     * ejecuta el comando guardando el estado actual y liberando el asiento (DISPONIBLE).
     * no hace nada si el asiento es nulo.
     */
    @Override public void ejecutar() { if(theAsiento==null) return; estadoAnterior=theAsiento.getEstado(); theAsiento.setEstado(EstadoAsiento.DISPONIBLE); }

    /**
     * deshace el comando restaurando el estado previo del asiento.
     * no hace nada si el asiento o el estado anterior son nulos, es decir,
     * no tiene definido un estado previo como tal.
     */
    @Override public void deshacer() { if(theAsiento!=null && estadoAnterior!=null) theAsiento.setEstado(estadoAnterior); }
}
