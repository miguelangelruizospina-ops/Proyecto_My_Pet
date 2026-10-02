package com.example.My_Pet.repository.Modulo_4_servicio_comunidad;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.My_Pet.model.Modulo_4_servicio_comunidad.PublicacionForo;

import java.util.List;

// @Repository marca este componente como la capa de acceso a datos (DAO)
@Repository
public interface PublicacionForoRepository extends JpaRepository<PublicacionForo, Integer> {
    
    // Método personalizado automático para listar todas las publicaciones de un usuario específico
    // Genera por debajo: SELECT * FROM publicacion_foro WHERE id_usuario = ?
    List<PublicacionForo> findByUsuarioIdUsuario(Integer idUsuario);
}