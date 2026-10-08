package com.example.My_Pet.model.Modulo_4_servicio_comunidad;

// Librerías para relacionar las clases Java con las tablas de la base de datos.

import jakarta.persistence.*;

// Libreria Java

import java.time.LocalDateTime;

//Clases propias del poryecto.

import com.example.My_Pet.model.Modulo_1_gestion_usuario.Usuario;

// Librerías para controlar cómo se manejan los datos en JSON.

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// Representa la tabla "publicacion foro" de la base de datos.

@Entity
@Table(name = "publicacion_foro")
public class PublicacionForo {

    // Identificador unico de una publicacion en el foro.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_publicacion")
    private Integer idPublicacion;

    // Informacion de la publicacion en el foro

    @Column(length = 200, nullable = false)
    private String titulo;

    @Column(length = 500, nullable = false)
    private String contenido;

    @Column(name = "fecha", nullable = false, updatable = false)
    private LocalDateTime fecha;

    // Relación entre la publicación y el usuario que la creó.

    @ManyToOne
    @JoinColumn(
        name = "id_usuario",
        referencedColumnName = "id_usuario",
        nullable = false
    )
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Usuario usuario;

    // Constructor vacío requerido para crear la publicacion en el foro.

    public PublicacionForo() {
    }

    // Constructor para crear una publicación con sus datos.

    public PublicacionForo(
            Integer idPublicacion,
            String titulo,
            String contenido,
            LocalDateTime fecha,
            Usuario usuario) {

        this.idPublicacion = idPublicacion;
        this.titulo = titulo;
        this.contenido = contenido;
        this.fecha = fecha;
        this.usuario = usuario;
    }

    // Getters y setters de los datos de la publicacion en el foro.

    public Integer getIdPublicacion() {
        return idPublicacion;
    }
    public void setIdPublicacion(Integer idPublicacion) {
        this.idPublicacion = idPublicacion;
    }
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public String getContenido() {
        return contenido;
    }
    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
    public LocalDateTime getFecha() {
        return fecha;
    }
    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
    public Usuario getUsuario() {
        return usuario;
    }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}