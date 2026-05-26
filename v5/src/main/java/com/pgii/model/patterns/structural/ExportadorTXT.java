package com.pgii.model.patterns.structural;
import com.pgii.model.interfaces.IExportable;

/** ExportadorTXT. TODO: implementar exportar(). */
public class ExportadorTXT implements IExportable {
    public ExportadorTXT() {}
    public ExportadorTXT(String contenido) {}
    @Override public boolean exportar(String ruta) { return false; /* TODO */ }
    @Override public String getTipoFormato() { return "ExportadorTXT".replace("Exportador",""); }
}
