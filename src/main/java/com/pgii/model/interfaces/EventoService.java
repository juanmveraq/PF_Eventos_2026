package com.pgii.model.interfaces;
import com.pgii.model.entities.Evento;
import com.pgii.model.enums.CategoriaEvento;
import java.util.List;

/**
 * contrato de implementacion de servicio para la gestion de eventos en la plataforma.
 * define las operaciones de creacion, actualizacion, eliminacion y cambio de estado
 * que debe implementar el controlador de eventos.
 */
public interface EventoService {

    /**
     * aqui crea un nuevo evento en el sistema asociado a un recinto existente.
     * @param nombre Nombre del evento.
     * @param categoria Categoria del evento (CONCIERTO, TEATRO, CONFERENCIA).
     * @param descripcion Descripcion del evento.
     * @param ciudad Ciudad donde se realizara el evento.
     * @param fechaHora Fecha y hora del evento en formato texto.
     * @param idRecinto Identificador del recinto donde se realizara el evento.
     * @param infoExtra1 Informacion adicional especifica del tipo de evento (ej. artista).
     * @param infoExtra2 Segunda informacion adicional especifica (ej. genero musical).
     * @return el evento creado, o null si el recinto no existe.
     */
    public Evento crearEvento(String nombre, CategoriaEvento categoria, String descripcion,
                       String ciudad, String fechaHora, int idRecinto,
                       String infoExtra1, String infoExtra2);

    /**
     * actualiza los datos de un evento existente.
     * @param idEvento Identificador del evento a actualizar.
     * @param nombre Nuevo nombre del evento.
     * @param descripcion Nueva descripcion del evento.
     * @param ciudad Nueva ciudad del evento.
     * @param fechaHora Nueva fecha y hora del evento.
     * @return verdaerp (true) si el evento fue encontrado y actualizado, falso (false) en caso contrario.
     */
    public boolean actualizarEvento(int idEvento, String nombre, String descripcion, String ciudad, String fechaHora);

    /**
     * elimina un evento del sistema por su identificador.
     * @param idEvento Identificador del evento a eliminar.
     * @return verdadero (true) si el evento fue eliminado, falso (false) en caso contrario.
     */
    public boolean eliminarEvento(int idEvento);

    /**
     * retorna la lista completa de eventos registrados en la plataforma.
     * @return lista de todos los eventos.
     */
    public List<Evento> listarEventos();

    /**
     * busca un evento por su identificador unico.
     * @param idEvento Identificador del evento.
     * @return el evento encontrado, o null si no existe.
     */
    public Evento buscarPorId(int idEvento);

    /**
     * cambia el estado de un evento a PUBLICADO si se encuentra en BORRADOR o PAUSADO.
     * @param idEvento Identificador del evento a publicar.
     * @return verdadero (true) si la transicion fue valida y exitosa, falso (false) en caso contrario.
     */
    public boolean publicarEvento(int idEvento);

    /**
     * cambia el estado de un evento PUBLICADO a PAUSADO.
     * @param idEvento Identificador del evento a pausar.
     * @return verdadero (true) si el evento estaba publicado y fue pausado, falso (false) en caso contrario.
     */
    public boolean pausarEvento(int idEvento);

    /**
     * cambia el estado de un evento a CANCELADO.
     * @param idEvento Identificador del evento a cancelar.
     * @return verdadero (true) si el evento fue encontrado y cancelado, falso (false) en caso contrario.
     */
    public boolean cancelarEvento(int idEvento);

    /**
     * filtra y retorna los eventos que pertenecen a una categoria especifica.
     * @param categoria Categoria por la cual filtrar.
     * @return lista de eventos de la categoria indicada.
     */
    public List<Evento> filtrarPorCategoria(CategoriaEvento categoria);

    /**
     * filtra y retorna los eventos que se realizan en una ciudad especifica.
     * @param ciudad Ciudad por la cual filtrar (sin distinguir mayusculas).
     * @return lista de eventos de la ciudad indicada.
     */
    public List<Evento> filtrarPorCiudad(String ciudad);
}
