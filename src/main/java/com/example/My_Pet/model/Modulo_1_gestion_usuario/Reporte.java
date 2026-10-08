package com.example.My_Pet.model.Modulo_1_gestion_usuario;

// Librerías para relacionar las clases Java con las tablas de la base de datos.

import jakarta.persistence.*;

// Librería para controlar cómo se muestran los datos en JSON.

import com.fasterxml.jackson.annotation.JsonIgnore;

// Libreria de Java

import java.time.LocalDateTime;

// Representa la tabla "Reporte" de la base de datos.

@Entity
@Table(name = "reporte")
public class Reporte {

    // Identificador único del reporte.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reporte")
    private Integer idReporte;

    // Usuario que crea el reporte.
    
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

  // Datos principales del reporte.

    @Column(nullable = false, length = 1000)
    private String descripcion;

    @Column(length = 500)
    private String pagina;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(length = 1000)
    private String navegador;

    @Column(nullable = false, length = 20)
    private String estado;

    @Column(length = 2000)
    private String respuesta;

    // Constructor vacio para crear la entidad. 

    public Reporte() {
    }

     // // Getters y setters de los datos del reporte.

    public Integer getIdReporte() {
        return idReporte;
    }
    public void setIdReporte(Integer idReporte) {
        this.idReporte = idReporte;
    }
    public Usuario getUsuario() {
        return usuario;
    }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public String getPagina() {
        return pagina;
    }
    public void setPagina(String pagina) {
        this.pagina = pagina;
    }
    public LocalDateTime getFecha() {
        return fecha;
    }
    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
    public String getNavegador() {
        return navegador;
    }
    public void setNavegador(String navegador) {
        this.navegador = navegador;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public String getRespuesta() {
        return respuesta;
    }
    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }
}