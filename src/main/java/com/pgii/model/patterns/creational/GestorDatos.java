package com.pgii.model.patterns.creational;
import com.pgii.model.entities.*;
import java.util.ArrayList;
import java.util.List;

/**
 * repositorio central de datos de la plataforma TuBoletaRobinson.
 * implementa el patron Singleton para garantizar que exista una sola instancia
 * que actua como fuente unica de verdad para todos los datos del sistema.
 * proporciona metodos de busqueda y generacion de identificadores unicos para cada entidad.
 */
public class GestorDatos {

    /** una unica que podemos tener de GestorDatos (patron Singleton). */
    private static GestorDatos instancia;

    /** lista de todos los usuarios registrados en la plataforma. */
    private List<Usuario> listaUsuarios = new ArrayList<>();

    /** lista de todos los eventos registrados en la plataforma. */
    private List<Evento> listaEventos = new ArrayList<>();

    /** lista de todos los recintos registrados en la plataforma. */
    private List<Recinto> listaRecintos = new ArrayList<>();

    /** lista de todas las compras realizadas en la plataforma. */
    private List<Compra> listaCompras = new ArrayList<>();

    /** lista de todas las incidencias registradas en el sistema. */
    private List<Incidencia> listaIncidencias = new ArrayList<>();

    /** contador autoincremental para generar ids de usuarios. */
    private int contIdUsuario=1;
    /** contador autoincremental para generar ids de eventos. */
    private int contIdEvento=1;
    /** contador autoincremental para generar ids de recintos. */
    private int contIdRecinto=1;
    /** contador autoincremental para generar ids de zonas. */
    private int contIdZona=1;
    /** contador autoincremental para generar ids de asientos. */
    private int contIdAsiento=1;
    /** contador autoincremental para generar ids de compras. */
    private int contIdCompra=1;
    /** contador autoincremental para generar ids de entradas. */
    private int contIdEntrada=1;
    /** contador autoincremental para generar ids de pagos. */
    private int contIdPago=1;
    /** contador autoincremental para generar ids de incidencias. */
    private int contIdIncidencia=1;

    private GestorDatos() {}

    /**
     * retorna la unica instancia de GestorDatos creandola si no existe.
     * metodo sincronizado para garantizar seguridad en entornos multi-hilo.
     * @return la instancia unica de GestorDatos.
     */
    public static synchronized GestorDatos getInstancia() {
        if (instancia == null) instancia = new GestorDatos();
        return instancia;
    }

    /**
     * genera y retorna el siguiente id disponible para un usuario.
     * @return un nuevo id unico para usuario.
     */
    public int generarIdUsuario()    { return contIdUsuario++; }

    /**
     * genera y retorna el siguiente id disponible para un evento.
     * @return nuevo id unico para evento.
     */
    public int generarIdEvento()     { return contIdEvento++; }

    /**
     * genera y retorna el siguiente id disponible para un recinto.
     * @return nuevo id unico para recinto.
     */
    public int generarIdRecinto()    { return contIdRecinto++; }

    /**
     * genera y retorna el siguiente id disponible para una zona.
     * @return nuevo id unico para zona.
     */
    public int generarIdZona()       { return contIdZona++; }

    /**
     * genera y retorna el siguiente id disponible para un asiento.
     * @return nuevo id unico para asiento.
     */
    public int generarIdAsiento()    { return contIdAsiento++; }

    /**
     * genera y retorna el siguiente id disponible para una compra.
     * @return nuevo id unico para compra.
     */
    public int generarIdCompra()     { return contIdCompra++; }

    /**
     * genera y retorna el siguiente id disponible para una entrada.
     * @return nuevo id unico para entrada.
     */
    public int generarIdEntrada()    { return contIdEntrada++; }

    /**
     * genera y retorna el siguiente id disponible para un pago.
     * @return nuevo id unico para pago.
     */
    public int generarIdPago()       { return contIdPago++; }

    /**
     * genera y retorna el siguiente id disponible para una incidencia.
     * @return nuevo id unico para incidencia.
     */
    public int generarIdIncidencia() { return contIdIncidencia++; }

    /** retorna la lista completa de usuarios. */
    public List<Usuario>    getListaUsuarios()    { return listaUsuarios; }
    /** retorna la lista completa de eventos. */
    public List<Evento>     getListaEventos()     { return listaEventos; }
    /** retorna la lista completa de recintos. */
    public List<Recinto>    getListaRecintos()    { return listaRecintos; }
    /** retorna la lista completa de compras. */
    public List<Compra>     getListaCompras()     { return listaCompras; }
    /** retorna la lista completa de incidencias. */
    public List<Incidencia> getListaIncidencias() { return listaIncidencias; }

    /**
     * busca un usuario por su identificador unico.
     * @param id Identificador del usuario a buscar.
     * @return el usuario encontrado, o null si no existe.
     */
    public Usuario buscarUsuarioPorId(int id) {
        for (int i=0;i<listaUsuarios.size();i++) if(listaUsuarios.get(i).getIdUsuario()==id) return listaUsuarios.get(i); return null;
    }

    /**
     * busca un usuario por su correo electronico.
     * @param correo Correo electronico del usuario.
     * @return el usuario encontrado, o null si no existe.
     */
    public Usuario buscarUsuarioPorCorreo(String correo) {
        for (int i=0;i<listaUsuarios.size();i++) if(listaUsuarios.get(i).getCorreo().equals(correo)) return listaUsuarios.get(i); return null;
    }

    /**
     * busca un evento por su identificador unico.
     * @param id identificador del evento a buscar.
     * @return el evento encontrado, o null si no existe.
     */
    public Evento buscarEventoPorId(int id) {
        for (int i=0;i<listaEventos.size();i++) if(listaEventos.get(i).getIdEvento()==id) return listaEventos.get(i); return null;
    }

    /**
     * busca un recinto por su identificador unico.
     * @param id identificador del recinto a buscar.
     * @return el recinto encontrado, o null si no existe.
     */
    public Recinto buscarRecintoPorId(int id) {
        for (int i=0;i<listaRecintos.size();i++) if(listaRecintos.get(i).getIdRecinto()==id) return listaRecintos.get(i); return null;
    }

    /**
     * busca una compra por su identificador unico.
     * @param id identificador de la compra a buscar.
     * @return la compra encontrada, o null si no existe.
     */
    public Compra buscarCompraPorId(int id) {
        for (int i=0;i<listaCompras.size();i++) if(listaCompras.get(i).getIdCompra()==id) return listaCompras.get(i); return null;
    }

    /**
     * busca una zona por su identificador recorriendo todos los recintos.
     * @param id Identificador de la zona a buscar.
     * @return la zona encontrada, o null si no existe en ningun recinto.
     */
    public Zona buscarZonaPorId(int id) {
        for (int i=0;i<listaRecintos.size();i++) { Recinto r=listaRecintos.get(i);
            for (int j=0;j<r.getListaZonas().size();j++) if(r.getListaZonas().get(j).getIdZona()==id) return r.getListaZonas().get(j);
        } return null;
    }

    /**
     * busca un asiento por su identificador recorriendo todos los recintos y zonas.
     * @param id Identificador del asiento a buscar.
     * @return el asiento encontrado, o null si no existe.
     */
    public Asiento buscarAsientoPorId(int id) {
        for (int i=0;i<listaRecintos.size();i++) { Recinto r=listaRecintos.get(i);
            for (int j=0;j<r.getListaZonas().size();j++) { Zona z=r.getListaZonas().get(j);
                for (int k=0;k<z.getListaAsientos().size();k++) if(z.getListaAsientos().get(k).getIdAsiento()==id) return z.getListaAsientos().get(k);
            }
        } return null;
    }
}
