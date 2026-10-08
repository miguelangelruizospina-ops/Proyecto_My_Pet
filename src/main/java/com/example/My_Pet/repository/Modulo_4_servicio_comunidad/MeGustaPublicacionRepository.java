package com.example.My_Pet.repository.Modulo_4_servicio_comunidad;

// Clase propia del proyecto.

import com.example.My_Pet.model.Modulo_4_servicio_comunidad.MeGustaPublicacion;

// Librerias del spring.

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Libreria de Java.

import java.util.Optional;

// Repositorio para gestionar los me gusta de las publicaciones  del foro en la base de datos.

@Repository
public interface MeGustaPublicacionRepository
        extends JpaRepository<MeGustaPublicacion, Integer> {
    Optional<MeGustaPublicacion>
    findByUsuarioIdUsuarioAndPublicacionIdPublicacion(
            Integer idUsuario,
            Integer idPublicacion
    );
    long countByPublicacionIdPublicacion(Integer idPublicacion);
}