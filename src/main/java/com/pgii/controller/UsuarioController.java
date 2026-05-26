package com.pgii.controller;
import com.pgii.model.entities.Usuario;
import com.pgii.model.enums.RolUsuario;
import com.pgii.model.interfaces.UsuarioService;
import com.pgii.model.patterns.creational.GestorDatos;
import java.util.List;

/**
 * controlador que implementa la logica de negocio para la gestion de usuarios.
 * este actua como intermediario entre la vista y el modelo, delegando el almacenamiento
 * en GestorDatos (patron Singleton).
 * implementa UsuarioService.
 */
public class UsuarioController implements UsuarioService {

    /** instancia del gestor de datos (Singleton) que almacena todos los usuarios. */
    private GestorDatos gestor;

    public UsuarioController() { gestor = GestorDatos.getInstancia(); }

    /**
     * registrar un nuevo usuario con contrasena personalizada si el correo no esta en uso.
     * @param nombre Nombre completo del usuario.
     * @param correo Correo electronico unico del usuario.
     * @param contrasena Contrasena de acceso del usuario.
     * @param tel Numero de telefono del usuario.
     * @param rol Rol asignado (USUARIO o ADMINISTRADOR).
     * @return el usuario creado, o null si el correo ya estaba registrado.
     */
    public Usuario registrar(String nombre, String correo, String contrasena,
                             String tel, RolUsuario rol) {
        if (buscarPorCorreo(correo) != null) return null;
        Usuario u = new Usuario(gestor.generarIdUsuario(), nombre, correo,
                                contrasena, tel, rol);
        // Saldo inicial para clientes registrados por el sistema de login
        if (rol == RolUsuario.USUARIO) {
            u.setSaldo(500_000);
            u.setSaldoTarjeta(500_000);
            u.setNumeroTarjeta("0000000000000000");
        }
        gestor.getListaUsuarios().add(u);
        return u;
    }

    /**
     * actualiza la contrasena del usuario indicado.
     * @param id Identificador del usuario.
     * @param contrasena Nueva contrasena.
     * @return verdadero si el usuario fue encontrado y actualizado, falso en caso contrario.
     */
    public boolean actualizarContrasena(int id, String contrasena) {
        Usuario u = gestor.buscarUsuarioPorId(id);
        if (u == null || contrasena == null || contrasena.isEmpty()) return false;
        u.setContrasena(contrasena);
        return true;
    }

    /**
     * actualiza el rol del usuario indicado.
     * @param id  Identificador del usuario.
     * @param rol Nuevo rol a asignar.
     * @return verdadero si el usuario fue encontrado y actualizado, falso en caso contrario.
     */
    public boolean actualizarRol(int id, RolUsuario rol) {
        Usuario u = gestor.buscarUsuarioPorId(id);
        if (u == null || rol == null) return false;
        u.setRol(rol);
        return true;
    }

    /**
     * registarr un usuario usando la firma de la interfaz UsuarioService, con contrasena "1234" por defecto.
     * @param nombre Nombre completo del usuario.
     * @param correo Correo electronico unico del usuario.
     * @param tel Numero de telefono del usuario.
     * @param rol Rol asignado al usuario.
     * @return el usuario creado, o null si el correo ya estaba registrado.
     */
    @Override public Usuario registrar(String nombre, String correo,
                                       String tel, RolUsuario rol) {
        return registrar(nombre, correo, "1234", tel, rol);
    }

    /**
     * verificar si las credenciales de acceso de un usuario son correctas.
     * @param correo Correo electronico del usuario.
     * @param contrasena Contrasena a verificar.
     * @return verdadero (true) si el usuario existe y la contrasena coincide, falso (false) en caso contrario.
     */
    public boolean verificarContrasena(String correo, String contrasena) {
        Usuario u = buscarPorCorreo(correo);
        return u != null && u.getContrasena().equals(contrasena);
    }

    /**
     * actualizacion los datos de un usuario existente. Solo actualiza campos no vacios.
     * @param id Identificador del usuario a actualizar.
     * @param nombre Nuevo nombre del usuario.
     * @param correo Nuevo correo del usuario.
     * @param tel Nuevo telefono del usuario.
     * @return verdadero (true) si el usuario fue encontrado y actualizado, falso (false) en caso contrario.
     */
    @Override public boolean actualizar(int id, String nombre, String correo, String tel) {
        Usuario u = gestor.buscarUsuarioPorId(id); if (u == null) return false;
        if (nombre != null && !nombre.isEmpty()) u.setNombre(nombre);
        if (correo != null && !correo.isEmpty()) u.setCorreo(correo);
        if (tel    != null && !tel.isEmpty())    u.setTelefono(tel);
        return true;
    }

    /**
     * elimina un usuario del sistema por su identificador.
     * @param id Identificador del usuario a eliminar.
     * @return verdadero (true) si el usuario fue encontrado y eliminado, falso (false) en caso contrario.
     */
    @Override public boolean eliminar(int id) {
        List<Usuario> l = gestor.getListaUsuarios();
        for (int i = 0; i < l.size(); i++)
            if (l.get(i).getIdUsuario() == id) { l.remove(i); return true; }
        return false;
    }

    /** retorna el usuario con el id indicado, o null si no existe. */
    @Override public Usuario buscarPorId(int id) { return gestor.buscarUsuarioPorId(id); }

    /** retorna el usuario con el correo indicado, o null si no existe. */
    @Override public Usuario buscarPorCorreo(String correo) {
        return gestor.buscarUsuarioPorCorreo(correo);
    }

    /** retorna la lista de todos los usuarios registrados. */
    @Override public List<Usuario> listarUsuarios() { return gestor.getListaUsuarios(); }

    /**
     * agrega un metodo de pago a la lista del usuario indicado.
     * @param id Identificador del usuario.
     * @param m Descripcion del metodo de pago.
     * @return verdadero (true) si el usuario fue encontrado y el metodo fue agregado, falso (false) en caso contrario.
     */
    @Override public boolean agregarMetodoPago(int id, String m) {
        Usuario u = gestor.buscarUsuarioPorId(id); if (u == null) return false;
        u.agregarMetodoPago(m); return true;
    }

    /**
     * descuenta el monto indicado del saldo del usuario si tiene suficiente disponible.
     * entonces se usa al realizar pagos con efectivo desde el saldo del usuario.
     * @param id    Identificador del usuario al que se le cobrara.
     * @param monto Monto en pesos a descontar del saldo.
     * @return verdadero (true) si el saldo era suficiente y se descontó, falso (false) si el
     * usuario no existe o el saldo es insuficiente.
     */
    public boolean cobrar(int id, double monto) {
        Usuario u = gestor.buscarUsuarioPorId(id);
        if (u == null || u.getSaldo() < monto) return false;
        u.setSaldo(u.getSaldo() - monto);
        return true;
    }

    /**
     * abona el monto indicado al saldo del usuario.
     * este se usa para devolver dinero al cancelar una compra o procesar un reembolso.
     * @param id    Identificador del usuario al que se le abonara.
     * @param monto Monto en pesos a agregar al saldo.
     * @return verdaero (true) si el usuario fue encontrado y el monto fue abonado, falso (false) en
     * caso contrario.
     */
    public boolean abonar(int id, double monto) {
        Usuario u = gestor.buscarUsuarioPorId(id);
        if (u == null) return false;
        u.setSaldo(u.getSaldo() + monto);
        return true;
    }

    /**
     * asigna directamente un saldo inicial al usuario indicado.
     * es util al crear usuarios desde el panel administrador o en la inicializacion.
     * @param id    Identificador del usuario.
     * @param saldo Saldo inicial en pesos colombianos.
     * @return verdadero (true) si el usuario fue encontrado y el saldo asignado, falso (false) en
     * caso contrario.
     */
    public boolean asignarSaldo(int id, double saldo) {
        Usuario u = gestor.buscarUsuarioPorId(id);
        if (u == null) return false;
        u.setSaldo(saldo);
        return true;
    }

    /**
     * descuento el monto indicado del saldo de tarjeta del usuario si tiene suficiente disponible.
     * este se usa al realizar pagos con tarjeta (Visa, Mastercard, PSE) desde el saldo de tarjeta.
     * @param id    Identificador del usuario al que se le cobrara.
     * @param monto Monto en pesos a descontar del saldo de tarjeta.
     * @return verdadero (true) si el saldo de tarjeta era suficiente y se descontó, falso (false) si el usuario no existe
     * o el saldo de tarjeta es insuficiente.
     */
    public boolean cobrarTarjeta(int id, double monto) {
        Usuario u = gestor.buscarUsuarioPorId(id);
        if (u == null || u.getSaldoTarjeta() < monto) return false;
        u.setSaldoTarjeta(u.getSaldoTarjeta() - monto);
        return true;
    }

    /**
     * abona el monto indicado al saldo de tarjeta del usuario.
     * este se usa para devolver dinero al cancelar o reembolsar compras que fueron pagadas con tarjeta.
     * @param id    Identificador del usuario al que se le abonara.
     * @param monto Monto en pesos a agregar al saldo de tarjeta.
     * @return verdadero (true) si el usuario fue encontrado y el monto fue abonado, falso (false) en
     * caso contrario.
     */
    public boolean abonarTarjeta(int id, double monto) {
        Usuario u = gestor.buscarUsuarioPorId(id);
        if (u == null) return false;
        u.setSaldoTarjeta(u.getSaldoTarjeta() + monto);
        return true;
    }

    /**
     * asigna directamente un saldo de tarjeta al usuario indicado.
     * es util al crear usuarios desde el panel administrador o en la inicializacion.
     * @param id    Identificador del usuario.
     * @param saldo Saldo de tarjeta inicial en pesos colombianos.
     * @return verdadero (true) si el usuario fue encontrado y el saldo de tarjeta asignado, falso
     * (false) en caso contrario.
     */
    public boolean asignarSaldoTarjeta(int id, double saldo) {
        Usuario u = gestor.buscarUsuarioPorId(id);
        if (u == null) return false;
        u.setSaldoTarjeta(saldo);
        return true;
    }

    /**
     * registra el numero de tarjeta del usuario indicado.
     * este numero se usa para validar exactamente el numero ingresado al momento de pagar.
     * @param id     Identificador del usuario.
     * @param numero Numero de tarjeta a registrar (sin espacios ni caracteres especiales).
     * @return verdaderp (true) si el usuario fue encontrado y el numero asignado, falso (false) en
     * caso contrario.
     */
    public boolean asignarNumeroTarjeta(int id, String numero) {
        Usuario u = gestor.buscarUsuarioPorId(id);
        if (u == null) return false;
        u.setNumeroTarjeta(numero);
        return true;
    }
}
