package com.example.My_Pet.service.Modulo_1_gestion_usuario;

import com.example.My_Pet.model.Modulo_1_gestion_usuario.Usuario;
import com.example.My_Pet.model.Modulo_1_gestion_usuario.PerfilUsuario;
import com.example.My_Pet.repository.Modulo_1_gestion_usuario.PerfilUsuarioRepository;
import com.example.My_Pet.repository.Modulo_1_gestion_usuario.UsuarioRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PerfilUsuarioServiceTest {

    @Mock
    private PerfilUsuarioRepository perfilUsuarioRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private PerfilUsuarioService perfilUsuarioService;

    @Test
    void guardarPerfilPersistePerfilCompletoTrasElRegistro() {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(54);
        usuario.setNombre("Ana");

        PerfilUsuario perfil = new PerfilUsuario();
        perfil.setUsuario(usuario);
        perfil.setNombreUsuario("Ana");
        perfil.setFotoPerfil("/Front%20end/fotos/ana.jpg");
        perfil.setBiografia("");
        perfil.setTelefono("");
        perfil.setCiudad("");
        perfil.setGenero("");
        perfil.setFechaNacimiento(LocalDate.of(2000, 1, 1));

        when(usuarioRepository.findById(54)).thenReturn(Optional.of(usuario));
        when(perfilUsuarioRepository.existsById(54)).thenReturn(false);

        PerfilUsuario resultado = perfilUsuarioService.guardarPerfil(perfil);

        assertEquals(54, resultado.getIdUsuario());
        assertEquals("Ana", resultado.getNombreUsuario());
        verify(entityManager).persist(resultado);
        verify(perfilUsuarioRepository, never()).save(any());
    }
}