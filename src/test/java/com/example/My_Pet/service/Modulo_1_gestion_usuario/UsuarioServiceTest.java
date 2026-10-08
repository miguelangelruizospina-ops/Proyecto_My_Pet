package com.example.My_Pet.service.Modulo_1_gestion_usuario;

// Clases propias del proyecto.

import com.example.My_Pet.model.Modulo_1_gestion_usuario.Usuario;
import com.example.My_Pet.repository.Modulo_1_gestion_usuario.UsuarioRepository;

// Librerías de JUnit para ejecutar las pruebas.

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

// Librerías de Mockito para crear objetos simulados y verificar las operaciones.

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

// Librería de Spring Security para gestionar la codificación de contraseñas.

import org.springframework.security.crypto.password.PasswordEncoder;

// Librerías de Java.

import java.util.Optional;

// Métodos utilizados para comprobar los resultados y verificar las operaciones.

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// Habilita Mockito para esta clase de pruebas.

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    // Repositorio simulado para gestionar los usuarios.
    
    @Mock
    private UsuarioRepository usuarioRepository;

    // Codificador simulado para verificar el manejo de las contraseñas.
    
    @Mock
    private PasswordEncoder passwordEncoder;

    // Servicio que será utilizado durante las pruebas.
    
    @InjectMocks
    private UsuarioService usuarioService;

    // Verifica que el registro guarde el usuario sin crear un perfil.
    
    @Test
    void registrarUsuarioGuardaUsuarioSinCrearPerfil() {

        // Crea el usuario utilizado durante la prueba.
    
        Usuario usuario = new Usuario();

        usuario.setCorreo("  Persona@Ejemplo.com ");
        usuario.setNombre("Persona");
        usuario.setContrasena("secreto123");

        // Simula que el correo todavía no está registrado.
    
        when(usuarioRepository.existsByCorreoIgnoreCase("persona@ejemplo.com"))
                .thenReturn(false);

        // Simula la codificación de la contraseña.
    
        when(passwordEncoder.encode("secreto123")).thenReturn("$2a$encoded");

        // Simula el guardado y devuelve el mismo usuario recibido.
    
        when(usuarioRepository.save(any(Usuario.class)))
                .thenAnswer(invocacion -> invocacion.getArgument(0));

        // Ejecuta el registro del usuario.
    
        Usuario resultado = usuarioService.registrarUsuario(usuario);

        // Verifica que el usuario y sus datos principales sean correctos.
    
        assertSame(usuario, resultado);
        assertEquals("persona@ejemplo.com", resultado.getCorreo());
        assertEquals("$2a$encoded", resultado.getContrasena());

        // Verifica que el usuario se guarde en el repositorio.
    
        verify(usuarioRepository).save(usuario);
    }

    // Verifica que el registro rechace un correo que ya existe.
    
    @Test
    void registrarUsuarioRechazaCorreoDuplicado() {

        // Crea el usuario utilizado durante la prueba.
    
        Usuario usuario = new Usuario();

        usuario.setCorreo("persona@ejemplo.com");
        usuario.setContrasena("secreto123");

        // Simula que el correo ya está registrado.
    
        when(usuarioRepository.existsByCorreoIgnoreCase("persona@ejemplo.com"))
                .thenReturn(true);

        // Verifica que el servicio rechace el registro.
    
        assertThrows(IllegalArgumentException.class,
                () -> usuarioService.registrarUsuario(usuario));

        // Verifica que el usuario no sea guardado cuando el correo está duplicado.
    
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    // Verifica que la actualización rechace un correo utilizado por otra cuenta.
    
    @Test
    void actualizarUsuarioRechazaCorreoDeOtraCuenta() {

        // Crea el usuario existente que será actualizado.
    
        Usuario existente = new Usuario();

        existente.setIdUsuario(7);
        existente.setCorreo("persona@ejemplo.com");

        // Crea los cambios que se intentarán aplicar.
    
        Usuario cambios = new Usuario();

        cambios.setCorreo("otra@ejemplo.com");

        // Simula la búsqueda del usuario existente.
    
        when(usuarioRepository.findById(7)).thenReturn(Optional.of(existente));

        // Simula que el nuevo correo pertenece a otra cuenta.
    
        when(usuarioRepository.existsByCorreoIgnoreCaseAndIdUsuarioNot(
                "otra@ejemplo.com",
                7
        )).thenReturn(true);

        // Verifica que la actualización sea rechazada.
        assertThrows(IllegalArgumentException.class,
                () -> usuarioService.actualizarUsuario(7, cambios));

        // Verifica que el usuario no sea guardado cuando el correo está ocupado.
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    // Verifica que el inicio de sesión valide la contraseña mediante su hash.
    
    @Test
    void iniciarSesionValidaHashDeContrasena() {

        // Crea el usuario utilizado durante la autenticación.
        
        Usuario usuario = new Usuario();

        usuario.setCorreo("persona@ejemplo.com");
        usuario.setContrasena("$2a$hash");

        // Simula la búsqueda del usuario por correo.
        
        when(usuarioRepository.findByCorreo("persona@ejemplo.com"))
                .thenReturn(Optional.of(usuario));

        // Simula la validación de la contraseña contra el hash almacenado.
        
        when(passwordEncoder.matches("secreto123", "$2a$hash"))
                .thenReturn(true);

        // Verifica que el inicio de sesión devuelva el usuario autenticado.
        
        assertSame(
                usuario,
                usuarioService.iniciarSesion(
                        "persona@ejemplo.com",
                        "secreto123"
                )
        );
    }
}