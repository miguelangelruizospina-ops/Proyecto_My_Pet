// Servicio encargado de gestionar las notificaciones del sistema.

package com.example.My_Pet.service.Modulo_3_Agenda_Recordatorio;

// Modelos necesarios para gestionar las notificaciones y usuarios.

import com.example.My_Pet.model.Modulo_1_gestion_usuario.Usuario;

import com.example.My_Pet.model.Modulo_3_Agenda_Recordatorio.Notificacion;

// Repositorios necesarios para consultar y guardar la información.

import com.example.My_Pet.repository.Modulo_1_gestion_usuario.UsuarioRepository;

import com.example.My_Pet.repository.Modulo_3_Agenda_Recordatorio.NotificacionRepository;

// Librerías necesarias para el servicio y el manejo de fechas.

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import java.util.List;

// Define la clase como un servicio de Spring.

@Service

public class NotificacionService {

    // Conecta el servicio con la gestión de notificaciones.

    @Autowired

    private NotificacionRepository notificacionRepository;

    // Conecta el servicio con la gestión de usuarios.

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
            String mensaje,
            String tipo) {

        // Verificar que el mensaje no esté vacío.

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


        // Crear una nueva notificación.

        Notificacion notificacion = new Notificacion();


        // Asignar el mensaje de la notificación.

        notificacion.setMensaje(mensaje);


        // Registrar la fecha y hora de creación.

        notificacion.setFecha(LocalDateTime.now());


        // Toda notificación nueva comienza como no leída.

        notificacion.setEstado("NO_LEIDA");


        // Asignar el tipo de notificación.

        notificacion.setTipo(tipoNormalizado);


        // Asociar la notificación con el usuario.

        notificacion.setUsuario(usuario);


        // Guardar la notificación en la base de datos.

        return notificacionRepository.save(notificacion);

    }


    // Marcar una notificación como leída

    public Notificacion marcarComoLeida(Integer id) {

        // Buscar la notificación por su identificador.

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


        // Recorrer las notificaciones del usuario.

        for (Notificacion notificacion : notificaciones) {

            // Cambiar el estado de cada notificación a leída.

            notificacion.setEstado("LEIDA");

        }


        // Guardar los cambios realizados.

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