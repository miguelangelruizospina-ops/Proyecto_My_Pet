package com.example.My_Pet.service.Modulo_3_Agenda_Recordatorio;

import com.example.My_Pet.model.Modulo_1_gestion_usuario.Usuario;
import com.example.My_Pet.model.Modulo_3_Agenda_Recordatorio.Notificacion;
import com.example.My_Pet.repository.Modulo_1_gestion_usuario.UsuarioRepository;
import com.example.My_Pet.repository.Modulo_3_Agenda_Recordatorio.NotificacionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificacionService {

    @Autowired
    private NotificacionRepository notificacionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;


    // Obtener todas las notificaciones
    public List<Notificacion> obtenerTodas() {

        return notificacionRepository.findAll();
    }


    // Obtener las notificaciones de un usuario
    public List<Notificacion> obtenerPorUsuario(Integer idUsuario) {

        return notificacionRepository
                .findByUsuarioIdUsuario(idUsuario);
    }


    // Crear una notificación
    public Notificacion crear(
            Integer idUsuario,
            String mensaje) {

        if (mensaje == null || mensaje.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "El mensaje de la notificación no puede estar vacío."
            );
        }

        Usuario usuario = usuarioRepository
                .findById(idUsuario)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El usuario con ID "
                                        + idUsuario
                                        + " no existe."
                        )
                );

        Notificacion notificacion = new Notificacion();

        notificacion.setMensaje(mensaje);
        notificacion.setFecha(LocalDateTime.now());
        notificacion.setEstado("NO_LEIDA");
        notificacion.setUsuario(usuario);

        return notificacionRepository.save(notificacion);
    }


    // Marcar una notificación como leída
    public Notificacion marcarComoLeida(Integer id) {

        Notificacion notificacion =
                notificacionRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "La notificación con ID "
                                                + id
                                                + " no existe."
                                )
                        );

        notificacion.setEstado("LEIDA");

        return notificacionRepository.save(notificacion);
    }


    // Marcar todas las notificaciones de un usuario como leídas
    public void marcarTodasComoLeidas(Integer idUsuario) {

        List<Notificacion> notificaciones =
                notificacionRepository
                        .findByUsuarioIdUsuario(idUsuario);

        for (Notificacion notificacion : notificaciones) {

            notificacion.setEstado("LEIDA");
        }

        notificacionRepository.saveAll(notificaciones);
    }


    // Eliminar una notificación
    public void eliminar(Integer id) {

        if (!notificacionRepository.existsById(id)) {

            throw new IllegalArgumentException(
                    "La notificación con ID "
                            + id
                            + " no existe."
            );
        }

        notificacionRepository.deleteById(id);
    }
}
