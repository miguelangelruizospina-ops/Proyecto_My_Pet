package com.example.My_Pet.repository.Modulo_1_gestion_usuario;

import com.example.My_Pet.model.Modulo_1_gestion_usuario.Reporte;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/*
 * Repositorio de reportes.
 * Permite realizar las operaciones de acceso a la tabla reporte.
 */

@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Integer> {


    /*
     * Consulta los reportes asociados a un usuario.
     */

    List<Reporte> findByUsuarioIdUsuario(Integer idUsuario);

}