package com.example.My_Pet.controller.Modulo_2_gestion_mascotas;

// clases propias del proyecto.
import com.example.My_Pet.model.Modulo_2_gestion_mascota.Mascota;
import com.example.My_Pet.service.Modulo_2_gestion_mascotas.MascotaService;

// Librerías spring

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

// Librerías de Java.

import java.util.List;

// Define el controlador REST de mascotas.

@RestController
@RequestMapping("/api/mascotas")
public class MascotaController {

    // Conecta el controlador con la lógica del servicio.
    @Autowired
    private MascotaService mascotaService;


    // Lista todas las mascotas existentes. ***SOLO PARA ADMINISTRADORES***

    @GetMapping("/listar")
    @PreAuthorize("@autorizacion.esAdministrador()")
    public List<Mascota> listarMascotas() {
        return mascotaService.obtenerTodasLasMascotas();
    }


    //Lista una mascota existente asociada un usuario existente. 

    @GetMapping("/usuario/{idUsuario}")
    @PreAuthorize("@autorizacion.esUsuarioPropioOAdministrador(#p0)")
    public List<Mascota> obtenerMascotasPorUsuario(
            @PathVariable("idUsuario") Integer idUsuario) {
        return mascotaService.obtenerMascotasPorUsuario(idUsuario);
    }


    //Lista una mascota existente por su ID.

    @GetMapping("/{id}")
    @PreAuthorize("@autorizacion.esMascotaPropiaOAdministrador(#p0)")
    public Mascota obtenerMascotaPorId(
            @PathVariable("id") Integer id) {
        return mascotaService.obtenerMascotaPorId(id);
    }

    //Registra una nueva mascota.

    @PostMapping("/crear")
    @PreAuthorize("@autorizacion.esUsuarioPropioOAdministrador(#p0.usuario?.idUsuario)")
    public Mascota crearMascota(
            @RequestBody Mascota mascota) {
        return mascotaService.guardarMascota(mascota);
    }

    // Actualiza los datos de una mascota registrada.

    @PutMapping("/actualizar/{id}")
    @PreAuthorize("@autorizacion.esMascotaPropiaOAdministrador(#p0)")
    public Mascota actualizarMascota(
            @PathVariable("id") Integer id,
            @RequestBody Mascota mascota) {
        return mascotaService.actualizarMascota(id, mascota);
    }

    // Elimina una mascota reigistrada. ***SOLO PARA EL ADMINISTRADOR O EL USUARIO PROPIETARIO DE LA MASCOTA***

    @DeleteMapping("/eliminar/{id}")
    @PreAuthorize("@autorizacion.esMascotaPropiaOAdministrador(#p0)")
    public String eliminarMascota(
            @PathVariable("id") Integer id) {
        mascotaService.eliminarMascota(id);
        return "Mascota eliminada correctamente";
    }

}