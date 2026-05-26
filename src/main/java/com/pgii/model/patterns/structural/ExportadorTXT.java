package com.pgii.model.patterns.structural;
import com.pgii.model.interfaces.IExportable;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Exportador que escribe un contenido de texto libre en un archivo TXT.
 * Implementa IExportable y recibe el contenido a exportar en el constructor.
 * Util para exportar reportes decorados generados como cadenas de texto.
 */
public class ExportadorTXT implements IExportable {

    /** Contenido de texto que se escribira en el archivo. */
    private String contenido;

    public ExportadorTXT(String contenido) { this.contenido=contenido; }

    /**
     * exporta el contenido de texto al archivo en la ruta indicada.
     * @param rutaArchivo Ruta completa del archivo TXT a generar (con extension .txt).
     * @return verdadero si el archivo fue creado exitosamente, false si ocurrio un error de escritura.
     */
    @Override public boolean exportar(String rutaArchivo) {
        try (FileWriter fw = new FileWriter(rutaArchivo)) {
            fw.write(contenido); return true;
        } catch (IOException e) { System.out.println("error al intentar generar TXT: "+e.getMessage()); return false; }
    }

    /** retorna el tipo de formato que genera este exportador. */
    @Override public String getTipoFormato() { return "TXT"; }
}
