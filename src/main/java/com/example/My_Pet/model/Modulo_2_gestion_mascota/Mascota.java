// Modelo que representa la información de una mascota.
package com.example.My_Pet.model.Modulo_2_gestion_mascota;

// Librerías necesarias para el mapeo de la entidad y el manejo de fechas.
import jakarta.persistence.*;
import java.time.LocalDate;
import com.example.My_Pet.model.Modulo_1_gestion_usuario.Usuario;

// Define la clase como una entidad de la base de datos.
@Entity
@Table(name = "mascota")
public class Mascota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mascota")
    private Integer idMascota;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 50)
    private String especie;

    @Column(nullable = false, length = 50)
    private String raza;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Lob
    private String foto;

    // Relación entre la mascota y su dueño.
    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    // Constructor vacío requerido por JPA.
    public Mascota() {
    }

    // Constructor para crear una mascota con sus datos.
    public Mascota(Integer idMascota, String nombre, String especie,
                   String raza, LocalDate fechaNacimiento,
                   String foto, Usuario usuario) {
        this.idMascota = idMascota;
        this.nombre = nombre;
        this.especie = especie;
        this.raza = raza;
        this.fechaNacimiento = fechaNacimiento;
        this.foto = foto;
        this.usuario = usuario;
    }

    // Getters y setters- Métodos para obtener y modificar los datos.
    public Integer getIdMascota() {
        return idMascota;
    }

    public void setIdMascota(Integer idMascota) {
        this.idMascota = idMascota;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

}