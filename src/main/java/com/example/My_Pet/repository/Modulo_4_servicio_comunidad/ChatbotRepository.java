package com.example.My_Pet.repository.Modulo_4_servicio_comunidad;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.My_Pet.model.Modulo_4_servicio_comunidad.Chatbot;

import java.util.List;

@Repository
public interface ChatbotRepository extends JpaRepository<Chatbot, Integer> {

    List<Chatbot> findByUsuarioIdUsuario(Integer idUsuario);

}