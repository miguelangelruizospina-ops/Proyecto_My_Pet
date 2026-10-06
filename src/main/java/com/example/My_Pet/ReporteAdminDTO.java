package com.example.My_Pet;

import com.example.My_Pet.model.Modulo_1_gestion_usuario.Reporte;
import com.example.My_Pet.model.Modulo_1_gestion_usuario.Usuario;

import java.time.LocalDateTime;

// DTO PARA MOSTRAR LOS REPORTES EN EL PANEL DEL ADMINISTRADOR
public class ReporteAdminDTO {

    private Integer idReporte;
    private UsuarioResumen usuario;
    private String descripcion;
    private String pagina;
    private LocalDateTime fecha;
    private String navegador;
    private String estado;
    private String respuesta;

    // CONSTRUCTOR
    public ReporteAdminDTO(Reporte reporte) {

        this.idReporte = reporte.getIdReporte();
        this.descripcion = reporte.getDescripcion();
        this.pagina = reporte.getPagina();
        this.fecha = reporte.getFecha();
        this.navegador = reporte.getNavegador();
        this.estado = reporte.getEstado();
        this.respuesta = reporte.getRespuesta();

        Usuario usuarioReporte = reporte.getUsuario();

        if (usuarioReporte != null) {
            this.usuario = new UsuarioResumen(
                    usuarioReporte.getIdUsuario(),
                    usuarioReporte.getNombre(),
                    usuarioReporte.getApellidos(),
                    usuarioReporte.getUsuario()
            );
        }
    }

    // GETTERS

    public Integer getIdReporte() {
        return idReporte;
    }

    public UsuarioResumen getUsuario() {
        return usuario;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getPagina() {
        return pagina;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public String getNavegador() {
        return navegador;
    }

    public String getEstado() {
        return estado;
    }

    public String getRespuesta() {
        return respuesta;
    }

    // INFORMACIÓN BÁSICA DEL USUARIO
    public record UsuarioResumen(
            Integer idUsuario,
            String nombre,
            String apellidos,
            String usuario
    ) {
    }
}