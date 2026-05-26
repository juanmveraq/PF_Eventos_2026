package com.pgii.model.enums;

/**
 * define los posibles estados de una entrada en la plataforma.
 * cada estado representa una etapa diferente en el ciclo de vida de la entrada:
 * y definimos los 3 siguientes
 * ACTIVA: la entrada es valida y puede ser utilizada.
 * USADA: la entrada ya fue escaneada en el ingreso al evento.
 * ANULADA: la entrada fue invalidada por cancelacion o reembolso.
 */
public enum EstadoEntrada { ACTIVA, USADA, ANULADA }
