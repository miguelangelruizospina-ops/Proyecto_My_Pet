package com.example.My_Pet.controller.Modulo_3_Agenda_Recordatorio;

//clases del proyecto

import com.example.My_Pet.model.Modulo_3_Agenda_Recordatorio.Notificacion;
import com.example.My_Pet.service.Modulo_3_Agenda_Recordatorio.NotificacionService;

//Librerías de Spring.

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

//Librerías de Java.

import java.util.List;

// Controlador REST para las operaciones de notificaciones.

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    // Servicio encargado de gestionar las notificaciones.

    @Autowired
    private NotificacionService notificacionService;

    // Obtiene todas las notificaciones del sistema. ***SOLO PARA ADMINISTRADORES***

    @GetMapping("/listar")
    @PreAuthorize("@autorizacion.esAdministrador()")
    public List<Notificacion> listarTodo() {
        return notificacionService.obtenerTodas();
    }

    // Obtiene las notificaciones pertenecientes a un usuario existente.

    @GetMapping("/usuario/{idUsuario}")
    @PreAuthorize("@autorizacion.esUsuarioPropioOAdministrador(#idUsuario)")
    public ResponseEntity<List<Notificacion>> listarPorUsuario(
            @PathVariable("idUsuario")
            @P("idUsuario") Integer idUsuario) {
        List<Notificacion> notificaciones =
                notificacionService.obtenerPorUsuario(idUsuario);
        return ResponseEntity.ok(notificaciones);
    }

    // Crea una nueva notificación para un usuario que tenga mascotas registradas.

    @PostMapping("/crear")
    @PreAuthorize("@autorizacion.esUsuarioPropioOAdministrador(#idUsuario)")
    public ResponseEntity<?> crear(
            @RequestParam
            @P("idUsuario") Integer idUsuario,
            @RequestParam String mensaje,
            @RequestParam(
                    required = false,
                    defaultValue = "EMERGENCIA"
            )
            String tipo) {
        try {
            Notificacion notificacion =
                    notificacionService.crear(
                            idUsuario,
                            mensaje,
                            tipo
                    );
            return ResponseEntity.ok(notificacion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    // Marca una notificación específica de un usuario como leída.

    @PutMapping("/leer/{id}")
    @PreAuthorize("@autorizacion.esNotificacionPropiaOAdministrador(#id)")
    public ResponseEntity<?> marcarComoLeida(
            @PathVariable("id")
            @P("id") Integer id) {
        try {
            Notificacion notificacion =
                    notificacionService.marcarComoLeida(id);
            return ResponseEntity.ok(notificacion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    // Marca todas las notificaciones de un usuario como leídas.

    @PutMapping("/leer-todas/{idUsuario}")
    @PreAuthorize("@autorizacion.esUsuarioPropioOAdministrador(#idUsuario)")
    public ResponseEntity<?> marcarTodasComoLeidas(
            @PathVariable("idUsuario")
            @P("idUsuario") Integer idUsuario) {
        try {
            notificacionService.marcarTodasComoLeidas(idUsuario);
            return ResponseEntity.ok(
                    "Todas las notificaciones fueron marcadas como leídas."
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    // Elimina una notificación perteneciente al usuario.

    @DeleteMapping("/{id}")
    @PreAuthorize("@autorizacion.esNotificacionPropiaOAdministrador(#id)")
    public ResponseEntity<?> eliminarNotificacion(
            @PathVariable("id")
            @P("id") Integer id) {
        try {
            notificacionService.eliminar(id);
            return ResponseEntity.ok(
                    "Notificación eliminada correctamente."
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    // Elimina todas las notificaciones pertenecientes a un usuario.

    @DeleteMapping("/usuario/{idUsuario}")
    @PreAuthorize("@autorizacion.esUsuarioPropioOAdministrador(#idUsuario)")
    public ResponseEntity<?> eliminarTodas(
            @PathVariable("idUsuario")
            @P("idUsuario") Integer idUsuario) {
        try {
            notificacionService.eliminarTodas(idUsuario);
            return ResponseEntity.ok(
                    "Todas las notificaciones fueron eliminadas correctamente."
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }
}