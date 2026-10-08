package com.example.My_Pet.repository.Modulo_2_gestion_mascotas;

// Libreria del spring.

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Clase propias del proyeco.

import com.example.My_Pet.model.Modulo_2_gestion_mascota.HistorialMedico;

// Libreria de Java

import java.util.List;

// Repositorio para gestionar el historial medico de las mascotas en la base de datos.

@Repository
public interface HistorialMedicoRepository extends JpaRepository<HistorialMedico, Integer> {
    // Busca todos los registros médicos pertenecientes a una mascota
    List<HistorialMedico> findByMascotaIdMascota(Integer idMascota);
}