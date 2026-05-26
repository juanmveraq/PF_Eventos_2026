package com.pgii.model.patterns.structural;
import com.pgii.model.interfaces.IReporteComponente;

/** FirmaDecorador para reportes. TODO: implementar mostrarContenido() con el contenido correspondiente. */
public class FirmaDecorador extends DecoradorReporteBase {
    public FirmaDecorador(IReporteComponente c) { super(c); }
    @Override public String mostrarContenido() {
        // TODO: agregar encabezado/firma al contenido del componente envuelto
        return componente.mostrarContenido();
    }
    @Override public int contarElementos() { return componente.contarElementos(); }
}
