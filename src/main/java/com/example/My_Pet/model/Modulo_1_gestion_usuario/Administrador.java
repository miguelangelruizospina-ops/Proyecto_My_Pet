// Modelo que representa la información de un administrador.
package com.example.My_Pet.model.Modulo_1_gestion_usuario;

// Librerías necesarias para el mapeo de la entidad.
import jakarta.persistence.*;

// Define la clase como una entidad de la base de datos.
@Entity
@Table(name = "administrador")
public class Administrador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_admin")
    private Integer idAdmin;

    @Column(length = 200, nullable = false)
    private String permisos;

    // Relación entre el administrador y el usuario.
    @ManyToOne
    @JoinColumn(
            name = "id_usuario",
            referencedColumnName = "id_usuario",
            nullable = false
    )
    private Usuario usuario;

    // Constructor vacío requerido por JPA.
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

    // Getters y setters.
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