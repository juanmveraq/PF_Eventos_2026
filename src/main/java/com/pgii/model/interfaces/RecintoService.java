package com.pgii.model.interfaces;
import com.pgii.model.entities.Recinto;
import java.util.List;

/**
 * contrato de servicio para la gestion de recintos en la plataforma.
 * define las operaciones CRUD y de administracion de zonas que debe implementar
 * el controlador de recintos.
 */
public interface RecintoService {

    /**
     * crea un nuevo recinto en el sistema.
     * @param nombre Nombre del recinto.
     * @param direccion Direccion fisica del recinto.
     * @param ciudad Ciudad donde esta ubicado el recinto.
     * @return el recinto creado.
     */
    public Recinto crearRecinto(String nombre, String direccion, String ciudad);

    /**
     * actualiza los datos de un recinto existente.
     * @param idRecinto Identificador del recinto a actualizar.
     * @param nombre Nuevo nombre del recinto.
     * @param direccion Nueva direccion del recinto.
     * @param ciudad Nueva ciudad del recinto.
     * @return verdaero (true) si el recinto fue encontrado y actualizado, falso (false) en caso contrario.
     */
    public boolean actualizarRecinto(int idRecinto, String nombre, String direccion, String ciudad);

    /**
     * elimina un recinto del sistema por su identificador.
     * @param idRecinto Identificador del recinto a eliminar.
     * @return verdaero (true) si el recinto fue eliminado, falso (false) en caso contrario.
     */
    public boolean eliminarRecinto(int idRecinto);

    /**
     * busca un recinto por su identificador unico.
     * @param idRecinto Identificador del recinto.
     * @return el recinto encontrado, o null si no existe.
     */
    public Recinto buscarPorId(int idRecinto);

    /**
     * retorna la lista completa de recintos registrados en la plataforma.
     * @return lista de todos los recintos.
     */
    public List<Recinto> listarRecintos();

    /**
     * agrega una nueva zona a un recinto existente.
     * @param idRecinto Identificador del recinto donde se agrega la zona.
     * @param nombreZona Nombre de la nueva zona (ej. "VIP Central").
     * @param capacidad Capacidad maxima de asientos de la zona.
     * @param precioBase Precio base de la entrada para esta zona.
     * @return verdadero (true) si el recinto fue encontrado y la zona fue agregada, falso (false) en caso contrario.
     */
    public boolean agregarZona(int idRecinto, String nombreZona, int capacidad, double precioBase);
}
