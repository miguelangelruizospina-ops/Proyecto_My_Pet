package com.example.My_Pet.service.Modulo_1_gestion_usuario;

//  Clases propias del proyecto

import com.example.My_Pet.model.Modulo_1_gestion_usuario.PerfilUsuario;
import com.example.My_Pet.model.Modulo_1_gestion_usuario.Usuario;
import com.example.My_Pet.repository.Modulo_1_gestion_usuario.PerfilUsuarioRepository;
import com.example.My_Pet.repository.Modulo_1_gestion_usuario.UsuarioRepository;

// Librerías de Jakarta para relacionar las clases Java con las tablas de la base de datos.

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

// Librerias del spring

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Libreria de Java

import java.util.List;
import java.util.Optional;

 // Gestiona las operaciones de consulta, registro, actualización y eliminación de perfiles de usuario en la base de datos.

@Service
public class PerfilUsuarioService {
    @Autowired
    private PerfilUsuarioRepository perfilUsuarioRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @PersistenceContext
    private EntityManager entityManager;

   // Gestiona las operaciones de consulta, registro, actualización y eliminación de perfiles de usuario en la base de datos. 

    @Transactional
    public PerfilUsuario guardarPerfil(PerfilUsuario perfilUsuario) {
        if (perfilUsuario.getUsuario() == null) {
            throw new IllegalArgumentException("El usuario es obligatorio");
        }
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

        // Asocia el perfil al usuario existente y lo guarda en la base de datos.

        perfilUsuario.setUsuario(usuarioBD);
        perfilUsuario.setIdUsuario(idUsuario);
        entityManager.persist(perfilUsuario);
        return perfilUsuario;
    }

    // Gestiona la consulta y actualización de los perfiles de usuario en la base de datos.

    public Optional<PerfilUsuario> buscarPerfilPorId(int id) {
        return perfilUsuarioRepository.findById(id);
    }
    public List<PerfilUsuario> obtenerTodosLosPerfiles() {
        return perfilUsuarioRepository.findAll();
    }
    public PerfilUsuario obtenerPerfilPorId(int id) {
        return perfilUsuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Perfil de usuario no encontrado con ID: " + id
                ));
    }
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

        // Actualiza los datos del perfil existente y guarda los cambios en la base de datos.

        perfilExistente.setNombreUsuario(datosPerfil.getNombreUsuario());
        perfilExistente.setFotoPerfil(datosPerfil.getFotoPerfil());
        perfilExistente.setBiografia(datosPerfil.getBiografia());
        perfilExistente.setTelefono(datosPerfil.getTelefono());
        perfilExistente.setCiudad(datosPerfil.getCiudad());
        perfilExistente.setGenero(datosPerfil.getGenero());
        perfilExistente.setFechaNacimiento(datosPerfil.getFechaNacimiento());
        return perfilUsuarioRepository.save(perfilExistente);
    }

    // Elimina un perfil de usuario registrado por su ID.

    @Transactional
    public void eliminarPerfil(int id) {
        PerfilUsuario perfilExistente = obtenerPerfilPorId(id);
        perfilUsuarioRepository.delete(perfilExistente);
    }
}