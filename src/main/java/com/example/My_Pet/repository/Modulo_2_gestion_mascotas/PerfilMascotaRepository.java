package com.example.My_Pet.repository.Modulo_2_gestion_mascotas;

// Librerias del Spring. 

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Clases propias del proyecto

import com.example.My_Pet.model.Modulo_2_gestion_mascota.PerfilMascota;

// Libreria Java
import java.util.Optional;

// Repositorio para gestionar el perfil de la mascota en la base de datos.
@Repository
public interface PerfilMascotaRepository extends JpaRepository<PerfilMascota, Integer> {
    Optional<PerfilMascota> findByMascotaIdMascota(Integer idMascota);
}