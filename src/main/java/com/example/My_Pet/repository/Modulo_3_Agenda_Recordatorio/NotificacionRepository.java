package com.example.My_Pet.repository.Modulo_3_Agenda_Recordatorio;

// Librerias del Spring.

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Clases propias del proyecto.

import com.example.My_Pet.model.Modulo_3_Agenda_Recordatorio.Notificacion;

//Libreria de Java.

import java.util.List;

// Repositorio para gestionar las notificaciones de los usuarios en la base de datos.

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Integer> {
    List<Notificacion> findByUsuarioIdUsuario(Integer idUsuario);
}