
// Controlador para la gestión de emergencias.

package com.example.My_Pet.controller.Modulo_5_emergencia;

// Librerías necesarias para el controlador y la seguridad.

import com.example.My_Pet.model.Modulo_5_emergencia.Emergencia;

import com.example.My_Pet.service.Modulo_5_emergencia.EmergenciaService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

import java.util.List;

// Define el controlador REST de emergencias.

@RestController

@RequestMapping("/api/emergencias")

public class EmergenciaController {

    // Conecta el controlador con el servicio de emergencias.

    @Autowired

    private EmergenciaService emergenciaService;

    // GET - Lista todas las emergencias. Solo para administradores.

    @GetMapping("/listar")

    @PreAuthorize("@autorizacion.esAdministrador()")

    public List<Emergencia> listarTodo() {

        return emergenciaService.obtenerTodas();

    }

    // GET - Lista las emergencias de un usuario.

    @GetMapping("/usuario/{idUsuario}")

    @PreAuthorize("@autorizacion.esUsuarioPropioOAdministrador(#p0)")

    public List<Emergencia> listarPorUsuario(

            // Se especifica explícitamente el nombre de la variable de la URL.

            @PathVariable("idUsuario") Integer idUsuario) {

        return emergenciaService.obtenerPorUsuario(idUsuario);

    }

    // POST - Registra una nueva emergencia.

    @PostMapping("/guardar")

    @PreAuthorize("@autorizacion.esUsuarioPropioOAdministrador(#p0.usuario?.idUsuario)")

    public ResponseEntity<?> crearEmergencia(

            @RequestBody Emergencia emergencia) {

        try {

            Emergencia nuevaEmergencia =

                    emergenciaService.guardar(emergencia);

            return ResponseEntity.ok(nuevaEmergencia);

        } catch (IllegalArgumentException e) {

            return ResponseEntity.badRequest().body(e.getMessage());

        }

    }

    // PUT - Marca una emergencia como resuelta.

    @PutMapping("/resolver/{id}")

    @PreAuthorize("@autorizacion.esEmergenciaPropiaOAdministrador(#p0)")

    public ResponseEntity<?> resolverEmergencia(

            // Se especifica explícitamente el nombre de la variable de la URL.

            @PathVariable("id") Integer id) {

        try {

            Emergencia emergenciaResuelta =

                    emergenciaService.resolver(id);

            return ResponseEntity.ok(emergenciaResuelta);

        } catch (IllegalArgumentException e) {

            return ResponseEntity.badRequest().body(e.getMessage());

        }

    }

    // DELETE - Elimina una emergencia por su ID.

    @DeleteMapping("/eliminar/{id}")

    @PreAuthorize("@autorizacion.esEmergenciaPropiaOAdministrador(#p0)")

    public ResponseEntity<?> eliminarEmergencia(

            // Se especifica explícitamente el nombre de la variable de la URL.

            @PathVariable("id") Integer id) {

        try {

            emergenciaService.eliminar(id);

            return ResponseEntity.ok(

                    "Emergencia eliminada correctamente."

            );

        } catch (IllegalArgumentException e) {

            return ResponseEntity.badRequest().body(

                    e.getMessage()

            );

        }

    }

}
