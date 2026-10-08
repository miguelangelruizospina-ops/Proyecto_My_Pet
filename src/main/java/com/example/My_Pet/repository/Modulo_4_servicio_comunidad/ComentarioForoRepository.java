package com.example.My_Pet.repository.Modulo_4_servicio_comunidad;

// Clases propias del proyecto.

import com.example.My_Pet.model.Modulo_4_servicio_comunidad.ComentarioForo;

// Librerias del spring.

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Libreria de Java

import java.util.List;

// Repositorio para gestionar los comentatios del foro en la base de datos.

@Repository
public interface ComentarioForoRepository
        extends JpaRepository<ComentarioForo, Integer> {
    List<ComentarioForo> findByPublicacionIdPublicacion(Integer idPublicacion);
    List<ComentarioForo> findByComentarioPadreIdComentario(Integer idComentarioPadre);
}