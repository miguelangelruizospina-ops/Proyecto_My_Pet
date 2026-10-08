package com.example.My_Pet.repository.Modulo_2_gestion_mascotas;

// Clases propias del proyecto.

import com.example.My_Pet.model.Modulo_2_gestion_mascota.Documento;

// Librerias del spring.

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Libreria de Java

import java.util.List;

// Repositorio para gestionar los documentos de las mascotas en la base de datos.

@Repository
public interface DocumentoRepository
        extends JpaRepository<Documento, Integer> {
    List<Documento> findByUsuario_IdUsuario(Integer idUsuario);
    List<Documento> findByMascota_IdMascota(Integer idMascota);
}
