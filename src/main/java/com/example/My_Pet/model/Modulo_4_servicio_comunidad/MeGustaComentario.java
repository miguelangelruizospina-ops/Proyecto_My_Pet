package com.example.My_Pet.model.Modulo_4_servicio_comunidad;

// Clase propia del proyecto.

import com.example.My_Pet.model.Modulo_1_gestion_usuario.Usuario;

// Librerías para relacionar las clases Java con las tablas de la base de datos.

import jakarta.persistence.*;

// Libreria de Java

import java.time.LocalDateTime;

// Representa la tabla "me gusta comentario" de la base de datos.

@Entity
@Table(
    name = "me_gusta_comentario",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_like_comentario_usuario",
            columnNames = {"id_usuario", "id_comentario"}
        )
    }
)
public class MeGustaComentario {

    //Identificador unico de un me gusta comentario.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_me_gusta")
    private Integer idMeGusta;

     // Relación entre el "Me gusta" y el usuario que lo realizó.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "id_usuario",
        referencedColumnName = "id_usuario",
        nullable = false
    )
    private Usuario usuario;

    // Relación entre el "Me gusta" y el comentario asociado.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "id_comentario",
        referencedColumnName = "id_comentario",
        nullable = false
    )
    private ComentarioForo comentario;

    //Getters y setters de los datos del "Me gusta".

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;
    public MeGustaComentario() {
    }
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
    public ComentarioForo getComentario() {
        return comentario;
    }
    public void setComentario(ComentarioForo comentario) {
        this.comentario = comentario;
    }
    public LocalDateTime getFecha() {
        return fecha;
    }
    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}