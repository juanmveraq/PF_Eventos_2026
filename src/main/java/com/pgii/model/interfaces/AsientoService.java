package com.pgii.model.interfaces;
import com.pgii.model.entities.Asiento;
import java.util.List;

/**
 * contrato que tiene que implementar de servicio para la gestion de asientos en la plataforma.
 * define las operaciones de creacion y cambio de estado de asientos
 * que debe implementar el controlador de asientos.
 */
public interface AsientoService {

    /**
     * aqui se crea un nuevo asiento en una zona especifica del sistema.
     * @param idZona Identificador de la zona donde se crea el asiento.
     * @param fila Fila del asiento (ej. "A", "B").
     * @param numero Numero del asiento dentro de la fila.
     * @return el asiento asiento creado, o null si la zona no existe.
     */
    public Asiento crearAsiento(int idZona, String fila, int numero);

    /**
     * Habilita un asiento poniendolo en estado DISPONIBLE usando el patron Command.
     * @param idAsiento Identificador del asiento a habilitar.
     * @return true si el asiento fue encontrado y habilitado, false en caso contrario.
     */
    public boolean habilitarAsiento(int idAsiento);

    /**
     * Bloquea un asiento poniendolo en estado BLOQUEADO usando el patron Command.
     * @param idAsiento Identificador del asiento a bloquear.
     * @return true si el asiento fue encontrado y bloqueado, false en caso contrario.
     */
    public boolean bloquearAsiento(int idAsiento);

    /**
     * Libera un asiento poniendolo en estado DISPONIBLE directamente.
     * @param idAsiento Identificador del asiento a liberar.
     * @return true si el asiento fue encontrado y liberado, false en caso contrario.
     */
    public boolean liberarAsiento(int idAsiento);

    /**
     * Busca un asiento por su identificador unico.
     * @param idAsiento Identificador del asiento.
     * @return El asiento encontrado, o null si no existe.
     */
    public Asiento buscarPorId(int idAsiento);

    /**
     * Retorna todos los asientos pertenecientes a una zona especifica.
     * @param idZona Identificador de la zona.
     * @return Lista de asientos de la zona, o lista vacia si la zona no existe.
     */
    public List<Asiento> listarPorZona(int idZona);
}
