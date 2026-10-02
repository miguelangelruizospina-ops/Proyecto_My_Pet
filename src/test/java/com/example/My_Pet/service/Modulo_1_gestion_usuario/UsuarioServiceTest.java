package com.example.My_Pet.service.Modulo_1_gestion_usuario;

import com.example.My_Pet.model.Modulo_1_gestion_usuario.Usuario;
import com.example.My_Pet.repository.Modulo_1_gestion_usuario.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

        @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

        @Test
        void registrarUsuarioGuardaUsuarioSinCrearPerfil() {
        Usuario usuario = new Usuario();
        usuario.setCorreo("  Persona@Ejemplo.com ");
        usuario.setNombre("Persona");
        usuario.setContrasena("secreto123");

        when(usuarioRepository.existsByCorreoIgnoreCase("persona@ejemplo.com"))
                .thenReturn(false);
        when(passwordEncoder.encode("secreto123")).thenReturn("$2a$encoded");
        when(usuarioRepository.save(any(Usuario.class)))
                .thenAnswer(invocacion -> invocacion.getArgument(0));

        Usuario resultado = usuarioService.registrarUsuario(usuario);

        assertSame(usuario, resultado);
        assertEquals("persona@ejemplo.com", resultado.getCorreo());
        assertEquals("$2a$encoded", resultado.getContrasena());
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void registrarUsuarioRechazaCorreoDuplicado() {
        Usuario usuario = new Usuario();
        usuario.setCorreo("persona@ejemplo.com");
        usuario.setContrasena("secreto123");

        when(usuarioRepository.existsByCorreoIgnoreCase("persona@ejemplo.com"))
                .thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> usuarioService.registrarUsuario(usuario));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

        @Test
        void actualizarUsuarioRechazaCorreoDeOtraCuenta() {
                Usuario existente = new Usuario();
                existente.setIdUsuario(7);
                existente.setCorreo("persona@ejemplo.com");
                Usuario cambios = new Usuario();
                cambios.setCorreo("otra@ejemplo.com");

                when(usuarioRepository.findById(7)).thenReturn(Optional.of(existente));
                when(usuarioRepository.existsByCorreoIgnoreCaseAndIdUsuarioNot("otra@ejemplo.com", 7))
                                .thenReturn(true);

                assertThrows(IllegalArgumentException.class,
                                () -> usuarioService.actualizarUsuario(7, cambios));
                verify(usuarioRepository, never()).save(any(Usuario.class));
        }

    @Test
    void iniciarSesionValidaHashDeContrasena() {
        Usuario usuario = new Usuario();
        usuario.setCorreo("persona@ejemplo.com");
        usuario.setContrasena("$2a$hash");

        when(usuarioRepository.findByCorreo("persona@ejemplo.com"))
                .thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("secreto123", "$2a$hash"))
                .thenReturn(true);

        assertSame(usuario,
                usuarioService.iniciarSesion("persona@ejemplo.com", "secreto123"));
    }
}