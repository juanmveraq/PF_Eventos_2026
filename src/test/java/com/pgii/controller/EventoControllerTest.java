package com.pgii.controller;

import com.pgii.model.entities.*;
import com.pgii.model.enums.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * pruebas unitarias para {@link EventoController}.
 * cubre creacion, busqueda, actualizacion, cambios de estado,
 * filtrado por categoria y ciudad, y eliminacion de eventos.
 *
 * <p>el {@code @BeforeAll} crea un evento CONCIERTO compartido en estado BORRADOR.
 * los tests que realizan transiciones de estado o modifican datos crean su propio evento
 * para garantizar independencia entre pruebas.</p>
 */
class EventoControllerTest {

    static EventoController ctrl;
    static int idEvento;
    static int idRecinto;
    static final String CIUDAD = "CiudadTest";

    /**
     * crea el recinto y el evento compartido usados por los tests de lectura y filtrado.
     */
    @BeforeAll
    static void setup() {
        ctrl = new EventoController();
        RecintoController rCtrl = new RecintoController();
        Recinto r = rCtrl.crearRecinto("recinto para pruebas", "Calle 1", CIUDAD);
        rCtrl.agregarZona(r.getIdRecinto(), "General", 50, 50000);
        idRecinto = r.getIdRecinto();

        Evento e = ctrl.crearEvento("evento para pruebas", CategoriaEvento.CONCIERTO,
                "Desc", CIUDAD, "2026-12-01 18:00", idRecinto, "Artista", "Rock");
        assertNotNull(e);
        idEvento = e.getIdEvento();
    }

    /**
     * verifica que crearEvento registra el evento y puede recuperarse por id.
     * Metodo probado: {@code crearEvento(nombre, cat, desc, ciudad, fechaHora, idRecinto, info1, info2)}.
     */
    @Test
    void crear_existeEnGestor() {
        assertNotNull(ctrl.buscarPorId(idEvento));
    }

    /**
     * verifica que listarEventos retorna al menos un evento registrado.
     * Metodo probado: {@code listarEventos()}.
     */
    @Test
    void listar_noVacio() {
        assertFalse(ctrl.listarEventos().isEmpty());
    }

    /**
     * verifica que buscarPorId retorna el evento con el nombre correcto.
     * Metodo probado: {@code buscarPorId(id)}.
     */
    @Test
    void buscarId_retornaEvento() {
        Evento e = ctrl.buscarPorId(idEvento);
        assertNotNull(e);
        assertEquals("evento para pruebas", e.getNombre());
    }

    /**
     * verifica que actualizarEvento cambia el nombre del evento.
     * usa un evento propio para no alterar el nombre del evento compartido del setup.
     * Metodo probado: {@code actualizarEvento(id, nombre, desc, ciudad, fecha)}.
     */
    @Test
    void actualizar_cambiaNombre() {
        Evento fresco = ctrl.crearEvento("EventoActualizar", CategoriaEvento.CONFERENCIA,
                "Desc", CIUDAD, "2026-12-02 10:00", idRecinto, "X", "Y");
        assertNotNull(fresco);
        assertTrue(ctrl.actualizarEvento(fresco.getIdEvento(), "EventoRenombrado", null, null, null));
        assertEquals("EventoRenombrado", ctrl.buscarPorId(fresco.getIdEvento()).getNombre());
    }

    /**
     * verifica que publicarEvento cambia el estado a PUBLICADO desde BORRADOR.
     * crea un evento propio en estado BORRADOR para no depender del estado del evento compartido.
     * Metodo probado: {@code publicarEvento(id)}.
     */
    @Test
    void publicar_cambiAPublicado() {
        Evento fresco = ctrl.crearEvento("EventoPublicar", CategoriaEvento.TEATRO,
                "Desc", CIUDAD, "2026-12-03 20:00", idRecinto, "X", "Y");
        assertNotNull(fresco);
        assertTrue(ctrl.publicarEvento(fresco.getIdEvento()));
        assertEquals(EstadoEvento.PUBLICADO, ctrl.buscarPorId(fresco.getIdEvento()).getEstado());
    }

    /**
     * verifica que pausarEvento cambia el estado a PAUSADO cuando el evento esta PUBLICADO.
     * crea un evento propio, lo publica y luego lo pausa para garantizar el estado inicial correcto.
     * Metodo probado: {@code pausarEvento(id)}.
     */
    @Test
    void pausar_cambiAPausado() {
        Evento fresco = ctrl.crearEvento("EventoPausar", CategoriaEvento.CONCIERTO,
                "Desc", CIUDAD, "2026-12-04 21:00", idRecinto, "X", "Y");
        assertNotNull(fresco);
        ctrl.publicarEvento(fresco.getIdEvento());
        assertTrue(ctrl.pausarEvento(fresco.getIdEvento()));
        assertEquals(EstadoEvento.PAUSADO, ctrl.buscarPorId(fresco.getIdEvento()).getEstado());
    }

    /**
     * se verifica que cancelarEvento cambia el estado a CANCELADO.
     * crea un evento propio para no afectar el evento compartido del setup.
     * Metodo probado: {@code cancelarEvento(id)}.
     */
    @Test
    void cancelar_cambiACancelado() {
        Evento fresco = ctrl.crearEvento("EventoCancelar", CategoriaEvento.CONCIERTO,
                "Desc", CIUDAD, "2026-12-05 22:00", idRecinto, "X", "Y");
        assertNotNull(fresco);
        assertTrue(ctrl.cancelarEvento(fresco.getIdEvento()));
        assertEquals(EstadoEvento.CANCELADO, ctrl.buscarPorId(fresco.getIdEvento()).getEstado());
    }

    /**
     * verifica que filtrarPorCategoria retorna al menos un evento de la categoria CONCIERTO.
     * Metodo probado: {@code filtrarPorCategoria(cat)}.
     */
    @Test
    void filtrarCategoria_encuentraConciertos() {
        assertFalse(ctrl.filtrarPorCategoria(CategoriaEvento.CONCIERTO).isEmpty());
    }

    /**
     * verifica que filtrarPorCiudad retorna al menos un evento en la ciudad de prueba.
     * Metodo probado: {@code filtrarPorCiudad(ciudad)}.
     */
    @Test
    void filtrarCiudad_encuentraEvento() {
        assertFalse(ctrl.filtrarPorCiudad(CIUDAD).isEmpty());
    }

    /**
     * verifica que eliminarEvento borra el evento y ya no puede encontrarse.
     * crea un evento propio para no eliminar el evento compartido del setup.
     * Metodo probado: {@code eliminarEvento(id)}.
     */
    @Test
    void eliminar_yaNoExiste() {
        Evento fresco = ctrl.crearEvento("EventoEliminar", CategoriaEvento.TEATRO,
                "Desc", CIUDAD, "2026-12-06 19:00", idRecinto, "X", "Y");
        assertNotNull(fresco);
        assertTrue(ctrl.eliminarEvento(fresco.getIdEvento()));
        assertNull(ctrl.buscarPorId(fresco.getIdEvento()));
    }
}
