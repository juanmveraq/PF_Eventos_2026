package com.pgii.model.interfaces;

/**
 * interfaz del patron Decorator para los componentes de reporte de la plataforma.
 * define el contrato base que deben cumplir tanto los componentes simples (ReporteCompra)
 * como los decoradores (EncabezadoDecorador, FirmaDecorador) que los envuelven.
 */
public interface IReporteComponente {

    /**
     * enera y retorna el contenido textual del reporte o componente.
     * @return la cadena con el contenido formateado del reporte.
     */
    public String mostrarContenido();

    /**
     * retorna la cantidad de elementos contenidos en este componente de reporte.
     * @return un numero de elementos del reporte.
     */
    public int contarElementos();
}
