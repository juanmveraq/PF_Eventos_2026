package com.pgii.model.patterns.behavioral;
import com.pgii.model.entities.Asiento;
import com.pgii.model.enums.EstadoAsiento;
import com.pgii.model.interfaces.ICommand;

/** Comando que libera un asiento. TODO: completar deshacer(). */
public class LiberarAsientoCommand implements ICommand {
    private Asiento theAsiento;
    private EstadoAsiento estadoAnterior;
    public LiberarAsientoCommand(Asiento a) { theAsiento=a; }
    @Override public void ejecutar() {
        if(theAsiento==null) return;
        estadoAnterior=theAsiento.getEstado();
        theAsiento.setEstado(EstadoAsiento.DISPONIBLE);
    }
    @Override public void deshacer() { /* TODO: theAsiento.setEstado(estadoAnterior) */ }
}
