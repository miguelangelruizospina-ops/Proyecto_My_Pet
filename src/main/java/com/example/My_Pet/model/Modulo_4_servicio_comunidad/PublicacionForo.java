// Modelo que representa una publicación realizada en el foro.
package com.example.My_Pet.model.Modulo_4_servicio_comunidad;

// Librerías necesarias para el mapeo de la entidad y el manejo de fechas.
import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.example.My_Pet.model.Modulo_1_gestion_usuario.Usuario;

// Define la clase como una entidad de la base de datos.
@Entity
@Table(name = "publicacion_foro")
public class PublicacionForo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_publicacion")
    private Integer idPublicacion;

    @Column(length = 200, nullable = false)
    private String titulo;

    @Column(length = 500, nullable = false)
    private String contenido;

    @Column(name = "fecha", nullable = false, updatable = false)
    private LocalDateTime fecha;

    // Relación entre la publicación y el usuario que la creó.
    @ManyToOne
    @JoinColumn(
        name = "id_usuario",
        referencedColumnName = "id_usuario",
        nullable = false
    )
    private Usuario usuario;

    // Constructor vacío requerido por JPA.
    public PublicacionForo() {
    }

    // Constructor para crear una publicación con sus datos.
    public PublicacionForo(
            Integer idPublicacion,
            String titulo,
            String contenido,
            LocalDateTime fecha,
            Usuario usuario) {
        this.idPublicacion = idPublicacion;
        this.titulo = titulo;
        this.contenido = contenido;
        this.fecha = fecha;
        this.usuario = usuario;
    }

    // Getters y setters- Métodos para obtener y modificar los datos.
    public Integer getIdPublicacion() {
        return idPublicacion;
    }

    public void setIdPublicacion(Integer idPublicacion) {
        this.idPublicacion = idPublicacion;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
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

}