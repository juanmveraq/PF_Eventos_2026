package com.pgii.model.patterns.structural;
import com.pgii.model.entities.Compra;
import com.pgii.model.interfaces.IExportable;
import com.pgii.model.patterns.creational.GestorDatos;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * exportar que genera un archivo CSV con el listado de compras del sistema.
 * implementa IExportable y escribe los datos en formato de valores separados por comas.
 * obtiene los datos directamente desde GestorDatos (patron Singleton).
 */
public class ExportadorCSV implements IExportable {

    /**
     * exporta el listado de compras al archivo CSV en la ruta indicada.
     * el archivo incluye columnas: idCompra, usuario, evento, total, estado, fecha.
     * @param rutaArchivo Ruta completa del archivo CSV a generar.
     * @return verdadero (true) si el archivo fue creado exitosamente, falso (false) si ocurrio un error de escritura.
     */
    @Override public boolean exportar(String rutaArchivo) {
        List<Compra> lista = GestorDatos.getInstancia().getListaCompras();
        try (FileWriter fw = new FileWriter(rutaArchivo)) {
            fw.write("idCompra,usuario,evento,total,estado,fecha\n");
            for (int i=0;i<lista.size();i++) {
                Compra c = lista.get(i);
                fw.write(c.getIdCompra()+","+c.getTheUsuario().getNombre()+","+
                         c.getTheEvento().getNombre()+","+c.getTotal()+","+c.getEstadoActual()+","+c.getFechaCreacion()+"\n");
            }
            return true;
        } catch (IOException e) { System.out.println("error al generar CSV: "+e.getMessage()); return false; }
    }

    /** retorna el tipo de formato que genera este exportador. */
    @Override public String getTipoFormato() { return "CSV"; }
}
