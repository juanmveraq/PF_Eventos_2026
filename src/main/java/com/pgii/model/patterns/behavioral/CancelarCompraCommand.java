package com.pgii.model.patterns.behavioral;
import com.pgii.model.entities.Compra;
import com.pgii.model.interfaces.ICommand;
import com.pgii.model.interfaces.IEstadoCompra;

/** Comando que cancela una compra. TODO: completar deshacer(). */
public class CancelarCompraCommand implements ICommand {
    private Compra theCompra;
    private IEstadoCompra estadoAnterior;
    public CancelarCompraCommand(Compra c) { theCompra=c; }
    @Override public void ejecutar() {
        if(theCompra==null) return;
        estadoAnterior=theCompra.getTheEstado();
        theCompra.getTheEstado().cancelar(theCompra);
    }
    @Override public void deshacer() { /* TODO: theCompra.setTheEstado(estadoAnterior) */ }
}
