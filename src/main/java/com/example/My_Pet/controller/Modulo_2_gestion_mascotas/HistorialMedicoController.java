
// Controlador para la gestión de historiales médicos de las mascotas.
// Paquete donde se encuentra el controlador.
package com.example.My_Pet.controller.Modulo_2_gestion_mascotas;

// Librerías necesarias para el controlador.
import com.example.My_Pet.model.Modulo_2_gestion_mascota.HistorialMedico;
import com.example.My_Pet.service.Modulo_2_gestion_mascotas.HistorialMedicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Define el controlador REST para gestionar los historiales médicos.

@RestController

@RequestMapping("/api/historiales-medicos")

public class HistorialMedicoController {

    @Autowired

    private HistorialMedicoService historialMedicoService;

    // Lista todos los historiales médicos. Solo para administradores.

    @GetMapping("/listar")

    @PreAuthorize("@autorizacion.esAdministrador()")

    public List<HistorialMedico> listarTodo() {

        return historialMedicoService.obtenerTodos();

    }

    // Lista los historiales médicos asociados a una mascota.

    @GetMapping("/mascota/{idMascota}")

    @PreAuthorize("@autorizacion.esMascotaPropiaOAdministrador(#p0)")

    public ResponseEntity<List<HistorialMedico>> listarPorMascota(

            @PathVariable Integer idMascota) {

        List<HistorialMedico> historial =

                historialMedicoService.obtenerPorMascota(idMascota);

        return ResponseEntity.ok(historial);

    }

    // Registra un nuevo historial médico para una mascota.

    @PostMapping("/guardar")

    @PreAuthorize(

            "@autorizacion.esMascotaPropiaOAdministrador(#p0.mascota?.idMascota)"

    )

    public ResponseEntity<?> crearHistorial(

            @RequestBody HistorialMedico historial) {

        try {

            HistorialMedico nuevoHistorial =

                    historialMedicoService.guardar(historial);

            return ResponseEntity.ok(nuevoHistorial);

        } catch (IllegalArgumentException e) {

            return ResponseEntity

                    .badRequest()

                    .body(e.getMessage());

        }

    }

    // Actualiza la información de un historial médico existente.

    @PutMapping("/actualizar/{id}")

    @PreAuthorize("@autorizacion.esHistorialPropioOAdministrador(#p0)")

    public ResponseEntity<?> actualizarHistorial(

            @PathVariable Integer id,

            @RequestBody HistorialMedico datos) {

        try {

            HistorialMedico historialActualizado =

                    historialMedicoService.actualizar(id, datos);

            return ResponseEntity.ok(historialActualizado);

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

    // Elimina un historial médico existente.

    @DeleteMapping("/eliminar/{id}")

    @PreAuthorize("@autorizacion.esHistorialPropioOAdministrador(#p0)")

    public ResponseEntity<?> eliminarHistorial(

            @PathVariable Integer id) {

        try {

            historialMedicoService.eliminar(id);

            return ResponseEntity.ok(

                    "Historial médico eliminado correctamente"

            );

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

}
