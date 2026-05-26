package com.pgii.model.patterns.behavioral;
import com.pgii.model.entities.Asiento;
import com.pgii.model.enums.EstadoAsiento;
import com.pgii.model.interfaces.ICommand;

/** Comando que bloquea un asiento. TODO: completar deshacer(). */
public class BloquearAsientoCommand implements ICommand {
    private Asiento theAsiento;
    private EstadoAsiento estadoAnterior;
    public BloquearAsientoCommand(Asiento a) { theAsiento=a; }
    @Override public void ejecutar() {
        if(theAsiento==null) return;
        estadoAnterior=theAsiento.getEstado();
        theAsiento.setEstado(EstadoAsiento.BLOQUEADO);
    }
    @Override public void deshacer() { /* TODO: theAsiento.setEstado(estadoAnterior) */ }
}
