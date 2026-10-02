// Controlador para la gestión de notificaciones.
package com.example.My_Pet.controller.Modulo_3_Agenda_Recordatorio;

// Librerías necesarias para el controlador y la seguridad.
import com.example.My_Pet.model.Modulo_3_Agenda_Recordatorio.Notificacion;
import com.example.My_Pet.service.Modulo_3_Agenda_Recordatorio.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

// Define el controlador REST de notificaciones.
@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    // Conecta el controlador con el servicio de notificaciones.
    @Autowired
    private NotificacionService notificacionService;

    // GET - Lista todas las notificaciones. Solo para administradores.
    @GetMapping("/listar")
    @PreAuthorize("@autorizacion.esAdministrador()")
    public List<Notificacion> listarTodo() {
        return notificacionService.obtenerTodas();
    }

    // GET - Lista las notificaciones asociadas a un usuario.
    @GetMapping("/usuario/{idUsuario}")
    @PreAuthorize("@autorizacion.esUsuarioPropioOAdministrador(#p0)")
    public ResponseEntity<List<Notificacion>> listarPorUsuario(@PathVariable Integer idUsuario) {
        List<Notificacion> notificaciones = notificacionService.obtenerPorUsuario(idUsuario);
        return ResponseEntity.ok(notificaciones);
    }

    // POST - Crea una nueva notificación.
    @PostMapping("/crear")
    @PreAuthorize("@autorizacion.esUsuarioPropioOAdministrador(#p0)")
    public ResponseEntity<?> crear(
            @RequestParam Integer idUsuario,
            @RequestParam String mensaje) {
        try {
            Notificacion notificacion = notificacionService.crear(idUsuario, mensaje);
            return ResponseEntity.ok(notificacion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // PUT - Marca una notificación como leída.
    @PutMapping("/leer/{id}")
    @PreAuthorize("@autorizacion.esNotificacionPropiaOAdministrador(#p0)")
    public ResponseEntity<?> marcarComoLeida(@PathVariable Integer id) {
        try {
            Notificacion notificacion = notificacionService.marcarComoLeida(id);
            return ResponseEntity.ok(notificacion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // PUT - Marca todas las notificaciones de un usuario como leídas.
    @PutMapping("/leer-todas/{idUsuario}")
    @PreAuthorize("@autorizacion.esUsuarioPropioOAdministrador(#p0)")
    public ResponseEntity<?> marcarTodasComoLeidas(@PathVariable Integer idUsuario) {
        try {
            notificacionService.marcarTodasComoLeidas(idUsuario);
            return ResponseEntity.ok("Todas las notificaciones fueron marcadas como leídas.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // DELETE - Elimina una notificación por su ID.
    @DeleteMapping("/{id}")
    @PreAuthorize("@autorizacion.esNotificacionPropiaOAdministrador(#p0)")
    public ResponseEntity<?> eliminarNotificacion(@PathVariable Integer id) {
        try {
            notificacionService.eliminar(id);
            return ResponseEntity.ok("Notificación eliminada correctamente.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}