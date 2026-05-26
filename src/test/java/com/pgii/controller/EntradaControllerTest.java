package com.pgii.controller;

import com.pgii.model.entities.*;
import com.pgii.model.enums.*;
import com.pgii.model.patterns.behavioral.PagoEfectivo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * prueba unitarias para {@link EntradaController}.
 * esta cubre generacion de entradas, busqueda por id, listado por compra,
 * marcado como usada y anulacion.
 *
 * <p>el {@code @BeforeAll} prepara el entorno completo (recinto, zona, asiento, evento,
 * usuario, compra confirmada) y genera una entrada de referencia ({@code idEntrada}).
 * nuestros tests que necesitan modificar el estado de una entrada crean la suya propia
 * para no interferir entre si.</p>
 */
class EntradaControllerTest {

    static EntradaController ctrl;
    static int idCompra;
    static int idZona;
    static int idEntrada;

    /**
     * prepara un entorno completo con recinto, zona, evento, usuario y una compra
     * pagada y confirmada. con esto genera ademas una entrada de referencia para los tests de lectura.
     */
    @BeforeAll
    static void setup() {
        ctrl = new EntradaController();
        RecintoController rCtrl = new RecintoController();
        EventoController  eCtrl = new EventoController();
        UsuarioController uCtrl = new UsuarioController();
        AsientoController aCtrl = new AsientoController();
        CompraController  cCtrl = new CompraController();

        Recinto r = rCtrl.crearRecinto("un recinto con entradas", "Dir C", "Pereira");
        rCtrl.agregarZona(r.getIdRecinto(), "VIP", 5, 150000);
        Zona z = r.getListaZonas().get(0);
        idZona = z.getIdZona();
        Asiento a = aCtrl.crearAsiento(idZona, "A", 1);

        Evento ev = eCtrl.crearEvento("une evento con estradas", CategoriaEvento.TEATRO,
                "Desc", "Pereira", "2026-10-10 19:00", r.getIdRecinto(), "X", "Y");
        eCtrl.publicarEvento(ev.getIdEvento());

        Usuario u = uCtrl.registrar("user para test", "userparatest@gmail.com", "123", "123", RolUsuario.USUARIO);

        Compra c = cCtrl.crearCompra(u.getIdUsuario(), ev.getIdEvento(), idZona, a.getIdAsiento());
        assertNotNull(c);
        idCompra = c.getIdCompra();
        cCtrl.pagarCompra(idCompra, new PagoEfectivo("REF-E"));
        cCtrl.confirmarCompra(idCompra);

        // entrada de referencia para los tests de busqueda y listado
        Entrada ref = ctrl.generarEntrada(idCompra, idZona, -1, 150000);
        assertNotNull(ref);
        idEntrada = ref.getIdEntrada();
    }

    /**
     * verifica que generarEntrada crea una nueva entrada y la agrega a la compra indicada.
     * Metodo probado: {@code generarEntrada(idCompra, idZona, idAsiento, precio)}.
     */
    @Test
    void generar_entradaNoNula() {
        Entrada e = ctrl.generarEntrada(idCompra, idZona, -1, 150000);
        assertNotNull(e);
    }

    /**
     * verifica que buscarPorId retorna la entrada de referencia creada en el setup.
     * Metodo probado: {@code buscarPorId(id)}.
     */
    @Test
    void buscarId_retornaEntrada() {
        assertNotNull(ctrl.buscarPorId(idEntrada));
    }

    /**
     * verifica que listarPorCompra retorna todas las entradas de la compra indicada.
     * Metodo probado: {@code listarPorCompra(id)}.
     */
    @Test
    void listarPorCompra_noVacio() {
        assertFalse(ctrl.listarPorCompra(idCompra).isEmpty());
    }

    /**
     * verifica que marcarUsada cambia el estado de una entrada a USADA.
     * genera una entrada propia para no interferir con otros tests.
     * Metodo probado: {@code marcarUsada(id)}.
     */
    @Test
    void marcarUsada_cambiAUsada() {
        Entrada propia = ctrl.generarEntrada(idCompra, idZona, -1, 150000);
        assertNotNull(propia);
        assertTrue(ctrl.marcarUsada(propia.getIdEntrada()));
        assertEquals(EstadoEntrada.USADA, ctrl.buscarPorId(propia.getIdEntrada()).getEstado());
    }

    /**
     * verifica que anularEntrada cambia el estado a ANULADA y libera el asiento si existia.
     * genera una entrada propia para no interferir con otros tests.
     * Metodo probado: {@code anularEntrada(id)}.
     */
    @Test
    void anular_cambiAAnulada() {
        Entrada propia = ctrl.generarEntrada(idCompra, idZona, -1, 150000);
        assertNotNull(propia);
        assertTrue(ctrl.anularEntrada(propia.getIdEntrada()));
        assertEquals(EstadoEntrada.ANULADA, ctrl.buscarPorId(propia.getIdEntrada()).getEstado());
    }
}
