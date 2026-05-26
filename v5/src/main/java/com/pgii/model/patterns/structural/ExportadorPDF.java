package com.pgii.model.patterns.structural;
import com.pgii.model.interfaces.IExportable;

/** ExportadorPDF. TODO: implementar exportar(). */
public class ExportadorPDF implements IExportable {
    public ExportadorPDF() {}
    public ExportadorPDF(String contenido) {}
    @Override public boolean exportar(String ruta) { return false; /* TODO */ }
    @Override public String getTipoFormato() { return "ExportadorPDF".replace("Exportador",""); }
}
