package com.example.My_Pet.repository.Modulo_4_servicio_comunidad;

// Librerias del spring.

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Clases propias del proyecto.

import com.example.My_Pet.model.Modulo_4_servicio_comunidad.Servicio;

// Libreria de Java

import java.util.List;

// Repositorio para gestionar los servicios registrados por el usuario en la base de datos.

@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Integer> {
    List<Servicio> findByTipo(String tipo);
}