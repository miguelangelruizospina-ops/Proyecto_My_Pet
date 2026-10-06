package com.example.My_Pet.model.Modulo_5_emergencia;

import jakarta.persistence.*;

import java.time.LocalDateTime;

import com.example.My_Pet.model.Modulo_1_gestion_usuario.Usuario;
import com.example.My_Pet.model.Modulo_2_gestion_mascota.Mascota;

@Entity
@Table(name = "emergencia")
public class Emergencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_emergencia")
    private Integer idEmergencia;

    @Column(length = 150, nullable = false)
    private String tipo;

    @Column(length = 300, nullable = false)
    private String descripcion;

    @Column(name = "fecha", nullable = false, updatable = false)
    private LocalDateTime fecha;

    @Column(length = 30, nullable = false)
    private String estado;

    @Column(name = "ultima_notificacion")
    private LocalDateTime ultimaNotificacion;

    @ManyToOne
    @JoinColumn(
        name = "id_usuario",
        referencedColumnName = "id_usuario",
        nullable = false
    )
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_mascota")
    private Mascota mascota;

    public Emergencia() {
        this.estado = "PENDIENTE";
    }

    public Emergencia(
            Integer idEmergencia,
            String tipo,
            String descripcion,
            LocalDateTime fecha,
            Usuario usuario) {

        this.idEmergencia = idEmergencia;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.estado = "PENDIENTE";
        this.usuario = usuario;
    }

    public Integer getIdEmergencia() {
        return idEmergencia;
    }

    public void setIdEmergencia(Integer idEmergencia) {
        this.idEmergencia = idEmergencia;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    public LocalDateTime getUltimaNotificacion() {
        return ultimaNotificacion;
    }

    public void setUltimaNotificacion(
            LocalDateTime ultimaNotificacion) {

        this.ultimaNotificacion = ultimaNotificacion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Mascota getMascota() {
        return mascota;
    }

    public void setMascota(Mascota mascota) {
        this.mascota = mascota;
    }
}
