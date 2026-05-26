package com.pgii.model.patterns.structural;
import com.pgii.model.interfaces.IReporteComponente;
import java.time.LocalDate;

/**
 * decorador que reporta que agrega una firma institucional al final del contenido.
 * muestra el nombre del sistema generador y la fecha actual del reporte.
 * aplica el patron Decorator sobre IReporteComponente.
 */
public class FirmaDecorador extends DecoradorReporteBase {

    public FirmaDecorador(IReporteComponente c) { super(c); }

    /**
     * genera el contenido del reporte agregando la firma institucional despues del contenido base.
     * @return cadena con el contenido del componente envuelto seguido de la firma del sistema.
     */
    @Override public String mostrarContenido() {
        StringBuilder sb = new StringBuilder();
        sb.append(componente.mostrarContenido());
        sb.append("\n================================================\n");
        sb.append("Reporte generado por: Sistema PGII\n");
        sb.append("Fecha: ").append(LocalDate.now()).append("\n");
        sb.append("================================================\n");
        return sb.toString();
    }

    /** delega el conteo de elementos al componente envuelto. */
    @Override public int contarElementos() { return componente.contarElementos(); }
}
