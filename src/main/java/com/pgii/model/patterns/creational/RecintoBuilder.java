package com.pgii.model.patterns.creational;
import com.pgii.model.entities.*;

/**
 * constructor de objetos Recinto que implementa el patron Builder.
 * permite armar un recinto paso a paso, inicializarlo y agregar zonas de forma encadenada.
 * garantiza que el recinto solo se construya cuando todos los datos esten listos.
 */
public class RecintoBuilder {

    /** identificador del recinto a construir. */
    private int idRecinto;

    /** nombre del recinto. */
    private String nombre;

    /** direccion del recinto. */
    private String direccion;

    /** ciudad del recinto. */
    private String ciudad;

    /** objeto recinto en construccion. Se crea con iniciarRecinto(). */
    private Recinto recinto;

    /** asigna el id del recinto.
     * @param v Id del recinto.
     * @return este builder para encadenamiento. */
    public RecintoBuilder setIdRecinto(int v) { idRecinto=v; return this; }

    /** asigna el nombre del recinto.
     * @param v Nombre del recinto.
     * @return este builder para encadenamiento. */
    public RecintoBuilder setNombre(String v) { nombre=v; return this; }

    /** asigna la direccion del recinto.
     * @param v Direccion del recinto.
     * @return este builder para encadenamiento. */
    public RecintoBuilder setDireccion(String v) { direccion=v; return this; }

    /** asigna la ciudad del recinto.
     * @param v Ciudad del recinto.
     * @return este builder para encadenamiento. */
    public RecintoBuilder setCiudad(String v) { ciudad=v; return this; }

    /**
     * inicializa el objeto Recinto con los datos configurados previamente.
     * debe de llamarse antes de agregar zonas o de invocar build().
     * @return este builder para encadenamiento.
     */
    public RecintoBuilder iniciarRecinto() { recinto=new Recinto(idRecinto,nombre,direccion,ciudad); return this; }

    /**
     * agrega una nueva zona al recinto en construccion.
     * @param idZona Identificador de la zona.
     * @param nom Nombre de la zona.
     * @param cap Capacidad de la zona.
     * @param precio Precio base de la zona.
     * @return este builder para encadenamiento.
     * @throws IllegalStateException si iniciarRecinto() no fue llamado antes.
     */
    public RecintoBuilder agregarZona(int idZona, String nom, int cap, double precio) {
        if (recinto==null) throw new IllegalStateException("tiene que llamar iniciarRecinto() antes de agregar zonas");
        recinto.agregarZona(new Zona(idZona, nom, cap, precio, recinto)); return this;
    }

    /**
     * retorna el recinto completamente construido.
     * @return el objeto Recinto con todos los datos configurados.
     * @throws IllegalStateException si iniciarRecinto() no fue llamado antes.
     */
    public Recinto build() {
        if (recinto==null) throw new IllegalStateException("tiene que llamar iniciarRecinto() antes de construir el recinto");
        return recinto;
    }
}
