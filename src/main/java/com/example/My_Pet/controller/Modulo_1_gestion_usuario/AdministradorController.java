// Paquete donde se encuentra el controlador.
package com.example.My_Pet.controller.Modulo_1_gestion_usuario;

// Librerías necesarias para el controlador.
import com.example.My_Pet.model.Modulo_1_gestion_usuario.Administrador;
import com.example.My_Pet.service.Modulo_1_gestion_usuario.AdministradorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

// Controlador para la gestión de administradores.
@RestController
@RequestMapping("/api/administradores")
public class AdministradorController {

    // Conecta el controlador con la lógica del servicio.
    @Autowired
    private AdministradorService administradorService;

    // GET - Lista todos los administradores.
    @GetMapping("/listar")
    public List<Administrador> listarAdministradores() {
        return administradorService.obtenerTodos();
    }

    // GET - Busca un administrador por su ID.
    @GetMapping("/{id}")
    public ResponseEntity<Administrador> obtenerPorId(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(administradorService.obtenerPorId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // GET - Busca el administrador asociado a un usuario.
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<Administrador> obtenerPorUsuario(@PathVariable Integer idUsuario) {
        try {
            return ResponseEntity.ok(administradorService.obtenerPorUsuario(idUsuario));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // POST - Registra un nuevo administrador.
    @PostMapping("/crear")
    public ResponseEntity<?> crearAdministrador(@RequestBody Administrador administrador) {
        try {
            Administrador nuevoAdministrador = administradorService.guardar(administrador);
            return ResponseEntity.ok(nuevoAdministrador);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // PUT - Actualiza los datos de un administrador.
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizarAdministrador(
            @PathVariable Integer id,
            @RequestBody Administrador administrador) {
        try {
            Administrador administradorActualizado = administradorService.actualizar(id, administrador);
            return ResponseEntity.ok(administradorActualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE - Elimina un administrador por su ID.
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminarAdministrador(@PathVariable Integer id) {
        try {
            administradorService.eliminar(id);
            return ResponseEntity.ok("Administrador eliminado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

}