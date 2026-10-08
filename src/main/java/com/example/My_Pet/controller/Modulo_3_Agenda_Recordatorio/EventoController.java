package com.example.My_Pet.controller.Modulo_3_Agenda_Recordatorio;

//clases del proyecto

import com.example.My_Pet.model.Modulo_3_Agenda_Recordatorio.Evento;
import com.example.My_Pet.service.Modulo_3_Agenda_Recordatorio.EventoService;

//clases de Spring.

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

//clases de Java.

import java.util.List;

// Define el controlador REST de eventos.

@RestController
@RequestMapping("/api/eventos")
public class EventoController {

    // Conecta el controlador con el servicio de eventos.
    @Autowired
    private EventoService eventoService;


    // Lista todos los eventos registrados. ***SOLO PARA ADMINISTRADORES***

    @GetMapping("/listar")
    @PreAuthorize("@autorizacion.esAdministrador()")
    public List<Evento> listarTodo() {
        return eventoService.obtenerTodos();
    }


    // Lista los eventos asociados a una mascota existente.

    @GetMapping("/mascota/{idMascota}")
    @PreAuthorize("@autorizacion.esMascotaPropiaOAdministrador(#p0)")
    public ResponseEntity<List<Evento>> listarPorMascota(
            @PathVariable("idMascota") Integer idMascota) {
        List<Evento> eventos =
                eventoService.obtenerPorMascota(idMascota);
        return ResponseEntity.ok(eventos);
    }

    // Registra un nuevo evento de una mascota existente.

    @PostMapping("/guardar")
    @PreAuthorize("@autorizacion.puedeGuardarEvento(#p0)")
    public ResponseEntity<?> crearEvento(
            @RequestBody Evento evento) {
        try {
            Evento nuevoEvento =
                    eventoService.guardar(evento);
            return ResponseEntity.ok(nuevoEvento);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    e.getMessage()
            );
        }
    }

    //Actualiza un evento existente asociado  a una mascot existente.

    @PutMapping("/actualizar/{id}")
    @PreAuthorize("@autorizacion.esEventoPropioOAdministrador(#p0)")
    public ResponseEntity<?> actualizarEvento(
            @PathVariable("id") Integer id,
            @RequestBody Evento evento) {
        try {
            Evento eventoActualizado =
                    eventoService.actualizar(
                            id,
                            evento
                    );
            return ResponseEntity.ok(eventoActualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    e.getMessage()
            );
        }
    }

    // Elimina un evento existente  por su ID.

    @DeleteMapping("/eliminar/{id}")
    @PreAuthorize("@autorizacion.esEventoPropioOAdministrador(#p0)")
    public ResponseEntity<?> eliminarEvento(
            @PathVariable("id") Integer id) {
        try {
            eventoService.eliminar(id);
            return ResponseEntity.ok(
                    "Evento eliminado correctamente"
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    e.getMessage()
            );
        }
    }
}