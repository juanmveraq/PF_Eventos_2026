package com.pgii.model.interfaces;
import com.pgii.model.entities.Usuario;
import com.pgii.model.enums.RolUsuario;
import java.util.List;

/**
 * contrato de servicio para la gestion de usuarios en la plataforma.
 * define las operaciones CRUD y de autenticacion que debe implementar el controlador de usuarios.
 */
public interface UsuarioService {

    /**
     * registra un nuevo usuario en el sistema si el correo no esta en uso.
     * @param nombre Nombre completo del usuario.
     * @param correo Correo electronico unico del usuario.
     * @param telefono Numero de telefono del usuario.
     * @param rol Rol asignado al usuario (USUARIO o ADMINISTRADOR).
     * @return el usuario creado, o null si el correo ya estaba registrado.
     */
    public Usuario registrar(String nombre, String correo, String telefono, RolUsuario rol);

    /**
     * actualiza los datos de un usuario existente.
     * @param idUsuario Identificador del usuario a actualizar.
     * @param nombre Nuevo nombre del usuario.
     * @param correo Nuevo correo del usuario.
     * @param telefono Nuevo telefono del usuario.
     * @return verdadero (true) si el usuario fue encontrado y actualizado, falso (false) en caso contrario.
     */
    public boolean actualizar(int idUsuario, String nombre, String correo, String telefono);

    /**
     * Elimina un usuario del sistema por su identificador.
     * @param idUsuario Identificador del usuario a eliminar.
     * @return verdadero (true) si el usuario fue encontrado y eliminado, falso (false) en caso contrario.
     */
    public boolean eliminar(int idUsuario);

    /**
     * busca un usuario por su identificador unico.
     * @param idUsuario Identificador del usuario.
     * @return usuario encontrado, o null si no existe.
     */
    public Usuario buscarPorId(int idUsuario);

    /**
     * busca un usuario por su correo electronico.
     * @param correo Correo electronico del usuario.
     * @return el usuario encontrado, o null si no existe.
     */
    public Usuario buscarPorCorreo(String correo);

    /**
     * retorna la lista completa de usuarios registrados en la plataforma.
     * @return listta de todos los usuarios.
     */
    public List<Usuario> listarUsuarios();

    /**
     * agregamos un metodo de pago a la lista de un usuario existente.
     * @param idUsuario Identificador del usuario.
     * @param metodoPago Descripcion del metodo de pago a agregar.
     * @return verdadero (true) si el usuario fue encontrado y el metodo fue agregado, falso (false) en caso contrario.
     */
    public boolean agregarMetodoPago(int idUsuario, String metodoPago);
}
