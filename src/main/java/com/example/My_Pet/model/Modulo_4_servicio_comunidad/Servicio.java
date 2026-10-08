package com.example.My_Pet.model.Modulo_4_servicio_comunidad;

// Clases propias del proyecto

import com.example.My_Pet.model.Modulo_1_gestion_usuario.Usuario;

// Librerías para relacionar las clases Java con las tablas de la base de datos.

import jakarta.persistence.*;

//  Representa la tabla "servicio" de la base de datos.

@Entity
@Table(name = "servicio")
public class Servicio {

    // Identificador unico del servicio.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_servicio")
    private Integer idServicio;

    @Column(length = 200, nullable = false)
    private String nombre;

    @Column(length = 200, nullable = false)
    private String tipo;

    @Column(length = 300, nullable = false)
    private String ubicacion;

    @Column(length = 300)
    private String descripcion;

    @Column(name = "calificacion")
    private Integer calificacion;

    // Relación entre el servicio y el usuario que lo registra.

    @ManyToOne
    @JoinColumn(
        name = "id_usuario",
        referencedColumnName = "id_usuario",
        nullable = false
    )
    private Usuario usuario;

    // Constructor vacío requerido para crear la entidad servicio.
    public Servicio() {
    }

    // Constructor para crear un servicio con sus datos.

    public Servicio(
            Integer idServicio,
            String nombre,
            String tipo,
            String ubicacion,
            String descripcion,
            Integer calificacion,
            Usuario usuario) {

        this.idServicio = idServicio;
        this.nombre = nombre;
        this.tipo = tipo;
        this.ubicacion = ubicacion;
        this.descripcion = descripcion;
        this.calificacion = calificacion;
        this.usuario = usuario;
    }

    // Getters y setters de los datos del servicio.

    public Integer getIdServicio() {
        return idServicio;
    }
    public void setIdServicio(Integer idServicio) {
        this.idServicio = idServicio;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public String getUbicacion() {
        return ubicacion;
    }
    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public Integer getCalificacion() {
        return calificacion;
    }
    public void setCalificacion(Integer calificacion) {
        this.calificacion = calificacion;
    }
    public Usuario getUsuario() {
        return usuario;
    }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}