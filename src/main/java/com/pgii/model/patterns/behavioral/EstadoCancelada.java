package com.pgii.model.patterns.behavioral;
import com.pgii.model.entities.Compra;
import com.pgii.model.enums.EstadoCompra;
import com.pgii.model.interfaces.IEstadoCompra;

/**
 * estado de una compra que fue cancelada por el usuario o el sistema.
 * implementa el patron State para el ciclo de vida de la compra.
 * desde este estado solo se puede reembolsar; las demas operaciones no estan permitidas.
 */
public class EstadoCancelada implements IEstadoCompra {

    @Override public boolean pagar(Compra compra)    { return false; }

    @Override public boolean confirmar(Compra compra) { return false; }

    @Override public boolean cancelar(Compra compra)  { return false; }

    @Override public boolean reembolsar(Compra compra) {
        compra.setTheEstado(new EstadoReembolsada()); return true;
    }

    @Override public EstadoCompra getEstadoEnum() { return EstadoCompra.CANCELADA; }

    @Override public String getNombre() { return "CANCELADA"; }
}
