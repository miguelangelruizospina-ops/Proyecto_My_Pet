package com.example.My_Pet.model.Modulo_3_Agenda_Recordatorio;

// Librerías para relacionar las clases Java con las tablas de la base de datos.

import jakarta.persistence.*;

//Libreria Java

import java.time.LocalDateTime;

//Clases propias del Proyecto.

import com.example.My_Pet.model.Modulo_2_gestion_mascota.Mascota;

// Representa la tabla "evento" de la base de datos.

@Entity
@Table(name = "evento")
public class Evento {

    // Identificador único de un evento registrado.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evento")
    private Integer idEvento;

    // Información del evento registrado.

    @Column(name = "tipo_evento", length = 150, nullable = false)
    private String tipoEvento;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(length = 300)
    private String descripcion;

    // Relación entre el evento y la mascota asociada.

    @ManyToOne
    @JoinColumn(name = "id_mascota", referencedColumnName = "id_mascota", nullable = false)
    private Mascota mascota;

    // Constructor vacío requerido para crear la entidad evento.

    public Evento() {
    }

    // Constructor para crear un evento con sus datos.

    public Evento(Integer idEvento, String tipoEvento, LocalDateTime fecha, String descripcion, Mascota mascota) {
        this.idEvento = idEvento;
        this.tipoEvento = tipoEvento;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.mascota = mascota;
    }

    // Getters y setters- Métodos para obtener y modificar los datos.

    public Integer getIdEvento() {
        return idEvento;
    }
    public void setIdEvento(Integer idEvento) {
        this.idEvento = idEvento;
    }
    public String getTipoEvento() {
        return tipoEvento;
    }
    public void setTipoEvento(String tipoEvento) {
        this.tipoEvento = tipoEvento;
    }
    public LocalDateTime getFecha() {
        return fecha;
    }
    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public Mascota getMascota() {
        return mascota;
    }
    public void setMascota(Mascota mascota) {
        this.mascota = mascota;
    }
}