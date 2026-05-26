package com.pgii.model.patterns.behavioral;
import com.pgii.model.entities.Compra;
import com.pgii.model.enums.EstadoCompra;
import com.pgii.model.interfaces.IEstadoCompra;

/**
 * estado de una compra que ya fue pagada exitosamente pero aun no confirmada.
 * implementa el patron State para el ciclo de vida de la compra.
 * desde este estado se puede confirmar, cancelar o reembolsar la compra.
 */
public class EstadoPagada implements IEstadoCompra {

    @Override public boolean pagar(Compra compra) { return false; }

    @Override public boolean confirmar(Compra compra) {
        compra.setTheEstado(new EstadoConfirmada()); return true;
    }

    @Override public boolean cancelar(Compra compra) {
        compra.setTheEstado(new EstadoCancelada()); return true;
    }

    @Override public boolean reembolsar(Compra compra) {
        compra.setTheEstado(new EstadoReembolsada()); return true;
    }

    @Override public EstadoCompra getEstadoEnum() { return EstadoCompra.PAGADA; }

    @Override public String getNombre() { return "PAGADA"; }
}
