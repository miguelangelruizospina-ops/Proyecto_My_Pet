
package com.example.My_Pet.repository.Modulo_5_emergencia;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.My_Pet.model.Modulo_5_emergencia.Emergencia;

import java.util.List;

@Repository
public interface EmergenciaRepository
        extends JpaRepository<Emergencia, Integer> {

    // Recupera las emergencias de un usuario.

    List<Emergencia> findByUsuarioIdUsuario(Integer idUsuario);

    // Recupera las emergencias que continúan pendientes.

    List<Emergencia> findByEstado(String estado);
}