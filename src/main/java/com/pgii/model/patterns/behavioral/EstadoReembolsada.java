package com.pgii.model.patterns.behavioral;
import com.pgii.model.entities.Compra;
import com.pgii.model.enums.EstadoCompra;
import com.pgii.model.interfaces.IEstadoCompra;

/**
 * estado de una compra que fue reembolsada al usuario.
 * implementa el patron State para el ciclo de vida de la compra.
 * solo permite cancelar (liberar asiento desde admin); las demas operaciones no estan permitidas.
 */
public class EstadoReembolsada implements IEstadoCompra {

    @Override public boolean pagar(Compra compra)     { return false; }

    @Override public boolean confirmar(Compra compra)  { return false; }

    @Override public boolean cancelar(Compra compra) {
        compra.setTheEstado(new EstadoCancelada()); return true;
    }

    @Override public boolean reembolsar(Compra compra) { return false; }

    @Override public EstadoCompra getEstadoEnum() { return EstadoCompra.REEMBOLSADA; }

    @Override public String getNombre() { return "REEMBOLSADA"; }
}
