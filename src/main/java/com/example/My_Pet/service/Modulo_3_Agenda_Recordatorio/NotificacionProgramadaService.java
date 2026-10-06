// Servicio encargado de generar notificaciones programadas.

package com.example.My_Pet.service.Modulo_3_Agenda_Recordatorio;

// Modelos necesarios para las notificaciones programadas.

import com.example.My_Pet.model.Modulo_3_Agenda_Recordatorio.Evento;
import com.example.My_Pet.model.Modulo_5_emergencia.Emergencia;

// Servicios necesarios para gestionar eventos, notificaciones y emergencias.

import com.example.My_Pet.service.Modulo_5_emergencia.EmergenciaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

// Define la clase como un servicio de Spring.

@Service
public class NotificacionProgramadaService {

    // Conecta el servicio con la gestión de eventos de la agenda.

    @Autowired
    private EventoService eventoService;

    // Conecta el servicio con la gestión de notificaciones.

    @Autowired
    private NotificacionService notificacionService;

    // Conecta el servicio con la gestión de emergencias.

    @Autowired
    private EmergenciaService emergenciaService;

    // Revisa los eventos y las emergencias cada minuto.

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

    // Genera una notificación diez minutos antes del evento.

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

                // Solo procesa eventos que ocurran dentro
                // de los próximos diez minutos.

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

                // Crea la notificación de agenda.

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

    // Genera una nueva notificación cada cinco minutos
    // mientras la emergencia permanezca pendiente.

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

                // Crea una notificación identificada como emergencia.

                notificacionService.crear(
                        emergencia.getUsuario().getIdUsuario(),
                        mensaje,
                        "EMERGENCIA"
                );

                // Guarda el momento de la última notificación.

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