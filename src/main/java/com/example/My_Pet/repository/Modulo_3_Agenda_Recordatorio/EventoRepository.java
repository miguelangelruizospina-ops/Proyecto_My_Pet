package com.example.My_Pet.repository.Modulo_3_Agenda_Recordatorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.My_Pet.model.Modulo_3_Agenda_Recordatorio.Evento;

import java.util.List;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Integer> {
    // Recupera todos los eventos de una mascota específica
    List<Evento> findByMascotaIdMascota(Integer idMascota);
}