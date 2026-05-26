package com.pgii.model.patterns.structural;
import com.pgii.model.interfaces.IReporteComponente;
public abstract class DecoradorReporteBase implements IReporteComponente {
    protected IReporteComponente componente;
    public DecoradorReporteBase(IReporteComponente c) { componente=c; }
    public IReporteComponente getComponente() { return componente; }
}
