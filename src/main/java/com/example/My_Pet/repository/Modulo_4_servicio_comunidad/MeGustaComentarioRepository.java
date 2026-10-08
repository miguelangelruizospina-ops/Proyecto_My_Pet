package com.example.My_Pet.repository.Modulo_4_servicio_comunidad;

// Clase propia del proyecto. 

import com.example.My_Pet.model.Modulo_4_servicio_comunidad.MeGustaComentario;

// Libreria del spring.

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Libreria de Java.

import java.util.Optional;

// Repositorio para gestionar los me gusta de los  comentatios del foro en la base de datos.

@Repository
public interface MeGustaComentarioRepository
        extends JpaRepository<MeGustaComentario, Integer> {
    Optional<MeGustaComentario>
    findByUsuarioIdUsuarioAndComentarioIdComentario(
            Integer idUsuario,
            Integer idComentario
    );
    long countByComentarioIdComentario(Integer idComentario);
}