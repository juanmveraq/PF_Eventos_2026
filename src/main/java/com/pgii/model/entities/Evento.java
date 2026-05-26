package com.pgii.model.entities;
import com.pgii.model.enums.CategoriaEvento;
import com.pgii.model.enums.EstadoEvento;
import com.pgii.model.interfaces.IObservable;
import com.pgii.model.interfaces.IObserver;
import java.util.ArrayList;
import java.util.List;

/**
 * tenemos la clase abstracta que representa un evento en la plataforma TuBoletaRobinson.
 * esta almacena informacion general del evento como nombre, ciudad, fecha y recinto asignado.
 * implementa el patron Observer (IObservable) para notificar cambios de estado.
 * las subclases concretas (que son Concierto, Teatro, Conferencia) son creadas por EventoFactory.
 */
public abstract class Evento implements IObservable {

    /** identificador unico del evento generado por GestorDatos. */
    private int idEvento;

    /** nombre del evento que se muestra al usuario. */
    private String nombre;

    /** categorias del evento (las cuales sonCONCIERTO, TEATRO, CONFERENCIA). */
    private CategoriaEvento categoria;

    /** descripcion textual del evento. */
    private String descripcion;

    /** ciudad donde se realiza el evento. */
    private String ciudad;

    /** fecha y hora del evento en formato texto (ej. "2026-08-15 20:00", se añadira con un calendario que pasa a texto). */
    private String fechaHora;

    /** estado actual del evento en su ciclo de vida. */
    private EstadoEvento estado;

    /** recinto fisico donde se llevara a cabo el evento. */
    private Recinto theRecinto;

    /** politica de cancelacion del evento definida por el administrador. */
    private String politicaCancelacion;

    /** politica de reembolso del evento definida por el administrador. */
    private String politicaReembolso;

    /** lista de observadores suscritos al evento (patron Observer). */
    private List<IObserver> listaObservers;

    /** lista de compras realizadas para este evento. */
    private List<Compra> listaCompras;

    public Evento(int idEvento, String nombre, CategoriaEvento categoria,
                  String descripcion, String ciudad, String fechaHora) {
        this.idEvento = idEvento; this.nombre = nombre; this.categoria = categoria;
        this.descripcion = descripcion; this.ciudad = ciudad; this.fechaHora = fechaHora;
        this.estado = EstadoEvento.BORRADOR;
        this.politicaCancelacion = "puede cancelar en cualquier momento antes del evento";
        this.politicaReembolso = "se debolvera el valor de su compra siempre y cuando tenga pagado el seguro";
        this.listaObservers = new ArrayList<>();
        this.listaCompras = new ArrayList<>();
    }

    /**
     * retornamos informacion especifica segun el tipo de evento.
     * este debe ser implementado por cada subclase concreta.
     * @return es una cadena con la informacion particular del tipo de evento.
     */
    public abstract String getInfoEspecifica();

    /**
     * cambia el estado del evento y notifica a todos los observadores suscritos.
     * @param nuevoEstado Nuevo estado a asignar. No puede ser nulo.
     */
    public void cambiarEstado(EstadoEvento nuevoEstado) {
        if (nuevoEstado == null) throw new IllegalArgumentException("Estado nulo");
        this.estado = nuevoEstado;
        notificarObservers("Evento '" + nombre + "' cambio estado a: " + nuevoEstado);
    }

    /**
     * agrega una compra a la lista de compras del evento.
     * @param c Compra a agregar. Se ignora si es nula.
     */
    public void agregarCompra(Compra c) { if (c != null) listaCompras.add(c); }

    /**
     * suscribe un observador al evento si no estaba ya registrado.
     * @param o Observador a agregar.
     */
    @Override public void agregarObserver(IObserver o) {
        if (o == null) return;
        for (int i = 0; i < listaObservers.size(); i++) if (listaObservers.get(i) == o) return;
        listaObservers.add(o);
    }

    /**
     * elimina un observador de la lista de suscriptores del evento.
     * @param o Observador a eliminar.
     */
    @Override public void eliminarObserver(IObserver o) { listaObservers.remove(o); }

    /**
     * manda noti a todos los observadores suscritos con un mensaje.
     * @param msg Mensaje que se envia a cada observador.
     */
    @Override public void notificarObservers(String msg) {
        for (int i = 0; i < listaObservers.size(); i++) listaObservers.get(i).actualizar(msg);
    }

    /** retorna el id del evento. */
    public int getIdEvento() { return idEvento; }
    /** asigna el id del evento. */
    public void setIdEvento(int v) { idEvento = v; }
    /** retorna el nombre del evento. */
    public String getNombre() { return nombre; }
    /** asigna el nombre del evento. */
    public void setNombre(String v) { nombre = v; }
    /** retorna la categoria del evento. */
    public CategoriaEvento getCategoria() { return categoria; }
    /** asigna la categoria del evento. */
    public void setCategoria(CategoriaEvento v) { categoria = v; }
    /** retorna la descripcion del evento. */
    public String getDescripcion() { return descripcion; }
    /** asigna la descripcion del evento. */
    public void setDescripcion(String v) { descripcion = v; }
    /** retorna la ciudad del evento. */
    public String getCiudad() { return ciudad; }
    /** asigna la ciudad del evento. */
    public void setCiudad(String v) { ciudad = v; }
    /** retorna la fecha y hora del evento. */
    public String getFechaHora() { return fechaHora; }
    /** asigna la fecha y hora del evento. */
    public void setFechaHora(String v) { fechaHora = v; }
    /** retorna el estado actual del evento. */
    public EstadoEvento getEstado() { return estado; }
    /** asigna el estado del evento directamente sin notificar observers. */
    public void setEstado(EstadoEvento v) { estado = v; }
    /** retorna el recinto donde se realiza el evento. */
    public Recinto getTheRecinto() { return theRecinto; }
    /** asigna el recinto del evento. */
    public void setTheRecinto(Recinto v) { theRecinto = v; }
    /** retorna la politica de cancelacion del evento. */
    public String getPoliticaCancelacion() { return politicaCancelacion; }
    /** asigna la politica de cancelacion del evento. */
    public void setPoliticaCancelacion(String v) { politicaCancelacion = v; }
    /** retorna la politica de reembolso del evento. */
    public String getPoliticaReembolso() { return politicaReembolso; }
    /** asigna la politica de reembolso del evento. */
    public void setPoliticaReembolso(String v) { politicaReembolso = v; }
    /** retorna la lista de observadores suscritos al evento. */
    public List<IObserver> getListaObservers() { return listaObservers; }
    /** retorna la lista de compras del evento. */
    public List<Compra> getListaCompras() { return listaCompras; }

    @Override public String toString() { return nombre + ", de categoria: " + categoria; }
}
