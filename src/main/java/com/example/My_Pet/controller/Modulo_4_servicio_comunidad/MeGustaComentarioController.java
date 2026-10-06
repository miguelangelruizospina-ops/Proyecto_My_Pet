package com.example.My_Pet.controller.Modulo_4_servicio_comunidad;

// Importa el servicio encargado de gestionar los Me gusta de los comentarios
import com.example.My_Pet.service.Modulo_4_servicio_comunidad.MeGustaComentarioService;

// Importa el objeto que representa al usuario autenticado
import com.example.My_Pet.security.UsuarioPrincipal;

// Inyecciones y configuración de Spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

// Seguridad de Spring
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

// Anotaciones para crear el controlador REST
import org.springframework.web.bind.annotation.*;

// Permite devolver datos en formato clave-valor
import java.util.Map;

@RestController
@RequestMapping("/api/me-gusta/comentarios")
public class MeGustaComentarioController {

    // Servicio que contiene la lógica para agregar,
    // quitar y consultar los Me gusta de los comentarios
    @Autowired
    private MeGustaComentarioService meGustaService;


    // =========================================================
    // CAMBIAR ME GUSTA
    // =========================================================

    // Endpoint utilizado cuando el usuario presiona el corazón.
    //
    // Si el usuario todavía no ha dado Me gusta:
    //     → se agrega.
    //
    // Si ya había dado Me gusta:
    //     → se elimina.
    //
    // El usuario debe estar autenticado.
    @PostMapping("/{idComentario}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> cambiarMeGusta(

            // Se especifica explícitamente "idComentario"
            // porque el proyecto no utiliza la información de
            // nombres de parámetros disponible mediante reflexión.
            @PathVariable("idComentario") Integer idComentario) {

        // Obtiene el usuario actualmente autenticado
        UsuarioPrincipal usuarioPrincipal =
                obtenerUsuarioAutenticado();

        // Si no se pudo identificar al usuario,
        // devuelve error 401.
        if (usuarioPrincipal == null) {
            return ResponseEntity.status(401)
                    .body(
                            "No se pudo identificar al usuario autenticado."
                    );
        }

        try {

            // Obtiene el ID del usuario autenticado
            Integer idUsuario =
                    usuarioPrincipal.idUsuario();

            // Cambia el estado del Me gusta:
            // true  = se agregó
            // false = se quitó
            boolean dado =
                    meGustaService.cambiarMeGusta(
                            idUsuario,
                            idComentario
                    );

            // Consulta nuevamente la cantidad total
            // de Me gusta que tiene el comentario.
            long cantidad =
                    meGustaService.contarMeGustas(
                            idComentario
                    );

            // Devuelve al JavaScript:
            // {
            //     "meGusta": true/false,
            //     "cantidad": número
            // }
            return ResponseEntity.ok(
                    Map.of(
                            "meGusta", dado,
                            "cantidad", cantidad
                    )
            );

        } catch (IllegalArgumentException e) {

            // Devuelve el mensaje generado por el servicio
            // cuando el comentario o usuario no existe,
            // o cuando existe algún dato inválido.
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }


    // =========================================================
    // CONTAR ME GUSTAS
    // =========================================================

    // Endpoint utilizado para consultar cuántos Me gusta
    // tiene actualmente un comentario.
    @GetMapping("/{idComentario}/cantidad")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> contarMeGustas(

            // Se especifica explícitamente el nombre
            // del parámetro de la URL.
            @PathVariable("idComentario") Integer idComentario) {

        try {

            // Consulta la cantidad de Me gusta
            // asociados al comentario.
            long cantidad =
                    meGustaService.contarMeGustas(
                            idComentario
                    );

            // Devuelve:
            // {
            //     "cantidad": número
            // }
            return ResponseEntity.ok(
                    Map.of("cantidad", cantidad)
            );

        } catch (IllegalArgumentException e) {

            // Si el comentario no existe,
            // devuelve un error 400.
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }


    // =========================================================
    // VERIFICAR SI EL USUARIO DIO ME GUSTA
    // =========================================================

    // Endpoint utilizado para saber si el usuario actualmente
    // autenticado ya le dio Me gusta a determinado comentario.
    //
    // Esto permite que el corazón aparezca:
    //
    // vacío   → si todavía no ha dado Me gusta.
    //
    // lleno   → si ya dio Me gusta.
    @GetMapping("/{idComentario}/mi-me-gusta")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> verificarMeGusta(

            // Se especifica explícitamente el nombre
            // del parámetro de la URL.
            @PathVariable("idComentario") Integer idComentario) {

        // Obtiene el usuario autenticado
        UsuarioPrincipal usuarioPrincipal =
                obtenerUsuarioAutenticado();

        // Si no existe una sesión válida,
        // devuelve error 401.
        if (usuarioPrincipal == null) {
            return ResponseEntity.status(401)
                    .body(
                            "No se pudo identificar al usuario autenticado."
                    );
        }

        try {

            // Obtiene el ID del usuario autenticado
            Integer idUsuario =
                    usuarioPrincipal.idUsuario();

            // Consulta si ese usuario ya dio Me gusta
            // al comentario indicado.
            boolean meGusta =
                    meGustaService.usuarioDioMeGusta(
                            idUsuario,
                            idComentario
                    );

            // Devuelve:
            // {
            //     "meGusta": true/false
            // }
            return ResponseEntity.ok(
                    Map.of("meGusta", meGusta)
            );

        } catch (IllegalArgumentException e) {

            // Devuelve el mensaje del servicio
            // cuando el comentario no existe.
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }


    // =========================================================
    // OBTENER USUARIO AUTENTICADO
    // =========================================================

    // Obtiene el usuario que inició sesión desde
    // el SecurityContext de Spring Security.
    private UsuarioPrincipal obtenerUsuarioAutenticado() {

        // Obtiene la autenticación almacenada
        // para la solicitud actual.
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        // Si no existe autenticación,
        // no hay usuario identificado.
        if (authentication == null) {
            return null;
        }

        // Obtiene el principal almacenado
        // durante el inicio de sesión.
        Object principal =
                authentication.getPrincipal();

        // Comprueba que el principal sea nuestro
        // UsuarioPrincipal.
        if (principal instanceof UsuarioPrincipal usuarioPrincipal) {
            return usuarioPrincipal;
        }

        // Si no corresponde a UsuarioPrincipal,
        // no se puede identificar al usuario.
        return null;
    }
}