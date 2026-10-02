package com.example.My_Pet.repository.Modulo_4_servicio_comunidad;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.My_Pet.model.Modulo_4_servicio_comunidad.Servicio;

import java.util.List;

// @Repository marca este componente como la capa de acceso a datos (DAO)
// Al extender de JpaRepository, ya incluye los métodos nativos para guardar, listar y borrar
@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Integer> {
    
    // Método personalizado automático: Spring Data JPA interpreta el nombre "findByTipo"
    // y genera por debajo la consulta: SELECT * FROM servicio WHERE tipo = ?
    List<Servicio> findByTipo(String tipo);
}