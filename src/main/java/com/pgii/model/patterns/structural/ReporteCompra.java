package com.pgii.model.patterns.structural;
import com.pgii.model.entities.*;
import com.pgii.model.enums.TipoServicio;
import com.pgii.model.interfaces.IReporteComponente;
import java.util.List;

/**
 * componente base de reporte que genera el contenido detallado de una compra.
 * implementa IReporteComponente y puede ser envuelto por decoradores (patron Decorator)
 * para agregar encabezados, firmas u otro contenido adicional al reporte.
 */
public class ReporteCompra implements IReporteComponente {

    /** la compra cuyos datos se mostraran en el reporte. */
    private Compra theCompra;

    public ReporteCompra(Compra c) { theCompra=c; }

    /**
     * genera y retorna el contenido detallado de la compra como texto formateado.
     * incluye datos del usuario, evento, entradas, servicios y total de la compra.
     * @return Cadena con el contenido del reporte de la compra.
     */
    @Override public String mostrarContenido() {
        StringBuilder sb = new StringBuilder();
        sb.append("Compra #").append(theCompra.getIdCompra()).append("\n");
        sb.append("Usuario: ").append(theCompra.getTheUsuario().getNombre()).append("\n");
        sb.append("Correo:  ").append(theCompra.getTheUsuario().getCorreo()).append("\n");
        sb.append("Evento:  ").append(theCompra.getTheEvento().getNombre()).append("\n");
        sb.append("Fecha:   ").append(theCompra.getFechaCreacion()).append("\n");
        sb.append("Estado:  ").append(theCompra.getEstadoActual()).append("\n");
        sb.append("\nEntradas:\n");
        List<Entrada> entradas = theCompra.getListaEntradas();
        for (int i=0; i<entradas.size(); i++) {
            Entrada e = entradas.get(i);
            sb.append("  - La zona: ").append(e.getTheZona().getNombre());
            if (e.getTheAsiento() != null) sb.append(" | Asiento: ").append(e.getTheAsiento().toString());
            sb.append(" | Precio: $").append(e.getPrecioFinal()).append("\n");
        }
        if (!theCompra.getListaServicios().isEmpty()) {
            sb.append("\nServicios adicionales:\n");
            List<TipoServicio> serv = theCompra.getListaServicios();
            for (int i=0; i<serv.size(); i++) sb.append("  + ").append(serv.get(i)).append("\n");
        }
        sb.append("\nTOTAL: $").append(theCompra.getTotal()).append("\n");
        return sb.toString();
    }

    /**
     * retorna la cantidad de elementos del reporte. Para una compra individual siempre es 1.
     * @return 1, ya que este componente representa una sola compra.
     */
    @Override public int contarElementos() { return 1; }
}
