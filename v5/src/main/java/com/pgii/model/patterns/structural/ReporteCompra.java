package com.pgii.model.patterns.structural;
import com.pgii.model.entities.Compra;
import com.pgii.model.interfaces.IReporteComponente;

/** Reporte base de una Compra. TODO: completar mostrarContenido(). */
public class ReporteCompra implements IReporteComponente {
    private Compra theCompra;
    public ReporteCompra(Compra c) { theCompra=c; }
    @Override public String mostrarContenido() {
        // TODO: generar detalle completo de la compra
        return "Compra #" + theCompra.getIdCompra();
    }
    @Override public int contarElementos() { return 1; }
}
