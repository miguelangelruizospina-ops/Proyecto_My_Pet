package com.example.My_Pet.model.Modulo_4_servicio_comunidad;

import com.example.My_Pet.model.Modulo_1_gestion_usuario.Usuario;

import jakarta.persistence.*;
import java.time.LocalDateTime;

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

    // =========================================================
    // ID DEL ME GUSTA
    // =========================================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_me_gusta")
    private Integer idMeGusta;


    // =========================================================
    // USUARIO QUE DIO EL ME GUSTA
    // =========================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "id_usuario",
        referencedColumnName = "id_usuario",
        nullable = false
    )
    private Usuario usuario;


    // =========================================================
    // PUBLICACIÓN A LA QUE SE LE DIO ME GUSTA
    // =========================================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "id_publicacion",
        referencedColumnName = "id_publicacion",
        nullable = false
    )
    private PublicacionForo publicacion;


    // =========================================================
    // FECHA DEL ME GUSTA
    // =========================================================

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public MeGustaPublicacion() {
    }


    // =========================================================
    // GETTERS Y SETTERS
    // =========================================================

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