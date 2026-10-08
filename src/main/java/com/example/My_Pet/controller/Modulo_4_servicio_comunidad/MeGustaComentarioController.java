package com.example.My_Pet.controller.Modulo_4_servicio_comunidad;

// Clases propias del proyecto.

import com.example.My_Pet.service.Modulo_4_servicio_comunidad.MeGustaComentarioService;
import com.example.My_Pet.security.UsuarioPrincipal;

// Librerías de Spring.

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

// Librerías de Java.

import java.util.Map;

// Controlador REST para gestionar los "Me gusta" de los comentarios.

@RestController
@RequestMapping("/api/me-gusta/comentarios")
public class MeGustaComentarioController {

    // Servicio encargado de gestionar los "Me gusta" de los comentarios.

    @Autowired
    private MeGustaComentarioService meGustaService;

    // Agrega o quita el "Me gusta" de un comentario y devuelve la cantidad actual.

    @PostMapping("/{idComentario}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> cambiarMeGusta(
            @PathVariable("idComentario") Integer idComentario) {
        UsuarioPrincipal usuarioPrincipal =
                obtenerUsuarioAutenticado();

        // Verifica que se pueda identificar al usuario que inició sesión.

        if (usuarioPrincipal == null) {
            return ResponseEntity.status(401)
                    .body(
                            "No se pudo identificar al usuario autenticado."
                    );
        }
        try {
            Integer idUsuario =
                    usuarioPrincipal.idUsuario();
            boolean dado =
                    meGustaService.cambiarMeGusta(
                            idUsuario,
                            idComentario
                    );
            long cantidad =
                    meGustaService.contarMeGustas(
                            idComentario
                    );
            return ResponseEntity.ok(
                    Map.of(
                            "meGusta", dado,
                            "cantidad", cantidad
                    )
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }

    // Consulta la cantidad de "Me gusta" de un comentario registrado en el foro.

    @GetMapping("/{idComentario}/cantidad")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> contarMeGustas(
            @PathVariable("idComentario") Integer idComentario) {
        try {
            long cantidad =
                    meGustaService.contarMeGustas(
                            idComentario
                    );
            return ResponseEntity.ok(
                    Map.of("cantidad", cantidad)
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }

    // Verifica si el usuario que inició sesión dio "Me gusta" a un comentario registrado en el foro.

    @GetMapping("/{idComentario}/mi-me-gusta")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> verificarMeGusta(
            @PathVariable("idComentario") Integer idComentario) {

        // Obtiene el usuario que inició sesión.

        UsuarioPrincipal usuarioPrincipal =
                obtenerUsuarioAutenticado();

        // Verifica que se pueda identificar al usuario que inició sesión.

        if (usuarioPrincipal == null) {
            return ResponseEntity.status(401)
                    .body(
                            "No se pudo identificar al usuario autenticado."
                    );
        }
        try {
            Integer idUsuario =
                    usuarioPrincipal.idUsuario();
            boolean meGusta =
                    meGustaService.usuarioDioMeGusta(
                            idUsuario,
                            idComentario
                    );
            return ResponseEntity.ok(
                    Map.of("meGusta", meGusta)
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }

    // Obtiene la autenticacion del usuario que inició sesión.

    private UsuarioPrincipal obtenerUsuarioAutenticado() {
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();
        if (authentication == null) {
            return null;
        }
        Object principal =
                authentication.getPrincipal();
        if (principal instanceof UsuarioPrincipal usuarioPrincipal) {
            return usuarioPrincipal;
        }
        return null;
    }
}