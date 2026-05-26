package com.pgii.model.interfaces;

/**
 * otra interfaz usade en el patron Observer que define al receptor de notificaciones.
 * Las clases que implementen esta interfaz pueden suscribirse a objetos IObservable
 * para recibir actualizaciones cuando ocurra un cambio de estado.
 */
public interface IObserver {

    /**
     * recibe y procesa una notificacion enviada por el objeto observable.
     * @param mensaje Contenido del mensaje de notificacion.
     */
    public void actualizar(String mensaje);
}
