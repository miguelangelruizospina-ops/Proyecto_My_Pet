 // Controlador para la gestión de perfiles de mascotas.
package com.example.My_Pet.controller.Modulo_2_gestion_mascotas;

// Librerías necesarias para el controlador y la seguridad.
import com.example.My_Pet.model.Modulo_2_gestion_mascota.PerfilMascota;
import com.example.My_Pet.service.Modulo_2_gestion_mascotas.PerfilMascotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

// Define el controlador REST de perfiles de mascotas.
@RestController
@RequestMapping("/api/perfiles-mascotas")
public class PerfilMascotaController {

    // Conecta el controlador con el servicio de perfiles.
    @Autowired
    private PerfilMascotaService perfilMascotaService;

    // GET - Lista todos los perfiles. Solo para administradores.
    @GetMapping("/listar")
    @PreAuthorize("@autorizacion.esAdministrador()")
    public List<PerfilMascota> listarPerfiles() {
        return perfilMascotaService.obtenerTodos();
    }

    // GET - Busca un perfil por su ID.
    @GetMapping("/{id}")
    @PreAuthorize("@autorizacion.esPerfilMascotaPropioOAdministrador(#p0)")
    public ResponseEntity<PerfilMascota> obtenerPorId(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(perfilMascotaService.obtenerPorId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // GET - Busca el perfil asociado a una mascota.
    @GetMapping("/mascota/{idMascota}")
    @PreAuthorize("@autorizacion.esMascotaPropiaOAdministrador(#p0)")
    public ResponseEntity<PerfilMascota> obtenerPorMascota(@PathVariable Integer idMascota) {
        try {
            return ResponseEntity.ok(perfilMascotaService.obtenerPorMascota(idMascota));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // POST - Registra un nuevo perfil de mascota.
    @PostMapping("/guardar")
    @PreAuthorize("@autorizacion.esMascotaPropiaOAdministrador(#p0.mascota?.idMascota)")
    public ResponseEntity<?> crearPerfil(@RequestBody PerfilMascota perfil) {
        try {
            PerfilMascota nuevoPerfil = perfilMascotaService.guardar(perfil);
            return ResponseEntity.ok(nuevoPerfil);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // PUT - Actualiza un perfil de mascota existente.
    @PutMapping("/actualizar/{id}")
    @PreAuthorize("@autorizacion.esPerfilMascotaPropioOAdministrador(#p0)")
    public ResponseEntity<?> actualizarPerfil(
            @PathVariable Integer id,
            @RequestBody PerfilMascota perfil) {
        try {
            PerfilMascota perfilActualizado = perfilMascotaService.actualizar(id, perfil);
            return ResponseEntity.ok(perfilActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE - Elimina un perfil de mascota por su ID.
    @DeleteMapping("/eliminar/{id}")
    @PreAuthorize("@autorizacion.esPerfilMascotaPropioOAdministrador(#p0)")
    public ResponseEntity<String> eliminarPerfil(@PathVariable Integer id) {
        try {
            perfilMascotaService.eliminar(id);
            return ResponseEntity.ok("Perfil de mascota eliminado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

}