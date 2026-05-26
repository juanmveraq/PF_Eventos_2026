package com.pgii.model.interfaces;
import com.pgii.model.entities.Entrada;
import java.util.List;

/**
 * Contrato de servicio para la gestion de entradas en la plataforma.
 * Define las operaciones de generacion, anulacion y consulta de entradas
 * que debe implementar el controlador de entradas.
 */
public interface EntradaService {

    /**
     * Genera una nueva entrada para una compra existente y la asocia a un asiento.
     * @param idCompra Identificador de la compra a la que pertenece la entrada.
     * @param idZona Identificador de la zona de la entrada.
     * @param idAsiento Identificador del asiento; si es positivo se marca como VENDIDO.
     * @param precioFinal Precio final de la entrada.
     * @return La entrada generada, o null si la compra o zona no existen.
     */
    public Entrada generarEntrada(int idCompra, int idZona, int idAsiento, double precioFinal);

    /**
     * Anula una entrada, liberando el asiento asociado si existe.
     * @param idEntrada Identificador de la entrada a anular.
     * @return true si la entrada fue encontrada y anulada, false en caso contrario.
     */
    public boolean anularEntrada(int idEntrada);

    /**
     * Marca una entrada como usada luego del ingreso al evento.
     * @param idEntrada Identificador de la entrada a marcar.
     * @return true si la entrada fue encontrada y marcada, false en caso contrario.
     */
    public boolean marcarUsada(int idEntrada);

    /**
     * Busca una entrada por su identificador unico buscando en todas las compras.
     * @param idEntrada Identificador de la entrada.
     * @return La entrada encontrada, o null si no existe.
     */
    public Entrada buscarPorId(int idEntrada);

    /**
     * Retorna todas las entradas pertenecientes a una compra especifica.
     * @param idCompra Identificador de la compra.
     * @return Lista de entradas de la compra, o lista vacia si la compra no existe.
     */
    public List<Entrada> listarPorCompra(int idCompra);
}
