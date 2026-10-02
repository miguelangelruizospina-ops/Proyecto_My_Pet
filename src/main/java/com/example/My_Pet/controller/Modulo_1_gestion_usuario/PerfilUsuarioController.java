// Controlador del módulo de gestión de perfiles de usuario.
package com.example.My_Pet.controller.Modulo_1_gestion_usuario;

import com.example.My_Pet.model.Modulo_1_gestion_usuario.PerfilUsuario;
import com.example.My_Pet.service.Modulo_1_gestion_usuario.PerfilUsuarioService;

// Librerías necesarias para el controlador y la seguridad.
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


// Define esta clase como controlador REST.
@RestController
@RequestMapping("/api/perfiles")
public class PerfilUsuarioController {

    // Conecta el controlador con la lógica del servicio.
    @Autowired
    private PerfilUsuarioService perfilUsuarioService;


    // GET - Lista todos los perfiles. Solo para administradores.
    @GetMapping("/listar")
    @PreAuthorize("@autorizacion.esAdministrador()")
    public List<PerfilUsuario> listarPerfiles() {
        return perfilUsuarioService.obtenerTodosLosPerfiles();
    }


    // GET - Busca un perfil por ID y valida el acceso del usuario.
    @GetMapping("/{id}")
    @PreAuthorize("@autorizacion.esUsuarioPropioOAdministrador(#p0)")
    public ResponseEntity<PerfilUsuario> obtenerPorId(@PathVariable int id) {
        return perfilUsuarioService.buscarPerfilPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    // POST - Crea un nuevo perfil y valida los permisos de acceso.
    @PostMapping("/crear")
    @PreAuthorize("@autorizacion.esUsuarioPropioOAdministrador(#p0.usuario?.idUsuario)")
    public PerfilUsuario crearPerfil(
            @RequestBody PerfilUsuario perfilUsuario) {

        try {
            return perfilUsuarioService.guardarPerfil(perfilUsuario);

        } catch (IllegalArgumentException error) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,
                    error.getMessage(),
                    error
            );
        }
    }


    // PUT - Actualiza un perfil existente.
    @PutMapping("/actualizar/{id}")
    @PreAuthorize("@autorizacion.esUsuarioPropioOAdministrador(#p0)")
    public PerfilUsuario actualizarPerfil(
            @PathVariable int id,
            @RequestBody PerfilUsuario perfilUsuario) {

        return perfilUsuarioService.actualizarPerfil(id, perfilUsuario);
    }


    // DELETE - Elimina un perfil existente.
    @DeleteMapping("/eliminar/{id}")
    @PreAuthorize("@autorizacion.esUsuarioPropioOAdministrador(#p0)")
    public String eliminarPerfil(@PathVariable int id) {

        perfilUsuarioService.eliminarPerfil(id);

        return "Perfil de usuario eliminado correctamente";
    }
}