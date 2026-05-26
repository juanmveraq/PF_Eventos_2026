package com.pgii.model.patterns.structural;
import com.pgii.model.interfaces.IReporteComponente;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Decorador de reporte que agrega un encabezado institucional al contenido del componente.
 * Muestra el nombre de la plataforma, la universidad y la fecha de generacion del reporte.
 * Aplica el patron Decorator sobre IReporteComponente.
 */
public class EncabezadoDecorador extends DecoradorReporteBase {

    public EncabezadoDecorador(IReporteComponente c) { super(c); }

    /**
     * Genera el contenido del reporte agregando el encabezado institucional antes del contenido base.
     * @return Cadena con el encabezado seguido del contenido del componente envuelto.
     */
    @Override public String mostrarContenido() {
        StringBuilder sb = new StringBuilder();
        sb.append("================================================\n");
        sb.append("   PLATAFORMA DE GESTION DE EVENTOS - PGII\n");
        sb.append("          Universidad del Quindio\n");
        sb.append("================================================\n");
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        sb.append("Generado: ").append(LocalDateTime.now().format(fmt)).append("\n");
        sb.append("================================================\n\n");
        sb.append(componente.mostrarContenido());
        return sb.toString();
    }

    /** delega el conteo de elementos al componente envuelto. */
    @Override public int contarElementos() { return componente.contarElementos(); }
}
