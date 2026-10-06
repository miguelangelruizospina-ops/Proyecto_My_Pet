// Repositorio para la gestión de recordatorios de la agenda.

package com.example.My_Pet.repository.Modulo_3_Agenda_Recordatorio;

// Librerías necesarias para el repositorio y las consultas.

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.My_Pet.model.Modulo_3_Agenda_Recordatorio.Recordatorio;

import java.time.LocalDateTime;
import java.util.List;

// Define la interfaz como un repositorio de Spring.

@Repository
public interface RecordatorioRepository
        extends JpaRepository<Recordatorio, Integer> {

    // Recupera los recordatorios asociados a un usuario.

    List<Recordatorio> findByUsuarioIdUsuario(Integer idUsuario);

    // Recupera los recordatorios asociados a una mascota.

    List<Recordatorio> findByMascotaIdMascota(Integer idMascota);

    // Recupera los recordatorios pendientes cuya fecha ya llegó.

    List<Recordatorio> findByEstadoAndFechaLessThanEqual(
            String estado,
            LocalDateTime fecha);
}