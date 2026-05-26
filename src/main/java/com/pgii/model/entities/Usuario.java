package com.pgii.model.entities;
import com.pgii.model.enums.RolUsuario;
import java.util.ArrayList;
import java.util.List;

/**
 * representa a un usuario registrado en la plataforma TuBoletaRobinson.
 * se encarga de almacenar la informacion personal, metodos de pago y lista de compras del usuario.
 * este puede tener rol de USUARIO o ADMINISTRADOR segun el enum RolUsuario.
 */
public class Usuario {

    /** identificador unico del usuario generado por GestorDatos. */
    private int idUsuario;

    /** nombre completo del usuario. */
    private String nombre;

    /** correo electronico unico que identifica la cuenta. */
    private String correo;

    /** contraseña de acceso del usuario. */
    private String contrasena;

    /** numero de telefono de contacto del usuario. */
    private String telefono;

    /** rol del usuario dentro de la plataforma (solo tenemos USUARIO o ADMINISTRADOR). */
    private RolUsuario rol;

    /** lista de metodos de pago registrados por el usuario (ej. tarjeta, efectivo). */
    private List<String> listaMetodosPago;

    /** lista de compras realizadas por el usuario en la plataforma. */
    private List<Compra> listaCompras;

    /**
     * saldo disponible del usuario en pesos colombianos para pagos en efectivo.
     * usamos metodos para aplicar descuenta al pagar con efectivo y se abona al recibir reembolsos de pagos en efectivo.
     */
    private double saldo;

    /**
     * saldo disponible en la tarjeta del usuario en pesos colombianos.
     * se descuenta al pagar con tarjeta y se abona al cancelar o reembolsar compras pagadas con tarjeta.
     */
    private double saldoTarjeta;

    /**
     * numero de tarjeta registrado por el usuario.
     * se usa para validar que el numero ingresado al pagar coincida exactamente con el registrado.
     */
    private String numeroTarjeta;

    public Usuario(int idUsuario, String nombre, String correo,
                   String contrasena, String telefono, RolUsuario rol) {
        this.idUsuario = idUsuario; this.nombre = nombre;
        this.correo = correo; this.contrasena = contrasena;
        this.telefono = telefono; this.rol = rol;
        this.listaMetodosPago = new ArrayList<>();
        this.listaCompras = new ArrayList<>();
        this.saldo = 0;
        this.saldoTarjeta = 0;
        this.numeroTarjeta = "";
    }

    /**
     * agrega una compra a la lista de compras del usuario.
     * @param c Compra a agregar. Se ignora si es nula.
     */
    public void agregarCompra(Compra c) { if (c != null) listaCompras.add(c); }

    /**
     * agrega un metodo de pago a la lista del usuario.
     * @param m Descripcion del metodo de pago. Se ignora si es nulo o vacio.
     */
    public void agregarMetodoPago(String m) { if (m != null && !m.isEmpty()) listaMetodosPago.add(m); }

    /** retorna el id del usuario. */
    public int getIdUsuario() { return idUsuario; }
    /** asigana el id del usuario. */
    public void setIdUsuario(int v) { idUsuario = v; }
    /** retorna el nombre del usuario. */
    public String getNombre() { return nombre; }
    /** asigna el nombre del usuario. */
    public void setNombre(String v) { nombre = v; }
    /** retorna el correo del usuario. */
    public String getCorreo() { return correo; }
    /** asigna el correo del usuario. */
    public void setCorreo(String v) { correo = v; }
    /** retorna la contrasena del usuario. */
    public String getContrasena() { return contrasena; }
    /** asigna la contrasena del usuario. */
    public void setContrasena(String v) { contrasena = v; }
    /** retorna el telefono del usuario. */
    public String getTelefono() { return telefono; }
    /** asigna el telefono del usuario. */
    public void setTelefono(String v) { telefono = v; }
    /** retorna el rol del usuario. */
    public RolUsuario getRol() { return rol; }
    /** asigna el rol del usuario. */
    public void setRol(RolUsuario v) { rol = v; }
    /** retorna la lista de metodos de pago registrados. */
    public List<String> getListaMetodosPago() { return listaMetodosPago; }
    /** retorna la lista de compras del usuario. */
    public List<Compra> getListaCompras() { return listaCompras; }
    /** retorna el saldo disponible del usuario en pesos colombianos (pagos en efectivo). */
    public double getSaldo() { return saldo; }
    /** asigna el saldo del usuario en pesos colombianos (pagos en efectivo). */
    public void setSaldo(double v) { saldo = v; }
    /** asigna el saldo disponible en la tarjeta del usuario en pesos colombianos. */
    public double getSaldoTarjeta() { return saldoTarjeta; }
    /** asigna el saldo de tarjeta del usuario en pesos colombianos. */
    public void setSaldoTarjeta(double v) { saldoTarjeta = v; }
    /** retorna el numero de tarjeta registrado por el usuario. */
    public String getNumeroTarjeta() { return numeroTarjeta; }
    /** asigna el numero de tarjeta del usuario. */
    public void setNumeroTarjeta(String v) { numeroTarjeta = v != null ? v : ""; }

    @Override public String toString() { return nombre + " (" + correo + ")"; }
}
