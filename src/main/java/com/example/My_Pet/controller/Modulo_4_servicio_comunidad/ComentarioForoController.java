package com.example.My_Pet.controller.Modulo_4_servicio_comunidad;

import com.example.My_Pet.model.Modulo_4_servicio_comunidad.ComentarioForo;
import com.example.My_Pet.security.UsuarioPrincipal;
import com.example.My_Pet.service.Modulo_4_servicio_comunidad.ComentarioForoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comentarios-foro")
public class ComentarioForoController {

    @Autowired
    private ComentarioForoService comentarioService;

    // ==========================================
    // LISTAR COMENTARIOS DE UNA PUBLICACIÓN
    // ==========================================

    @GetMapping("/publicacion/{idPublicacion}")
    public ResponseEntity<?> listarPorPublicacion(
            @PathVariable("idPublicacion") Integer idPublicacion) {

        try {

            List<ComentarioForo> comentarios =
                    comentarioService.obtenerPorPublicacion(
                            idPublicacion
                    );

            return ResponseEntity.ok(comentarios);

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    // ==========================================
    // CREAR COMENTARIO
    // ==========================================

    @PostMapping("/publicacion/{idPublicacion}")
    public ResponseEntity<?> crearComentario(
            @PathVariable("idPublicacion") Integer idPublicacion,
            @RequestBody Map<String, String> datos) {

        UsuarioPrincipal usuarioPrincipal =
                obtenerUsuarioAutenticado();

        if (usuarioPrincipal == null) {

            return ResponseEntity
                    .status(401)
                    .body(
                            "No se pudo identificar al usuario autenticado."
                    );
        }

        try {

            Integer idUsuario =
                    usuarioPrincipal.idUsuario();

            String contenido =
                    datos.get("contenido");

            ComentarioForo comentario =
                    comentarioService.guardar(
                            idUsuario,
                            idPublicacion,
                            contenido
                    );

            return ResponseEntity.ok(comentario);

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    // ==========================================
    // CREAR RESPUESTA
    // ==========================================

    @PostMapping("/{idComentario}/responder")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> responderComentario(
            @PathVariable("idComentario") Integer idComentario,
            @RequestBody Map<String, String> datos) {

        UsuarioPrincipal usuarioPrincipal =
                obtenerUsuarioAutenticado();

        if (usuarioPrincipal == null) {

            return ResponseEntity
                    .status(401)
                    .body(
                            "No se pudo identificar al usuario autenticado."
                    );
        }

        try {

            Integer idUsuario =
                    usuarioPrincipal.idUsuario();

            String contenido =
                    datos.get("contenido");

            ComentarioForo respuesta =
                    comentarioService.responder(
                            idUsuario,
                            idComentario,
                            contenido
                    );

            return ResponseEntity.ok(respuesta);

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    // ==========================================
    // LISTAR RESPUESTAS
    // ==========================================

    @GetMapping("/{idComentario}/respuestas")
    public ResponseEntity<?> listarRespuestas(
            @PathVariable("idComentario") Integer idComentario) {

        try {

            List<ComentarioForo> respuestas =
                    comentarioService.obtenerRespuestas(
                            idComentario
                    );

            return ResponseEntity.ok(respuestas);

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    // ==========================================
    // ELIMINAR COMENTARIO
    // ==========================================

    @DeleteMapping("/{idComentario}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> eliminarComentario(
            @PathVariable("idComentario") Integer idComentario) {

        try {

            comentarioService.eliminar(
                    idComentario
            );

            return ResponseEntity.ok(
                    "Comentario eliminado correctamente."
            );

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    // ==========================================
    // OBTENER USUARIO AUTENTICADO
    // ==========================================

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