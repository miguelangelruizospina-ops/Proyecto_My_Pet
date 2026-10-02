// Modelo que representa los documentos asociados a un usuario y una mascota.

// Paquete donde se encuentra la entidad.
package com.example.My_Pet.model.Modulo_2_gestion_mascota;

// Librerías necesarias para la entidad y las relaciones.

import com.example.My_Pet.model.Modulo_1_gestion_usuario.Usuario;

import jakarta.persistence.*;

import java.time.LocalDate;

// Define la clase como una entidad de la base de datos.
@Entity

@Table(name = "documento")

public class Documento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_documento")
    private Integer idDocumento;

    @Column(
            name = "tipo_documento",
            length = 50,
            nullable = false
    )
    private String tipoDocumento;

    @Column(
            name = "nombre_documento",
            length = 255
    )
    private String nombreDocumento;

    @Column(name = "fecha_documento")
    private LocalDate fechaDocumento;

    @Column(
            name = "archivo",
            length = 300,
            nullable = false
    )
    private String archivo;

    // Relación entre el documento y el usuario propietario.
    @ManyToOne
    @JoinColumn(
            name = "id_usuario",
            referencedColumnName = "id_usuario",
            nullable = false
    )
    private Usuario usuario;

    // Relación entre el documento y la mascota asociada.
    @ManyToOne
    @JoinColumn(
            name = "id_mascota",
            referencedColumnName = "id_mascota",
            nullable = false
    )
    private Mascota mascota;

    // Constructor vacío requerido por JPA.
    public Documento() {
    }

    // Constructor para crear un documento con sus datos.
    public Documento(
            Integer idDocumento,
            String tipoDocumento,
            String nombreDocumento,
            LocalDate fechaDocumento,
            String archivo,
            Usuario usuario,
            Mascota mascota) {

        this.idDocumento = idDocumento;
        this.tipoDocumento = tipoDocumento;
        this.nombreDocumento = nombreDocumento;
        this.fechaDocumento = fechaDocumento;
        this.archivo = archivo;
        this.usuario = usuario;
        this.mascota = mascota;
    }

    // Getters y setters.

    public Integer getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(Integer idDocumento) {
        this.idDocumento = idDocumento;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNombreDocumento() {
        return nombreDocumento;
    }

    public void setNombreDocumento(String nombreDocumento) {
        this.nombreDocumento = nombreDocumento;
    }

    public LocalDate getFechaDocumento() {
        return fechaDocumento;
    }

    public void setFechaDocumento(LocalDate fechaDocumento) {
        this.fechaDocumento = fechaDocumento;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
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
