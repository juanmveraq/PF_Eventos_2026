package com.pgii.controller;

import com.pgii.model.entities.Recinto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * prueba unitarias para {@link RecintoController}.
 * esta cubre creacion, busqueda, actualizacion, adicion de zonas y eliminacion de recintos.
 *
 * <p>El {@code @BeforeAll} crea un recinto compartido de referencia ({@code idRecinto}).
 * Los test que modifican datos del recinto crean el suyo propio para no interferir
 * con los test de lectura que verifican el nombre original.</p>
 */
class RecintoControllerTest {

    static RecintoController ctrl;
    static int idRecinto;

    /**
     * Inicializa el controlador y crea el recinto base que usan los test de lectura.
     */
    @BeforeAll
    static void setup() {
        ctrl = new RecintoController();
        Recinto r = ctrl.crearRecinto("recinto para pruebas", "Av 10 #5-20", "Medellin");
        assertNotNull(r);
        idRecinto = r.getIdRecinto();
    }

    /**
     * verifica que crearRecinto registra el recinto mediante el patron Builder
     * y puede recuperarse por su id.
     * Metodo probado: {@code crearRecinto(nombre, dir, ciudad)}.
     */
    @Test
    void crear_existeEnGestor() {
        assertNotNull(ctrl.buscarPorId(idRecinto));
    }

    /**
     * verifica que buscarPorId retorna el recinto con el nombre correcto.
     * Metodo probado: {@code buscarPorId(id)}.
     */
    @Test
    void buscarId_retornaRecinto() {
        assertEquals("recinto para pruebas", ctrl.buscarPorId(idRecinto).getNombre());
    }

    /**
     * verifica que listarRecintos retorna al menos un recinto registrado.
     * Metodo probado: {@code listarRecintos()}.
     */
    @Test
    void listar_noVacio() {
        assertFalse(ctrl.listarRecintos().isEmpty());
    }

    /**
     * verifica que actualizarRecinto cambia el nombre del recinto.
     * usa un recinto propio para no alterar el nombre del recinto compartido del setup.
     * metodo probado: {@code actualizarRecinto(id, nombre, dir, ciudad)}.
     */
    @Test
    void actualizar_cambiaDatos() {
        Recinto fresco = ctrl.crearRecinto("RecintoActualizar", "Dir", "Cali");
        assertNotNull(fresco);
        assertTrue(ctrl.actualizarRecinto(fresco.getIdRecinto(), "RecintoRenombrado", null, null));
        assertEquals("RecintoRenombrado", ctrl.buscarPorId(fresco.getIdRecinto()).getNombre());
    }

    /**
     * Verifica que agregarZona incrementa en uno la cantidad de zonas del recinto.
     * Metodo probado: {@code agregarZona(idRec, nom, cap, precio)}.
     */
    @Test
    void agregarZona_aumentaConteo() {
        int antes = ctrl.buscarPorId(idRecinto).getListaZonas().size();
        assertTrue(ctrl.agregarZona(idRecinto, "General", 100, 60000));
        assertEquals(antes + 1, ctrl.buscarPorId(idRecinto).getListaZonas().size());
    }

    /**
     * esto verifica que eliminarRecinto borra el recinto y ya no puede encontrarse.
     * crea su propio recinto para no eliminar el recinto compartido del setup.
     * Metodo probado: {@code eliminarRecinto(id)}.
     */
    @Test
    void eliminar_yaNoExiste() {
        Recinto r = ctrl.crearRecinto("ParaEliminar", "Dir", "Cali");
        int id = r.getIdRecinto();
        assertTrue(ctrl.eliminarRecinto(id));
        assertNull(ctrl.buscarPorId(id));
    }
}
