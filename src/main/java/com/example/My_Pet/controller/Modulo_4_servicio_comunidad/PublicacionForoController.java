package com.example.My_Pet.controller.Modulo_4_servicio_comunidad;

//clases del proyecto

import com.example.My_Pet.model.Modulo_1_gestion_usuario.Usuario;
import com.example.My_Pet.model.Modulo_4_servicio_comunidad.PublicacionForo;
import com.example.My_Pet.security.UsuarioPrincipal;
import com.example.My_Pet.service.Modulo_4_servicio_comunidad.PublicacionForoService;

//Librerias del spring.

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

//Librerias de Java

import java.util.List;

// Controlador REST para gestionar las publicaciones del foro.

@RestController
@RequestMapping("/api/publicaciones-foro")
public class PublicacionForoController {

 // Servicio encargado de gestionar las publicaciones del foro.

    @Autowired
    private PublicacionForoService forumService;

   //Lista todas las publicaciones (no comentarios) en el foro.

    @GetMapping("/listar")
    public List<PublicacionForo> listarTodo() {
        return forumService.obtenerTodas();
    }

    //Lista todas las publicaciones (no comentarios) del foro por su ID para el usuario y adminsitradores.

    @GetMapping("/usuario/{idUsuario}")
    @PreAuthorize("@autorizacion.esUsuarioPropioOAdministrador(#idUsuario)")
    public List<PublicacionForo> listarPorUsuario(
            @PathVariable("idUsuario") Integer idUsuario) {
        return forumService.obtenerPorUsuario(idUsuario);
    }

    
    // Crea una publicacion nueva de un usario existente.
   
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

    //Editar una  publicacion ya creada por el usuario existente

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

    // Elimina las publicaciones creadas  en el foro

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

    //Se obtiene la autenticacion del usuario que va a crear la publicacion en el foro.

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