package com.pgii.model.interfaces;
import com.pgii.model.entities.Compra;
import com.pgii.model.enums.TipoServicio;
import java.util.List;

/**
 * contrato de servicio para la gestion del ciclo de vida de compras en la plataforma.
 * define las operaciones de creacion, pago, confirmacion, cancelacion y consulta
 * que debe implementar el controlador de compras.
 */
public interface CompraService {

    /**
     * crea una nueva compra para un usuario, reservando el asiento indicado.
     * @param idUsuario Identificador del usuario que realiza la compra.
     * @param idEvento Identificador del evento seleccionado.
     * @param idZona Identificador de la zona seleccionada.
     * @param idAsiento Identificador del asiento; si es 0 o negativo no se asigna asiento especifico.
     * @return la compra creada con estado CREADA, o null si alguna entidad no existe o el asiento no esta disponible.
     */
    public Compra crearCompra(int idUsuario, int idEvento, int idZona, int idAsiento);

    /**
     * procesa el pago de una compra usando la estrategia de pago indicada.
     * @param idCompra Identificador de la compra a pagar.
     * @param estrategia Estrategia de pago a aplicar (puede ser efectivo, tarjeta, etc.).
     * @return verdadero (es decir, true) si el pago fue exitoso, falso (false) en caso contrario.
     */
    public boolean pagarCompra(int idCompra, IPagoStrategy estrategia);

    /**
     * confirma una compra que ya fue pagada, activando las entradas.
     * @param idCompra Identificador de la compra a confirmar.
     * @return verdadero (true) si la confirmacion fue procesada, falso (false) si la compra no existe o no tiene estado.
     */
    public boolean confirmarCompra(int idCompra);

    /**
     * cancela una compra y libera los asientos reservados.
     * @param idCompra Identificador de la compra a cancelar.
     * @return verdaaero (true) si la cancelacion fue procesada, falso (false) si la compra no existe o no tiene estado.
     */
    public boolean cancelarCompra(int idCompra);

    /**
     * procesa el reembolso de una compra pagada o confirmada.
     * @param idCompra Identificador de la compra a reembolsar.
     * @return verdadero (true) si el reembolso fue procesado, falso (false) si la compra no existe o no tiene estado.
     */
    public boolean reembolsarCompra(int idCompra);

    /**
     * agregar un servicio adicional a una compra existente.
     * @param idCompra Identificador de la compra.
     * @param servicio Tipo de servicio adicional a agregar.
     * @return verdad (true) si el servicio fue agregado, falso (false) si la compra o el servicio son invalidos.
     */
    public boolean agregarServicio(int idCompra, TipoServicio servicio);

    /**
     * busca una compra por su identificador unico.
     * @param idCompra Identificador de la compra.
     * @return la compra encontrada, o null si no existe.
     */
    public Compra buscarPorId(int idCompra);

    /**
     * retorna la lista completa de compras registradas en la plataforma.
     * @return lista de todas las compras.
     */
    public List<Compra> listarCompras();

    /**
     * retorna todas las compras realizadas por un usuario especifico.
     * @param idUsuario Identificador del usuario.
     * @return lista de compras del usuario, o lista vacia si el usuario no existe.
     */
    public List<Compra> listarComprasPorUsuario(int idUsuario);
}
