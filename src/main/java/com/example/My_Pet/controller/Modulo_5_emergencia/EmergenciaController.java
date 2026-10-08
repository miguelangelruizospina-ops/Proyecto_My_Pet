package com.example.My_Pet.controller.Modulo_5_emergencia;

// Clases del proyecto.

import com.example.My_Pet.model.Modulo_5_emergencia.Emergencia;
import com.example.My_Pet.service.Modulo_5_emergencia.EmergenciaService;

//Librerias del Spring.

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

//Librerias de Java

import java.util.List;

// Define el controlador REST de emergencias.

@RestController
@RequestMapping("/api/emergencias")
public class EmergenciaController {

    // Conecta el controlador con el servicio de emergencias.
    @Autowired
    private EmergenciaService emergenciaService;

    // Lista todas las emergencias ***SOLO PARA ADMINISTRADORES***.

    @GetMapping("/listar")
    @PreAuthorize("@autorizacion.esAdministrador()")
    public List<Emergencia> listarTodo() {
        return emergenciaService.obtenerTodas();
    }

    // ista las emergencias de un usuario registrado

    @GetMapping("/usuario/{idUsuario}")
    @PreAuthorize("@autorizacion.esUsuarioPropioOAdministrador(#p0)")
    public List<Emergencia> listarPorUsuario(
            @PathVariable("idUsuario") Integer idUsuario) {
        return emergenciaService.obtenerPorUsuario(idUsuario);
    }

    //  Registra una nueva emergencia de un usuario registrado.

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

    //Marca una emergencia como resuelta por el usuario y desactiva las notificaciones de emergencia.

    @PutMapping("/resolver/{id}")
    @PreAuthorize("@autorizacion.esEmergenciaPropiaOAdministrador(#p0)")
    public ResponseEntity<?> resolverEmergencia(
            @PathVariable("id") Integer id) {
        try {
            Emergencia emergenciaResuelta =
                    emergenciaService.resolver(id);
            return ResponseEntity.ok(emergenciaResuelta);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Elimina una emergencia registrada por su ID para el usuario y admi.

    @DeleteMapping("/eliminar/{id}")
    @PreAuthorize("@autorizacion.esEmergenciaPropiaOAdministrador(#p0)")
    public ResponseEntity<?> eliminarEmergencia(
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
