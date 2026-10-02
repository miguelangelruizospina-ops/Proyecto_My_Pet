package com.example.My_Pet.service.Modulo_4_servicio_comunidad;

import com.example.My_Pet.model.Modulo_1_gestion_usuario.Usuario;
import com.example.My_Pet.model.Modulo_4_servicio_comunidad.Chatbot;
import com.example.My_Pet.repository.Modulo_1_gestion_usuario.UsuarioRepository;
import com.example.My_Pet.repository.Modulo_4_servicio_comunidad.ChatbotRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatbotServiceTest {

    @Mock
    private ChatbotRepository chatbotRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private ChatbotService chatbotService;

    @Test
    void saludoRespondeYSeGuardaSinLlamarAlModelo() {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(12);
        when(usuarioRepository.findById(12)).thenReturn(Optional.of(usuario));
        when(chatbotRepository.save(any(Chatbot.class)))
                .thenAnswer(invocacion -> invocacion.getArgument(0));

        Chatbot respuesta = chatbotService.procesarMensaje(12, "¡Buenos días!");

        assertEquals("¡Buenos días!", respuesta.getMensaje());
        assertEquals(
                "¡Hola! Soy el asistente de My Pet. ¿En qué puedo ayudarte con el cuidado de tu mascota?",
                respuesta.getRespuesta()
        );
        verify(chatbotRepository).save(respuesta);
    }
}