package com.pgii.model.patterns.structural;
import com.pgii.model.interfaces.IReporteComponente;

/**
 * clase que es abstracta, base del patron Decorator para los componentes de reporte.
 * esta envuelve un objeto IReporteComponente y sirve de base para los decoradores
 * concretos de reporte (como: EncabezadoDecorador, FirmaDecorador).
 * Las subclases agregan contenido al inicio o fin del reporte del componente envuelto.
 */
public abstract class DecoradorReporteBase implements IReporteComponente {

    /** El componente de reporte que este decorador envuelve. */
    protected IReporteComponente componente;

    public DecoradorReporteBase(IReporteComponente c) { componente=c; }

    /**
     * Retorna el componente de reporte interno envuelto por este decorador.
     * @return El componente IReporteComponente que fue decorado.
     */
    public IReporteComponente getComponente() { return componente; }
}
