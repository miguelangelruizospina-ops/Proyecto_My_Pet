// Modelo que representa el perfil de un usuario.

package com.example.My_Pet.model.Modulo_1_gestion_usuario;

// Librerías necesarias para el mapeo de la entidad.

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;

// Define la clase como una entidad de la base de datos.

@Entity
@Table(name = "perfil_usuario")
public class PerfilUsuario {

    // Identificador del usuario asociado al perfil.

    @Id
    @Column(name = "id_usuario")
    private Integer idUsuario;

    // Nombre de usuario mostrado en el perfil.

    @Column(name = "nombre_usuario", nullable = false, length = 100)
    private String nombreUsuario;

    // Foto de perfil del usuario.

    @Lob
    @Column(name = "foto_perfil", nullable = false)
    private String fotoPerfil;

    // Biografía del usuario.

    @Column(nullable = false, length = 300)
    private String biografia;

    // Número de teléfono del usuario.

    @Column(nullable = false, length = 20)
    private String telefono;

    // Ciudad donde reside el usuario.

    @Column(nullable = false, length = 100)
    private String ciudad;

    // Género registrado en el perfil.

    @Column(nullable = false, length = 50)
    private String genero;

    // Fecha de nacimiento del usuario.

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    // Relación uno a uno entre el perfil y el usuario.

    @OneToOne
    @MapsId
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    // Constructor vacío requerido por JPA.

    public PerfilUsuario() {
    }

    // Getters y setters.

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