package com.pgii.model.patterns.behavioral;
import com.pgii.model.entities.Compra;
import com.pgii.model.interfaces.ICommand;
import com.pgii.model.interfaces.IEstadoCompra;

/**
 * comando que cancela una compra y permite deshacer la operacion restaurando su estado previo.
 * implementa el patron Command (ICommand) para encapsular la cancelacion
 * con soporte de deshacer (undo).
 */
public class CancelarCompraCommand implements ICommand {

    /** la compra sobre la que se ejecuta el comando de cancelacion. */
    private Compra theCompra;

    /** estado previo de la compra antes de ejecutar el comando, para permitir el deshacer. */
    private IEstadoCompra estadoAnterior;

    public CancelarCompraCommand(Compra c) { theCompra=c; }

    /**
     * ejecuta el comando guardando el estado actual y aplicando la cancelacion segun el patron State.
     * no hace nada si la compra es nula.
     */
    @Override public void ejecutar() { if(theCompra==null) return; estadoAnterior=theCompra.getTheEstado(); theCompra.getTheEstado().cancelar(theCompra); }

    /**
     * deshace el comando restaurando el estado previo de la compra.
     * no hace nada si la compra o el estado anterior son nulos.
     */
    @Override public void deshacer() { if(theCompra!=null && estadoAnterior!=null) theCompra.setTheEstado(estadoAnterior); }
}
