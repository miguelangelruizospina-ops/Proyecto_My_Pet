// Controlador para la gestión del chatbot.
package com.example.My_Pet.controller.Modulo_4_servicio_comunidad;

// Librerías necesarias para el controlador y la seguridad.
import com.example.My_Pet.model.Modulo_4_servicio_comunidad.Chatbot;
import com.example.My_Pet.service.Modulo_4_servicio_comunidad.ChatbotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

// Define el controlador REST del chatbot.
@RestController
@RequestMapping("/api/chatbot")
public class ChatbotController {

    // Conecta el controlador con el servicio del chatbot.
    @Autowired
    private ChatbotService chatbotService;

    // POST - Envía un mensaje al chatbot.
    @PostMapping("/mensaje")
    @PreAuthorize("@autorizacion.esUsuarioPropioOAdministrador(#p0)")
    public Chatbot enviarMensaje(
            @RequestParam Integer idUsuario,
            @RequestParam String mensaje) {
        return chatbotService.procesarMensaje(idUsuario, mensaje);
    }

    // GET - Obtiene el historial de mensajes de un usuario.
    @GetMapping("/historial/{idUsuario}")
    @PreAuthorize("@autorizacion.esUsuarioPropioOAdministrador(#p0)")
    public List<Chatbot> obtenerHistorial(@PathVariable Integer idUsuario) {
        return chatbotService.obtenerHistorial(idUsuario);
    }

    // DELETE - Elimina un mensaje del chatbot por su ID.
    @DeleteMapping("/eliminar/{idChatbot}")
    @PreAuthorize("@autorizacion.esChatbotPropioOAdministrador(#p0)")
    public String eliminarMensaje(@PathVariable Integer idChatbot) {
        chatbotService.eliminarMensaje(idChatbot);
        return "Mensaje eliminado correctamente";
    }

}