package com.example.My_Pet.controller.Modulo_1_gestion_usuario;

// LIBRERÍAS NECESARIAS PARA EL CONTROLADOR
import com.example.My_Pet.ReporteAdminDTO;
import com.example.My_Pet.model.Modulo_1_gestion_usuario.Reporte;
import com.example.My_Pet.security.UsuarioPrincipal;
import com.example.My_Pet.service.Modulo_1_gestion_usuario.ReporteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// CONTROLADOR PARA GESTIONAR LOS REPORTES DE AYUDA
@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    // GET PARA OBTENER TODOS LOS REPORTES
    // Disponible únicamente para administradores.
    @GetMapping("/listar")
    @PreAuthorize("@autorizacion.esAdministrador()")
    public List<ReporteAdminDTO> listarReportes() {

        return reporteService.obtenerTodosLosReportes();
    }

    // GET PARA OBTENER LOS REPORTES DE UN USUARIO
    // El usuario puede consultar sus propios reportes.
    // El administrador puede consultar los de cualquier usuario.
    @GetMapping("/usuario/{idUsuario}")
    @PreAuthorize("@autorizacion.esUsuarioPropioOAdministrador(#p0)")
    public List<Reporte> obtenerReportesPorUsuario(
            @PathVariable("idUsuario") Integer idUsuario) {

        return reporteService.obtenerReportesPorUsuario(idUsuario);
    }

    // GET PARA OBTENER UN REPORTE ESPECÍFICO
    @GetMapping("/{id}")
    @PreAuthorize("@autorizacion.esReportePropioOAdministrador(#p0)")
    public Reporte obtenerReportePorId(
            @PathVariable("id") Integer id) {

        return reporteService.obtenerReportePorId(id);
    }

    // POST PARA CREAR UN NUEVO REPORTE
    // El usuario autenticado se obtiene desde la sesión.
    @PostMapping("/crear")
    @PreAuthorize("isAuthenticated()")
    public Reporte crearReporte(
            @RequestBody Reporte reporte) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof UsuarioPrincipal usuarioPrincipal)) {

            throw new RuntimeException(
                    "No se pudo identificar al usuario autenticado"
            );
        }

        Integer idUsuario =
                usuarioPrincipal.idUsuario();

        return reporteService.guardarReporte(
                idUsuario,
                reporte
        );
    }

    // PUT PARA ACTUALIZAR EL ESTADO Y LA RESPUESTA
    // Disponible únicamente para administradores.
    @PutMapping("/actualizar/{id}")
    @PreAuthorize("@autorizacion.esAdministrador()")
    public Reporte actualizarReporte(
            @PathVariable("id") Integer id,
            @RequestParam("estado") String estado,
            @RequestParam(
                    value = "respuesta",
                    required = false
            ) String respuesta) {

        return reporteService.actualizarReporte(
                id,
                estado,
                respuesta
        );
    }
}