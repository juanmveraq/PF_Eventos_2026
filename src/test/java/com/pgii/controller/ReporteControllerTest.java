package com.pgii.controller;

import com.pgii.model.entities.*;
import com.pgii.model.enums.*;
import com.pgii.model.patterns.behavioral.PagoEfectivo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.io.File;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para {@link ReporteController}.
 * Verifica que los tres metodos de exportacion generan el archivo correspondiente
 * en la ruta indicada: CSV con datos de compras, PDF con datos de compras y TXT con texto libre.
 *
 * <p>El {@code @BeforeAll} crea una compra confirmada para que los exportadores
 * tengan datos que escribir. Cada test genera su propio archivo con timestamp
 * y lo elimina al finalizar para no dejar residuos en disco.</p>
 */
class ReporteControllerTest {

    static ReporteController ctrl;

    /**
     * Inicializa el controlador y crea una compra confirmada para que
     * los exportadores CSV y PDF tengan datos disponibles.
     */
    @BeforeAll
    static void setup() {
        ctrl = new ReporteController();
        RecintoController rCtrl = new RecintoController();
        EventoController  eCtrl = new EventoController();
        UsuarioController uCtrl = new UsuarioController();
        AsientoController aCtrl = new AsientoController();
        CompraController  cCtrl = new CompraController();

        Recinto r = rCtrl.crearRecinto("recinto", "dir", "armenia");
        rCtrl.agregarZona(r.getIdRecinto(), "general", 5, 60000);
        Zona z = r.getListaZonas().get(0);
        Asiento a = aCtrl.crearAsiento(z.getIdZona(), "A", 1);

        Evento ev = eCtrl.crearEvento("evento reporte", CategoriaEvento.CONCIERTO,
                "Desc", "armenia", "2026-07-20 19:00", r.getIdRecinto(), "X", "Y");
        eCtrl.publicarEvento(ev.getIdEvento());

        Usuario u = uCtrl.registrar("usuario random", "reporte@gmail.com", "pass", "0", RolUsuario.USUARIO);

        Compra c = cCtrl.crearCompra(u.getIdUsuario(), ev.getIdEvento(), z.getIdZona(), a.getIdAsiento());
        if (c != null) {
            cCtrl.pagarCompra(c.getIdCompra(), new PagoEfectivo("REF-R1"));
            cCtrl.confirmarCompra(c.getIdCompra());
        }
    }

    /**
     * Verifica que exportarCSV genera el archivo CSV en la ruta indicada.
     * Metodo probado: {@code exportarCSV(ruta)}.
     */
    @Test
    void exportarCSV_creaArchivo() {
        String ruta = "reporte de test" + System.currentTimeMillis() + ".csv";
        assertTrue(ctrl.exportarCSV(ruta));
        File archivo = new File(ruta);
        assertTrue(archivo.exists());
        archivo.delete();
    }

    /**
     * Verifica que exportarPDF genera el archivo PDF en la ruta indicada.
     * Metodo probado: {@code exportarPDF(ruta)}.
     */
    @Test
    void exportarPDF_creaArchivo() {
        String ruta = "reporte de test" + System.currentTimeMillis() + ".pdf";
        assertTrue(ctrl.exportarPDF(ruta));
        File archivo = new File(ruta);
        assertTrue(archivo.exists());
        archivo.delete();
    }

    /**
     * verificacion que exportarTXT escribe el contenido dado en el archivo indicado
     * y que el archivo tiene contenido.
     * metodo probado: {@code exportarTXT(contenido, ruta)}.
     */
    @Test
    void exportarTXT_creaArchivo() {
        String ruta = "reporte de test" + System.currentTimeMillis() + ".txt";
        String contenido = "Reporte de prueba TuBoletaRobinson";
        assertTrue(ctrl.exportarTXT(contenido, ruta));
        File archivo = new File(ruta);
        assertTrue(archivo.exists());
        assertTrue(archivo.length() > 0);
        archivo.delete();
    }
}
