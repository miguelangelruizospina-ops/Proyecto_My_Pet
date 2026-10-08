package com.example.My_Pet.repository.Modulo_1_gestion_usuario;

// Libreria del spring.

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Clase propia del proyecto.

import com.example.My_Pet.model.Modulo_1_gestion_usuario.PerfilUsuario;

// Repositorio para gestionar los perfiles de los usuarios en la base de datos.

@Repository
public interface PerfilUsuarioRepository extends JpaRepository<PerfilUsuario, Integer> {
}