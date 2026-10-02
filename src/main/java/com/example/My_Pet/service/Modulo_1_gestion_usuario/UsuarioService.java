// Servicio encargado de gestionar las operaciones de los usuarios.
// Paquete donde se encuentra el servicio
package com.example.My_Pet.service.Modulo_1_gestion_usuario;

// Librerías necesarias para el servicio.
import com.example.My_Pet.model.Modulo_1_gestion_usuario.Usuario;
import com.example.My_Pet.repository.Modulo_1_gestion_usuario.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

// Define la clase como un servicio de Spring.

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Registra un nuevo usuario.
    @Transactional
    public Usuario registrarUsuario(Usuario usuario) {

        // Verifica que el correo sea obligatorio.
        if (usuario.getCorreo() == null || usuario.getCorreo().isBlank()) {
            throw new IllegalArgumentException(
                    "El correo es obligatorio."
            );
        }

        usuario.setCorreo(
                usuario.getCorreo().trim().toLowerCase(Locale.ROOT)
        );

        // Verifica que no exista otra cuenta con el mismo correo.
        if (usuarioRepository.existsByCorreoIgnoreCase(
                usuario.getCorreo())) {

            throw new IllegalArgumentException(
                    "Ya existe una cuenta con ese correo."
            );
        }

        // Verifica que la contraseña sea obligatoria.
        if (usuario.getContrasena() == null ||
                usuario.getContrasena().isBlank()) {

            throw new IllegalArgumentException(
                    "La contraseña es obligatoria."
            );
        }

        usuario.setRol("usuario");
        usuario.setFechaCreacion(LocalDateTime.now());

        // Encripta la contraseña antes de guardarla.
        usuario.setContrasena(
                passwordEncoder.encode(usuario.getContrasena())
        );

        Usuario usuarioGuardado =
                usuarioRepository.save(usuario);

        return usuarioGuardado;
    }

    // Lista todos los usuarios registrados.
    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }

    // Busca un usuario por su ID.
    public Usuario obtenerUsuarioPorId(int id) {

        Optional<Usuario> usuario =
                usuarioRepository.findById(id);

        if (usuario.isPresent()) {
            return usuario.get();
        }

        throw new RuntimeException(
                "Usuario no encontrado con ID: " + id
        );
    }

    // Actualiza los datos de un usuario.
    public Usuario actualizarUsuario(
            int id,
            Usuario datosUsuario) {

        Usuario usuarioExistente =
                obtenerUsuarioPorId(id);

        // Verifica que el correo sea obligatorio.
        if (datosUsuario.getCorreo() == null ||
                datosUsuario.getCorreo().isBlank()) {

            throw new IllegalArgumentException(
                    "El correo es obligatorio."
            );
        }

        String correo =
                datosUsuario.getCorreo()
                        .trim()
                        .toLowerCase(Locale.ROOT);

        // Verifica que el correo no pertenezca a otro usuario.
        if (usuarioRepository.existsByCorreoIgnoreCaseAndIdUsuarioNot(
                correo, id)) {

            throw new IllegalArgumentException(
                    "Ya existe una cuenta con ese correo."
            );
        }

        usuarioExistente.setNombre(
                datosUsuario.getNombre()
        );

        usuarioExistente.setApellidos(
                datosUsuario.getApellidos()
        );

        usuarioExistente.setUsuario(
                datosUsuario.getUsuario()
        );

        usuarioExistente.setCorreo(correo);

        return usuarioRepository.save(usuarioExistente);
    }

    // Elimina un usuario por su ID.
    public void eliminarUsuario(int id) {

        Usuario usuarioExistente =
                obtenerUsuarioPorId(id);

        usuarioRepository.delete(usuarioExistente);
    }

    // Verifica las credenciales para iniciar sesión.
    public Usuario iniciarSesion(
            String correo,
            String contrasena) {

        // Verifica que el correo y la contraseña hayan sido enviados.
        if (correo == null || contrasena == null) {
            throw new IllegalArgumentException(
                    "Correo o contraseña incorrectos."
            );
        }

        Optional<Usuario> usuarioEncontrado =
                usuarioRepository.findByCorreo(
                        correo.trim().toLowerCase(Locale.ROOT)
                );

        if (usuarioEncontrado.isPresent()) {

            Usuario usuario =
                    usuarioEncontrado.get();

            String contrasenaGuardada =
                    usuario.getContrasena();

            // Verifica si la contraseña almacenada utiliza BCrypt.
            boolean esHashBcrypt =
                    contrasenaGuardada != null &&
                    contrasenaGuardada.matches("^\\$2[aby]\\$.*");

            boolean credencialesValidas =
                    esHashBcrypt
                            ? passwordEncoder.matches(
                                    contrasena,
                                    contrasenaGuardada
                            )
                            : contrasenaGuardada != null &&
                              contrasenaGuardada.equals(contrasena);

            if (credencialesValidas) {

                // Actualiza las contraseñas antiguas al formato BCrypt.
                if (!esHashBcrypt) {
                    usuario.setContrasena(
                            passwordEncoder.encode(contrasena)
                    );

                    usuarioRepository.save(usuario);
                }

                return usuario;
            }
        }

        throw new RuntimeException(
                "Correo o contraseña incorrectos"
        );
    }

    // Permite cambiar la contraseña de un usuario.
    public Usuario cambiarContrasena(
            String correo,
            String contrasenaActual,
            String nuevaContrasena) {

        // Verifica que se hayan enviado los datos necesarios.
        if (correo == null || contrasenaActual == null) {
            throw new IllegalArgumentException(
                    "Correo o contraseña actual incorrectos."
            );
        }

        // Verifica que la nueva contraseña tenga mínimo 8 caracteres.
        if (nuevaContrasena == null ||
                nuevaContrasena.length() < 8) {

            throw new IllegalArgumentException(
                    "La nueva contraseña debe tener al menos 8 caracteres."
            );
        }

        Optional<Usuario> usuarioEncontrado =
                usuarioRepository.findByCorreo(
                        correo.trim().toLowerCase(Locale.ROOT)
                );

        if (usuarioEncontrado.isPresent()) {

            Usuario usuario =
                    usuarioEncontrado.get();

            String contrasenaGuardada =
                    usuario.getContrasena();

            // Verifica si la contraseña almacenada utiliza BCrypt.
            boolean esHashBcrypt =
                    contrasenaGuardada != null &&
                    contrasenaGuardada.matches("^\\$2[aby]\\$.*");

            boolean actualValida =
                    esHashBcrypt
                            ? passwordEncoder.matches(
                                    contrasenaActual,
                                    contrasenaGuardada
                            )
                            : contrasenaGuardada != null &&
                              contrasenaGuardada.equals(contrasenaActual);

            // Impide cambiar la contraseña si la actual es incorrecta.
            if (!actualValida) {
                throw new IllegalArgumentException(
                        "La contraseña actual es incorrecta."
                );
            }

            usuario.setContrasena(
                    passwordEncoder.encode(nuevaContrasena)
            );

            return usuarioRepository.save(usuario);
        }

        throw new IllegalArgumentException(
                "No existe un usuario registrado con ese correo"
        );
    }
}