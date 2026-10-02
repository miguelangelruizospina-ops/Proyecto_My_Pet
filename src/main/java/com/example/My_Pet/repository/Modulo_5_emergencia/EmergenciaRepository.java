package com.example.My_Pet.repository.Modulo_5_emergencia;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.My_Pet.model.Modulo_5_emergencia.Emergencia;

import java.util.List;

// @Repository indica que esta interfaz se encarga de las transacciones CRUD en la base de datos
@Repository
public interface EmergenciaRepository extends JpaRepository<Emergencia, Integer> {
    
    // Método automático basado en nombres: Spring Data genera el SQL por debajo:
    // SELECT * FROM emergencia WHERE id_usuario = ?
    List<Emergencia> findByUsuarioIdUsuario(Integer idUsuario);
}