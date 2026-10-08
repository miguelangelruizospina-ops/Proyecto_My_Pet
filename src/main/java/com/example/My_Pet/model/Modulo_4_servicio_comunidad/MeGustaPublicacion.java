package com.example.My_Pet.model.Modulo_4_servicio_comunidad;

//Clase propia del proyecto.

import com.example.My_Pet.model.Modulo_1_gestion_usuario.Usuario;

// Librerías para relacionar las clases Java con las tablas de la base de datos.

import jakarta.persistence.*;

// Libreria de Java

import java.time.LocalDateTime;

// Representa la tabla "me gusta comentario" de la base de datos.

@Entity
@Table(
    name = "me_gusta_publicacion",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_like_publicacion_usuario",
            columnNames = {"id_usuario", "id_publicacion"}
        )
    }
)
public class MeGustaPublicacion {

    // Identificador unico de un me gusta de una publicacion del foro (no comentario).

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_me_gusta")
    private Integer idMeGusta;

    // Relacion del usuario que dio me gusta a la publicacion.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "id_usuario",
        referencedColumnName = "id_usuario",
        nullable = false
    )
    private Usuario usuario;

    // Relación entre el "Me gusta" y la publicación asociada.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "id_publicacion",
        referencedColumnName = "id_publicacion",
        nullable = false
    )
    private PublicacionForo publicacion;

    // Fecha en la que se realizó el "Me gusta".
    
    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    // Constructor vacío requerido para crear la entidad me gusta publicacion.

    public MeGustaPublicacion() {
    }

   // Getters y setters de los datos del "Me gusta".

    public Integer getIdMeGusta() {
        return idMeGusta;
    }

    public void setIdMeGusta(Integer idMeGusta) {
        this.idMeGusta = idMeGusta;
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

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}