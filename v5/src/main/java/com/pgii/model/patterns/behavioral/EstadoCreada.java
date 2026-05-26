package com.pgii.model.patterns.behavioral;
import com.pgii.model.entities.Compra;
import com.pgii.model.enums.EstadoCompra;
import com.pgii.model.interfaces.IEstadoCompra;

/**
 * estado inicial de una compra recien creada antes de que sea pagada.
 * implementa el patron State para el ciclo de vida de la compra.
 * desde este estado solo se puede pagar o cancelar la compra.
 */
public class EstadoCreada implements IEstadoCompra {

    @Override public boolean pagar(Compra compra) {
        compra.setTheEstado(new EstadoPagada()); return true;
    }

    @Override public boolean confirmar(Compra compra) { return false; }

    @Override public boolean cancelar(Compra compra) {
        compra.setTheEstado(new EstadoCancelada()); return true;
    }

    @Override public boolean reembolsar(Compra compra) { return false; }

    @Override public EstadoCompra getEstadoEnum() { return EstadoCompra.CREADA; }

    @Override public String getNombre() { return "CREADA"; }
}
