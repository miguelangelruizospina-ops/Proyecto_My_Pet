package com.example.My_Pet.controller.Modulo_1_gestion_usuario;

// Clases propias del proyecto.

import com.example.My_Pet.model.Modulo_1_gestion_usuario.Administrador;
import com.example.My_Pet.service.Modulo_1_gestion_usuario.AdministradorService;

// Librerías de Spring.

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Librerías de Java.

import java.util.List;

// Controlador REST para gestionar la información de los administradores.

@RestController
@RequestMapping("/api/administradores")
public class AdministradorController {

    // Servicio encargado de gestionar los administradores.

    @Autowired
    private AdministradorService administradorService;

    // Lista todos los administradores.

    @GetMapping("/listar")
    public List<Administrador> listarAdministradores() {

        return administradorService.obtenerTodos();
    }

    // Busca un administrador por su ID.

    @GetMapping("/{id}")
    public ResponseEntity<Administrador> obtenerPorId(
            @PathVariable Integer id) {

        try {

            return ResponseEntity.ok(
                    administradorService.obtenerPorId(id)
            );

        } catch (RuntimeException e) {

            return ResponseEntity.notFound().build();
        }
    }

    // Busca el administrador asociado a un usuario.

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<Administrador> obtenerPorUsuario(
            @PathVariable Integer idUsuario) {

        try {

            return ResponseEntity.ok(
                    administradorService.obtenerPorUsuario(idUsuario)
            );

        } catch (RuntimeException e) {

            return ResponseEntity.notFound().build();
        }
    }

    // Registra un nuevo administrador.

    @PostMapping("/crear")
    public ResponseEntity<?> crearAdministrador(
            @RequestBody Administrador administrador) {

        try {

            Administrador nuevoAdministrador =
                    administradorService.guardar(administrador);

            return ResponseEntity.ok(nuevoAdministrador);

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    // Actualiza los datos de un administrador.

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizarAdministrador(
            @PathVariable Integer id,
            @RequestBody Administrador administrador) {

        try {

            Administrador administradorActualizado =
                    administradorService.actualizar(
                            id,
                            administrador
                    );

            return ResponseEntity.ok(administradorActualizado);

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());

        } catch (RuntimeException e) {

            return ResponseEntity
                    .notFound()
                    .build();
        }
    }

    // Elimina un administrador por su ID.

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminarAdministrador(
            @PathVariable Integer id) {

        try {

            administradorService.eliminar(id);

            return ResponseEntity.ok(
                    "Administrador eliminado correctamente"
            );

        } catch (RuntimeException e) {

            return ResponseEntity
                    .notFound()
                    .build();
        }
    }
}