package com.pgii.model.interfaces;

/**
 * interfaz usada en el patron Observer que define el sujeto observable.
 * las clases que implementen esta interfaz pueden ser observadas por objetos IObserver.
 * en la plataforma, Evento implementa esta interfaz para notificar cambios de estado.
 */
public interface IObservable {

    /**
     * pone un suscriptor un observador para recibir notificaciones de este objeto.
     * @param observer Observador a registrar.
     */
    public void agregarObserver(IObserver observer);

    /**
     * elimina un observador de la lista de suscriptores.
     * @param observer Observador a eliminar.
     */
    public void eliminarObserver(IObserver observer);

    /**
     * envia un mensaje a todos los observadores suscritos.
     * @param mensaje Texto del mensaje a notificar.
     */
    public void notificarObservers(String mensaje);
}
