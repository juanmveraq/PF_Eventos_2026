package com.pgii.model.entities;
import java.util.ArrayList;
import java.util.List;

/**
 * representa un recinto fisico donde se realizan los eventos que crremos en nuestra plataforma.
 * almacena la informacion de ubicacion y gestiona la lista de zonas que lo componen.
 * es construido mediante el patron Builder (RecintoBuilder).
 */
public class Recinto {

    /** identificador unico del recinto generado por GestorDatos. */
    private int idRecinto;

    /** nombre del recinto. */
    private String nombre;

    /** direccion fisica del recinto. */
    private String direccion;

    /** ciudad donde esta ubicado el recinto. */
    private String ciudad;

    /** lista de zonas que componen el recinto (en este caso VIP, General y el estandar, cada uno con precio propio.). */
    private List<Zona> listaZonas;

    public Recinto(int idRecinto, String nombre, String direccion, String ciudad) {
        this.idRecinto = idRecinto; this.nombre = nombre;
        this.direccion = direccion; this.ciudad = ciudad;
        this.listaZonas = new ArrayList<>();
    }

    /**
     * agraga una zona al recinto.
     * @param z Zona a agregar. Se ignora si es nula.
     */
    public void agregarZona(Zona z) { if (z != null) listaZonas.add(z); }

    /**
     * esto elimina una zona del recinto por su identificador.
     * @param id Identificador de la zona a eliminar.
     * @return verdaero si la zona fue encontrada y eliminada, falso en caso contrario.
     */
    public boolean eliminarZona(int id) {
        for (int i = 0; i < listaZonas.size(); i++) {
            if (listaZonas.get(i).getIdZona() == id) { listaZonas.remove(i); return true; }
        }
        return false;
    }

    /**
     * busca zona dentro del recinto por su identificador.
     * @param id Identificador de la zona a buscar.
     * @return la zona encontrada, o null si no existe.
     */
    public Zona buscarZonaPorId(int id) {
        for (int i = 0; i < listaZonas.size(); i++) {
            if (listaZonas.get(i).getIdZona() == id) return listaZonas.get(i);
        }
        return null;
    }

    /**
     * calcula la capacidad total del recinto sumando las capacidades de todas sus zonas.
     * @return Total de asientos del recinto.
     */
    public int getCapacidadTotal() {
        int t = 0;
        for (int i = 0; i < listaZonas.size(); i++) t += listaZonas.get(i).getCapacidad();
        return t;
    }

    /** retorna el id del recinto. */
    public int getIdRecinto() { return idRecinto; }
    /** asigna el id del recinto. */
    public void setIdRecinto(int v) { idRecinto = v; }
    /** retorna el nombre del recinto. */
    public String getNombre() { return nombre; }
    /** asigna el nombre del recinto. */
    public void setNombre(String v) { nombre = v; }
    /** retorna la direccion del recinto. */
    public String getDireccion() { return direccion; }
    /** asigna la direccion del recinto. */
    public void setDireccion(String v) { direccion = v; }
    /** retorna la ciudad del recinto. */
    public String getCiudad() { return ciudad; }
    /** asigna la ciudad del recinto. */
    public void setCiudad(String v) { ciudad = v; }
    /** retorna la lista de zonas del recinto. */
    public List<Zona> getListaZonas() { return listaZonas; }

    @Override public String toString() { return nombre + " - " + ciudad; }
}
