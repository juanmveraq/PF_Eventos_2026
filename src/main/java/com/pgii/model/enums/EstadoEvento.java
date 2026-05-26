package com.pgii.model.enums;

/**
 * define los posibles estados del ciclo de vida de un evento.
 * cada estado representa una etapa diferente en la gestión y disponibilidad del evento:
 * y tenemos los 5 siguientes:
 * BORRADOR: el evento fue creado pero no ha sido publicado al publico.
 * PUBLICADO: el evento esta visible y disponible para la venta de entradas.
 * PAUSADO: la venta de entradas fue suspendida temporalmente.
 * CANCELADO: el evento fue cancelado definitivamente.
 * FINALIZADO: el evento ya se llevo a cabo.
 */
public enum EstadoEvento { BORRADOR, PUBLICADO, PAUSADO, CANCELADO, FINALIZADO }
