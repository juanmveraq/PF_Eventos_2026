package com.pgii.model.interfaces;
import com.pgii.model.entities.Compra;
import com.pgii.model.enums.EstadoCompra;

/**
 * interfaz que susa el patron State que define el comportamiento de cada estado del ciclo de vida de una compra.
 * cada implementacion concreta (como tenemos en el enum y luego cada tipo de estado en
 * los behavioral:EstadoCreada, EstadoPagada, etc.) define que operaciones
 * estan permitidas y como transiciona la compra al siguiente estado.
 * retorna verdadero (true) si la operacion fue ejecutada, falso (false) si no esta permitida en el estado actual.
 */
public interface IEstadoCompra {

    /**
     * procesa el pago de la compra si el estado actual lo permite.
     * @param compra Compra sobre la que se aplica la transicion.
     * @return verdadero (true) si la operacion fue ejecutada, falso (false) si no esta permitida.
     */
    public boolean pagar(Compra compra);

    /**
     * confirma la compra si el estado actual lo permite.
     * @param compra Compra sobre la que se aplica la transicion.
     * @return verdadero (true) si la operacion fue ejecutada, falso (false) si no esta permitida.
     */
    public boolean confirmar(Compra compra);

    /**
     * cancela la compra si el estado actual lo permite.
     * @param compra Compra sobre la que se aplica la transicion.
     * @return verdadero (true) si la operacion fue ejecutada, falso (false) si no esta permitida.
     */
    public boolean cancelar(Compra compra);

    /**
     * Reembolsa la compra si el estado actual lo permite.
     * @param compra Compra sobre la que se aplica la transicion.
     * @return verdadero (true) si la operacion fue ejecutada, falso (false) si no esta permitida.
     */
    public boolean reembolsar(Compra compra);

    /**
     * retorna el enum EstadoCompra correspondiente a este estado.
     * @return el valor del enum que representa este estado.
     */
    public EstadoCompra getEstadoEnum();

    /**
     * retorna el nombre en texto de este estado de compra.
     * @return Nombre del estado como cadena (por ejemplo: "CREADA").
     */
    public String getNombre();
}
