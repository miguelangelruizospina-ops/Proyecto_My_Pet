package com.example.My_Pet.model.Modulo_1_gestion_usuario;

// Librerías para relacionar las clases Java con las tablas de la base de datos.

import jakarta.persistence.*;

// Librería para controlar cómo se muestran los datos en JSON.

import com.fasterxml.jackson.annotation.JsonProperty;

// Libreria de Java

import java.time.LocalDateTime;

// Representa la tabla "Usuari" de la base de datos.

@Entity
@Table(name = "usuario")
public class Usuario {

    // Identificador unico del usuario registrado.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private int idUsuario;

    // Datos y credenciales del usuario.

    private String nombre;
    private String apellidos;
    private String usuario;
    @Column(nullable = false, unique = true)
    private String correo;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String contrasena;
    private String rol;
    private LocalDateTime fechaCreacion;

    // Constructor vacío para crear la entidad.

    public Usuario() {
    }

   // // Getters y setters de los datos del usuario.

    public int getIdUsuario() {
        return idUsuario;
    }
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getApellidos() {
        return apellidos;
    }
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }
    public String getUsuario() {
        return usuario;
    }
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    public String getCorreo() {
        return correo;
    }
    public void setCorreo(String correo) {
        this.correo = correo;
    }
    public String getContrasena() {
        return contrasena;
    }
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
    public String getRol() {
        return rol;
    }
    public void setRol(String rol) {
        this.rol = rol;
    }
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}