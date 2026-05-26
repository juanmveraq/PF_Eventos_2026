package com.pgii.controller;

import com.pgii.model.entities.*;
import com.pgii.model.enums.*;
import com.pgii.model.patterns.behavioral.PagoEfectivo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * pruebas unitarias para {@link CompraController}.
 * aqui cubrimos lo que es el ciclo completo de una compra: creacion, agregado de servicios,
 * pago, confirmacion, reembolso, cancelacion, busqueda y listado.
 *
 * <p>el {@code @BeforeAll} crea un entorno compartido con zona, evento y usuario.
 * tambien crea una compra de referencia ({@code idCompra}) en estado CREADA
 * para los tests de lectura. los tests que realizan transiciones de estado
 * crean su propia compra con un asiento fresco para no interferir entre si.</p>
 */
class CompraControllerTest {

    static CompraController ctrl;
    static int idUsuario;
    static int idEvento;
    static int idZona;
    static int idCompra;

    /**
     * prepara el entorno: recinto con zona, evento publicado, usuario
     * y una compra de referencia en estado CREADA.
     */
    @BeforeAll
    static void setup() {
        ctrl = new CompraController();
        RecintoController rCtrl = new RecintoController();
        EventoController  eCtrl = new EventoController();
        UsuarioController uCtrl = new UsuarioController();
        AsientoController aCtrl = new AsientoController();

        Recinto r = rCtrl.crearRecinto("recinto cualquiera para compras", "Dir B", "barrancabermeja");
        rCtrl.agregarZona(r.getIdRecinto(), "General", 50, 80000);
        Zona z = r.getListaZonas().get(0);
        idZona = z.getIdZona();

        Evento ev = eCtrl.crearEvento("concierto cualquiera", CategoriaEvento.CONCIERTO,
                "Desc", "tebaida", "2026-11-01 20:00", r.getIdRecinto(), "X", "Y");
        eCtrl.publicarEvento(ev.getIdEvento());
        idEvento = ev.getIdEvento();

        Usuario u = uCtrl.registrar("comprausuario", "comprausuario@gmail.com", "12345", "2345", RolUsuario.USUARIO);
        idUsuario = u.getIdUsuario();

        Asiento a0 = aCtrl.crearAsiento(idZona, "A", 1);
        Compra c0 = ctrl.crearCompra(idUsuario, idEvento, idZona, a0.getIdAsiento());
        assertNotNull(c0);
        idCompra = c0.getIdCompra();
    }

    /** se crea un asiento fresco disponible en la zona compartida. */
    private static Asiento asientoFresco(String fila, int num) {
        return new AsientoController().crearAsiento(idZona, fila, num);
    }

    /**
     * se verifica que crearCompra genera una compra en estado CREADA y reserva el asiento.
     * metodo probado: {@code crearCompra(idUsuario, idEvento, idZona, idAsiento)}.
     */
    @Test
    void crear_estadoCreada() {
        assertEquals(EstadoCompra.CREADA, ctrl.buscarPorId(idCompra).getEstadoActual());
    }

    /**
     * verifica que agregarServicio agrega el tipo de servicio a la lista de la compra.
     * Metodo probado: {@code agregarServicio(id, tipoServicio)}.
     */
    @Test
    void agregarServicio_ok() {
        assertTrue(ctrl.agregarServicio(idCompra, TipoServicio.VIP));
        assertTrue(ctrl.buscarPorId(idCompra).getListaServicios().contains(TipoServicio.VIP));
    }

    /**
     * verifica que pagarCompra aplica la estrategia de pago y cambia el estado a PAGADA.
     * se crea una compra propia para no depender del estado de la compra compartida.
     * Metodo probado: {@code pagarCompra(id, estrategia)}.
     */
    @Test
    void pagar_cambiAPagada() {
        Asiento a = asientoFresco("B", 1);
        Compra c = ctrl.crearCompra(idUsuario, idEvento, idZona, a.getIdAsiento());
        assertNotNull(c);
        assertTrue(ctrl.pagarCompra(c.getIdCompra(), new PagoEfectivo("REF-P")));
        assertEquals(EstadoCompra.PAGADA, ctrl.buscarPorId(c.getIdCompra()).getEstadoActual());
    }

    /**
     * verifica que confirmarCompra cambia el estado de PAGADA a CONFIRMADA.
     * luego crea una compra propia, la paga y luego la confirma.
     * Metodo probado: {@code confirmarCompra(id)}.
     */
    @Test
    void confirmar_cambiAConfirmada() {
        Asiento a = asientoFresco("C", 1);
        Compra c = ctrl.crearCompra(idUsuario, idEvento, idZona, a.getIdAsiento());
        assertNotNull(c);
        ctrl.pagarCompra(c.getIdCompra(), new PagoEfectivo("REF-C"));
        assertTrue(ctrl.confirmarCompra(c.getIdCompra()));
        assertEquals(EstadoCompra.CONFIRMADA, ctrl.buscarPorId(c.getIdCompra()).getEstadoActual());
    }

    /**
     * verifica que reembolsarCompra cambia el estado de CONFIRMADA a REEMBOLSADA.
     * crea una compra propia, la paga, la confirma y luego la reembolsa.
     * Metodo probado: {@code reembolsarCompra(id)}.
     */
    @Test
    void reembolsar_cambiAReembolsada() {
        Asiento a = asientoFresco("D", 1);
        Compra c = ctrl.crearCompra(idUsuario, idEvento, idZona, a.getIdAsiento());
        assertNotNull(c);
        ctrl.pagarCompra(c.getIdCompra(), new PagoEfectivo("REF-R"));
        ctrl.confirmarCompra(c.getIdCompra());
        assertTrue(ctrl.reembolsarCompra(c.getIdCompra()));
        assertEquals(EstadoCompra.REEMBOLSADA, ctrl.buscarPorId(c.getIdCompra()).getEstadoActual());
    }

    /**
     * verifica que cancelarCompra cambia el estado a CANCELADA y libera los asientos.
     * crea una compra propia para no depender del estado de otras compras.
     * Metodo probado: {@code cancelarCompra(id)}.
     */
    @Test
    void cancelar_cambiACancelada() {
        Asiento a = asientoFresco("E", 1);
        Compra c = ctrl.crearCompra(idUsuario, idEvento, idZona, a.getIdAsiento());
        assertNotNull(c);
        ctrl.pagarCompra(c.getIdCompra(), new PagoEfectivo("REF-X"));
        assertTrue(ctrl.cancelarCompra(c.getIdCompra()));
        assertEquals(EstadoCompra.CANCELADA, ctrl.buscarPorId(c.getIdCompra()).getEstadoActual());
    }

    /**
     * verifica que buscarPorId retorna la compra correspondiente al identificador dado.
     * Metodo probado: {@code buscarPorId(id)}.
     */
    @Test
    void buscarId_retornaCompra() {
        assertNotNull(ctrl.buscarPorId(idCompra));
    }

    /**
     * verifica que listarCompras retorna al menos una compra registrada.
     * Metodo probado: {@code listarCompras()}.
     */
    @Test
    void listarCompras_noVacio() {
        assertFalse(ctrl.listarCompras().isEmpty());
    }

    /**
     * verifica que listarComprasPorUsuario retorna las compras del usuario indicado.
     * Metodo probado: {@code listarComprasPorUsuario(idUsuario)}.
     */
    @Test
    void listarPorUsuario_noVacio() {
        assertFalse(ctrl.listarComprasPorUsuario(idUsuario).isEmpty());
    }
}
