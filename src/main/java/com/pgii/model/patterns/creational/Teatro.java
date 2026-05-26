package com.pgii.model.patterns.creational;
import com.pgii.model.entities.Evento;
import com.pgii.model.enums.CategoriaEvento;

/**
 * implementa concreta de Evento para eventos de tipo obra de teatro.
 * esta extiende de Evento con informacion especifica de la obra: titulo y compania teatral.
 * es creada por EventoFactory cuando la categoria es TEATRO.
 */
public class Teatro extends Evento {

    /** el titulo de la obra teatral que se presentara. */
    private String obra;

    /** el nombre de la compania o grupo teatral que realiza la presentacion. */
    private String compania;

    public Teatro(int id, String nombre, String desc, String ciudad, String fecha, String obra, String comp) {
        super(id, nombre, CategoriaEvento.TEATRO, desc, ciudad, fecha);
        this.obra = obra; this.compania = comp;
    }

    /**
     * retorna la informacion especifica del teatro: titulo de la obra y compania.
     * @return cadena con la obra y la compania teatral.
     */
    @Override public String getInfoEspecifica() { return "Obra: " + obra + " | Tema cental: " + compania; }

    /** retorna el titulo de la obra teatral. */
    public String getObra() { return obra; }
    /** asigna el titulo de la obra teatral. */
    public void setObra(String v) { obra=v; }
    /** retorna el nombre de la compania teatral. */
    public String getCompania() { return compania; }
    /** asigna el nombre de la compania teatral. */
    public void setCompania(String v) { compania=v; }
}
