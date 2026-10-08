package com.example.My_Pet.controller.Modulo_4_servicio_comunidad;

//clases del proyecto

import com.example.My_Pet.security.UsuarioPrincipal;
import com.example.My_Pet.service.Modulo_4_servicio_comunidad.MeGustaPublicacionService;

//librerias del spring

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

//Librerias de Java

import java.util.Map;

// Controlador REST para gestionar los "Me gusta" de las publicaciones del foro (no comentarios).

@RestController
@RequestMapping("/api/me-gusta/publicaciones")
public class MeGustaPublicacionController {

    // Servicio encargado de agregar, quitar y consultar los Me gusta de las publicaciones del foro (no comentarios).

    @Autowired
    private MeGustaPublicacionService meGustaService;

// Agrega o quita el "Me gusta" de una publicación del foro (no comentarios) y devuelve la cantidad actual.

    @PostMapping("/{idPublicacion}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> cambiarMeGusta(
            @PathVariable("idPublicacion") Integer idPublicacion) {

        // Obtiene la autenticacion del usuario que inició sesión.

        UsuarioPrincipal usuarioPrincipal =
                obtenerUsuarioAutenticado();
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
                            idPublicacion
                    );

            // Obtiene la cantidad actual de Me gusta.

            long cantidad =
                    meGustaService.contarMeGustas(
                            idPublicacion
                    );

            // Devuelve al JavaScript el estado  y la cantidad actual.

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

    // Consulta únicamente el total de "Me gusta" de una publicación en el foro (no comentarios).

    @GetMapping("/{idPublicacion}/cantidad")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> contarMeGustas(
            @PathVariable("idPublicacion") Integer idPublicacion) {
        try {
            long cantidad =
                    meGustaService.contarMeGustas(
                            idPublicacion
                    );
            return ResponseEntity.ok(
                    Map.of(
                            "cantidad",
                            cantidad
                    )
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }

    // Permite saber si el usuario actual ya dio Me gusta  a una publicación determinada en el foro.

    @GetMapping("/{idPublicacion}/mi-me-gusta")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> verificarMeGusta(
            @PathVariable("idPublicacion") Integer idPublicacion) {
        UsuarioPrincipal usuarioPrincipal =
                obtenerUsuarioAutenticado();
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
                            idPublicacion
                    );
            return ResponseEntity.ok(
                    Map.of(
                            "meGusta",
                            meGusta
                    )
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }

    // Obtiene el UsuarioPrincipal almacenado  en la sesión de Spring Security.

    private UsuarioPrincipal obtenerUsuarioAutenticado() {

        // Obtiene la autenticación actual.

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