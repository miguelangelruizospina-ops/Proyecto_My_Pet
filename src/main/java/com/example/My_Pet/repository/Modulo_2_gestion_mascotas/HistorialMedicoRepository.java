package com.example.My_Pet.repository.Modulo_2_gestion_mascotas;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.My_Pet.model.Modulo_2_gestion_mascota.HistorialMedico;

import java.util.List;

@Repository
public interface HistorialMedicoRepository extends JpaRepository<HistorialMedico, Integer> {
    // Busca todos los registros médicos pertenecientes a una mascota
    List<HistorialMedico> findByMascotaIdMascota(Integer idMascota);
}