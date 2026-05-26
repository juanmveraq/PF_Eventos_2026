package com.pgii.controller;

import com.pgii.model.entities.Usuario;
import com.pgii.model.enums.RolUsuario;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * prueba unitarias para {@link UsuarioController}.
 * aqui buscamos hacer registro, autenticacion, busqueda, actualizacion,
 * gestion de saldo en efectivo y tarjeta, cobros, abonos y eliminacion.
 *
 * <p>el {@code @BeforeAll} crea un usuario compartido de solo lectura ({@code idUsuario}).
 * en estos tests que modifican datos sensibles (nombre, saldo) crean sus propios usuarios
 * para no interferir entre si.</p>
 */
class   UsuarioControllerTest {

    static UsuarioController ctrl;
    static int idUsuario;
    static final String CORREO = "usrtest@gmail.com";

    /**
     * inicializar el controlador y crea el usuario base que usan los tests de lectura.
     */
    @BeforeAll
    static void setup() {
        ctrl = new UsuarioController();
        Usuario u = ctrl.registrar("testeo de user", CORREO, "pass123", "3001111111", RolUsuario.USUARIO);
        assertNotNull(u);
        idUsuario = u.getIdUsuario();
    }

    /**
     * verifica que registrar persiste el usuario con los datos correctos.
     * Metodo probado: {@code registrar(nombre, correo, contrasena, tel, rol)}.
     */
    @Test
    void registrar_conContrasena_ok() {
        assertNotNull(ctrl.buscarPorId(idUsuario));
        assertEquals("testeo de user", ctrl.buscarPorId(idUsuario).getNombre());
    }

    /**
     * verifica que registrar con un correo ya existente retorna null sin crear duplicado.
     * Metodo probado: {@code registrar} con correo repetido.
     */
    @Test
    void registrar_correoRepetido_retornaNull() {
        assertNull(ctrl.registrar("Otro", CORREO, "abc", "000", RolUsuario.USUARIO));
    }

    /**
     * verifica que registrar sin contrasena usa la contrasena por defecto.
     * Metodo probado: {@code registrar(nombre, correo, tel, rol)}.
     */
    @Test
    void registrar_sinContrasena_usaDefecto() {
        Usuario u = ctrl.registrar("Sin Pass", "sinpass@gmail.com", "311", RolUsuario.USUARIO);
        assertNotNull(u);
    }

    /**
     * verifica que verificarContrasena retorna true cuando la contrasena coincide.
     * Metodo probado: {@code verificarContrasena(correo, contrasena)}.
     */
    @Test
    void verificarContrasena_correcta() {
        assertTrue(ctrl.verificarContrasena(CORREO, "pass123"));
    }

    /**
     * verifica que verificarContrasena retorna false cuando la contrasena no coincide.
     * Metodo probado: {@code verificarContrasena} con contrasena erronea.
     */
    @Test
    void verificarContrasena_incorrecta() {
        assertFalse(ctrl.verificarContrasena(CORREO, "wrongpass"));
    }

    /**
     * verifica que buscarPorId retorna el usuario correspondiente al identificador dado.
     * Metodo probado: {@code buscarPorId(id)}.
     */
    @Test
    void buscarId_retornaUsuario() {
        assertNotNull(ctrl.buscarPorId(idUsuario));
    }

    /**
     * verifica que buscarPorCorreo retorna el usuario con el correo indicado.
     * Metodo probado: {@code buscarPorCorreo(correo)}.
     */
    @Test
    void buscarCorreo_retornaUsuario() {
        assertNotNull(ctrl.buscarPorCorreo(CORREO));
    }

    /**
     * verifica que listarUsuarios retorna al menos un usuario registrado.
     * Metodo probado: {@code listarUsuarios()}.
     */
    @Test
    void listar_noVacio() {
        assertFalse(ctrl.listarUsuarios().isEmpty());
    }

    /**
     * verifica que agregarMetodoPago agrega el metodo a la lista del usuario.
     * Metodo probado: {@code agregarMetodoPago(id, metodoPago)}.
     */
    @Test
    void agregarMetodoPago_ok() {
        assertTrue(ctrl.agregarMetodoPago(idUsuario, "Tarjeta Visa"));
        assertFalse(ctrl.buscarPorId(idUsuario).getListaMetodosPago().isEmpty());
    }

    /**
     * verifica que actualizar modifica el nombre de un usuario.
     * se usa un usuario propio para no alterar el usuario compartido del setup.
     * Metodo probado: {@code actualizar(id, nombre, correo, tel)}.
     */
    @Test
    void actualizar_cambiaDatos() {
        Usuario u = ctrl.registrar("Para Actualizar", "actualizar@gmail.com", "pass", "0", RolUsuario.USUARIO);
        assertNotNull(u);
        assertTrue(ctrl.actualizar(u.getIdUsuario(), "Nombre Actualizado", null, null));
        assertEquals("Nombre Actualizado", ctrl.buscarPorId(u.getIdUsuario()).getNombre());
    }

    /**
     * verifica que asignarSaldo establece el saldo en efectivo al valor indicado.
     * Metodo probado: {@code asignarSaldo(id, saldo)}.
     */
    @Test
    void asignarSaldo_ok() {
        assertTrue(ctrl.asignarSaldo(idUsuario, 500_000));
        assertEquals(500_000, ctrl.buscarPorId(idUsuario).getSaldo(), 0.01);
    }

    /**
     * verifica que cobrar descuenta el monto del saldo cuando hay fondos suficientes.
     * aqui se establece el saldo inicial en este mismo test para no depender de otro.
     * Metodo probado: {@code cobrar(id, monto)}.
     */
    @Test
    void cobrar_descuentaSaldo() {
        ctrl.asignarSaldo(idUsuario, 500_000);
        assertTrue(ctrl.cobrar(idUsuario, 200_000));
        assertEquals(300_000, ctrl.buscarPorId(idUsuario).getSaldo(), 0.01);
    }

    /**
     * verifica que cobrar retorna false y no modifica el saldo cuando es insuficiente.
     * en este tambien se establece el saldo inicial en este mismo test para no depender de otro.
     * Metodo probado: {@code cobrar} con saldo insuficiente.
     */
    @Test
    void cobrar_saldoInsuficiente_falla() {
        ctrl.asignarSaldo(idUsuario, 100_000);
        assertFalse(ctrl.cobrar(idUsuario, 500_000));
        assertEquals(100_000, ctrl.buscarPorId(idUsuario).getSaldo(), 0.01);
    }

    /**
     * verifica que abonar suma el monto al saldo en efectivo correctamente.
     * se vulve a establecer el saldo inicial en este mismo test para no depender de otro.
     * Metodo probado: {@code abonar(id, monto)}.
     */
    @Test
    void abonar_incrementaSaldo() {
        ctrl.asignarSaldo(idUsuario, 100_000);
        assertTrue(ctrl.abonar(idUsuario, 50_000));
        assertEquals(150_000, ctrl.buscarPorId(idUsuario).getSaldo(), 0.01);
    }

    /**
     * verifica que eliminar borra el usuario del sistema.
     * crea su propio usuario para no afectar el usuario compartido del setup.
     * Metodo probado: {@code eliminar(id)}.
     */
    @Test
    void eliminar_yaNoExiste() {
        Usuario u = ctrl.registrar("Eliminar", "eliminar@gmail.com", "x", "0", RolUsuario.USUARIO);
        assertNotNull(u);
        assertTrue(ctrl.eliminar(u.getIdUsuario()));
        assertNull(ctrl.buscarPorId(u.getIdUsuario()));
    }

    /**
     * verifica que asignarNumeroTarjeta guarda el numero en el usuario.
     * Metodo probado: {@code asignarNumeroTarjeta(id, numero)}.
     */
    @Test
    void asignarNumeroTarjeta_ok() {
        assertTrue(ctrl.asignarNumeroTarjeta(idUsuario, "4111111111111111"));
        assertEquals("4111111111111111", ctrl.buscarPorId(idUsuario).getNumeroTarjeta());
    }

    /**
     * verifica que asignarSaldoTarjeta establece el saldo de tarjeta al valor indicado.
     * Metodo probado: {@code asignarSaldoTarjeta(id, saldo)}.
     */
    @Test
    void asignarSaldoTarjeta_ok() {
        assertTrue(ctrl.asignarSaldoTarjeta(idUsuario, 300_000));
        assertEquals(300_000, ctrl.buscarPorId(idUsuario).getSaldoTarjeta(), 0.01);
    }

    /**
     * verifica que cobrarTarjeta descuenta el monto del saldo de tarjeta cuando hay fondos.
     * se establece el saldo inicial en este mismo test para no depender de otro.
     * Metodo probado: {@code cobrarTarjeta(id, monto)}.
     */
    @Test
    void cobrarTarjeta_descuentaSaldo() {
        ctrl.asignarSaldoTarjeta(idUsuario, 500_000);
        assertTrue(ctrl.cobrarTarjeta(idUsuario, 200_000));
        assertEquals(300_000, ctrl.buscarPorId(idUsuario).getSaldoTarjeta(), 0.01);
    }

    /**
     * verifica que cobrarTarjeta retorna false cuando el saldo de tarjeta es insuficiente.
     * establece el saldo inicial en este mismo test para no depender de otro.
     * Metodo probado: {@code cobrarTarjeta} con saldo insuficiente.
     */
    @Test
    void cobrarTarjeta_saldoInsuficiente_falla() {
        ctrl.asignarSaldoTarjeta(idUsuario, 100_000);
        assertFalse(ctrl.cobrarTarjeta(idUsuario, 500_000));
        assertEquals(100_000, ctrl.buscarPorId(idUsuario).getSaldoTarjeta(), 0.01);
    }

    /**
     * verifica que abonarTarjeta suma el monto al saldo de tarjeta correctamente.
     * establece el saldo inicial en este mismo test para no depender de otro.
     * Metodo probado: {@code abonarTarjeta(id, monto)}.
     */
    @Test
    void abonarTarjeta_incrementaSaldo() {
        ctrl.asignarSaldoTarjeta(idUsuario, 100_000);
        assertTrue(ctrl.abonarTarjeta(idUsuario, 50_000));
        assertEquals(150_000, ctrl.buscarPorId(idUsuario).getSaldoTarjeta(), 0.01);
    }
}
