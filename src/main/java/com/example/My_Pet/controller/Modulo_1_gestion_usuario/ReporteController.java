package com.example.My_Pet.controller.Modulo_1_gestion_usuario;

//librerías propias del proyecto. 

import com.example.My_Pet.ReporteAdminDTO;
import com.example.My_Pet.model.Modulo_1_gestion_usuario.Reporte;
import com.example.My_Pet.security.UsuarioPrincipal;
import com.example.My_Pet.service.Modulo_1_gestion_usuario.ReporteService;

//Librerias de Spring.

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

// Librerías de Java.
import java.util.List;

// Controlador para gestionar los reportes de ayuda.

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    // Servicio encargado de gestionar los reportes de ayuda.

    @Autowired
    private ReporteService reporteService;

   // Lista todos los reportes de ayuda. ***SOLO PARA ADMINISTRADORES***

    @GetMapping("/listar")
    @PreAuthorize("@autorizacion.esAdministrador()")
    public List<ReporteAdminDTO> listarReportes() {
        return reporteService.obtenerTodosLosReportes();
    }

    // Busca un reporte de ayuda por su ID.

    @GetMapping("/usuario/{idUsuario}")
    @PreAuthorize("@autorizacion.esUsuarioPropioOAdministrador(#p0)")
    public List<Reporte> obtenerReportesPorUsuario(
            @PathVariable("idUsuario") Integer idUsuario) {
        return reporteService.obtenerReportesPorUsuario(idUsuario);
    }

    // Lista los reportes de ayuda asociados a un usuario.

    @GetMapping("/{id}")
    @PreAuthorize("@autorizacion.esReportePropioOAdministrador(#p0)")
    public Reporte obtenerReportePorId(
            @PathVariable("id") Integer id) {
        return reporteService.obtenerReportePorId(id);
    }

    // Registra un nuevo reporte de ayuda por un usuario existente.

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

        Integer idUsuario = usuarioPrincipal.idUsuario();

        return reporteService.guardarReporte(
                idUsuario,
                reporte
        );
    }

    // Actualiza el estado de un reporte de ayuda. ***SOLO PARA ADMINISTRADORES***

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