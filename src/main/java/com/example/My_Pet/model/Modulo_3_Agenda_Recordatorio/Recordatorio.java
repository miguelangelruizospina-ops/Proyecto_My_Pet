package com.example.My_Pet.model.Modulo_3_Agenda_Recordatorio;

// Librerías para relacionar las clases Java con las tablas de la base de datos.

import jakarta.persistence.*;

//Libreria de Java

import java.time.LocalDateTime;

//Clases propias del proyecto.

import com.example.My_Pet.model.Modulo_1_gestion_usuario.Usuario;
import com.example.My_Pet.model.Modulo_2_gestion_mascota.Mascota;

// Representa la tabla "recordatorio" de la base de datos.

@Entity
@Table(name = "recordatorio")
public class Recordatorio {

    // Identifcador unico del recordatorio. 
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_recordatorio")
    private Integer idRecordatorio;

    // Información del recordatorio creado por el usuario.

    @Column(length = 300, nullable = false)
    private String mensaje;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(length = 100)
    private String estado;

    // Relación entre el recordatorio y el usuario al que pertenece.

    @ManyToOne
    @JoinColumn(
        name = "id_usuario",
        referencedColumnName = "id_usuario",
        nullable = false
    )
    private Usuario usuario;

    // Relación entre el recordatorio y la mascota a la que pertenece.

    @ManyToOne
    @JoinColumn(
        name = "id_mascota",
        referencedColumnName = "id_mascota",
        nullable = false
    )
    private Mascota mascota;

    // Constructor vacío requerido para crear la entidad recordatori.

    public Recordatorio() {
    }

    // Constructor para crear un recordatorio con sus datos.

    public Recordatorio(
            Integer idRecordatorio,
            String mensaje,
            LocalDateTime fecha,
            String estado,
            Usuario usuario,
            Mascota mascota) {

        this.idRecordatorio = idRecordatorio;
        this.mensaje = mensaje;
        this.fecha = fecha;
        this.estado = estado;
        this.usuario = usuario;
        this.mascota = mascota;
    }

    // Getters y setters de los datos del recordatorio.

    public Integer getIdRecordatorio() {
        return idRecordatorio;
    }
    public void setIdRecordatorio(Integer idRecordatorio) {
        this.idRecordatorio = idRecordatorio;
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