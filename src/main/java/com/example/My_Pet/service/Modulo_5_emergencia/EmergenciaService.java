// Servicio para la gestión de emergencias.

package com.example.My_Pet.service.Modulo_5_emergencia;

// Modelos y repositorios necesarios.

import com.example.My_Pet.model.Modulo_1_gestion_usuario.Usuario;

import com.example.My_Pet.model.Modulo_2_gestion_mascota.Mascota;

import com.example.My_Pet.model.Modulo_5_emergencia.Emergencia;

import com.example.My_Pet.repository.Modulo_1_gestion_usuario.UsuarioRepository;

import com.example.My_Pet.repository.Modulo_2_gestion_mascotas.MascotaRepository;

import com.example.My_Pet.repository.Modulo_5_emergencia.EmergenciaRepository;

import com.example.My_Pet.service.Modulo_3_Agenda_Recordatorio.NotificacionService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import java.util.List;

// Define la clase como un servicio de Spring.

@Service

public class EmergenciaService {

    @Autowired

    private EmergenciaRepository emergenciaRepository;

    @Autowired

    private UsuarioRepository usuarioRepository;

    @Autowired

    private MascotaRepository mascotaRepository;

    @Autowired

    private NotificacionService notificacionService;

    // Obtiene todas las emergencias.

    public List<Emergencia> obtenerTodas() {

        return emergenciaRepository.findAll();

    }

    // Obtiene las emergencias de un usuario.

    public List<Emergencia> obtenerPorUsuario(Integer idUsuario) {

        return emergenciaRepository.findByUsuarioIdUsuario(idUsuario);

    }

    // Obtiene únicamente las emergencias pendientes.

    public List<Emergencia> obtenerPendientes() {

        return emergenciaRepository.findByEstado("PENDIENTE");

    }

    // Guarda una emergencia nueva.

    public Emergencia guardar(Emergencia emergencia) {

        if (emergencia == null) {

            throw new IllegalArgumentException(
                    "La emergencia no puede estar vacía."
            );

        }

        if (emergencia.getUsuario() == null ||
            emergencia.getUsuario().getIdUsuario() <= 0) {

            throw new IllegalArgumentException(
                    "La emergencia debe estar asociada a un usuario válido."
            );

        }

        Integer idUsuario =
                emergencia.getUsuario().getIdUsuario();

        Usuario usuarioExistente =
                usuarioRepository.findById(idUsuario)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "El usuario con ID "
                                                + idUsuario
                                                + " no existe."
                                )
                        );

        emergencia.setUsuario(usuarioExistente);

        Mascota mascotaExistente = null;

        if (emergencia.getMascota() != null) {

            Integer idMascota =
                    emergencia.getMascota().getIdMascota();

            mascotaExistente =
                    mascotaRepository.findById(idMascota)
                            .orElseThrow(() ->
                                    new IllegalArgumentException(
                                            "La mascota con ID "
                                                    + idMascota
                                                    + " no existe."
                                    )
                            );

            if (mascotaExistente.getUsuario() == null ||
                !idUsuario.equals(
                        mascotaExistente.getUsuario().getIdUsuario()
                )) {

                throw new IllegalArgumentException(
                        "La emergencia debe estar asociada a una mascota de ese usuario."
                );

            }

            emergencia.setMascota(mascotaExistente);
        }

        boolean nueva =
                emergencia.getIdEmergencia() == null;

        if (nueva) {

            emergencia.setFecha(LocalDateTime.now());

            // Toda emergencia nueva comienza pendiente.

            emergencia.setEstado("PENDIENTE");

            emergencia.setUltimaNotificacion(null);
        }

        Emergencia guardada =
                emergenciaRepository.save(emergencia);

        // La primera notificación se genera inmediatamente.

        if (nueva) {

            String nombreMascota =
                    mascotaExistente != null
                    ? mascotaExistente.getNombre()
                    : "tu mascota";

            String mensaje =
                    "Emergencia registrada para "
                    + nombreMascota
                    + ". Revisa la situación y busca atención veterinaria "
                    + "si es necesario.";

            // Crear la notificación indicando que pertenece a una emergencia.

            notificacionService.crear(
                    idUsuario,
                    mensaje,
                    "EMERGENCIA"
            );

            // Guarda el momento de la primera notificación.

            guardada.setUltimaNotificacion(
                    LocalDateTime.now()
            );

            guardada =
                    emergenciaRepository.save(guardada);
        }

        return guardada;
    }

    // Actualiza el momento de la última notificación.

    public Emergencia actualizarUltimaNotificacion(
            Integer id,
            LocalDateTime fechaNotificacion) {

        Emergencia emergencia =
                emergenciaRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "La emergencia con ID "
                                                + id
                                                + " no existe."
                                )
                        );

        emergencia.setUltimaNotificacion(fechaNotificacion);

        return emergenciaRepository.save(emergencia);
    }

    // Marca una emergencia como resuelta.

    public Emergencia resolver(Integer id) {

        Emergencia emergencia =
                emergenciaRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "La emergencia con ID "
                                                + id
                                                + " no existe."
                                )
                        );

        // Si ya está resuelta, no se modifica.

        if ("RESUELTA".equalsIgnoreCase(
                emergencia.getEstado())) {

            return emergencia;
        }

        emergencia.setEstado("RESUELTA");

        return emergenciaRepository.save(emergencia);
    }

    // Elimina una emergencia.

    public void eliminar(Integer id) {

        if (!emergenciaRepository.existsById(id)) {

            throw new IllegalArgumentException(
                    "La emergencia con ID "
                            + id
                            + " no existe."
            );
        }

        emergenciaRepository.deleteById(id);
    }

}