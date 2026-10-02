// Controlador para la gestión de mascotas.
package com.example.My_Pet.controller.Modulo_2_gestion_mascotas;

import com.example.My_Pet.model.Modulo_2_gestion_mascota.Mascota;
import com.example.My_Pet.service.Modulo_2_gestion_mascotas.MascotaService;

// Librerías necesarias para el controlador y la seguridad.
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


// Define el controlador REST de mascotas.
@RestController
@RequestMapping("/api/mascotas")
public class MascotaController {

    // Conecta el controlador con la lógica del servicio.
    @Autowired
    private MascotaService mascotaService;


    // GET - Lista todas las mascotas. Solo para administradores.
    @GetMapping("/listar")
    @PreAuthorize("@autorizacion.esAdministrador()")
    public List<Mascota> listarMascotas() {
        return mascotaService.obtenerTodasLasMascotas();
    }


    // GET - Lista las mascotas de un usuario.
    @GetMapping("/usuario/{idUsuario}")
    @PreAuthorize("@autorizacion.esUsuarioPropioOAdministrador(#p0)")
    public List<Mascota> obtenerMascotasPorUsuario(
            @PathVariable Integer idUsuario) {

        return mascotaService.obtenerMascotasPorUsuario(idUsuario);
    }


    // GET - Busca una mascota por su ID.
    @GetMapping("/{id}")
    @PreAuthorize("@autorizacion.esMascotaPropiaOAdministrador(#p0)")
    public Mascota obtenerMascotaPorId(
            @PathVariable Integer id) {

        return mascotaService.obtenerMascotaPorId(id);
    }


    // POST - Registra una nueva mascota.
    @PostMapping("/crear")
    @PreAuthorize("@autorizacion.esUsuarioPropioOAdministrador(#p0.usuario?.idUsuario)")
    public Mascota crearMascota(
            @RequestBody Mascota mascota) {

        return mascotaService.guardarMascota(mascota);
    }


    // PUT - Actualiza los datos de una mascota.
    @PutMapping("/actualizar/{id}")
    @PreAuthorize("@autorizacion.esMascotaPropiaOAdministrador(#p0)")
    public Mascota actualizarMascota(
            @PathVariable Integer id,
            @RequestBody Mascota mascota) {

        return mascotaService.actualizarMascota(id, mascota);
    }


    // DELETE - Elimina una mascota por su ID.
    @DeleteMapping("/eliminar/{id}")
    @PreAuthorize("@autorizacion.esMascotaPropiaOAdministrador(#p0)")
    public String eliminarMascota(
            @PathVariable Integer id) {

        mascotaService.eliminarMascota(id);

        return "Mascota eliminada correctamente";
    }
}