package com.pgii.model.patterns.structural;
import com.pgii.model.interfaces.IExportable;

/** ExportadorCSV. TODO: implementar exportar(). */
public class ExportadorCSV implements IExportable {
    public ExportadorCSV() {}
    public ExportadorCSV(String contenido) {}
    @Override public boolean exportar(String ruta) { return false; /* TODO */ }
    @Override public String getTipoFormato() { return "ExportadorCSV".replace("Exportador",""); }
}
