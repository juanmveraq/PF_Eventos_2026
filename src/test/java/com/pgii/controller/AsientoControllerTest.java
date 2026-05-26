package com.pgii.controller;

import com.pgii.model.entities.*;
import com.pgii.model.enums.EstadoAsiento;
import com.pgii.model.patterns.creational.GestorDatos;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * las pruebas unitarias para {@link AsientoController}.
 * en este usamos la clase para creacion de asientos, bloqueo, habilitacion, liberacion,
 * busqueda por id y listado por zona.
 *
 * <p>el {@code @BeforeAll} crea una zona compartida ({@code idZona}) y un asiento
 * de referencia ({@code idAsiento}) para los tests de busqueda y listado.
 * luego, los tests que verifican el estado inicial crean su propio asiento;
 * los que modifican estado lo preestablecen directamente para no depender del orden.</p>
 */
class AsientoControllerTest {

    static AsientoController ctrl;
    static int idZona;
    static int idAsiento;

    /**
     * crear el recinto, la zona y el asiento de referencia usados por los tests de lectura.
     */
    @BeforeAll
    static void setup() {
        ctrl = new AsientoController();
        RecintoController rCtrl = new RecintoController();
        Recinto r = rCtrl.crearRecinto("recintorandom", "Dir A", "Cali");
        rCtrl.agregarZona(r.getIdRecinto(), "VIP", 10, 200000);
        Zona z = r.getListaZonas().get(0);
        idZona = z.getIdZona();
        Asiento a = ctrl.crearAsiento(idZona, "A", 1);
        assertNotNull(a);
        idAsiento = a.getIdAsiento();
    }

    /**
     * verifica que crearAsiento genera el asiento con estado inicial DISPONIBLE.
     * crea un asiento propio para que su estado no haya sido modificado por otro test.
     * Metodo probado: {@code crearAsiento(idZona, fila, num)}.
     */
    @Test
    void crear_estadoInicialDisponible() {
        Asiento fresco = ctrl.crearAsiento(idZona, "Z", 99);
        assertNotNull(fresco);
        assertEquals(EstadoAsiento.DISPONIBLE, fresco.getEstado());
    }

    /**
     * verifica que bloquearAsiento cambia el estado del asiento a BLOQUEADO
     * usando el patron Command.
     * Metodo probado: {@code bloquearAsiento(id)}.
     */
    @Test
    void bloquear_cambiaABloqueado() {
        assertTrue(ctrl.bloquearAsiento(idAsiento));
        assertEquals(EstadoAsiento.BLOQUEADO, ctrl.buscarPorId(idAsiento).getEstado());
    }

    /**
     * verifica que habilitarAsiento restaura el estado del asiento a DISPONIBLE
     * usando el patron Command con LiberarAsientoCommand.
     * preestablecemos el estado a BLOQUEADO directamente para no depender de que
     * bloquear_cambiaABloqueado haya corrido antes.
     * Metodo probado: {@code habilitarAsiento(id)}.
     */
    @Test
    void habilitar_restauraDisponible() {
        GestorDatos.getInstancia().buscarAsientoPorId(idAsiento).setEstado(EstadoAsiento.BLOQUEADO);
        assertTrue(ctrl.habilitarAsiento(idAsiento));
        assertEquals(EstadoAsiento.DISPONIBLE, ctrl.buscarPorId(idAsiento).getEstado());
    }

    /**
     * se verifica que liberarAsiento pone el asiento en DISPONIBLE sin importar el estado previo.
     * en este preestablecemos el estado a RESERVADO directamente para no depender de otro test.
     * Metodo probado: {@code liberarAsiento(id)}.
     */
    @Test
    void liberar_restauraDisponible() {
        GestorDatos.getInstancia().buscarAsientoPorId(idAsiento).setEstado(EstadoAsiento.RESERVADO);
        assertTrue(ctrl.liberarAsiento(idAsiento));
        assertEquals(EstadoAsiento.DISPONIBLE, ctrl.buscarPorId(idAsiento).getEstado());
    }

    /**
     * se verifica que buscarPorId retorna el asiento de referencia del setup.
     * Metodo probado: {@code buscarPorId(id)}.
     */
    @Test
    void buscarId_retornaAsiento() {
        assertNotNull(ctrl.buscarPorId(idAsiento));
    }

    /**
     * se verifica que listarPorZona retorna todos los asientos registrados en la zona indicada.
     * Metodo probado: {@code listarPorZona(idZona)}.
     */
    @Test
    void listarZona_noVacio() {
        assertFalse(ctrl.listarPorZona(idZona).isEmpty());
    }
}
