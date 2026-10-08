package com.example.My_Pet.repository.Modulo_2_gestion_mascotas;

// Librerias del Spring.

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//Clases propias del proyecto.

import com.example.My_Pet.model.Modulo_2_gestion_mascota.Mascota;

//Libreria Java

import java.util.List;

// Repositorio para gestionar las mascotas registradas en la base de datos.

@Repository
public interface MascotaRepository extends JpaRepository<Mascota, Integer> {
    List<Mascota> findByUsuarioIdUsuario(Integer idUsuario);
}