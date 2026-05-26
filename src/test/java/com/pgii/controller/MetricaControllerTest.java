package com.pgii.controller;

import com.pgii.model.entities.*;
import com.pgii.model.enums.*;
import com.pgii.model.patterns.behavioral.PagoEfectivo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * pruebas unitarias para {@link MetricaController}.
 * este cubre el calculo de totales de ventas, compras, cancelaciones,
 * conteo de servicios adicionales, identificacion del top evento
 * y listado de eventos con compras.
 *
 * <p>El {@code @BeforeAll} crea una compra confirmada con servicio VIP
 * y una compra cancelada. Todos los tests son de solo lectura sobre esos datos,
 * por lo que son completamente independientes entre si.</p>
 */
class MetricaControllerTest {

    static MetricaController ctrl;

    /**
     * crea los datos minimos: una compra confirmada con servicio VIP
     * y una compra cancelada, para que todas las metricas tengan valores positivos.
     */
    @BeforeAll
    static void setup() {
        ctrl = new MetricaController();
        RecintoController rCtrl = new RecintoController();
        EventoController  eCtrl = new EventoController();
        UsuarioController uCtrl = new UsuarioController();
        AsientoController aCtrl = new AsientoController();
        CompraController  cCtrl = new CompraController();

        Recinto r = rCtrl.crearRecinto("recito para metricas", "Dir M", "Bucaramanga");
        rCtrl.agregarZona(r.getIdRecinto(), "General", 5, 50000);
        Zona z = r.getListaZonas().get(0);
        Asiento a1 = aCtrl.crearAsiento(z.getIdZona(), "A", 1);
        Asiento a2 = aCtrl.crearAsiento(z.getIdZona(), "A", 2);

        Evento ev = eCtrl.crearEvento("evento pa' metricas", CategoriaEvento.CONFERENCIA,
                "Desc", "Bucaramanga", "2026-09-05 09:00", r.getIdRecinto(), "X", "Y");
        eCtrl.publicarEvento(ev.getIdEvento());

        Usuario u = uCtrl.registrar("usuario para metricas", "metrica@gmail.com", "123", "123", RolUsuario.USUARIO);

        Compra c1 = cCtrl.crearCompra(u.getIdUsuario(), ev.getIdEvento(), z.getIdZona(), a1.getIdAsiento());
        cCtrl.agregarServicio(c1.getIdCompra(), TipoServicio.VIP);
        cCtrl.pagarCompra(c1.getIdCompra(), new PagoEfectivo("REF-M1"));
        cCtrl.confirmarCompra(c1.getIdCompra());

        Compra c2 = cCtrl.crearCompra(u.getIdUsuario(), ev.getIdEvento(), z.getIdZona(), a2.getIdAsiento());
        cCtrl.pagarCompra(c2.getIdCompra(), new PagoEfectivo("REF-M2"));
        cCtrl.cancelarCompra(c2.getIdCompra());
    }

    /**
     * verifica que getTotalCompras retorna un valor mayor a cero.
     * Metodo probado: {@code getTotalCompras()}.
     */
    @Test
    void totalCompras_mayorCero() {
        assertTrue(ctrl.getTotalCompras() > 0);
    }

    /**
     * verifica que getTotalVentas retorna un valor positivo sumando compras PAGADA y CONFIRMADA.
     * Metodo probado: {@code getTotalVentas()}.
     */
    @Test
    void totalVentas_mayorCero() {
        assertTrue(ctrl.getTotalVentas() > 0);
    }

    /**
     * verifica que getTotalCancelaciones cuenta al menos una compra en estado CANCELADA.
     * Metodo probado: {@code getTotalCancelaciones()}.
     */
    @Test
    void totalCancelaciones_mayorCero() {
        assertTrue(ctrl.getTotalCancelaciones() > 0);
    }

    /**
     * verifica que getConteoServicio cuenta el servicio VIP agregado en el setup.
     * Metodo probado: {@code getConteoServicio(tipoServicio)}.
     */
    @Test
    void conteoServicio_vip_mayorCero() {
        assertTrue(ctrl.getConteoServicio(TipoServicio.VIP) > 0);
    }

    /**
     * verifica que getTopEvento retorna una cadena no nula con el nombre del evento
     * que tiene mas compras registradas.
     * Metodo probado: {@code getTopEvento()}.
     */
    @Test
    void topEvento_noNulo() {
        assertNotNull(ctrl.getTopEvento());
        assertFalse(ctrl.getTopEvento().isEmpty());
    }

    /**
     * verifica que getListaEventosConCompras retorna al menos un evento.
     * Metodo probado: {@code getListaEventosConCompras()}.
     */
    @Test
    void listaEventosConCompras_noVacio() {
        assertFalse(ctrl.getListaEventosConCompras().isEmpty());
    }
}
