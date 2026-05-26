package com.pgii.model.enums;

/**
 * define los servicios (y de una vez directamente los precios) de los servicios adicionales que puede contratar un usuario junto con su compra.
 * con esto usamos el decorador de servicios para agregar estos beneficios a las entradas y reflejar su costo en el precio final.
 * definimos las suguientes:
 * VIP: acceso a zona VIP con beneficios exclusivos (costo +$50.000).
 * SEGURO: seguro de cancelacion de la entrada (costo +$15.000).
 * MERCHANDISING: paquete de articulos del evento (costo +$25.000).
 * PARQUEADERO: reserva de parqueadero en el recinto (costo +$20.000).
 * ACCESO_PREFERENCIAL: ingreso anticipado o preferencial al evento (costo +$30.000).
 */
public enum TipoServicio {
    VIP(50000), SEGURO(15000), MERCHANDISING(25000), PARQUEADERO(20000), ACCESO_PREFERENCIAL(30000);

    private final double precio;
    TipoServicio(double precio) { this.precio = precio; }
    public double getPrecio() { return precio; }
}
