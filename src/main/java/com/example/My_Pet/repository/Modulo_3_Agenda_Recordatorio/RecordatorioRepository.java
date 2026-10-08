package com.example.My_Pet.repository.Modulo_3_Agenda_Recordatorio;

// Librerías del spring.

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Clases propias del proyecto.

import com.example.My_Pet.model.Modulo_3_Agenda_Recordatorio.Recordatorio;

// Liberias de Java

import java.time.LocalDateTime;
import java.util.List;

// Repositorio para gestionar los recordatorios de los usuarios en la base de datos.

@Repository
public interface RecordatorioRepository
        extends JpaRepository<Recordatorio, Integer> {
    List<Recordatorio> findByUsuarioIdUsuario(Integer idUsuario);
    List<Recordatorio> findByMascotaIdMascota(Integer idMascota);
    List<Recordatorio> findByEstadoAndFechaLessThanEqual(
            String estado,
            LocalDateTime fecha);
}