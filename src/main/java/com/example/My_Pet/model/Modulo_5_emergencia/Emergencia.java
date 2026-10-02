// Modelo que representa una emergencia asociada a un usuario y una mascota.
package com.example.My_Pet.model.Modulo_5_emergencia;

// Librerías necesarias para el mapeo de la entidad y el manejo de fechas.
import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.example.My_Pet.model.Modulo_1_gestion_usuario.Usuario;
import com.example.My_Pet.model.Modulo_2_gestion_mascota.Mascota;

// Define la clase como una entidad de la base de datos.
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

    // Relación entre la emergencia y el usuario que la registra.
    @ManyToOne
    @JoinColumn(
        name = "id_usuario",
        referencedColumnName = "id_usuario",
        nullable = false
    )
    private Usuario usuario;

    // Relación entre la emergencia y la mascota asociada.
    @ManyToOne
    @JoinColumn(name = "id_mascota")
    private Mascota mascota;

    // Constructor vacío requerido por JPA.
    public Emergencia() {
    }

    // Constructor para crear una emergencia con sus datos.
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
        this.usuario = usuario;
    }

    // Getters y setters- Métodos para obtener y modificar los datos.
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