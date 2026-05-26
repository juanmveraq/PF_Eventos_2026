package com.pgii.model.enums;

/**
 * Define los posibles estados del ciclo de vida de una compra.
 * estos estados controlan el flujo de la compra desde su creacion hasta su finalizacion o cancelacion.
 * cada estado representa una etapa diferente en el proceso de compra y determina las acciones permitidas.
 * y definimos las siguientes:
 * CREADA: la compra fue iniciada pero aun no se ha pagado.
 * PAGADA: el pago fue procesado exitosamente.
 * CONFIRMADA: la compra fue verificada y las entradas estan activas.
 * CANCELADA: la compra fue cancelada por el usuario o el sistema.
 * REEMBOLSADA: se proceso un reembolso para la compra.
 * INCIDENCIA: la compra tiene un problema reportado.
 */
public enum EstadoCompra { CREADA, PAGADA, CONFIRMADA, CANCELADA, REEMBOLSADA, INCIDENCIA }
