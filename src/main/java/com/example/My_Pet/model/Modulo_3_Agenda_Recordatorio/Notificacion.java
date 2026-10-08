package com.example.My_Pet.model.Modulo_3_Agenda_Recordatorio;

// Librerías para relacionar las clases Java con las tablas de la base de datos.

import jakarta.persistence.*;

// Librería de Java.

import java.time.LocalDateTime;

// Clases propias del proyecto.

import com.example.My_Pet.model.Modulo_1_gestion_usuario.Usuario;

// Representa la tabla "notificacio" de la base de datos.

@Entity
@Table(name = "notificacion")
public class Notificacion {

    //  Identificador unico de la notificacion.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notificacion")
    private Integer idNotificacion;

    // Informacion de la notificacion creada.
    @Column(length = 300, nullable = false)
    private String mensaje;

    @Column(name = "fecha", nullable = false, updatable = false)
    private LocalDateTime fecha;

    @Column(length = 100)
    private String estado;


    // Tipo de notificación que permite diferenciar su origen.

    @Column(length = 50, nullable = false)
    private String tipo;


    // Relación entre la notificación y el usuario al que pertenece.

    @ManyToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario", nullable = false)
    private Usuario usuario;

    // Constructor vacío requerido para crear la entidad notificacion.

    public Notificacion() {

    }

    // Constructor para crear una notificación con sus datos.

    public Notificacion(
            Integer idNotificacion,
            String mensaje,
            LocalDateTime fecha,
            String estado,
            String tipo,
            Usuario usuario) {

        this.idNotificacion = idNotificacion;
        this.mensaje = mensaje;
        this.fecha = fecha;
        this.estado = estado;
        this.tipo = tipo;
        this.usuario = usuario;
    }

    // Getters y setters- Métodos para obtener y modificar los datos de la entidad notificacion.

    public Integer getIdNotificacion() {
        return idNotificacion;
    }
    public void setIdNotificacion(Integer idNotificacion) {
        this.idNotificacion = idNotificacion;
    }
    public String getMensaje() {
        return mensaje;
    }
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
    public LocalDateTime getFecha() {
        return fecha;
    }
    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public Usuario getUsuario() {
        return usuario;
    }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
