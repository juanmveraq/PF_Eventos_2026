package com.pgii.model.patterns.structural;
import com.pgii.model.interfaces.IReporteComponente;

/** EncabezadoDecorador para reportes. TODO: implementar mostrarContenido() con el contenido correspondiente. */
public class EncabezadoDecorador extends DecoradorReporteBase {
    public EncabezadoDecorador(IReporteComponente c) { super(c); }
    @Override public String mostrarContenido() {
        // TODO: agregar encabezado/firma al contenido del componente envuelto
        return componente.mostrarContenido();
    }
    @Override public int contarElementos() { return componente.contarElementos(); }
}
