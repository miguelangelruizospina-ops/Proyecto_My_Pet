package com.example.My_Pet.service.Modulo_3_Agenda_Recordatorio;

// Clases propias del proyecto.

import com.example.My_Pet.model.Modulo_3_Agenda_Recordatorio.Evento;
import com.example.My_Pet.model.Modulo_5_emergencia.Emergencia;
import com.example.My_Pet.service.Modulo_5_emergencia.EmergenciaService;

// Librerias del spring.

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

// Libreria de Java

import java.time.LocalDateTime;
import java.util.List;

// Gestiona la generación programada de notificaciones y emergencias a partir de los eventos de la agenda.

@Service
public class NotificacionProgramadaService {
    @Autowired
    private EventoService eventoService;
    @Autowired
    private NotificacionService notificacionService;
    @Autowired
    private EmergenciaService emergenciaService;

    // Revisa los eventos y las emergencias registrados cada minuto.

    @Scheduled(fixedRate = 60000)
    public void revisarNotificaciones() {
        try {
            revisarEventos();
        } catch (Exception error) {
            System.err.println(
                    "Error al revisar las notificaciones de agenda: "
                            + error.getMessage()
            );
        }
        try {
            revisarEmergencias();
        } catch (Exception error) {
            System.err.println(
                    "Error al revisar las notificaciones de emergencia: "
                            + error.getMessage()
            );
        }
    }

    // Genera una notificación al usuario diez minutos antes del evento.

    private void revisarEventos() {
        List<Evento> eventos = eventoService.obtenerTodos();
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime limite = ahora.plusMinutes(10);
        for (Evento evento : eventos) {
            try {

                // Verifica que el evento tenga una fecha válida.

                if (evento.getFecha() == null) {
                    continue;
                }

                // Procesa únicamente los eventos programados para los próximos diez minutos.

                if (evento.getFecha().isBefore(ahora)
                        || evento.getFecha().isAfter(limite)) {

                    continue;
                }

                // Verifica que el evento tenga una mascota válida.

                if (evento.getMascota() == null
                        || evento.getMascota().getIdMascota() == null
                        || evento.getMascota().getIdMascota() <= 0) {
                    System.err.println(
                            "Evento inválido con ID: "
                                    + evento.getIdEvento()
                                    + ". No tiene una mascota válida."
                    );
                    continue;
                }
                String mensaje =
                        "Recordatorio: "
                                + evento.getTipoEvento();
                if (evento.getDescripcion() != null
                        && !evento.getDescripcion()
                                .trim()
                                .isEmpty()) {
                    mensaje +=
                            " - "
                                    + evento.getDescripcion()
                                    .trim();
                }

                // Crea la notificación de la agenda.

                notificacionService.crear(
                        evento.getMascota()
                                .getUsuario()
                                .getIdUsuario(),
                        mensaje,
                        "AGENDA"
                );
            } catch (Exception error) {
                System.err.println(
                        "No se pudo procesar el evento "
                                + evento.getIdEvento()
                                + ": "
                                + error.getMessage()
                );
            }
        }
    }

    // Genera una nueva notificación cada cinco minutos mientras la emergencia permanezca pendiente por solucionar.

    private void revisarEmergencias() {
        List<Emergencia> emergencias =
                emergenciaService.obtenerPendientes();
        LocalDateTime ahora =
                LocalDateTime.now();
        for (Emergencia emergencia : emergencias) {
            try {
                LocalDateTime ultimaNotificacion =
                        emergencia.getUltimaNotificacion();
                boolean debeNotificar =
                        ultimaNotificacion == null
                                || !ultimaNotificacion
                                .plusMinutes(5)
                                .isAfter(ahora);
                if (!debeNotificar) {
                    continue;
                }
                String nombreMascota =
                        emergencia.getMascota() != null
                                ? emergencia.getMascota().getNombre()
                                : "tu mascota";
                String mensaje =
                        "La emergencia de "
                                + nombreMascota
                                + " continúa pendiente. Revisa la situación.";

                // Crea una notificación emergencia de las emergencias que registra el usuario.

                notificacionService.crear(
                        emergencia.getUsuario().getIdUsuario(),
                        mensaje,
                        "EMERGENCIA"
                );

                // Registra la fecha y hora de la última notificación enviada.

                emergenciaService.actualizarUltimaNotificacion(
                        emergencia.getIdEmergencia(),
                        ahora
                );
            } catch (Exception error) {
                System.err.println(
                        "No se pudo procesar la emergencia "
                                + emergencia.getIdEmergencia()
                                + ": "
                                + error.getMessage()
                );
            }
        }
    }
}