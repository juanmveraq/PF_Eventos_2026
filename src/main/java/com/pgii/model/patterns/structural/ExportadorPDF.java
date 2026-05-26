package com.pgii.model.patterns.structural;
import com.pgii.model.entities.Compra;
import com.pgii.model.interfaces.IExportable;
import com.pgii.model.patterns.creational.GestorDatos;
import java.util.List;

/**
 * exportaodr que genera un archivo PDF con el listado de compras del sistema.
 * implementa IExportable y usa la libreria Apache PDFBox para crear el documento.
 * obtiene los datos directamente desde GestorDatos (patron Singleton).
 */
public class ExportadorPDF implements IExportable {

    /**
     * exporta el listado de compras al archivo PDF en la ruta indicada.
     * genera una pagina con el titulo del reporte y una linea por cada compra.
     * @param rutaArchivo Ruta completa del archivo PDF a generar (con extension .pdf).
     * @return verdadero (true) si el archivo fue creado exitosamente, falso (false) si ocurrio un error.
     */
    @Override public boolean exportar(String rutaArchivo) {
        List<Compra> lista = GestorDatos.getInstancia().getListaCompras();
        try {
            org.apache.pdfbox.pdmodel.PDDocument doc = new org.apache.pdfbox.pdmodel.PDDocument();
            org.apache.pdfbox.pdmodel.PDPage pag = new org.apache.pdfbox.pdmodel.PDPage();
            doc.addPage(pag);
            org.apache.pdfbox.pdmodel.PDPageContentStream cs =
                new org.apache.pdfbox.pdmodel.PDPageContentStream(doc, pag);
            cs.setFont(org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA_BOLD, 14);
            cs.beginText();
            cs.newLineAtOffset(40, 750);
            cs.showText("El reporte de Compras");
            cs.setFont(org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA, 10);
            float y = 720;
            for (int i=0;i<lista.size()&&y>50;i++) {
                Compra c = lista.get(i);
                cs.newLineAtOffset(0, -18);
                cs.showText("Compra #"+c.getIdCompra()+"  "+c.getTheUsuario().getNombre()+
                            "  "+c.getTheEvento().getNombre()+"  $"+c.getTotal()+"  "+c.getEstadoActual());
                y -= 18;
            }
            cs.endText(); cs.close();
            doc.save(rutaArchivo); doc.close();
            return true;
        } catch (Exception e) { System.out.println("error al intentar crear el PDF: "+e.getMessage()); return false; }
    }

    /** retorna el tipo de formato que genera este exportador. */
    @Override public String getTipoFormato() { return "PDF"; }
}
