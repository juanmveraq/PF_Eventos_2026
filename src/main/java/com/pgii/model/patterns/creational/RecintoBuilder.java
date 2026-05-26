package com.pgii.model.patterns.creational;
import com.pgii.model.entities.*;

/** Builder para construir objetos Recinto. TODO: completar agregarZona() y build(). */
public class RecintoBuilder {
    private int idRecinto;
    private String nombre, direccion, ciudad;
    private Recinto recinto;
    public RecintoBuilder setIdRecinto(int v)    { idRecinto=v;  return this; }
    public RecintoBuilder setNombre(String v)    { nombre=v;     return this; }
    public RecintoBuilder setDireccion(String v) { direccion=v;  return this; }
    public RecintoBuilder setCiudad(String v)    { ciudad=v;     return this; }
    public RecintoBuilder iniciarRecinto() { recinto=new Recinto(idRecinto,nombre,direccion,ciudad); return this; }
    // TODO: implementar agregarZona()
    public RecintoBuilder agregarZona(int idZona, String nom, int cap, double precio) { return this; }
    public Recinto build() { return recinto; }
}
