package com.example.My_Pet.controller.Modulo_1_gestion_usuario;

// Clases propias del proyecto.

import com.example.My_Pet.model.Modulo_1_gestion_usuario.PerfilUsuario;
import com.example.My_Pet.service.Modulo_1_gestion_usuario.PerfilUsuarioService;

// Librerías de Spring.

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

// Librerías de Java.

import java.util.List;

// Controlador REST para gestionar los perfiles de los usuarios.

@RestController

@RequestMapping("/api/perfiles")

public class PerfilUsuarioController {

    // Servicio encargado de gestionar los perfiles de los usuarios.

    @Autowired
    private PerfilUsuarioService perfilUsuarioService;

    // Lista todos los perfiles de los usuarios. ***SOLO PARA ADMINISTRADORES***

    @GetMapping("/listar")
    @PreAuthorize("@autorizacion.esAdministrador()")
    public List<PerfilUsuario> listarPerfiles() {
        return perfilUsuarioService.obtenerTodosLosPerfiles();
    }

    // Busca un perfil por su ID y valida el acceso del usuario.

    @GetMapping("/{id}")
    @PreAuthorize("@autorizacion.esUsuarioPropioOAdministrador(#p0)")
    public ResponseEntity<PerfilUsuario> obtenerPorId(
            @PathVariable("id") int id) {
        return perfilUsuarioService.buscarPerfilPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Registra un nuevo perfil de usuario y valida los permisos de acceso.

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

    // Actualiza un perfil de usuario existente.

    @PutMapping("/actualizar/{id}")
    @PreAuthorize("@autorizacion.esUsuarioPropioOAdministrador(#p0)")
    public PerfilUsuario actualizarPerfil(
            @PathVariable("id") int id,
            @RequestBody PerfilUsuario perfilUsuario) {
        return perfilUsuarioService.actualizarPerfil(
                id,
                perfilUsuario
        );
    }

    // Elimina un perfil de usuario existente.

    @DeleteMapping("/eliminar/{id}")
    @PreAuthorize("@autorizacion.esUsuarioPropioOAdministrador(#p0)")
    public String eliminarPerfil(
            @PathVariable("id") int id) {
        perfilUsuarioService.eliminarPerfil(id);
        return "Perfil de usuario eliminado correctamente";
    }
}