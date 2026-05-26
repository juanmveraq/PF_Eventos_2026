package com.pgii;
import com.pgii.controller.*;
import com.pgii.model.entities.*;
import com.pgii.model.enums.*;
import com.pgii.model.patterns.behavioral.*;
import com.pgii.model.patterns.creational.GestorDatos;

/**
 * clase utilitaria que para inicializar los datos de prueba de la plataforma TuBoletaRobinson.
 * se ejecuta al inicio de la aplicacion y crea usuarios, recintos, zonas, asientos,
 * eventos y una compra de ejemplo para demostrar el funcionamiento del sistema.
 * indispensable porque no tenemos para iniciar un admin en la app si no tenemos otro admin creado.
 * no puede ser instanciada; el metodo inicializar() es estatico.
 */
public class DataInitializer {

    /**
     * cargar unos datos de prueba para usar el sistema: usuarios, recintos con zonas y asientos,
     * eventos publicados y una compra de demostracion con pago confirmado.
     * debe llamarse una sola vez al arrancar la aplicacion (desde Main.start).
     */
    public static void inicializar() {
        GestorDatos gestor     = GestorDatos.getInstancia();
        UsuarioController uCtrl  = new UsuarioController();
        RecintoController rCtrl  = new RecintoController();
        EventoController  eCtrl  = new EventoController();
        CompraController  cCtrl  = new CompraController();
        AsientoController aCtrl  = new AsientoController();

        // Usuarios  (nombre, correo, contrasena, tel, rol)
        uCtrl.registrar("Admin",          "admin@gmail.com",   "admin123",  "3001234567", RolUsuario.ADMINISTRADOR);
        Usuario simon  = uCtrl.registrar("Simon Valencia",  "simon@gmail.com",  "simon123",  "3112345678", RolUsuario.USUARIO);
        Usuario samuel = uCtrl.registrar("Samuel Marin",    "samuel@gmail.com", "samuel123", "3223456789", RolUsuario.USUARIO);
        Usuario juan   = uCtrl.registrar("Juan Vera",       "juan@gmail.com",   "juan123",   "3134567890", RolUsuario.USUARIO);

        // Saldo inicial en efectivo y en tarjeta para cada usuario cliente
        uCtrl.asignarSaldo(simon.getIdUsuario(),  1_000_000);
        uCtrl.asignarSaldo(samuel.getIdUsuario(), 1_500_000);
        uCtrl.asignarSaldo(juan.getIdUsuario(),     500_000);
        uCtrl.asignarSaldoTarjeta(simon.getIdUsuario(),  2_000_000);
        uCtrl.asignarSaldoTarjeta(samuel.getIdUsuario(), 1_000_000);
        uCtrl.asignarSaldoTarjeta(juan.getIdUsuario(),     800_000);
        uCtrl.asignarNumeroTarjeta(simon.getIdUsuario(),  "4111111111111111");
        uCtrl.asignarNumeroTarjeta(samuel.getIdUsuario(), "5500005555555559");
        uCtrl.asignarNumeroTarjeta(juan.getIdUsuario(),   "4012888888881881");

        // Recinto Estadio Centenario
        // Asientos: VIP/Preferencial = 2 filas x 12 = 24 | General = 4 filas x 12 = 48
        Recinto estadio = rCtrl.crearRecinto("Estadio Centenario", "Av. Principal 100", "Armenia");
        rCtrl.agregarZona(estadio.getIdRecinto(), "VIP Oriental",            24, 250000);
        rCtrl.agregarZona(estadio.getIdRecinto(), "VIP Central",             24, 250000);
        rCtrl.agregarZona(estadio.getIdRecinto(), "VIP Occidental",          24, 250000);
        rCtrl.agregarZona(estadio.getIdRecinto(), "Preferencial Oriental",   24, 150000);
        rCtrl.agregarZona(estadio.getIdRecinto(), "Preferencial Central",    24, 150000);
        rCtrl.agregarZona(estadio.getIdRecinto(), "Preferencial Occidental", 24, 150000);
        rCtrl.agregarZona(estadio.getIdRecinto(), "General Oriental",        48,  80000);
        rCtrl.agregarZona(estadio.getIdRecinto(), "General Central",         48,  80000);
        rCtrl.agregarZona(estadio.getIdRecinto(), "General Occidental",      48,  80000);

        String[] filasCortas = {"A","B"};
        String[] filasLargas = {"A","B","C","D"};
        for (Zona zona : estadio.getListaZonas()) {
            String[] filas = zona.getNombre().startsWith("General") ? filasLargas : filasCortas;
            for (String fila : filas)
                for (int n = 1; n <= 12; n++)
                    aCtrl.crearAsiento(zona.getIdZona(), fila, n);
        }

        // Recinto Auditorio — 4 filas x 12 = 48 asientos por zona
        Recinto auditorio = rCtrl.crearRecinto("auditorio de la UQ", "Calle 11 #1-11", "Armenia");
        rCtrl.agregarZona(auditorio.getIdRecinto(), "VIP",     48, 180000);
        rCtrl.agregarZona(auditorio.getIdRecinto(), "General", 48, 100000);
        for (Zona zona : auditorio.getListaZonas())
            for (String fila : new String[]{"A","B","C","D"})
                for (int n = 1; n <= 12; n++)
                    aCtrl.crearAsiento(zona.getIdZona(), fila, n);

        // eventos
        Evento concierto = eCtrl.crearEvento("Raul explicando singleton", CategoriaEvento.CONFERENCIA,
            "el mejor programador que a pisado colombia ",
            "Armenia", "2026-08-15 20:00", estadio.getIdRecinto(), "raul", "patron singleton");
        concierto.setPoliticaCancelacion("puede cancelarse en cualquier momento antes del evento");
        concierto.setPoliticaReembolso("se devolvera el valor de su compra siempre y cuando pagara el seguro.");
        eCtrl.publicarEvento(concierto.getIdEvento());

        Evento conferencia = eCtrl.crearEvento("vera y simon debatiendo sobre P5 y P4", CategoriaEvento.TEATRO,
            "vera y simon en un arduo combate de intelecto (platon vs socrates)",
            "armenia", "2026-09-20 09:00", auditorio.getIdRecinto(), "vera y simon", "debate saga P");
        eCtrl.publicarEvento(conferencia.getIdEvento());

        Evento teatro = eCtrl.crearEvento("samuel concieto de rock", CategoriaEvento.CONCIERTO,
            "samuel con el mejor concierto de rock de la historia moderna",
            "armenia", "2026-10-05 19:30", auditorio.getIdRecinto(), "samuel", "concierto de rock");
        eCtrl.publicarEvento(teatro.getIdEvento());

        eCtrl.crearEvento("Festival Electronico", CategoriaEvento.CONCIERTO,
            "Proximamente...", "Cali", "2026-12-31 22:00", estadio.getIdRecinto(), "DJ Varios", "Electronica");

        // Compra demo
        Zona vipCentral = estadio.getListaZonas().get(1);
        Asiento primerAsiento = vipCentral.getListaAsientos().get(0);
        Compra c = cCtrl.crearCompra(simon.getIdUsuario(), concierto.getIdEvento(),
                                     vipCentral.getIdZona(), primerAsiento.getIdAsiento());
        if (c != null) {
            cCtrl.agregarServicio(c.getIdCompra(), TipoServicio.VIP);
            cCtrl.pagarCompra(c.getIdCompra(), new PagoTarjeta("Visa"));
            cCtrl.confirmarCompra(c.getIdCompra());
        }
        System.out.println("DataInitializer fue iniciada");
    }
}
