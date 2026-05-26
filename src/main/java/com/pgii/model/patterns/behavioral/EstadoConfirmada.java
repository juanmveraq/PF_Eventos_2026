package com.pgii.model.patterns.behavioral;
import com.pgii.model.entities.Compra;
import com.pgii.model.enums.EstadoCompra;
import com.pgii.model.interfaces.IEstadoCompra;

/**
 * estado de una compra que fue verificada y confirmada definitivamente.
 * implementa el patron State para el ciclo de vida de la compra.
 * desde este estado solo se puede reembolsar la compra.
 */
public class EstadoConfirmada implements IEstadoCompra {

    @Override public boolean pagar(Compra compra)    { return false; }

    @Override public boolean confirmar(Compra compra) { return false; }

    @Override public boolean cancelar(Compra compra)  {
        compra.setTheEstado(new EstadoCancelada()); return true;
    }

    @Override public boolean reembolsar(Compra compra) {
        compra.setTheEstado(new EstadoReembolsada()); return true;
    }

    @Override public EstadoCompra getEstadoEnum() { return EstadoCompra.CONFIRMADA; }

    @Override public String getNombre() { return "CONFIRMADA"; }
}
