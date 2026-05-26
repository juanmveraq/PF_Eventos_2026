package com.pgii.model.entities;
import com.pgii.model.enums.CategoriaEvento;
import com.pgii.model.enums.EstadoEvento;
import com.pgii.model.interfaces.IObservable;
import com.pgii.model.interfaces.IObserver;
import java.util.ArrayList;
import java.util.List;

public abstract class Evento implements IObservable {
    private int idEvento;
    private String nombre;
    private CategoriaEvento categoria;
    private String descripcion;
    private String ciudad;
    private String fechaHora;
    private EstadoEvento estado;
    private Recinto theRecinto;
    private String politicaCancelacion;
    private String politicaReembolso;
    private List<IObserver> listaObservers;
    private List<Compra> listaCompras;

    public Evento(int idEvento, String nombre, CategoriaEvento categoria,
                  String descripcion, String ciudad, String fechaHora) {
        this.idEvento = idEvento;
        this.nombre = nombre;
        this.categoria = categoria;
        this.descripcion = descripcion;
        this.ciudad = ciudad;
        this.fechaHora = fechaHora;
        this.estado = EstadoEvento.BORRADOR;
        this.listaObservers = new ArrayList<>();
        this.listaCompras = new ArrayList<>();
    }

    public abstract String getInfoEspecifica();

    // TODO: cambiarEstado(), agregarCompra()
    // TODO: implementar agregarObserver, eliminarObserver, notificarObservers
    // TODO: getters, setters y toString
    @Override public void agregarObserver(IObserver o) {}
    @Override public void eliminarObserver(IObserver o) {}
    @Override public void notificarObservers(String msg) {}
}
