package com.pgii.model.enums;

/**
 * define los posibles estados de un asiento en la plataforma.
 * cada estado representa una etapa diferente en el proceso de compra y disponibilidad del asiento:
 * aca disponemos de 4 diferentes:
 * DISPONIBLE: el asiento puede ser seleccionado por un usuario.
 * RESERVADO: el asiento esta temporalmente tomado durante una compra en proceso.
 * VENDIDO: el asiento ya fue pagado y confirmado.
 * BLOQUEADO: el asiento fue inhabilitado administrativamente.
 */
public enum EstadoAsiento { DISPONIBLE, RESERVADO, VENDIDO, BLOQUEADO }
