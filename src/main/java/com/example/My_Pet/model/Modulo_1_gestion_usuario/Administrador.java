package com.example.My_Pet.model.Modulo_1_gestion_usuario;

// Librerías para relacionar las clases Java con las tablas de la base de datos.

import jakarta.persistence.*;

// Representa la tabla "administrador" de la base de datos.

@Entity
@Table(name = "administrador")
public class Administrador {

    // Identificador único del administrador.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_admin")
    private Integer idAdmin;

    // Permisos asignados al administrador.
    @Column(length = 200, nullable = false)
    private String permisos;

    // Relaciona el administrador con el usuario al que pertenece.

    @ManyToOne
    @JoinColumn(
            name = "id_usuario",
            referencedColumnName = "id_usuario",
            nullable = false
    )
    private Usuario usuario;

    // Constructor vacío requerido para crear la entidad.

    public Administrador() {
    }

    // Constructor para crear un administrador con sus datos.

    public Administrador(
            Integer idAdmin,
            String permisos,
            Usuario usuario) {
        this.idAdmin = idAdmin;
        this.permisos = permisos;
        this.usuario = usuario;
    }

    // Getters y setters de los atributos del administrador.
    
    public Integer getIdAdmin() {
        return idAdmin;
    }

    public void setIdAdmin(Integer idAdmin) {
        this.idAdmin = idAdmin;
    }

    public String getPermisos() {
        return permisos;
    }

    public void setPermisos(String permisos) {
        this.permisos = permisos;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

}