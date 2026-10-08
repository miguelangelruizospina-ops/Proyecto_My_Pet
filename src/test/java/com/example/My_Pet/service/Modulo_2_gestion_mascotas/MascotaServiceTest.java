package com.example.My_Pet.service.Modulo_2_gestion_mascotas;

// Clases propias del proyecto.

import com.example.My_Pet.model.Modulo_1_gestion_usuario.Usuario;
import com.example.My_Pet.model.Modulo_2_gestion_mascota.Mascota;
import com.example.My_Pet.model.Modulo_2_gestion_mascota.PerfilMascota;
import com.example.My_Pet.repository.Modulo_1_gestion_usuario.UsuarioRepository;
import com.example.My_Pet.repository.Modulo_2_gestion_mascotas.MascotaRepository;
import com.example.My_Pet.repository.Modulo_2_gestion_mascotas.PerfilMascotaRepository;

// Librerías de JUnit para ejecutar las pruebas.

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

// Librerías de Mockito para crear objetos simulados y verificar las operaciones.

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

// Librerías de Java.

import java.util.Optional;

// Métodos utilizados para comprobar los resultados y verificar las operaciones.

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// Habilita Mockito para esta clase de pruebas.

@ExtendWith(MockitoExtension.class)
class MascotaServiceTest {

    // Repositorio simulado para gestionar las mascotas.

    @Mock
    private MascotaRepository mascotaRepository;

    // Repositorio simulado para consultar los usuarios.

    @Mock
    private UsuarioRepository usuarioRepository;

    // Repositorio simulado para gestionar los perfiles de las mascotas.

    @Mock
    private PerfilMascotaRepository perfilMascotaRepository;

    // Servicio que será utilizado durante la prueba.

    @InjectMocks
    private MascotaService mascotaService;

    // Verifica que al guardar una mascota se cree su perfil vinculado.

    @Test
    void guardarMascotaCreaPerfilVinculado() {

        // Crea el usuario asociado a la mascota.

        Usuario usuario = new Usuario();

        usuario.setIdUsuario(9);

        // Crea la mascota utilizada durante la prueba.

        Mascota mascota = new Mascota();

        mascota.setNombre("Luna");
        mascota.setUsuario(usuario);

        // Simula la búsqueda del usuario existente.

        when(usuarioRepository.findById(9)).thenReturn(Optional.of(usuario));

        // Simula el guardado de la mascota y asigna un identificador.

        when(mascotaRepository.save(mascota)).thenAnswer(invocacion -> {

            Mascota guardada = invocacion.getArgument(0);
            guardada.setIdMascota(21);
            return guardada;
        });

        // Simula el guardado del perfil asociado a la mascota.

        when(perfilMascotaRepository.save(any(PerfilMascota.class)))
                .thenAnswer(invocacion -> invocacion.getArgument(0));

        // Ejecuta el método que guarda la mascota.

        Mascota resultado = mascotaService.guardarMascota(mascota);

        // Verifica que el resultado corresponda a la mascota guardada.

        assertSame(mascota, resultado);

        // Verifica que se cree el perfil asociado a la mascota.

        verify(perfilMascotaRepository).save(any(PerfilMascota.class));
    }
}