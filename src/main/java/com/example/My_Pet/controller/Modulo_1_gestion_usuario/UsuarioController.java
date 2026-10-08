package com.example.My_Pet.controller.Modulo_1_gestion_usuario;

// Clases propias del proyecto.

import com.example.My_Pet.model.Modulo_1_gestion_usuario.Usuario;
import com.example.My_Pet.service.Modulo_1_gestion_usuario.UsuarioService;
import com.example.My_Pet.security.UsuarioPrincipal;

// Librerías de Spring.

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

//Librerias exrternas.

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//Librerías de Java.

import java.util.List;
// Controlador REST para gestionar la información de los usuarios.

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    // Solicitud utilizada para cambiar la contraseña.

    public record CambioContrasenaRequest(
            String correo,
            String contrasenaActual,
            String nuevaContrasena) {
    }

    // Servicio encargado de gestionar los usuarios.

    @Autowired
    private UsuarioService usuarioService;

    // Permite guardar el contexto de seguridad dentro de la sesión HTTP.

    @Autowired
    private SecurityContextRepository securityContextRepository;

    // Obtiene el token CSRF necesario para solicitudes protegidas.

    @GetMapping("/csrf")
    public CsrfToken csrf(CsrfToken token) {
        return token;
    }

    // Registra un nuevo usuario e inicia automáticamente su sesión.

    @PostMapping("/registro")
    public ResponseEntity<?> registrar(
            @RequestBody Usuario usuario,
            HttpServletRequest request,
            HttpServletResponse response) {
        try {
            Usuario usuarioCreado =
                    usuarioService.registrarUsuario(usuario);
            crearSesion(
                    usuarioCreado,
                    request,
                    response
            );
            return ResponseEntity.ok(usuarioCreado);
        } catch (IllegalArgumentException error) {
            return ResponseEntity
                    .badRequest()
                    .body(error.getMessage());
        }
    }

    // Valida las credenciales e inicia la sesión del usuario.

    @PostMapping("/login")
    public Usuario login(
            @RequestBody Usuario datosLogin,
            HttpServletRequest request,
            HttpServletResponse response) {
        Usuario usuario =
                usuarioService.iniciarSesion(
                        datosLogin.getCorreo(),
                        datosLogin.getContrasena()
                );
        crearSesion(
                usuario,
                request,
                response
        );
        return usuario;
    }

    // Crea la autenticación y guarda el contexto de seguridad en la sesión HTTP.

    private void crearSesion(
            Usuario usuario,
            HttpServletRequest request,
            HttpServletResponse response) {

        // Obtiene y normaliza el rol del usuario.

        String rol =
                usuario.getRol()
                        .trim()
                        .toUpperCase();

        // Crea el usuario principal utilizado por Spring Security.

        UsuarioPrincipal usuarioPrincipal =
                new UsuarioPrincipal(
                        usuario.getIdUsuario(),
                        rol
                );

        // Crea la autenticación con el rol correspondiente.

        UsernamePasswordAuthenticationToken authentication =
                UsernamePasswordAuthenticationToken.authenticated(
                        usuarioPrincipal,
                        null,
                        List.of(
                                new SimpleGrantedAuthority(
                                        "ROLE_" + rol
                                )
                        )
                );

        // Crea y establece el contexto de seguridad.

        SecurityContext context =
                SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        // Crea la sesión HTTP si no existe.

        request.getSession(true);

        // Guarda el contexto de seguridad en la sesión.

        securityContextRepository.saveContext(
                context,
                request,
                response
        );
    }

    // Cierra la sesión actual del usuario.

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            HttpServletRequest request,
            HttpServletResponse response) {

        // Limpia la autenticación actual.

        SecurityContextHolder.clearContext();

        // Invalida la sesión existente.

        if (request.getSession(false) != null) {
            request.getSession(false).invalidate();
        }
        return ResponseEntity.noContent().build();
    }

    // Cambia la contraseña de un usuario.

    @PutMapping("/cambiar-contrasena")
    public ResponseEntity<String> cambiarContrasena(
            @RequestBody CambioContrasenaRequest solicitud) {
        try {
            usuarioService.cambiarContrasena(
                    solicitud.correo(),
                    solicitud.contrasenaActual(),
                    solicitud.nuevaContrasena()
            );
            return ResponseEntity.ok(
                    "Contraseña actualizada correctamente."
            );
        } catch (IllegalArgumentException error) {
            return ResponseEntity
                    .badRequest()
                    .body(error.getMessage());
        }
    }

    // Lista todos los usuarios. ***SOLO PARA ADMINISTRADORES***

    @GetMapping("/listar")
    @PreAuthorize("@autorizacion.esAdministrador()")
    public List<Usuario> listar() {
        return usuarioService.obtenerTodosLosUsuarios();
    }

    // Busca un usuario por su ID y valida los permisos de acceso.

    @GetMapping("/{id}")
    @PreAuthorize("@autorizacion.esUsuarioPropioOAdministrador(#p0)")
    public Usuario obtenerPorId(
            @PathVariable("id") int id) {
        return usuarioService.obtenerUsuarioPorId(id);

    }

    // Actualiza los datos de un usuario y valida los permisos de acceso.

    @PutMapping("/actualizar/{id}")
    @PreAuthorize("@autorizacion.esUsuarioPropioOAdministrador(#p0)")
    public Usuario actualizar(
            @PathVariable("id") int id,
            @RequestBody Usuario usuario) {
        return usuarioService.actualizarUsuario(
                id,
                usuario
        );
    }

    // Elimina un usuario. ***SOLO PARA ADMINISTRADORES***

    @DeleteMapping("/eliminar/{id}")
    @PreAuthorize("@autorizacion.esAdministrador()")
    public String eliminar(
            @PathVariable("id") int id) {
        usuarioService.eliminarUsuario(id);
        return "Usuario eliminado correctamente";
    }
}