package com.example.My_Pet.repository.Modulo_4_servicio_comunidad;

// Librerias del spring.

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Clase propia del proyecto.

import com.example.My_Pet.model.Modulo_4_servicio_comunidad.Chatbot;

// Libreria de java.

import java.util.List;
// Repositorio para gestionar las conversaciones de los usuarios con el chatbot en la base de datos.

@Repository
public interface ChatbotRepository extends JpaRepository<Chatbot, Integer> {
    List<Chatbot> findByUsuarioIdUsuario(Integer idUsuario);
}