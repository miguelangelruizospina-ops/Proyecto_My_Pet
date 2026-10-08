package com.example.My_Pet.service.Modulo_4_servicio_comunidad;

// Clases propias del proyecto.

import com.example.My_Pet.model.Modulo_1_gestion_usuario.Usuario;
import com.example.My_Pet.model.Modulo_4_servicio_comunidad.Chatbot;
import com.example.My_Pet.repository.Modulo_1_gestion_usuario.UsuarioRepository;
import com.example.My_Pet.repository.Modulo_4_servicio_comunidad.ChatbotRepository;

// Librerías de JUnit para ejecutar las pruebas.

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

// Librerías de Mockito para crear objetos simulados y verificar las operaciones.

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

// Librerías de Java.

import java.util.Optional;

// Métodos utilizados para comprobar los resultados y verificar las operaciones.

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// Habilita Mockito para esta clase de pruebas.

@ExtendWith(MockitoExtension.class)
class ChatbotServiceTest {

    // Repositorio simulado para gestionar las conversaciones del chatbot.

    @Mock
    private ChatbotRepository chatbotRepository;

    // Repositorio simulado para consultar los usuarios.

    @Mock
    private UsuarioRepository usuarioRepository;

    // Servicio que será utilizado durante la prueba.

    @InjectMocks
    private ChatbotService chatbotService;

    // Verifica que un saludo genere una respuesta y se guarde sin consultar el modelo.

    @Test
    void saludoRespondeYSeGuardaSinLlamarAlModelo() {

        // Crea el usuario asociado a la conversación.

        Usuario usuario = new Usuario();

        usuario.setIdUsuario(12);

        // Simula la búsqueda del usuario existente.

        when(usuarioRepository.findById(12)).thenReturn(Optional.of(usuario));

        // Simula el guardado de la respuesta del chatbot.

        when(chatbotRepository.save(any(Chatbot.class)))
                .thenAnswer(invocacion -> invocacion.getArgument(0));

        // Ejecuta el procesamiento del mensaje enviado por el usuario.

        Chatbot respuesta =
                chatbotService.procesarMensaje(12, "¡Buenos días!");

        // Verifica que el mensaje recibido se conserve correctamente.

        assertEquals("¡Buenos días!", respuesta.getMensaje());

        // Verifica que el chatbot genere la respuesta esperada para el saludo.

        assertEquals(
                "¡Hola! Soy el asistente de My Pet. ¿En qué puedo ayudarte con el cuidado de tu mascota?",
                respuesta.getRespuesta()
        );

        // Verifica que la respuesta se guarde en el repositorio.

        verify(chatbotRepository).save(respuesta);
    }
}