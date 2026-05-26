package com.pgii.model.patterns.creational;
import com.pgii.model.entities.*;
import com.pgii.model.patterns.behavioral.EstadoCreada;
import java.time.LocalDate;

/**
 * constructor de objetos Compra que implementa el patron Builder.
 * permite armar una compra paso a paso asignando sus atributos de forma encadenada.
 * Al llamar a build(), valida los datos requeridos y crea la Compra con estado CREADA.
 */
public class CompraBuilder {

    /** identificador de la compra a construir. */
    private int idCompra;

    /** fecha de creacion de la compra. */
    private String fechaCreacion;

    /** usuario que realizara la compra. */
    private Usuario theUsuario;

    /** evento al que corresponde la compra. */
    private Evento theEvento;

    /** asigna el id de la compra.
     * @param v Id de la compra.
     * @return Este builder para encadenamiento. */
    public CompraBuilder setIdCompra(int v) { idCompra=v; return this; }

    /** asigna la fecha de creacion de la compra.
     * @param v Fecha en formato texto.
     * @return este builder para encadenamiento. */
    public CompraBuilder setFechaCreacion(String v) { fechaCreacion=v; return this; }

    /** asigna el usuario de la compra.
     * @param v Usuario que realiza la compra.
     * @return este builder para encadenamiento. */
    public CompraBuilder setUsuario(Usuario v) { theUsuario=v; return this; }

    /** asigna el evento de la compra.
     * @param v Evento seleccionado.
     * @return este builder para encadenamiento. */
    public CompraBuilder setEvento(Evento v) { theEvento=v; return this; }

    /**
     * construye y retorna la Compra con los datos acumulados.
     * inicializacion el estado de la compra como CREADA (patron State).
     * @return retorna instancia de Compra lista para usar.
     * @throws IllegalStateException si es que falta el usuario o el evento.
     */
    public Compra build() {
        if (theUsuario==null) throw new IllegalStateException("hace falta usuario");
        if (theEvento==null)  throw new IllegalStateException("hace falta evento");
        if (fechaCreacion==null) fechaCreacion = LocalDate.now().toString();
        Compra c = new Compra(idCompra, fechaCreacion, theUsuario, theEvento);
        c.setTheEstado(new EstadoCreada());
        return c;
    }
}
