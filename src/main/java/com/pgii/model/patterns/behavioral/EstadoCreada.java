package com.pgii.model.patterns.behavioral;
import com.pgii.model.entities.Compra;
import com.pgii.model.enums.EstadoCompra;
import com.pgii.model.interfaces.IEstadoCompra;

/** Estado EstadoCreada del ciclo de vida de Compra. TODO: implementar transiciones correctas. */
public class EstadoCreada implements IEstadoCompra {
    @Override public boolean pagar(Compra c)     { return false; /* TODO */ }
    @Override public boolean confirmar(Compra c)  { return false; /* TODO */ }
    @Override public boolean cancelar(Compra c)   { return false; /* TODO */ }
    @Override public boolean reembolsar(Compra c) { return false; /* TODO */ }
    @Override public EstadoCompra getEstadoEnum() { return null;  /* TODO */ }
    @Override public String getNombre()           { return "EstadoCreada"; }
}
