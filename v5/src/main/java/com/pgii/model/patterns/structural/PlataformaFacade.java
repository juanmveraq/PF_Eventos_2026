package com.pgii.model.patterns.structural;
import com.pgii.controller.*;
import com.pgii.model.entities.*;
import com.pgii.model.interfaces.*;
import com.pgii.model.patterns.creational.GestorDatos;
import java.util.List;

/** Facade de la plataforma. TODO: implementar todos los metodos. */
public class PlataformaFacade {
    private EventoController  eventoCtrl;
    private CompraController  compraCtrl;
    private UsuarioController usuarioCtrl;
    private RecintoController recintoCtrl;
    private GestorDatos gestor;
    public PlataformaFacade(EventoController ev, CompraController co, UsuarioController us, RecintoController re) {
        this.eventoCtrl=ev; this.compraCtrl=co; this.usuarioCtrl=us; this.recintoCtrl=re;
        this.gestor=GestorDatos.getInstancia();
    }
    // TODO: implementar obtenerEventosPublicados()
    public List<Evento> obtenerEventosPublicados() { return eventoCtrl.listarEventos(); }
    // TODO: implementar realizarCompra() coordinando crear+pagar+confirmar
    public Compra realizarCompra(int idU, int idEv, int idZ, int idA, IPagoStrategy est) { return null; }
    // TODO: implementar generarReporteCompra() con patron Decorator
    public String generarReporteCompra(int idCompra, boolean enc, boolean firma) { return "TODO"; }
    // TODO: implementar generarReporteEventos() con patron Decorator
    public String generarReporteEventos(boolean enc, boolean firma) { return "TODO"; }
    public EventoController   getEventoCtrl()   { return eventoCtrl; }
    public CompraController   getCompraCtrl()   { return compraCtrl; }
    public UsuarioController  getUsuarioCtrl()  { return usuarioCtrl; }
    public RecintoController  getRecintoCtrl()  { return recintoCtrl; }
}
