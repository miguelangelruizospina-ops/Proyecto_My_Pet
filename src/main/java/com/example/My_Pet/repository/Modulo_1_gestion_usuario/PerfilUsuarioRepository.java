package com.example.My_Pet.repository.Modulo_1_gestion_usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.My_Pet.model.Modulo_1_gestion_usuario.PerfilUsuario;

@Repository
public interface PerfilUsuarioRepository extends JpaRepository<PerfilUsuario, Integer> {

}