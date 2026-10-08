package com.example.My_Pet.model.Modulo_4_servicio_comunidad;

// Librerías para relacionar las clases Java con las tablas de la base de datos.

import jakarta.persistence.*;

//Librerias de Java

import java.time.LocalDateTime;

//Clases propias del proyecto. 

import com.example.My_Pet.model.Modulo_1_gestion_usuario.Usuario;

// Representa la tabla "Chatbot" de la base de datos.

@Entity
@Table(name = "chatbot")
public class Chatbot {

    // Identificador unico de una conversacion

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_chatbot")
    private Integer idChatbot;

    // Relación entre el mensaje del chatbot y el usuario.

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    // Informacion del mensaje creado

    @Column(name = "mensaje", nullable = false, columnDefinition = "TEXT")
    private String mensaje;

    @Column(name = "respuesta", nullable = false, columnDefinition = "TEXT")
    private String respuesta;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    // Constructor vacío requerido para crear la conversacion.
    
    public Chatbot() {
    }

    // Getters y setters de los datos de la conservacion.

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