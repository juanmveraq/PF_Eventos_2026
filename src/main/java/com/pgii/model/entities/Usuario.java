package com.pgii.model.entities;
import com.pgii.model.enums.RolUsuario;
import java.util.ArrayList;
import java.util.List;

public class Usuario {
    private int idUsuario;
    private String nombre;
    private String correo;
    private String contrasena;
    private String telefono;
    private RolUsuario rol;
    private List<String> listaMetodosPago;
    private List<Compra> listaCompras;
    private double saldo;
    private double saldoTarjeta;
    private String numeroTarjeta;

    public Usuario(int idUsuario, String nombre, String correo,
                   String contrasena, String telefono, RolUsuario rol) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.correo = correo;
        this.contrasena = contrasena;
        this.telefono = telefono;
        this.rol = rol;
        this.listaMetodosPago = new ArrayList<>();
        this.listaCompras = new ArrayList<>();
    }

    // TODO: agregarCompra(), agregarMetodoPago()
    // TODO: getters, setters y toString
}
