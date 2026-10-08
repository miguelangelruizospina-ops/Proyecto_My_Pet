package com.example.My_Pet.repository.Modulo_3_Agenda_Recordatorio;

// Libreria del spring.

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Clases propias del proyecto. 

import com.example.My_Pet.model.Modulo_3_Agenda_Recordatorio.Evento;

// Libreria de Java

import java.util.List;

// Repositorio para gestionar los eventos de las mascotas en la base de datos.

@Repository
public interface EventoRepository extends JpaRepository<Evento, Integer> {
    // Recupera todos los eventos de una mascota específica
    List<Evento> findByMascotaIdMascota(Integer idMascota);
}