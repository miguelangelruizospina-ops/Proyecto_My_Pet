
// Repositorio encargado de gestionar los documentos almacenados.

// Paquete donde se encuentra el repositorio.
package com.example.My_Pet.repository.Modulo_2_gestion_mascotas;

// Librerías necesarias para el repositorio.

import com.example.My_Pet.model.Modulo_2_gestion_mascota.Documento;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Permite realizar operaciones de persistencia sobre los documentos.
public interface DocumentoRepository
        extends JpaRepository<Documento, Integer> {

    // Lista los documentos asociados a un usuario.
    List<Documento> findByUsuario_IdUsuario(Integer idUsuario);

    // Lista los documentos asociados a una mascota.
    List<Documento> findByMascota_IdMascota(Integer idMascota);
}
