// Modelo que representa un comentario realizado en el foro.

package com.example.My_Pet.model.Modulo_4_servicio_comunidad;

import com.example.My_Pet.model.Modulo_1_gestion_usuario.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "comentario_foro")
public class ComentarioForo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comentario")
    private Integer idComentario;

    @Column(name = "contenido", length = 1000, nullable = false)
    private String contenido;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    // Usuario que realizó el comentario.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "id_usuario",
        referencedColumnName = "id_usuario",
        nullable = false
    )
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Usuario usuario;

    // Publicación a la que pertenece el comentario.
    // No se devuelve dentro del JSON para evitar el proxy de Hibernate
    // y evitar repetir toda la publicación en cada comentario.

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

    // Constructor vacío requerido por JPA.

    public ComentarioForo() {
    }

    // Getters y setters.

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