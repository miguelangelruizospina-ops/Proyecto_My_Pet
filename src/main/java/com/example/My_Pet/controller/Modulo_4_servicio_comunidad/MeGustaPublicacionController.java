package com.example.My_Pet.controller.Modulo_4_servicio_comunidad;

import com.example.My_Pet.security.UsuarioPrincipal;
import com.example.My_Pet.service.Modulo_4_servicio_comunidad.MeGustaPublicacionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/me-gusta/publicaciones")
public class MeGustaPublicacionController {

    // =========================================================
    // SERVICIO
    // =========================================================

    // Servicio encargado de agregar, quitar y consultar
    // los Me gusta de las publicaciones.
    @Autowired
    private MeGustaPublicacionService meGustaService;


    // =========================================================
    // CAMBIAR ME GUSTA
    // =========================================================

    // Agrega o elimina el Me gusta del usuario actual.
    //
    // Si el usuario no ha dado Me gusta:
    //     → se agrega.
    //
    // Si ya había dado Me gusta:
    //     → se elimina.
    @PostMapping("/{idPublicacion}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> cambiarMeGusta(

            // Se especifica explícitamente el nombre del parámetro
            // para evitar problemas de reflexión con Spring.
            @PathVariable("idPublicacion") Integer idPublicacion) {

        // Obtiene el usuario que inició sesión.
        UsuarioPrincipal usuarioPrincipal =
                obtenerUsuarioAutenticado();

        // Si no existe un usuario autenticado,
        // devuelve código 401.
        if (usuarioPrincipal == null) {
            return ResponseEntity.status(401)
                    .body(
                            "No se pudo identificar al usuario autenticado."
                    );
        }

        try {

            // Obtiene el ID del usuario autenticado.
            Integer idUsuario =
                    usuarioPrincipal.idUsuario();

            // Cambia el estado del Me gusta.
            //
            // true  = se agregó.
            // false = se eliminó.
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

            // Devuelve al JavaScript el estado
            // y la cantidad actual.
            return ResponseEntity.ok(
                    Map.of(
                            "meGusta", dado,
                            "cantidad", cantidad
                    )
            );

        } catch (IllegalArgumentException e) {

            // Devuelve los errores generados por el servicio.
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }


    // =========================================================
    // CONTAR ME GUSTAS
    // =========================================================

    // Consulta cuántos Me gusta tiene una publicación.
    @GetMapping("/{idPublicacion}/cantidad")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> contarMeGustas(

            // Nombre explícito del parámetro de la URL.
            @PathVariable("idPublicacion") Integer idPublicacion) {

        try {

            // Consulta la cantidad de Me gusta.
            long cantidad =
                    meGustaService.contarMeGustas(
                            idPublicacion
                    );

            // Devuelve la cantidad al frontend.
            return ResponseEntity.ok(
                    Map.of(
                            "cantidad",
                            cantidad
                    )
            );

        } catch (IllegalArgumentException e) {

            // Si la publicación no existe,
            // devuelve un error 400.
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }


    // =========================================================
    // VERIFICAR ME GUSTA DEL USUARIO
    // =========================================================

    // Permite saber si el usuario actual ya dio Me gusta
    // a una publicación determinada.
    @GetMapping("/{idPublicacion}/mi-me-gusta")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> verificarMeGusta(

            // Nombre explícito del parámetro de la URL.
            @PathVariable("idPublicacion") Integer idPublicacion) {

        // Obtiene el usuario autenticado.
        UsuarioPrincipal usuarioPrincipal =
                obtenerUsuarioAutenticado();

        // Si no existe usuario autenticado,
        // devuelve código 401.
        if (usuarioPrincipal == null) {
            return ResponseEntity.status(401)
                    .body(
                            "No se pudo identificar al usuario autenticado."
                    );
        }

        try {

            // Obtiene el ID del usuario.
            Integer idUsuario =
                    usuarioPrincipal.idUsuario();

            // Consulta si ese usuario ya dio Me gusta.
            boolean meGusta =
                    meGustaService.usuarioDioMeGusta(
                            idUsuario,
                            idPublicacion
                    );

            // Devuelve true o false al frontend.
            return ResponseEntity.ok(
                    Map.of(
                            "meGusta",
                            meGusta
                    )
            );

        } catch (IllegalArgumentException e) {

            // Devuelve el mensaje del servicio
            // en caso de error.
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }


    // =========================================================
    // OBTENER USUARIO AUTENTICADO
    // =========================================================

    // Obtiene el UsuarioPrincipal almacenado
    // en la sesión de Spring Security.
    private UsuarioPrincipal obtenerUsuarioAutenticado() {

        // Obtiene la autenticación actual.
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        // Si no existe autenticación,
        // devuelve null.
        if (authentication == null) {
            return null;
        }

        // Obtiene el principal de Spring Security.
        Object principal =
                authentication.getPrincipal();

        // Comprueba que sea nuestro UsuarioPrincipal.
        if (principal instanceof UsuarioPrincipal usuarioPrincipal) {
            return usuarioPrincipal;
        }

        // Si no corresponde al tipo esperado,
        // devuelve null.
        return null;
    }
}