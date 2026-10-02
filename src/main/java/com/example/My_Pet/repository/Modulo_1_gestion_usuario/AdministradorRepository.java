package com.example.My_Pet.repository.Modulo_1_gestion_usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.My_Pet.model.Modulo_1_gestion_usuario.Administrador;

import java.util.Optional;

@Repository
public interface AdministradorRepository
        extends JpaRepository<Administrador, Integer> {

    // Permite buscar un administrador usando el ID del usuario
    Optional<Administrador> findByUsuarioIdUsuario(Integer idUsuario);
}