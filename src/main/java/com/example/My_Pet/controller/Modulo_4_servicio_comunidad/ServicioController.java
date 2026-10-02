// Controlador para la gestión de servicios.
package com.example.My_Pet.controller.Modulo_4_servicio_comunidad;

// Librerías necesarias para el controlador y la seguridad.
import com.example.My_Pet.model.Modulo_4_servicio_comunidad.Servicio;
import com.example.My_Pet.service.Modulo_4_servicio_comunidad.ServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

// Define el controlador REST de servicios.
@RestController
@RequestMapping("/api/servicios")
public class ServicioController {

    // Conecta el controlador con el servicio de servicios.
    @Autowired
    private ServicioService servicioService;

    // GET - Lista todos los servicios.
    @GetMapping("/listar")
    public List<Servicio> listarTodo() {
        return servicioService.obtenerTodos();
    }

    // GET - Lista los servicios según su tipo.
    @GetMapping("/tipo/{tipo}")
    public List<Servicio> listarPorTipo(@PathVariable String tipo) {
        return servicioService.obtenerPorTipo(tipo);
    }

    // GET - Busca un servicio por su ID.
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        try {
            Servicio servicio = servicioService.obtenerPorId(id);
            return ResponseEntity.ok(servicio);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // POST - Registra un nuevo servicio.
    @PostMapping("/guardar")
    @PreAuthorize("@autorizacion.esUsuarioPropioOAdministrador(#p0.usuario?.idUsuario)")
    public ResponseEntity<?> crearServicio(@RequestBody Servicio servicio) {
        try {
            Servicio nuevoServicio = servicioService.guardar(servicio);
            return ResponseEntity.ok(nuevoServicio);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // PUT - Actualiza un servicio existente.
    @PutMapping("/{id}")
    @PreAuthorize("@autorizacion.esServicioPropioOAdministrador(#p0)")
    public ResponseEntity<?> actualizarServicio(
            @PathVariable Integer id,
            @RequestBody Servicio servicio) {
        try {
            Servicio servicioActualizado = servicioService.actualizar(id, servicio);
            return ResponseEntity.ok(servicioActualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // DELETE - Elimina un servicio por su ID.
    @DeleteMapping("/{id}")
    @PreAuthorize("@autorizacion.esServicioPropioOAdministrador(#p0)")
    public ResponseEntity<?> eliminarServicio(@PathVariable Integer id) {
        try {
            servicioService.eliminar(id);
            return ResponseEntity.ok("Servicio eliminado correctamente.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}