// Modelo que representa una conversación entre un usuario y el chatbot.
package com.example.My_Pet.model.Modulo_4_servicio_comunidad;

// Librerías necesarias para el mapeo de la entidad y el manejo de fechas.
import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.example.My_Pet.model.Modulo_1_gestion_usuario.Usuario;

// Define la clase como una entidad de la base de datos.
@Entity
@Table(name = "chatbot")
public class Chatbot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_chatbot")
    private Integer idChatbot;

    // Relación entre el mensaje del chatbot y el usuario.
    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "mensaje", nullable = false, columnDefinition = "TEXT")
    private String mensaje;

    @Column(name = "respuesta", nullable = false, columnDefinition = "TEXT")
    private String respuesta;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    // Constructor vacío requerido por JPA.
    public Chatbot() {
    }

    // Getters y setters- Métodos para obtener y modificar los datos.
    public Integer getIdChatbot() {
        return idChatbot;
    }

    public void setIdChatbot(Integer idChatbot) {
        this.idChatbot = idChatbot;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

}