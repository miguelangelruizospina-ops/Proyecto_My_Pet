package com.example.My_Pet.model.Modulo_1_gestion_usuario;

// LIBRERÍAS NECESARIAS
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

// ENTIDAD REPORTE
@Entity
@Table(name = "reporte")
public class Reporte {

    // IDENTIFICACIÓN DEL REPORTE
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reporte")
    private Integer idReporte;

    // USUARIO QUE REALIZA EL REPORTE
    // No se envía el objeto Usuario completo al frontend.
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    // INFORMACIÓN DEL PROBLEMA
    @Column(nullable = false, length = 1000)
    private String descripcion;

    @Column(length = 500)
    private String pagina;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(length = 1000)
    private String navegador;

    // ESTADO Y RESPUESTA DEL REPORTE
    @Column(nullable = false, length = 20)
    private String estado;

    @Column(length = 2000)
    private String respuesta;

    // CONSTRUCTOR
    public Reporte() {
    }

    // GETTERS Y SETTERS
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