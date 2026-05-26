package com.pgii.controller;
import com.pgii.model.interfaces.IExportable;
import com.pgii.model.patterns.structural.*;

/**
 * controlador encargado de gestionar la exportacion de reportes en distintos formatos.
 * usa los exportadores CSV, PDF y TXT que implementan la interfaz IExportable.
 * actua como punto de entrada para la generacion de reportes desde la vista.
 */
public class ReporteController {

    /** exportador de reportes en formato CSV. */
    private IExportable exportadorCSV;

    /** exportador de reportes en formato PDF. */
    private IExportable exportadorPDF;

    public ReporteController() {
        exportadorCSV = new ExportadorCSV();
        exportadorPDF = new ExportadorPDF();
    }

    /**
     * exportar el reporte de compras en formato CSV a la ruta indicada.
     * @param ruta Ruta del archivo CSV a generar.
     * @return verdad (true) si el archivo fue generado exitosamente, falso (false) si ocurrio un error.
     */
    public boolean exportarCSV(String ruta) { return exportadorCSV.exportar(ruta); }

    /**
     * exporta el reporte de compras en formato PDF a la ruta indicada.
     * @param ruta Ruta del archivo PDF a generar.
     * @return veradero (true) si el archivo fue generado exitosamente, falso (false) si ocurrio un error.
     */
    public boolean exportarPDF(String ruta) { return exportadorPDF.exportar(ruta); }

    /**
     * exporta un contenido de texto libre en formato TXT a la ruta indicada.
     * @param contenido Texto que se escribira en el archivo.
     * @param ruta Ruta del archivo TXT a generar.
     * @return verdadero (true) si el archivo fue generado exitosamente, falso (false) si ocurrio un error.
     */
    public boolean exportarTXT(String contenido, String ruta) { return new ExportadorTXT(contenido).exportar(ruta); }
}
