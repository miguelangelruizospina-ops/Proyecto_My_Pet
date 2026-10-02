// Definición del paquete: ubica esta interfaz dentro de la capa de acceso a datos (Repositorios)
package com.example.My_Pet.repository.Modulo_3_Agenda_Recordatorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.My_Pet.model.Modulo_3_Agenda_Recordatorio.Notificacion;

import java.util.List;

// @Repository: Indica que esta interfaz es un componente de persistencia que maneja las operaciones CRUD con la base de datos
@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Integer> {
    
    // Query Method (Método derivado): Spring Data JPA interpreta el nombre del método y genera el SQL automáticamente en tiempo de ejecución
    // La convención camello estructura la consulta equivalente a: SELECT * FROM notificacion WHERE id_usuario = ?
    List<Notificacion> findByUsuarioIdUsuario(Integer idUsuario);
}