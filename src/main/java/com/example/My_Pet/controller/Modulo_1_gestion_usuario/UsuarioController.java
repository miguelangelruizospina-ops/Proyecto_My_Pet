
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

    // ============================================================
    // DATOS PARA CAMBIAR LA CONTRASEÑA
    // ============================================================

    public record CambioContrasenaRequest(
            String correo,
            String contrasenaActual,
            String nuevaContrasena) {
    }


    // ============================================================
    // SERVICIO DE USUARIOS
    // ============================================================

    @Autowired
    private UsuarioService usuarioService;


    // ============================================================
    // REPOSITORIO DE SEGURIDAD
    // ============================================================

    // Permite guardar el SecurityContext dentro de la sesión HTTP.
    @Autowired
    private SecurityContextRepository securityContextRepository;


    // ============================================================
    // OBTENER TOKEN CSRF
    // ============================================================

    // El frontend utiliza este endpoint para obtener el token
    // necesario para las solicitudes que modifican información.

    @GetMapping("/csrf")
    public CsrfToken csrf(CsrfToken token) {
        return token;
    }


    // ============================================================
    // REGISTRO DE USUARIO
    // ============================================================

    // Registra el usuario y después inicia automáticamente
    // su sesión de seguridad.

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


    // ============================================================
    // INICIO DE SESIÓN
    // ============================================================

    // Valida las credenciales y crea la sesión de Spring Security.

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


    // ============================================================
    // CREAR SESIÓN DE SEGURIDAD
    // ============================================================

    // Crea el UsuarioPrincipal, establece la autenticación
    // y guarda el SecurityContext en la sesión HTTP.

    private void crearSesion(
            Usuario usuario,
            HttpServletRequest request,
            HttpServletResponse response) {

        // --------------------------------------------------------
        // OBTENER EL ROL DEL USUARIO
        // --------------------------------------------------------

        String rol =
                usuario.getRol()
                        .trim()
                        .toUpperCase();


        // --------------------------------------------------------
        // CREAR EL USUARIO PRINCIPAL
        // --------------------------------------------------------

        UsuarioPrincipal usuarioPrincipal =
                new UsuarioPrincipal(
                        usuario.getIdUsuario(),
                        rol
                );


        // --------------------------------------------------------
        // CREAR LA AUTENTICACIÓN
        // --------------------------------------------------------

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


        // --------------------------------------------------------
        // CREAR EL SECURITY CONTEXT
        // --------------------------------------------------------

        SecurityContext context =
                SecurityContextHolder.createEmptyContext();

        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);


        // --------------------------------------------------------
        // CREAR LA SESIÓN HTTP
        // --------------------------------------------------------

        request.getSession(true);


        // --------------------------------------------------------
        // GUARDAR EL SECURITY CONTEXT
        // --------------------------------------------------------

        securityContextRepository.saveContext(
                context,
                request,
                response
        );
    }


    // ============================================================
    // CERRAR SESIÓN
    // ============================================================

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


    // ============================================================
    // CAMBIAR CONTRASEÑA
    // ============================================================

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


    // ============================================================
    // LISTAR USUARIOS
    // ============================================================

    // Solo disponible para administradores.

    @GetMapping("/listar")
    @PreAuthorize("@autorizacion.esAdministrador()")
    public List<Usuario> listar() {
        return usuarioService.obtenerTodosLosUsuarios();
    }


    // ============================================================
    // OBTENER USUARIO POR ID
    // ============================================================

    // Permite consultar los datos propios o los de otro usuario
    // cuando se tienen permisos de administrador.

    @GetMapping("/{id}")
    @PreAuthorize("@autorizacion.esUsuarioPropioOAdministrador(#p0)")
    public Usuario obtenerPorId(
            @PathVariable("id") int id) {

        return usuarioService.obtenerUsuarioPorId(id);
    }


    // ============================================================
    // ACTUALIZAR USUARIO
    // ============================================================

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


    // ============================================================
    // ELIMINAR USUARIO
    // ============================================================

    // Exclusivo para administradores.

    @DeleteMapping("/eliminar/{id}")
    @PreAuthorize("@autorizacion.esAdministrador()")
    public String eliminar(
            @PathVariable("id") int id) {

        usuarioService.eliminarUsuario(id);

        return "Usuario eliminado correctamente";
    }
}
