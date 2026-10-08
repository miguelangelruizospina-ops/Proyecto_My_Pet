package com.example.My_Pet.repository.Modulo_1_gestion_usuario;

// Clases propias del proyecto.

import com.example.My_Pet.model.Modulo_1_gestion_usuario.Reporte;

//Librerias del spring.

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Libreria de Java

import java.util.List;

// Repositorio para gestionar los reportes de los usuarios en la base de datos.

@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Integer> {
    List<Reporte> findByUsuarioIdUsuario(Integer idUsuario);
}