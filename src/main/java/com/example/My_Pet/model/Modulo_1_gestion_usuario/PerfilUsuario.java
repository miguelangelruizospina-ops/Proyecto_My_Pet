package com.example.My_Pet.model.Modulo_1_gestion_usuario;

// Librerías para relacionar las clases Java con las tablas de la base de datos.

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

// Librerias de Java

import java.time.LocalDate;

// Representa la tabla "perfil de usuario" de la base de datos.

@Entity
@Table(name = "perfil_usuario")
public class PerfilUsuario {

    // Identificador del usuario asociado al perfil.

    @Id
    @Column(name = "id_usuario")
    private Integer idUsuario;

   // Datos principales del perfil del usuario.

    @Column(name = "nombre_usuario", nullable = false, length = 100)
    private String nombreUsuario;
    
    @Lob
    @Column(name = "foto_perfil", nullable = false)
    private String fotoPerfil;

    @Column(nullable = false, length = 300)
    private String biografia;

    @Column(nullable = false, length = 20)
    private String telefono;

    @Column(nullable = false, length = 100)
    private String ciudad;

    @Column(nullable = false, length = 50)
    private String genero;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    // Relación uno a uno entre el perfil y el usuario.

    @OneToOne
    @MapsId
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    // Constructor vacío requerido para crear la entidad.

    public PerfilUsuario() {
    }

    // // Getters y setters de los datos del perfil.

    public Integer getIdUsuario() {
        return idUsuario;
    }
    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }
    public String getNombreUsuario() {
        return nombreUsuario;
    }
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
    public String getFotoPerfil() {
        return fotoPerfil;
    }
    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }
    public String getBiografia() {
        return biografia;
    }
    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }
    public String getTelefono() {
        return telefono;
    }
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    public String getCiudad() {
        return ciudad;
    }
    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }
    public String getGenero() {
        return genero;
    }
    public void setGenero(String genero) {
        this.genero = genero;
    }
    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }
    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
    public Usuario getUsuario() {
        return usuario;
    }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}