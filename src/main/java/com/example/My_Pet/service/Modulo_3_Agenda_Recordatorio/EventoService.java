package com.example.My_Pet.service.Modulo_3_Agenda_Recordatorio;

// Clases propias del proyecto.

import com.example.My_Pet.model.Modulo_2_gestion_mascota.Mascota;
import com.example.My_Pet.model.Modulo_3_Agenda_Recordatorio.Evento;
import com.example.My_Pet.repository.Modulo_2_gestion_mascotas.MascotaRepository;
import com.example.My_Pet.repository.Modulo_3_Agenda_Recordatorio.EventoRepository;
import com.example.My_Pet.service.Modulo_3_Agenda_Recordatorio.NotificacionService;

// Librerias de Spring.

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Libreria de Java.

import java.time.LocalDateTime;
import java.util.List;

// Gestiona las operaciones de consulta, registro, actualización y eliminación de eventos en la base de datos.

@Service
public class EventoService {

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private MascotaRepository mascotaRepository;

    @Autowired
    private NotificacionService notificacionService;

    // Lista todos los eventos registrados o creados en la base de datos.

    public List<Evento> obtenerTodos() {
        return eventoRepository.findAll();
    }

    // Busca un evento por su ID en la base de datos.

    public Evento obtenerPorId(Integer id) {
        return eventoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "El evento con ID " + id + " no existe."));
    }

    // Lista los eventos creados de una mascota registrada.

    public List<Evento> obtenerPorMascota(Integer idMascota) {
        return eventoRepository.findByMascotaIdMascota(idMascota);
    }

    // Guarda un nuevo evento en la base de datos y crea su notificación.

    @Transactional
    public Evento guardar(Evento evento) {

        // Verifica que el evento sea válido.

        if (evento == null) {
            throw new IllegalArgumentException(
                    "No se puede guardar un evento vacío.");
        }

        // Verifica que tenga un tipo de evento.

        if (evento.getTipoEvento() == null ||
                evento.getTipoEvento().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El evento debe tener un tipo.");
        }

        // Verifica que la mascota sea válida.

        if (evento.getMascota() == null ||
                evento.getMascota().getIdMascota() == null ||
                evento.getMascota().getIdMascota() <= 0) {
            throw new IllegalArgumentException(
                    "El evento debe estar asociado a una mascota válida.");
        }

        Integer idMascota = evento.getMascota().getIdMascota();

        // Verifica que la mascota exista en la base de datos.

        Mascota mascotaExistente = mascotaRepository.findById(idMascota)
                .orElseThrow(() -> new IllegalArgumentException(
                        "La mascota con ID " + idMascota + " no existe."));

        // Asocia el evento con la mascota registrada.

        evento.setMascota(mascotaExistente);

        // Utiliza la fecha y hora actual si no se proporciona una fecha.

        if (evento.getFecha() == null) {
            evento.setFecha(LocalDateTime.now());
        }

        // Guarda el evento en la base de datos.

        Evento eventoGuardado = eventoRepository.save(evento);

        // Obtiene el identificador del propietario de la mascota.

        Integer idUsuario = mascotaExistente.getUsuario().getIdUsuario();

        // Construye el mensaje de la notificación del evento registrado.

        String mensaje = "Nuevo evento para "
                + mascotaExistente.getNombre() + ": "
                + eventoGuardado.getTipoEvento()
                + ". Fecha: " + eventoGuardado.getFecha();

        // Guarda la notificación asociada al propietario de la mascota.

        notificacionService.crear(idUsuario, mensaje, "AGENDA");

        // Retorna el evento guardado correctamente.

        return eventoGuardado;
    }

    // Actualiza un evento existente.

    public Evento actualizar(Integer id, Evento evento) {

        // Busca el evento que se quiere actualizar.

        Evento eventoExistente = eventoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "El evento con ID " + id + " no existe."));

        // Verifica que el evento recibido sea válido.

        if (evento == null) {
            throw new IllegalArgumentException(
                    "No se puede actualizar con un evento vacío.");
        }

        // Verifica que tenga un tipo de evento.

        if (evento.getTipoEvento() == null ||
                evento.getTipoEvento().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El evento debe tener un tipo.");
        }

        // Verifica que la mascota sea válida.

        if (evento.getMascota() == null ||
                evento.getMascota().getIdMascota() == null ||
                evento.getMascota().getIdMascota() <= 0) {
            throw new IllegalArgumentException(
                    "El evento debe estar asociado a una mascota válida.");
        }

        Integer idMascota = evento.getMascota().getIdMascota();

        // Verifica que la mascota exista en la base de datos.

        Mascota mascotaExistente = mascotaRepository.findById(idMascota)
                .orElseThrow(() -> new IllegalArgumentException(
                        "La mascota con ID " + idMascota + " no existe."));

        // Actualiza la información del evento.

        eventoExistente.setTipoEvento(evento.getTipoEvento().trim());
        eventoExistente.setDescripcion(evento.getDescripcion());
        eventoExistente.setFecha(evento.getFecha());
        eventoExistente.setMascota(mascotaExistente);

        // Guarda los cambios del evento en la base de datos.

        return eventoRepository.save(eventoExistente);
    }

    // Elimina un evento de la base de datos.

    public void eliminar(Integer id) {

        // Verifica que el evento exista antes de eliminarlo.

        if (!eventoRepository.existsById(id)) {
            throw new IllegalArgumentException(
                    "El evento con ID " + id + " no existe.");
        }

        // Elimina el evento registrado.

        eventoRepository.deleteById(id);
    }
}
