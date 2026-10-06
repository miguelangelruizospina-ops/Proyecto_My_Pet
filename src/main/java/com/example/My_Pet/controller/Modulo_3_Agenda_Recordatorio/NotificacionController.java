// Controlador encargado de gestionar las notificaciones.

package com.example.My_Pet.controller.Modulo_3_Agenda_Recordatorio;

import com.example.My_Pet.model.Modulo_3_Agenda_Recordatorio.Notificacion;

import com.example.My_Pet.service.Modulo_3_Agenda_Recordatorio.NotificacionService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.security.core.parameters.P;

import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controlador REST para las operaciones de notificaciones.

@RestController

@RequestMapping("/api/notificaciones")

public class NotificacionController {

    @Autowired

    private NotificacionService notificacionService;


    // =========================================================
    // CONSULTA DE NOTIFICACIONES
    // =========================================================

    // Obtiene todas las notificaciones del sistema.

    @GetMapping("/listar")

    @PreAuthorize("@autorizacion.esAdministrador()")

    public List<Notificacion> listarTodo() {

        return notificacionService.obtenerTodas();

    }


    // Obtiene las notificaciones pertenecientes a un usuario.

    @GetMapping("/usuario/{idUsuario}")

    @PreAuthorize("@autorizacion.esUsuarioPropioOAdministrador(#idUsuario)")

    public ResponseEntity<List<Notificacion>> listarPorUsuario(

            @PathVariable("idUsuario")

            @P("idUsuario") Integer idUsuario) {

        List<Notificacion> notificaciones =

                notificacionService.obtenerPorUsuario(idUsuario);

        return ResponseEntity.ok(notificaciones);

    }


    // =========================================================
    // CREACIÓN DE NOTIFICACIONES
    // =========================================================

    // Crea una nueva notificación para un usuario.

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


    // =========================================================
    // GESTIÓN DEL ESTADO DE LAS NOTIFICACIONES
    // =========================================================

    // Marca una notificación específica como leída.

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


    // =========================================================
    // ELIMINACIÓN DE NOTIFICACIONES
    // =========================================================

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