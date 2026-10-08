package com.example.My_Pet.service.Modulo_3_Agenda_Recordatorio;

// Clases propias del proyecto.

import com.example.My_Pet.model.Modulo_1_gestion_usuario.Usuario;
import com.example.My_Pet.model.Modulo_3_Agenda_Recordatorio.Notificacion;
import com.example.My_Pet.repository.Modulo_1_gestion_usuario.UsuarioRepository;
import com.example.My_Pet.repository.Modulo_3_Agenda_Recordatorio.NotificacionRepository;

// Librerías del spring

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Librerias de Java

import java.time.LocalDateTime;
import java.util.List;

// Gestiona las operaciones de consulta, registro, actualización y eliminación de notificaciones en la base 

@Service
public class NotificacionService {
    @Autowired
    private NotificacionRepository notificacionRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    // Lista todas las notificaciones registradas.

    public List<Notificacion> obtenerTodas() {
        return notificacionRepository.findAll();
    }

    // Obtener las notificaciones registradas  de un usuario

    public List<Notificacion> obtenerPorUsuario(Integer idUsuario) {
        return notificacionRepository
                .findByUsuarioIdUsuario(idUsuario);
    }


    // crea una nueva notificacion. 

    public Notificacion crear(
            Integer idUsuario,
            String mensaje,
            String tipo) {

        // Verifica que el mensaje de la notificación no esté vacío.

        if (mensaje == null || mensaje.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El mensaje de la notificación no puede estar vacío."
            );
        }

        // Verificar que el tipo de notificación no esté vacío.

        if (tipo == null || tipo.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El tipo de la notificación no puede estar vacío."
            );
        }

        // Convertir el tipo a mayúsculas para mantener un formato uniforme.

        String tipoNormalizado = tipo.trim().toUpperCase();


        // Verificar que el tipo corresponda a una agenda o emergencia.

        if (!tipoNormalizado.equals("AGENDA")
                && !tipoNormalizado.equals("EMERGENCIA")) {
            throw new IllegalArgumentException(
                    "El tipo de notificación debe ser AGENDA o EMERGENCIA."
            );
        }

        // Buscar el usuario al que pertenece la notificación.

        Usuario usuario = usuarioRepository
                .findById(idUsuario)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El usuario con ID "
                                        + idUsuario
                                        + " no existe."
                        )
                );

       // Crea y configura la nueva notificación.

        Notificacion notificacion = new Notificacion();
        notificacion.setMensaje(mensaje);
        notificacion.setFecha(LocalDateTime.now());

        // Toda notificación nueva comienza como no leída.

        notificacion.setEstado("NO_LEIDA");
        notificacion.setTipo(tipoNormalizado);
        notificacion.setUsuario(usuario);
        return notificacionRepository.save(notificacion);

    }


    // Marcar una notificación como leída

    public Notificacion marcarComoLeida(Integer id) {

        // Buscar la notificación por su ID.

        Notificacion notificacion =
                notificacionRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "La notificación con ID "
                                                + id
                                                + " no existe."
                                )
                        );


        // Cambiar el estado de la notificación a leída.

        notificacion.setEstado("LEIDA");


        // Guardar el cambio en la base de datos.

        return notificacionRepository.save(notificacion);
    }


    // Marcar todas las notificaciones de un usuario como leídas

    public void marcarTodasComoLeidas(Integer idUsuario) {

        // Obtener todas las notificaciones del usuario.

        List<Notificacion> notificaciones =
                notificacionRepository
                        .findByUsuarioIdUsuario(idUsuario);

        // Marca como leídas todas las notificaciones del usuario.

        for (Notificacion notificacion : notificaciones) {
            notificacion.setEstado("LEIDA");
        }
        notificacionRepository.saveAll(notificaciones);
    }


    // Eliminar una notificación

    public void eliminar(Integer id) {

        // Verificar que la notificación exista.

        if (!notificacionRepository.existsById(id)) {
            throw new IllegalArgumentException(
                    "La notificación con ID "
                            + id
                            + " no existe."
            );
        }


        // Eliminar la notificación de la base de datos.

        notificacionRepository.deleteById(id);
    }

    // Eliminar todas las notificaciones de un usuario

    public void eliminarTodas(Integer idUsuario) {

        // Obtener todas las notificaciones pertenecientes al usuario.

        List<Notificacion> notificaciones =
                notificacionRepository
                        .findByUsuarioIdUsuario(idUsuario);

        // Eliminar las notificaciones si existen.

        if (!notificaciones.isEmpty()) {
            notificacionRepository.deleteAll(notificaciones);
        }
    }
}