package com.example.My_Pet.repository.Modulo_4_servicio_comunidad;

// Librerias del spring.

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Clase propia del proyecto.

import com.example.My_Pet.model.Modulo_4_servicio_comunidad.PublicacionForo;

// Libreri de Java

import java.util.List;

// Repositorio para gestionar las publicaciones  del foro en la base de datos.

@Repository
public interface PublicacionForoRepository extends JpaRepository<PublicacionForo, Integer> {
    List<PublicacionForo> findByUsuarioIdUsuario(Integer idUsuario);
}