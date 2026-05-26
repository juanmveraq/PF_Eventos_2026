package com.pgii.controller;
import com.pgii.model.entities.*;
import com.pgii.model.enums.*;
import com.pgii.model.interfaces.*;
import com.pgii.model.patterns.behavioral.*;
import com.pgii.model.patterns.creational.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * controlador que implementa la logica de negocio para el ciclo de vida de las compras.
 * se encarga de gestionar la creacion, pago, confirmacion, cancelacion y reembolso de compras.
 * usamos CompraBuilder (patron Builder) para crear compras y aplica los estados
 * del patron State para las transiciones y el comportamiento.
 * delega el almacenamiento en GestorDatos (patron Singleton).
 * implementa CompraService.
 */
public class CompraController implements CompraService {

    /** instancia de nuestro gestor de datos (Singleton) que almacena todas las compras. */
    private GestorDatos gestor;

    public CompraController() { gestor = GestorDatos.getInstancia(); }

    /**
     * crea una nueva compra, que reservaa el asiento indicado para el usuario y evento dados.
     * @param idUsuario Identificador del usuario que esta realiza la compra.
     * @param idEvento Identificador del evento seleccionado.
     * @param idZona Identificador de la zona seleccionada.
     * @param idAsiento Identificador del asiento; si es mayor a 0 se reserva ese asiento especifico, y claramente para hacer una compra tiene que reservar asientos.
     * @return La compra creada en estado CREADA, o null si alguna entidad no existe o el asiento no esta disponible.
     */
    @Override
    public Compra crearCompra(int idUsuario, int idEvento, int idZona, int idAsiento) {
        Usuario u = gestor.buscarUsuarioPorId(idUsuario);
        Evento ev = gestor.buscarEventoPorId(idEvento);
        Zona z   = gestor.buscarZonaPorId(idZona);
        if(u==null||ev==null||z==null) return null;
        Asiento a = null;
        if(idAsiento > 0) {
            a = z.buscarAsientoPorId(idAsiento);
            if(a==null || a.getEstado()!=EstadoAsiento.DISPONIBLE) return null;
            a.setEstado(EstadoAsiento.RESERVADO);
        }
        int id = gestor.generarIdCompra();
        Compra compra = new CompraBuilder().setIdCompra(id)
                .setFechaCreacion(LocalDate.now().toString())
                .setUsuario(u).setEvento(ev).build();
        Entrada entrada = new Entrada(gestor.generarIdEntrada(), z, a, z.getPrecioBase(), compra);
        compra.agregarEntrada(entrada);
        gestor.getListaCompras().add(compra);
        u.agregarCompra(compra);
        ev.agregarCompra(compra);
        return compra;
    }

    /**
     * se encarga de procesar el pago de una compra existente y registra el objeto pago resultante.
     * @param id Identificador de la compra a pagar.
     * @param estrategia Estrategia de pago a utilizar (puede ser de tipo efectivo, tarjeta, etc.).
     * @return verdadero (true) si el pago fue procesado exitosamente, falso )false) en caso contrario.
     */

    @Override
    public boolean pagarCompra(int id, IPagoStrategy estrategia) {
        Compra c = gestor.buscarCompraPorId(id); if(c==null||estrategia==null) return false;
        boolean ok = c.pagar(estrategia);
        if(ok) {
            Pago p = new Pago(gestor.generarIdPago(), c.getTotal(), estrategia.getDescripcion(), c);
            p.setExitoso(true); p.setFechaPago(LocalDate.now().toString());
            p.setEsTarjeta(estrategia instanceof PagoTarjeta);
            c.setThePago(p);
        }
        return ok;
    }

    /**
     * confirma la compra una compra que ya fue pagada, y asi delegando la transicion al estado actual.
     * @param id Identificador de la compra a confirmar.
     * @return verdadero (true) si la confirmacion fue procesada, falso (false) si la compra no existe o no tiene estado.
     */
    @Override
    public boolean confirmarCompra(int id) {
        Compra c = gestor.buscarCompraPorId(id); if(c==null||c.getTheEstado()==null) return false;
        return c.getTheEstado().confirmar(c);
    }

    /**
     * cancela una compra y libera los asientos que tenia reservados o vendidos.
     * @param id Identificador de la compra a cancelar.
     * @return verdadero (true) si la cancelacion fue procesada, falso (false) si la compra no existe o no tiene estado.
     */
    @Override
    public boolean cancelarCompra(int id) {
        Compra c = gestor.buscarCompraPorId(id); if(c==null||c.getTheEstado()==null) return false;
        boolean ok = c.getTheEstado().cancelar(c);
        if (ok) liberarAsientos(c);
        return ok;
    }

    /**
     * procesar el reembolso de una compra delegando al estado actual.
     * @param id Identificador de la compra a reembolsar.
     * @return verdadero (true) si el reembolso fue procesado, falso (false) si la compra no existe o no tiene estado.
     */
    @Override public boolean reembolsarCompra(int id) {
        Compra c = gestor.buscarCompraPorId(id); if(c==null||c.getTheEstado()==null) return false;
        boolean ok = c.getTheEstado().reembolsar(c);
        if (ok) {
            Usuario u = c.getTheUsuario();
            if (u != null && c.getThePago() != null) {
                if (c.getThePago().isEsTarjeta()) u.setSaldoTarjeta(u.getSaldoTarjeta() + c.getTotal());
                else                               u.setSaldo(u.getSaldo() + c.getTotal());
            }
        }
        return ok;
    }

    /**
     * añadir un servicio adicional a la compra indicada.
     * @param id Identificador de la compra.
     * @param s Tipo de servicio adicional a agregar.
     * @return verdadero (true) si el servicio fue agregado, falso (false) si la compra o el servicio son invalidos.
     */

    @Override public boolean agregarServicio(int id, TipoServicio s) {
        Compra c = gestor.buscarCompraPorId(id); if(c==null||s==null) return false;
        c.agregarServicio(s); return true;
    }

    /** retorna la compra con el id indicado, o null si no existe. */
    @Override public Compra buscarPorId(int id) { return gestor.buscarCompraPorId(id); }

    /** retona la lista de todas las compras registradas. */
    @Override public List<Compra> listarCompras() { return gestor.getListaCompras(); }

    /**
     * retorna todas las compras realizadas por el usuario indicado.
     * @param idUsuario Identificador del usuario.
     * @return lista de compras del usuario, o lista vacia si el usuario no existe.
     */
    @Override public List<Compra> listarComprasPorUsuario(int idUsuario) {
        Usuario u = gestor.buscarUsuarioPorId(idUsuario);
        if(u==null) return new ArrayList<>(); return u.getListaCompras();
    }

    /**
     * libera todos los asientos asociados a las entradas de una compra, poniendolos en DISPONIBLE.
     * @param c Compra cuyos asientos se deben liberar.
     */
    private void liberarAsientos(Compra c) {
        List<Entrada> ent = c.getListaEntradas();
        for(int i=0;i<ent.size();i++) {
            Asiento a = ent.get(i).getTheAsiento();
            if(a != null) new LiberarAsientoCommand(a).ejecutar();
        }
    }
}
