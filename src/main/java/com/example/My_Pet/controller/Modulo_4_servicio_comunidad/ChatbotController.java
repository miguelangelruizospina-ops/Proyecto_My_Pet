package com.example.My_Pet.controller.Modulo_4_servicio_comunidad;

// Clases propias del proyecto.

import com.example.My_Pet.model.Modulo_4_servicio_comunidad.Chatbot;
import com.example.My_Pet.service.Modulo_4_servicio_comunidad.ChatbotService;

// Librerías de Spring.

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


// Librerías de Java.

import java.util.List;

// Controlador para la gestión del chatbot.

@RestController
@RequestMapping("/api/chatbot")
public class ChatbotController {

    // Conecta el controlador con el servicio del chatbot.

    @Autowired
    private ChatbotService chatbotService;

    //Envía un mensaje al chatbot y procesa la respuesta para el usuario.

    @PostMapping("/mensaje")
    @PreAuthorize("@autorizacion.esUsuarioPropioOAdministrador(#p0)")
    public Chatbot enviarMensaje(
            @RequestParam("idUsuario") Integer idUsuario,
            @RequestParam("mensaje") String mensaje) {
        return chatbotService.procesarMensaje(idUsuario, mensaje);
    }

    // Obtiene el historial de mensajes de un usuario registrado.

    @GetMapping("/historial/{idUsuario}")
    @PreAuthorize("@autorizacion.esUsuarioPropioOAdministrador(#p0)")
    public List<Chatbot> obtenerHistorial(
            @PathVariable(value = "idUsuario") Integer idUsuario) {
        return chatbotService.obtenerHistorial(idUsuario);
    }

    // Elimina un mensaje del chatbot por su ID.

    @DeleteMapping("/eliminar/{idChatbot}")
    @PreAuthorize("@autorizacion.esChatbotPropioOAdministrador(#p0)")
    public String eliminarMensaje(
            @PathVariable(value = "idChatbot") Integer idChatbot) {
        chatbotService.eliminarMensaje(idChatbot);
        return "Mensaje eliminado correctamente";
    }
}