package com.example.My_Pet.controller.Modulo_3_Agenda_Recordatorio;

//clases del proyecto

import com.example.My_Pet.model.Modulo_3_Agenda_Recordatorio.Recordatorio;
import com.example.My_Pet.service.Modulo_3_Agenda_Recordatorio.RecordatorioService;

//librerías de Spring.

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

//librerías de Java.

import java.util.List;

// Define el controlador REST de recordatorios.

@RestController
@RequestMapping("/api/recordatorios")
public class RecordatorioController {

    // Conecta el controlador con el servicio de recordatorios.
    @Autowired
    private RecordatorioService recordatorioService;


    //Lista todos los recordatorios. ***SOLO PARA ADMINISTRADORES***

    @GetMapping("/listar")
    @PreAuthorize("@autorizacion.esAdministrador()")
    public List<Recordatorio> listarTodo() {
        return recordatorioService.obtenerTodos();
    }

    // Lista los recordatorios asociados a un usuario registrado y que tenga mascotas registradas.

    @GetMapping("/usuario/{idUsuario}")
    @PreAuthorize("@autorizacion.esUsuarioPropioOAdministrador(#p0)")
    public ResponseEntity<List<Recordatorio>> listarPorUsuario(
            @PathVariable("idUsuario") Integer idUsuario) {
        List<Recordatorio> recordatorios =
                recordatorioService.obtenerPorUsuario(idUsuario);
        return ResponseEntity.ok(recordatorios);
    }


    // Lista los recordatorios asociados a una mascota registrada.

    @GetMapping("/mascota/{idMascota}")
    @PreAuthorize("@autorizacion.esMascotaPropiaOAdministrador(#p0)")
    public ResponseEntity<List<Recordatorio>> listarPorMascota(
            @PathVariable("idMascota") Integer idMascota) {
        List<Recordatorio> recordatorios =
                recordatorioService.obtenerPorMascota(idMascota);
        return ResponseEntity.ok(recordatorios);
    }

    // Registra un nuevo recordatorio de una mascota registrada.

    @PostMapping("/guardar")
    @PreAuthorize("@autorizacion.esUsuarioPropioOAdministrador(#p0.usuario?.idUsuario)")
    public ResponseEntity<?> crearRecordatorio(
            @RequestBody Recordatorio recordatorio) {
        try {
            Recordatorio nuevoRecordatorio =
                    recordatorioService.guardar(recordatorio);
            return ResponseEntity.ok(nuevoRecordatorio);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //Actualiza un recordatorio existente de una mascota..

    @PutMapping("/{id}")
    @PreAuthorize("@autorizacion.esRecordatorioPropioOAdministrador(#p0)")
    public ResponseEntity<?> actualizarRecordatorio(
            @PathVariable("id") Integer id,
            @RequestBody Recordatorio recordatorio) {
        try {
            Recordatorio recordatorioActualizado =
                    recordatorioService.actualizar(id, recordatorio);
            return ResponseEntity.ok(recordatorioActualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //Elimina un recordatorio de una mascota existente por su ID.

    @DeleteMapping("/{id}")
    @PreAuthorize("@autorizacion.esRecordatorioPropioOAdministrador(#p0)")
    public ResponseEntity<?> eliminarRecordatorio(
            @PathVariable("id") Integer id) {
        try {
            recordatorioService.eliminar(id);
            return ResponseEntity.ok(
                "Recordatorio eliminado correctamente."
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}