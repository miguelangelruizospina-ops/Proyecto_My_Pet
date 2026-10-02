// Controlador para la gestión de publicaciones del foro.
package com.example.My_Pet.controller.Modulo_4_servicio_comunidad;

// Librerías necesarias para el controlador y la seguridad.
import com.example.My_Pet.model.Modulo_4_servicio_comunidad.PublicacionForo;
import com.example.My_Pet.service.Modulo_4_servicio_comunidad.PublicacionForoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

// Define el controlador REST de publicaciones del foro.
@RestController
@RequestMapping("/api/publicaciones-foro")
public class PublicacionForoController {

    // Conecta el controlador con el servicio de publicaciones.
    @Autowired
    private PublicacionForoService forumService;

    // GET - Lista todas las publicaciones.
    @GetMapping("/listar")
    public List<PublicacionForo> listarTodo() {
        return forumService.obtenerTodas();
    }

    // GET - Lista las publicaciones de un usuario.
    @GetMapping("/usuario/{idUsuario}")
    @PreAuthorize("@autorizacion.esUsuarioPropioOAdministrador(#p0)")
    public List<PublicacionForo> listarPorUsuario(@PathVariable Integer idUsuario) {
        return forumService.obtenerPorUsuario(idUsuario);
    }

    // POST - Registra una nueva publicación.
    @PostMapping("/guardar")
    @PreAuthorize("@autorizacion.esUsuarioPropioOAdministrador(#p0.usuario?.idUsuario)")
    public ResponseEntity<?> crearPublicacion(@RequestBody PublicacionForo publicacion) {
        try {
            PublicacionForo nuevaPublicacion = forumService.guardar(publicacion);
            return ResponseEntity.ok(nuevaPublicacion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // PUT - Actualiza una publicación existente.
    @PutMapping("/{id}")
    @PreAuthorize("@autorizacion.esPublicacionPropiaOAdministrador(#p0)")
    public ResponseEntity<?> actualizarPublicacion(
            @PathVariable Integer id,
            @RequestBody PublicacionForo publicacion) {
        try {
            PublicacionForo actualizada = forumService.actualizar(id, publicacion);
            return ResponseEntity.ok(actualizada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // DELETE - Elimina una publicación por su ID.
    @DeleteMapping("/{id}")
    @PreAuthorize("@autorizacion.esPublicacionPropiaOAdministrador(#p0)")
    public ResponseEntity<?> eliminarPublicacion(@PathVariable Integer id) {
        try {
            forumService.eliminar(id);
            return ResponseEntity.ok("Publicación eliminada correctamente.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}