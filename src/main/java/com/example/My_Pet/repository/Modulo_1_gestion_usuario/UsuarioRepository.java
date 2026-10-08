package com.example.My_Pet.repository.Modulo_1_gestion_usuario;

// Libreria del spring.

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Clases propias del proyecto

import com.example.My_Pet.model.Modulo_1_gestion_usuario.Usuario;

// Libreria de Java

import java.util.Optional;

// Repositorio para gestionar los usuarios en la base de datos.

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByCorreo(String correo);
    boolean existsByCorreoIgnoreCase(String correo);
    boolean existsByCorreoIgnoreCaseAndIdUsuarioNot(String correo, int idUsuario);
}