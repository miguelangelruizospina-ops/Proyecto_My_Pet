// Controlador del módulo de gestión de usuarios.
package com.example.My_Pet.controller.Modulo_1_gestion_usuario;

import com.example.My_Pet.model.Modulo_1_gestion_usuario.Usuario;
import com.example.My_Pet.service.Modulo_1_gestion_usuario.UsuarioService;
import com.example.My_Pet.security.UsuarioPrincipal;

// Librerías para el funcionamiento del controlador y la seguridad.
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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;


// Define el controlador REST de usuarios.
@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    // Datos necesarios para cambiar la contraseña.
    public record CambioContrasenaRequest(
            String correo,
            String contrasenaActual,
            String nuevaContrasena) {
    }

    // Conecta el controlador con la lógica de usuarios.
    @Autowired
    private UsuarioService usuarioService;

    // Permite guardar y gestionar la sesión de seguridad.
    @Autowired
    private SecurityContextRepository securityContextRepository;


    // GET - Obtiene el token CSRF para las solicitudes protegidas.
    @GetMapping("/csrf")
    public CsrfToken csrf(CsrfToken token) {
        return token;
    }


    // POST - Registra un nuevo usuario e inicia su sesión.
    @PostMapping("/registro")
    public ResponseEntity<?> registrar(
            @RequestBody Usuario usuario,
            HttpServletRequest request,
            HttpServletResponse response) {

        try {
            Usuario usuarioCreado = usuarioService.registrarUsuario(usuario);
            crearSesion(usuarioCreado, request, response);
            return ResponseEntity.ok(usuarioCreado);

        } catch (IllegalArgumentException error) {
            return ResponseEntity.badRequest().body(error.getMessage());
        }
    }


    // POST - Valida las credenciales e inicia sesión.
    @PostMapping("/login")
    public Usuario login(
            @RequestBody Usuario datosLogin,
            HttpServletRequest request,
            HttpServletResponse response) {

        Usuario usuario = usuarioService.iniciarSesion(
                datosLogin.getCorreo(),
                datosLogin.getContrasena()
        );

        crearSesion(usuario, request, response);
        return usuario;
    }


    // Crea la sesión de seguridad con el usuario y su rol.
    private void crearSesion(
            Usuario usuario,
            HttpServletRequest request,
            HttpServletResponse response) {

        String rol = usuario.getRol().toUpperCase();

        var authentication = UsernamePasswordAuthenticationToken.authenticated(
                new UsuarioPrincipal(usuario.getIdUsuario(), rol),
                null,
                List.of(new SimpleGrantedAuthority("ROLE_" + rol))
        );

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        if (request.getSession(false) != null) {
            request.changeSessionId();
        }

        securityContextRepository.saveContext(context, request, response);
    }


    // POST - Cierra la sesión del usuario.
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            HttpServletRequest request,
            HttpServletResponse response) {

        SecurityContextHolder.clearContext();

        if (request.getSession(false) != null) {
            request.getSession(false).invalidate();
        }

        return ResponseEntity.noContent().build();
    }


    // PUT - Permite cambiar la contraseña del usuario.
    @PutMapping("/cambiar-contrasena")
    public ResponseEntity<String> cambiarContrasena(
            @RequestBody CambioContrasenaRequest solicitud) {

        try {
            usuarioService.cambiarContrasena(
                    solicitud.correo(),
                    solicitud.contrasenaActual(),
                    solicitud.nuevaContrasena()
            );

            return ResponseEntity.ok("Contraseña actualizada correctamente.");

        } catch (IllegalArgumentException error) {
            return ResponseEntity.badRequest().body(error.getMessage());
        }
    }


    // GET - Lista todos los usuarios. Solo para administradores.
    @GetMapping("/listar")
    @PreAuthorize("@autorizacion.esAdministrador()")
    public List<Usuario> listar() {
        return usuarioService.obtenerTodosLosUsuarios();
    }


    // GET - Busca un usuario por ID y valida sus permisos.
    @GetMapping("/{id}")
    @PreAuthorize("@autorizacion.esUsuarioPropioOAdministrador(#p0)")
    public Usuario obtenerPorId(@PathVariable int id) {
        return usuarioService.obtenerUsuarioPorId(id);
    }


    // PUT - Actualiza los datos de un usuario.
    @PutMapping("/actualizar/{id}")
    @PreAuthorize("@autorizacion.esUsuarioPropioOAdministrador(#p0)")
    public Usuario actualizar(
            @PathVariable int id,
            @RequestBody Usuario usuario) {

        return usuarioService.actualizarUsuario(id, usuario);
    }


    // DELETE - Elimina un usuario. Solo para administradores.
    @DeleteMapping("/eliminar/{id}")
    @PreAuthorize("@autorizacion.esAdministrador()")
    public String eliminar(@PathVariable int id) {

        usuarioService.eliminarUsuario(id);

        return "Usuario eliminado correctamente";
    }
}