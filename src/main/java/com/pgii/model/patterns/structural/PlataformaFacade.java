package com.pgii.model.patterns.structural;
import com.pgii.controller.*;
import com.pgii.model.entities.*;
import com.pgii.model.enums.*;
import com.pgii.model.interfaces.*;
import com.pgii.model.patterns.creational.GestorDatos;
import java.util.ArrayList;
import java.util.List;

/**
 * fachada principal de la plataforma TuBoletaRobinson que implementa el patron Facade.
 * simplifica el poder acceder a los subsistemas de eventos, compras, usuarios y recintos
 * exponiendo metodos de alto nivel que ocultan la complejidad de los controladores internos.
 * tambien integra el patron Decorator para generar reportes enriquecidos.
 */
public class PlataformaFacade {

    /** controlador de eventos de la plataforma. */
    private EventoController eventoCtrl;

    /** controlador de compras de la plataforma. */
    private CompraController compraCtrl;

    /** controlador de usuarios de la plataforma. */
    private UsuarioController usuarioCtrl;

    /** controlador de recintos de la plataforma. */
    private RecintoController recintoCtrl;

    /** instancia del gestor de datos (Singleton) para acceso directo a entidades. */
    private GestorDatos gestor;

    public PlataformaFacade(EventoController eventoCtrl, CompraController compraCtrl,
                             UsuarioController usuarioCtrl, RecintoController recintoCtrl) {
        this.eventoCtrl  = eventoCtrl;
        this.compraCtrl  = compraCtrl;
        this.usuarioCtrl = usuarioCtrl;
        this.recintoCtrl = recintoCtrl;
        this.gestor      = GestorDatos.getInstancia();
    }

    /**
     * retorna la lista de eventos que estan en estado PUBLICADO y disponibles al publico.
     * @return lista de eventos publicados.
     */
    public List<Evento> obtenerEventosPublicados() {
        List<Evento> todos = eventoCtrl.listarEventos();
        List<Evento> pub = new ArrayList<>();
        for (int i=0;i<todos.size();i++) if(todos.get(i).getEstado()==EstadoEvento.PUBLICADO) pub.add(todos.get(i));
        return pub;
    }

    /**
     * coordina el flujo completo de compra: crea la compra, paga y confirma en un solo paso.
     * @param idUsuario Identificador del usuario que compra.
     * @param idEvento Identificador del evento seleccionado.
     * @param idZona Identificador de la zona seleccionada.
     * @param idAsiento Identificador del asiento seleccionado.
     * @param estrategia Estrategia de pago a utilizar.
     * @return la compra confirmada, o null si algun paso fallo.
     */
    public Compra realizarCompra(int idUsuario, int idEvento, int idZona, int idAsiento, IPagoStrategy estrategia) {
        Compra c = compraCtrl.crearCompra(idUsuario, idEvento, idZona, idAsiento);
        if (c == null) return null;
        compraCtrl.pagarCompra(c.getIdCompra(), estrategia);
        compraCtrl.confirmarCompra(c.getIdCompra());
        return c;
    }

    /**
     * genera el reporte de una compra especifica usando el patron Decorator.
     * puede agregar encabezado y firma institucional al contenido base del reporte.
     * @param idCompra Identificador de la compra a reportar.
     * @param conEncabezado true para agregar encabezado institucional al reporte.
     * @param conFirma true para agregar firma institucional al reporte.
     * @return contenido del reporte como cadena de texto, o mensaje de error si no existe.
     */
    public String generarReporteCompra(int idCompra, boolean conEncabezado, boolean conFirma) {
        Compra compra = gestor.buscarCompraPorId(idCompra);
        if (compra == null) return "no se encontrola compra";
        IReporteComponente reporte = new ReporteCompra(compra);
        if (conEncabezado) reporte = new EncabezadoDecorador(reporte);
        if (conFirma)      reporte = new FirmaDecorador(reporte);
        return reporte.mostrarContenido();
    }

    /**
     * genera un reporte con el listado de todos los eventos del sistema.
     * puede agregar encabezado y firma usando el patron Decorator.
     * @param conEncabezado true para agregar encabezado institucional al reporte.
     * @param conFirma true para agregar firma institucional al reporte.
     * @return contenido del reporte de eventos como cadena de texto.
     */
    public String generarReporteEventos(boolean conEncabezado, boolean conFirma) {
        StringBuilder sb = new StringBuilder();
        sb.append("LISTADO DE EVENTOS\n");
        sb.append("-".repeat(40)).append("\n");
        List<Evento> eventos = gestor.getListaEventos();
        for (int i=0;i<eventos.size();i++) {
            Evento e = eventos.get(i);
            sb.append(e.getNombre()).append(" | ").append(e.getCategoria())
              .append(" | ").append(e.getCiudad()).append(" | ").append(e.getEstado()).append("\n");
        }
        IReporteComponente rep = new IReporteComponente() {
            public String mostrarContenido() { return sb.toString(); }
            public int contarElementos() { return eventos.size(); }
        };
        if (conEncabezado) rep = new EncabezadoDecorador(rep);
        if (conFirma)      rep = new FirmaDecorador(rep);
        return rep.mostrarContenido();
    }

    /** retorna el controlador de eventos de la plataforma. */
    public EventoController getEventoCtrl()   { return eventoCtrl; }
    /** retorna el controlador de compras de la plataforma. */
    public CompraController getCompraCtrl()   { return compraCtrl; }
    /** retorna el controlador de usuarios de la plataforma. */
    public UsuarioController getUsuarioCtrl() { return usuarioCtrl; }
    /** retorna el controlador de recintos de la plataforma. */
    public RecintoController getRecintoCtrl() { return recintoCtrl; }
}
