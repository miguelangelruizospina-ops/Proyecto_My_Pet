package com.example.My_Pet.controller.Modulo_4_servicio_comunidad;

import com.example.My_Pet.model.Modulo_1_gestion_usuario.Usuario;
import com.example.My_Pet.model.Modulo_4_servicio_comunidad.PublicacionForo;
import com.example.My_Pet.security.UsuarioPrincipal;
import com.example.My_Pet.service.Modulo_4_servicio_comunidad.PublicacionForoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/publicaciones-foro")
public class PublicacionForoController {

    @Autowired
    private PublicacionForoService forumService;

    // ========================================================
    // LISTAR TODAS LAS PUBLICACIONES
    // ========================================================

    @GetMapping("/listar")
    public List<PublicacionForo> listarTodo() {
        return forumService.obtenerTodas();
    }

    // ========================================================
    // LISTAR PUBLICACIONES DE UN USUARIO
    // ========================================================

    @GetMapping("/usuario/{idUsuario}")
    @PreAuthorize("@autorizacion.esUsuarioPropioOAdministrador(#idUsuario)")
    public List<PublicacionForo> listarPorUsuario(
            @PathVariable("idUsuario") Integer idUsuario) {

        return forumService.obtenerPorUsuario(idUsuario);
    }

    // ========================================================
    // CREAR PUBLICACIÓN
    // ========================================================

    @PostMapping("/guardar")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> crearPublicacion(
            @RequestBody PublicacionForo publicacion) {

        try {

            UsuarioPrincipal usuarioPrincipal =
                    obtenerUsuarioAutenticado();

            if (usuarioPrincipal == null) {
                return ResponseEntity.status(401)
                        .body("No se pudo identificar al usuario autenticado.");
            }

            Integer idUsuario =
                    usuarioPrincipal.idUsuario();

            Usuario usuario = new Usuario();
            usuario.setIdUsuario(idUsuario);

            publicacion.setUsuario(usuario);

            PublicacionForo nuevaPublicacion =
                    forumService.guardar(publicacion);

            return ResponseEntity.ok(nuevaPublicacion);

        } catch (IllegalArgumentException e) {

            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }

    // ========================================================
    // EDITAR PUBLICACIÓN
    // ========================================================

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarPublicacion(
            @PathVariable("id") Integer id,
            @RequestBody PublicacionForo publicacion) {

        UsuarioPrincipal usuarioPrincipal =
                obtenerUsuarioAutenticado();

        if (usuarioPrincipal == null) {
            return ResponseEntity.status(401)
                    .body("No se pudo identificar al usuario autenticado.");
        }

        try {

            PublicacionForo actualizada =
                    forumService.actualizar(
                            id,
                            publicacion,
                            usuarioPrincipal
                    );

            return ResponseEntity.ok(actualizada);

        } catch (SecurityException e) {

            return ResponseEntity.status(403)
                    .body(e.getMessage());

        } catch (IllegalArgumentException e) {

            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }

    // ========================================================
    // ELIMINAR PUBLICACIÓN
    // ========================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarPublicacion(
            @PathVariable("id") Integer id) {

        UsuarioPrincipal usuarioPrincipal =
                obtenerUsuarioAutenticado();

        if (usuarioPrincipal == null) {
            return ResponseEntity.status(401)
                    .body("No se pudo identificar al usuario autenticado.");
        }

        try {

            forumService.eliminar(
                    id,
                    usuarioPrincipal
            );

            return ResponseEntity.ok(
                    "Publicación eliminada correctamente."
            );

        } catch (SecurityException e) {

            return ResponseEntity.status(403)
                    .body(e.getMessage());

        } catch (IllegalArgumentException e) {

            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }

    // ========================================================
    // OBTENER USUARIO AUTENTICADO
    // ========================================================

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