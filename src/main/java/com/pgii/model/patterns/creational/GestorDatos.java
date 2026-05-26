package com.pgii.model.patterns.creational;
import com.pgii.model.entities.*;
import java.util.ArrayList;
import java.util.List;

/** Singleton - repositorio central de datos. TODO: implementar metodos de busqueda. */
public class GestorDatos {
    private static GestorDatos instancia;
    private List<Usuario>    listaUsuarios    = new ArrayList<>();
    private List<Evento>     listaEventos     = new ArrayList<>();
    private List<Recinto>    listaRecintos    = new ArrayList<>();
    private List<Compra>     listaCompras     = new ArrayList<>();
    private List<Incidencia> listaIncidencias = new ArrayList<>();
    private int contIdUsuario=1, contIdEvento=1, contIdRecinto=1, contIdZona=1;
    private int contIdAsiento=1, contIdCompra=1, contIdEntrada=1, contIdPago=1, contIdIncidencia=1;
    private GestorDatos() {}
    public static synchronized GestorDatos getInstancia() {
        if (instancia == null) instancia = new GestorDatos();
        return instancia;
    }
    public int generarIdUsuario()    { return contIdUsuario++; }
    public int generarIdEvento()     { return contIdEvento++; }
    public int generarIdRecinto()    { return contIdRecinto++; }
    public int generarIdZona()       { return contIdZona++; }
    public int generarIdAsiento()    { return contIdAsiento++; }
    public int generarIdCompra()     { return contIdCompra++; }
    public int generarIdEntrada()    { return contIdEntrada++; }
    public int generarIdPago()       { return contIdPago++; }
    public int generarIdIncidencia() { return contIdIncidencia++; }
    public List<Usuario>    getListaUsuarios()    { return listaUsuarios; }
    public List<Evento>     getListaEventos()     { return listaEventos; }
    public List<Recinto>    getListaRecintos()    { return listaRecintos; }
    public List<Compra>     getListaCompras()     { return listaCompras; }
    public List<Incidencia> getListaIncidencias() { return listaIncidencias; }
    // TODO: implementar buscarUsuarioPorId, buscarUsuarioPorCorreo,
    //        buscarEventoPorId, buscarRecintoPorId, buscarCompraPorId,
    //        buscarZonaPorId, buscarAsientoPorId
    public Usuario  buscarUsuarioPorId(int id)        { return null; }
    public Usuario  buscarUsuarioPorCorreo(String c)  { return null; }
    public Evento   buscarEventoPorId(int id)         { return null; }
    public Recinto  buscarRecintoPorId(int id)        { return null; }
    public Compra   buscarCompraPorId(int id)         { return null; }
    public Zona     buscarZonaPorId(int id)           { return null; }
    public Asiento  buscarAsientoPorId(int id)        { return null; }
}
