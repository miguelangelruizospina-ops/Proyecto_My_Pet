package com.example.My_Pet.model.Modulo_2_gestion_mascota;

// Clases del proyecto.

import com.example.My_Pet.model.Modulo_1_gestion_usuario.Usuario;

// Librerías para relacionar las clases Java con las tablas de la base de datos.

import jakarta.persistence.*;

//Librerias de Java

import java.time.LocalDate;

// Representa la tabla "Documento" de la base de datos.

@Entity
@Table(name = "documento")
public class Documento {

    // Identificador unido de un documento registrado.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_documento")
    private Integer idDocumento;

    // Datos  del documento registrado.

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

    // Constructor vacío para crear la entidad documento.
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

    // Getters y setters de los datos del documento registrado.

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
