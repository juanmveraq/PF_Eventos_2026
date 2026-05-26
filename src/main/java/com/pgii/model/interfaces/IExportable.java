package com.pgii.model.interfaces;

/**
 * Interfaz que define la capacidad de exportar datos a un archivo externo, necesaria para el adapter.
 * las clases que implementen esta interfaz (ExportadorCSV, ExportadorPDF, ExportadorTXT)
 * generan archivos en distintos formatos con la informacion de las compras del sistema.
 */
public interface IExportable {

    /**
     * exporta los datos al archivo indicado en la ruta especificada.
     * @param rutaArchivo Ruta completa del archivo a generar (incluyendo nombre y extension).
     * @return verdadero (true) si el archivo fue generado exitosamente, falso (false) si ocurrio un error.
     */
    public boolean exportar(String rutaArchivo);

    /**
     * retorna el tipo de formato que genera este exportador.
     * @return nombre del formato del formato (tenemos: "CSV", "PDF", "TXT").
     */
    public String getTipoFormato();
}
