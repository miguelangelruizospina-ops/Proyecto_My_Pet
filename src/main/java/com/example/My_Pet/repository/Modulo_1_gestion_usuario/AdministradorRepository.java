package com.example.My_Pet.repository.Modulo_1_gestion_usuario;

// Librerias del spring

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Clases propias del proyecto.

import com.example.My_Pet.model.Modulo_1_gestion_usuario.Administrador;

// Librerias de Java

import java.util.Optional;

// Repositorio para gestionar los administradores en la base de datos.

@Repository
public interface AdministradorRepository
        extends JpaRepository<Administrador, Integer> {
    Optional<Administrador> findByUsuarioIdUsuario(Integer idUsuario);
}