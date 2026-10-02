package com.example.My_Pet.repository.Modulo_3_Agenda_Recordatorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.My_Pet.model.Modulo_3_Agenda_Recordatorio.Recordatorio;

import java.util.List;

@Repository
public interface RecordatorioRepository extends JpaRepository<Recordatorio, Integer> {
    // Recupera las alertas asociadas a un usuario dueño
    List<Recordatorio> findByUsuarioIdUsuario(Integer idUsuario);
}