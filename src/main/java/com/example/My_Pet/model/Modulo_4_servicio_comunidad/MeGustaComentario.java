package com.example.My_Pet.model.Modulo_4_servicio_comunidad;

import com.example.My_Pet.model.Modulo_1_gestion_usuario.Usuario;
import jakarta.persistence.*;

import java.time.LocalDateTime;

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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_me_gusta")
    private Integer idMeGusta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "id_usuario",
        referencedColumnName = "id_usuario",
        nullable = false
    )
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "id_comentario",
        referencedColumnName = "id_comentario",
        nullable = false
    )
    private ComentarioForo comentario;

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