package com.example.My_Pet.service.Modulo_2_gestion_mascotas;

import com.example.My_Pet.model.Modulo_1_gestion_usuario.Usuario;
import com.example.My_Pet.model.Modulo_2_gestion_mascota.Mascota;
import com.example.My_Pet.model.Modulo_2_gestion_mascota.PerfilMascota;
import com.example.My_Pet.repository.Modulo_1_gestion_usuario.UsuarioRepository;
import com.example.My_Pet.repository.Modulo_2_gestion_mascotas.MascotaRepository;
import com.example.My_Pet.repository.Modulo_2_gestion_mascotas.PerfilMascotaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MascotaServiceTest {

    @Mock
    private MascotaRepository mascotaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PerfilMascotaRepository perfilMascotaRepository;

    @InjectMocks
    private MascotaService mascotaService;

    @Test
    void guardarMascotaCreaPerfilVinculado() {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(9);
        Mascota mascota = new Mascota();
        mascota.setNombre("Luna");
        mascota.setUsuario(usuario);

        when(usuarioRepository.findById(9)).thenReturn(Optional.of(usuario));
        when(mascotaRepository.save(mascota)).thenAnswer(invocacion -> {
            Mascota guardada = invocacion.getArgument(0);
            guardada.setIdMascota(21);
            return guardada;
        });
        when(perfilMascotaRepository.save(any(PerfilMascota.class)))
                .thenAnswer(invocacion -> invocacion.getArgument(0));

        Mascota resultado = mascotaService.guardarMascota(mascota);

        assertSame(mascota, resultado);
        verify(perfilMascotaRepository).save(any(PerfilMascota.class));
    }
}