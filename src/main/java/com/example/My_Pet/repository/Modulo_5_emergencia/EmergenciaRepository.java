package com.example.My_Pet.repository.Modulo_5_emergencia;

// Librerias del spring.

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Clases propias del proyecto.

import com.example.My_Pet.model.Modulo_5_emergencia.Emergencia;

// Librerias de Java

import java.util.List;

// Repositorio para gestionar emergencias de las mascotas en la base de datos.

@Repository
public interface EmergenciaRepository
        extends JpaRepository<Emergencia, Integer> {
    List<Emergencia> findByUsuarioIdUsuario(Integer idUsuario);
    List<Emergencia> findByEstado(String estado);
}