package com.example.My_Pet.service.Modulo_1_gestion_usuario;

// Clases propias del proyecto.

import com.example.My_Pet.model.Modulo_1_gestion_usuario.Usuario;
import com.example.My_Pet.model.Modulo_1_gestion_usuario.PerfilUsuario;
import com.example.My_Pet.repository.Modulo_1_gestion_usuario.PerfilUsuarioRepository;
import com.example.My_Pet.repository.Modulo_1_gestion_usuario.UsuarioRepository;

// Librerías de Jakarta para gestionar la persistencia de los datos.

import jakarta.persistence.EntityManager;

// Librerías de JUnit para ejecutar las pruebas.

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

// Librerías de Mockito para crear objetos simulados y verificar las operaciones.

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

// Librerías de Java.

import java.util.Optional;
import java.time.LocalDate;

// Métodos utilizados para comprobar los resultados y verificar las operaciones.

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// Habilita Mockito para esta clase de pruebas.

@ExtendWith(MockitoExtension.class)
class PerfilUsuarioServiceTest {

    // Repositorio simulado para gestionar los perfiles de usuario.

    @Mock
    private PerfilUsuarioRepository perfilUsuarioRepository;

    // Repositorio simulado para consultar los usuarios.

    @Mock
    private UsuarioRepository usuarioRepository;

    // Administrador de persistencia simulado para verificar el guardado del perfil.

    @Mock
    private EntityManager entityManager;

    // Servicio que será utilizado durante la prueba.

    @InjectMocks
    private PerfilUsuarioService perfilUsuarioService;

    // Verifica que un perfil nuevo se persista correctamente después del registro.

    @Test
    void guardarPerfilPersistePerfilCompletoTrasElRegistro() {

        // Crea el usuario asociado al perfil de prueba.

        Usuario usuario = new Usuario();

        usuario.setIdUsuario(54);
        usuario.setNombre("Ana");

        // Crea el perfil con la información utilizada durante la prueba.

        PerfilUsuario perfil = new PerfilUsuario();

        perfil.setUsuario(usuario);
        perfil.setNombreUsuario("Ana");
        perfil.setFotoPerfil("/Front%20end/fotos/ana.jpg");
        perfil.setBiografia("");
        perfil.setTelefono("");
        perfil.setCiudad("");
        perfil.setGenero("");
        perfil.setFechaNacimiento(LocalDate.of(2000, 1, 1));

        // Simula que el usuario existe en la base de datos.

        when(usuarioRepository.findById(54)).thenReturn(Optional.of(usuario));

        // Simula que el usuario todavía no tiene un perfil registrado.

        when(perfilUsuarioRepository.existsById(54)).thenReturn(false);

        // Ejecuta el método que guarda el perfil.

        PerfilUsuario resultado =
                perfilUsuarioService.guardarPerfil(perfil);

        // Verifica que el resultado conserve los datos principales esperados.

        assertEquals(54, resultado.getIdUsuario());
        assertEquals("Ana", resultado.getNombreUsuario());

        // Verifica que el perfil se persista mediante EntityManager.

        verify(entityManager).persist(resultado);

        // Verifica que el repositorio no utilice el método save().

        verify(perfilUsuarioRepository, never()).save(any());
    }
}