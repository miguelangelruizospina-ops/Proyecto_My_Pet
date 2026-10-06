package com.example.My_Pet.repository.Modulo_4_servicio_comunidad;

import com.example.My_Pet.model.Modulo_4_servicio_comunidad.MeGustaPublicacion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MeGustaPublicacionRepository
        extends JpaRepository<MeGustaPublicacion, Integer> {

    // Busca si un usuario ya dio Me gusta a una publicación.
    //
    // Si existe:
    //     → el usuario ya dio Me gusta.
    //
    // Si no existe:
    //     → el usuario todavía no ha dado Me gusta.
    Optional<MeGustaPublicacion>
    findByUsuarioIdUsuarioAndPublicacionIdPublicacion(
            Integer idUsuario,
            Integer idPublicacion
    );


    // Cuenta la cantidad total de Me gusta
    // que tiene una publicación.
    long countByPublicacionIdPublicacion(Integer idPublicacion);
}