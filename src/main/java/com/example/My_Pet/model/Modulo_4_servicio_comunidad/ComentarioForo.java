package com.example.My_Pet.model.Modulo_4_servicio_comunidad;

// Clases propias del proyecto.

import com.example.My_Pet.model.Modulo_1_gestion_usuario.Usuario;

// Librerías para controlar cómo se manejan los datos en JSON.

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// Librerías para relacionar las clases Java con las tablas de la base de datos.

import jakarta.persistence.*;

// Libreria de Java

import java.time.LocalDateTime;

// Representa la tabla "comentario" de la base de datos.

@Entity
@Table(name = "comentario_foro")
public class ComentarioForo {

    // Identificador unico de un comentario.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comentario")
    private Integer idComentario;

    // Informacion del comentario creado. 

    @Column(name = "contenido", length = 1000, nullable = false)
    private String contenido;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    // Usuario que realizó el comentario respondiendo a una publicacion del foro.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "id_usuario",
        referencedColumnName = "id_usuario",
        nullable = false
    )
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Usuario usuario;

    // Publicación a la que pertenece el comentario.
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "id_publicacion",
        referencedColumnName = "id_publicacion",
        nullable = false
    )
    @JsonIgnore
    private PublicacionForo publicacion;

    // Comentario padre cuando se trata de una respuesta.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "id_comentario_padre",
        referencedColumnName = "id_comentario"
    )
    private ComentarioForo comentarioPadre;

    // Constructor vacío requerido para crear la entidad comentario foro.

    public ComentarioForo() {
    }

    // Getters y setters de los datos del comentario del foro.

    public Integer getIdComentario() {
        return idComentario;
    }
    public void setIdComentario(Integer idComentario) {
        this.idComentario = idComentario;
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
    public PublicacionForo getPublicacion() {
        return publicacion;
    }
    public void setPublicacion(PublicacionForo publicacion) {
        this.publicacion = publicacion;
    }
    public ComentarioForo getComentarioPadre() {
        return comentarioPadre;
    }
    public void setComentarioPadre(ComentarioForo comentarioPadre) {
        this.comentarioPadre = comentarioPadre;
    }
}