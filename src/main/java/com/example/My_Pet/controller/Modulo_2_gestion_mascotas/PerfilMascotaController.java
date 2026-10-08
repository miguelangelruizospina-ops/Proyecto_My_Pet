package com.example.My_Pet.controller.Modulo_2_gestion_mascotas;

// Clases propias del proyecto.

import com.example.My_Pet.model.Modulo_2_gestion_mascota.PerfilMascota;
import com.example.My_Pet.service.Modulo_2_gestion_mascotas.PerfilMascotaService;

// Librerías de Spring.

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Librerías de Java.

import java.util.List;

// Define el controlador REST de perfiles de mascotas.

@RestController
@RequestMapping("/api/perfiles-mascotas")
public class PerfilMascotaController {

    // Conecta el controlador con el servicio de perfiles.

    @Autowired
    private PerfilMascotaService perfilMascotaService;


   //Lista todos los perfiles de mascotas existentes. ***SOLO PARA ADMINISTRADORES***

    @GetMapping("/listar")
    @PreAuthorize("@autorizacion.esAdministrador()")
    public List<PerfilMascota> listarPerfiles() {
        return perfilMascotaService.obtenerTodos();
    }


    // BUsca el perfil de una mascota existente por su ID. ***SOLO PARA EL ADMINISTRADOR O EL USUARIO PROPIETARIO DE LA MASCOTA***

    @GetMapping("/{id}")
    @PreAuthorize("@autorizacion.esPerfilMascotaPropioOAdministrador(#p0)")
    public ResponseEntity<PerfilMascota> obtenerPorId(
            @PathVariable("id") Integer id) {
        try {
            return ResponseEntity.ok(
                    perfilMascotaService.obtenerPorId(id)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    //  Busca el perfil asociado a una mascota existente por su ID. ***SOLO PARA EL ADMINISTRADOR O EL USUARIO PROPIETARIO DE LA MASCOTA***

    @GetMapping("/mascota/{idMascota}")
    @PreAuthorize("@autorizacion.esMascotaPropiaOAdministrador(#p0)")
    public ResponseEntity<PerfilMascota> obtenerPorMascota(
            @PathVariable("idMascota") Integer idMascota) {
        try {
            return ResponseEntity.ok(
                    perfilMascotaService.obtenerPorMascota(idMascota)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Registra un nuevo perfil de mascota de una mascota creada.

    @PostMapping("/guardar")
    @PreAuthorize("@autorizacion.esMascotaPropiaOAdministrador(#p0.mascota?.idMascota)")
    public ResponseEntity<?> crearPerfil(
            @RequestBody PerfilMascota perfil) {
        try {
            PerfilMascota nuevoPerfil =
                    perfilMascotaService.guardar(perfil);
            return ResponseEntity.ok(nuevoPerfil);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // PActualiza los datos del  perfil de mascota existente.

    @PutMapping("/actualizar/{id}")
    @PreAuthorize("@autorizacion.esPerfilMascotaPropioOAdministrador(#p0)")
    public ResponseEntity<?> actualizarPerfil(
            @PathVariable("id") Integer id,
            @RequestBody PerfilMascota perfil) {
        try {
            PerfilMascota perfilActualizado =
                    perfilMascotaService.actualizar(id, perfil);
            return ResponseEntity.ok(perfilActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Elimina un perfil de mascota  existente por su ID.

    @DeleteMapping("/eliminar/{id}")
    @PreAuthorize("@autorizacion.esPerfilMascotaPropioOAdministrador(#p0)")
    public ResponseEntity<String> eliminarPerfil(
            @PathVariable("id") Integer id) {
        try {
            perfilMascotaService.eliminar(id);
            return ResponseEntity.ok(
                    "Perfil de mascota eliminado correctamente"
            );
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}