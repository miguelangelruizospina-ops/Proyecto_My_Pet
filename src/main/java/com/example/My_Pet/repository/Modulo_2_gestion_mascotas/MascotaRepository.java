package com.example.My_Pet.repository.Modulo_2_gestion_mascotas;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.My_Pet.model.Modulo_2_gestion_mascota.Mascota;

import java.util.List;

@Repository
public interface MascotaRepository extends JpaRepository<Mascota, Integer> {

    // Permite buscar todas las mascotas que pertenecen a un usuario
    List<Mascota> findByUsuarioIdUsuario(Integer idUsuario);
}