// Modelo que representa un recordatorio asociado a un usuario y una mascota.

package com.example.My_Pet.model.Modulo_3_Agenda_Recordatorio;

// Librerías necesarias para el mapeo de la entidad y el manejo de fechas.

import jakarta.persistence.*;

import java.time.LocalDateTime;

import com.example.My_Pet.model.Modulo_1_gestion_usuario.Usuario;
import com.example.My_Pet.model.Modulo_2_gestion_mascota.Mascota;

// Define la clase como una entidad de la base de datos.

@Entity
@Table(name = "recordatorio")
public class Recordatorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_recordatorio")
    private Integer idRecordatorio;

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

    // Constructor vacío requerido por JPA.

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

    // Getters y setters.

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