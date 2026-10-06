package com.example.My_Pet.repository.Modulo_4_servicio_comunidad;

import com.example.My_Pet.model.Modulo_4_servicio_comunidad.MeGustaComentario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

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