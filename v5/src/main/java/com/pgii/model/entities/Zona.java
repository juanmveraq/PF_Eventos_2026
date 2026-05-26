package com.pgii.model.entities;
import com.pgii.model.enums.EstadoAsiento;
import java.util.ArrayList;
import java.util.List;

/**
 * representa una zona dentro de un recinto ( puede poner VIP, General, Preferencial).
 * gestiona la lista de asientos que pertenecen a la zona y su precio base.
 * cada zona pertenece a un unico recinto.
 */
public class Zona {

    /** identificador unico de la zona generado por GestorDatos. */
    private int idZona;

    /** nombre descriptivo de la zona (ej. "VIP Central"). */
    private String nombre;

    /** capacidad maxima de asientos en la zona. */
    private int capacidad;

    /** precio base por entrada en esta zona (en pesos colombianos). */
    private double precioBase;

    /** lista de asientos fisicos que pertenecen a esta zona. */
    private List<Asiento> listaAsientos;

    /** recinto al que pertenece esta zona. */
    private Recinto ownedByRecinto;

    public Zona(int idZona, String nombre, int capacidad, double precioBase, Recinto ownedByRecinto) {
        this.idZona = idZona;
        this.nombre = nombre;
        this.capacidad = capacidad;
        this.precioBase = precioBase;
        this.listaAsientos = new ArrayList<>();
        this.ownedByRecinto = ownedByRecinto;
    }

    /**
     * agrega un asiento a la lista de asientos de la zona.
     * @param a Asiento a agregar. Se ignora si es nulo.
     */
    public void agregarAsiento(Asiento a) { if (a != null) listaAsientos.add(a); }

    /**
     * elimina un asiento de la zona por su identificador.
     * @param id Identificador del asiento a eliminar.
     * @return true si el asiento fue encontrado y eliminado, false en caso contrario.
     */
    public boolean eliminarAsiento(int id) {
        for (int i = 0; i < listaAsientos.size(); i++) {
            if (listaAsientos.get(i).getIdAsiento() == id) { listaAsientos.remove(i); return true; }
        }
        return false;
    }

    /**
     * busca un asiento dentro de la zona por su identificador.
     * @param id Identificador del asiento a buscar.
     * @return El asiento encontrado, o null si no existe.
     */
    public Asiento buscarAsientoPorId(int id) {
        for (int i = 0; i < listaAsientos.size(); i++) {
            if (listaAsientos.get(i).getIdAsiento() == id) return listaAsientos.get(i);
        }
        return null;
    }

    /**
     * calculamos con esto cuantos asientos estan disponibles en la zona restando los ocupados.
     * @return Numero de asientos con estado DISPONIBLE.
     */
    public int getCapacidadDisponible() {
        int ocup = 0;
        for (int i = 0; i < listaAsientos.size(); i++) {
            if (listaAsientos.get(i).getEstado() != EstadoAsiento.DISPONIBLE) ocup++;
        }
        return capacidad - ocup;
    }

    /** retorna el id de la zona. */
    public int getIdZona() { return idZona; }
    /** asigna el id de la zona. */
    public void setIdZona(int v) { idZona = v; }
    /** retorna el nombre de la zona. */
    public String getNombre() { return nombre; }
    /** asigna el nombre de la zona. */
    public void setNombre(String v) { nombre = v; }
    /** retorna la capacidad total de la zona. */
    public int getCapacidad() { return capacidad; }
    /** asigna la capacidad total de la zona. */
    public void setCapacidad(int v) { capacidad = v; }
    /** retorna el precio base de la zona. */
    public double getPrecioBase() { return precioBase; }
    /** asigna el precio base de la zona. */
    public void setPrecioBase(double v) { precioBase = v; }
    /** retorna la lista de asientos de la zona. */
    public List<Asiento> getListaAsientos() { return listaAsientos; }
    /** retorna el recinto al que pertenece la zona. */
    public Recinto getOwnedByRecinto() { return ownedByRecinto; }
    /** asigna el recinto al que pertenece la zona. */
    public void setOwnedByRecinto(Recinto v) { ownedByRecinto = v; }

    @Override public String toString() { return nombre + " - $" + (long)precioBase; }
}
