package com.example.My_Pet.repository.Modulo_4_servicio_comunidad;

import com.example.My_Pet.model.Modulo_4_servicio_comunidad.ComentarioForo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComentarioForoRepository
        extends JpaRepository<ComentarioForo, Integer> {

    List<ComentarioForo> findByPublicacionIdPublicacion(Integer idPublicacion);

    List<ComentarioForo> findByComentarioPadreIdComentario(Integer idComentarioPadre);
}