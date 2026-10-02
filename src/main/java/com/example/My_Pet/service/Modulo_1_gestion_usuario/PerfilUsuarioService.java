// Servicio encargado de gestionar los perfiles de usuario.

// Paquete donde se encuentra el servicio.

package com.example.My_Pet.service.Modulo_1_gestion_usuario;

// Librerías necesarias para el servicio.

import com.example.My_Pet.model.Modulo_1_gestion_usuario.PerfilUsuario;
import com.example.My_Pet.model.Modulo_1_gestion_usuario.Usuario;
import com.example.My_Pet.repository.Modulo_1_gestion_usuario.PerfilUsuarioRepository;
import com.example.My_Pet.repository.Modulo_1_gestion_usuario.UsuarioRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

// Define la clase como un servicio de Spring.

@Service
public class PerfilUsuarioService {

    @Autowired
    private PerfilUsuarioRepository perfilUsuarioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PersistenceContext
    private EntityManager entityManager;

    // Registra un nuevo perfil de usuario.
    @Transactional
    public PerfilUsuario guardarPerfil(PerfilUsuario perfilUsuario) {

        // Verifica que el perfil esté asociado a un usuario.
        if (perfilUsuario.getUsuario() == null) {
            throw new IllegalArgumentException("El usuario es obligatorio");
        }

        // Verifica que la fecha de nacimiento haya sido proporcionada.
        if (perfilUsuario.getFechaNacimiento() == null) {
            throw new IllegalArgumentException(
                    "La fecha de nacimiento es obligatoria"
            );
        }

        int idUsuario = perfilUsuario.getUsuario().getIdUsuario();

        // Busca el usuario existente en la base de datos.
        Usuario usuarioBD = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException(
                        "El usuario con ID " + idUsuario + " no existe"
                ));

        // Verifica que el usuario no tenga ya un perfil.
        if (perfilUsuarioRepository.existsById(idUsuario)) {
            throw new IllegalArgumentException(
                    "El usuario con ID " + idUsuario + " ya tiene un perfil"
            );
        }

        // Asocia el usuario existente al perfil.
        perfilUsuario.setUsuario(usuarioBD);

        // @MapsId utiliza el mismo ID del usuario para el perfil.
        perfilUsuario.setIdUsuario(idUsuario);

        entityManager.persist(perfilUsuario);

        return perfilUsuario;
    }

    // Busca un perfil por su ID y devuelve un resultado opcional.
    public Optional<PerfilUsuario> buscarPerfilPorId(int id) {
        return perfilUsuarioRepository.findById(id);
    }

    // Lista todos los perfiles registrados.
    public List<PerfilUsuario> obtenerTodosLosPerfiles() {
        return perfilUsuarioRepository.findAll();
    }

    // Busca un perfil por su ID.
    public PerfilUsuario obtenerPerfilPorId(int id) {
        return perfilUsuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Perfil de usuario no encontrado con ID: " + id
                ));
    }

    // Actualiza los datos de un perfil.
    @Transactional
    public PerfilUsuario actualizarPerfil(
            int id,
            PerfilUsuario datosPerfil) {

        PerfilUsuario perfilExistente = obtenerPerfilPorId(id);

        // Verifica que la fecha de nacimiento haya sido proporcionada.
        if (datosPerfil.getFechaNacimiento() == null) {
            throw new IllegalArgumentException(
                    "La fecha de nacimiento es obligatoria"
            );
        }

        perfilExistente.setNombreUsuario(datosPerfil.getNombreUsuario());
        perfilExistente.setFotoPerfil(datosPerfil.getFotoPerfil());
        perfilExistente.setBiografia(datosPerfil.getBiografia());
        perfilExistente.setTelefono(datosPerfil.getTelefono());
        perfilExistente.setCiudad(datosPerfil.getCiudad());
        perfilExistente.setGenero(datosPerfil.getGenero());
        perfilExistente.setFechaNacimiento(datosPerfil.getFechaNacimiento());

        return perfilUsuarioRepository.save(perfilExistente);
    }

    // Elimina un perfil por su ID.
    @Transactional
    public void eliminarPerfil(int id) {

        PerfilUsuario perfilExistente = obtenerPerfilPorId(id);

        perfilUsuarioRepository.delete(perfilExistente);
    }
}