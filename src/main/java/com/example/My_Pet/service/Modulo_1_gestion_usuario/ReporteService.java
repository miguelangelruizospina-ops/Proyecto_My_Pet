package com.example.My_Pet.service.Modulo_1_gestion_usuario;

import com.example.My_Pet.ReporteAdminDTO;
import com.example.My_Pet.model.Modulo_1_gestion_usuario.Reporte;
import com.example.My_Pet.model.Modulo_1_gestion_usuario.Usuario;
import com.example.My_Pet.repository.Modulo_1_gestion_usuario.ReporteRepository;
import com.example.My_Pet.repository.Modulo_1_gestion_usuario.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

// SERVICIO PARA GESTIONAR LOS REPORTES
@Service
public class ReporteService {

    @Autowired
    private ReporteRepository reporteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // OBTENER TODOS LOS REPORTES PARA EL ADMINISTRADOR
    public List<ReporteAdminDTO> obtenerTodosLosReportes() {

        return reporteRepository.findAll()
                .stream()
                .map(ReporteAdminDTO::new)
                .toList();
    }

    // OBTENER LOS REPORTES DE UN USUARIO
    public List<Reporte> obtenerReportesPorUsuario(Integer idUsuario) {

        return reporteRepository.findByUsuarioIdUsuario(idUsuario);
    }

    // OBTENER UN REPORTE POR ID
    public Reporte obtenerReportePorId(Integer idReporte) {

        return reporteRepository.findById(idReporte)
                .orElseThrow(() -> new RuntimeException(
                        "Reporte no encontrado con ID: " + idReporte
                ));
    }

    // GUARDAR NUEVO REPORTE
    @Transactional
    public Reporte guardarReporte(
            Integer idUsuario,
            Reporte reporte) {

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException(
                        "El usuario con ID " + idUsuario + " no existe"
                ));

        reporte.setUsuario(usuario);
        reporte.setFecha(LocalDateTime.now());

        // Todo reporte nuevo comienza pendiente.
        reporte.setEstado("pendiente");

        // El administrador todavía no ha dado una respuesta.
        reporte.setRespuesta(null);

        return reporteRepository.save(reporte);
    }

    // ACTUALIZAR ESTADO Y RESPUESTA DEL REPORTE
    @Transactional
    public Reporte actualizarReporte(
            Integer idReporte,
            String estado,
            String respuesta) {

        Reporte reporte = obtenerReportePorId(idReporte);

        // VALIDAR ESTADO
        if (!estado.equals("pendiente")
                && !estado.equals("en_revision")
                && !estado.equals("solucionado")) {

            throw new IllegalArgumentException(
                    "Estado de reporte no válido: " + estado
            );
        }

        // ACTUALIZAR ESTADO
        reporte.setEstado(estado);

        // GUARDAR RESPUESTA
        if (respuesta != null && !respuesta.trim().isEmpty()) {
            reporte.setRespuesta(respuesta.trim());
        } else {
            reporte.setRespuesta(null);
        }

        return reporteRepository.save(reporte);
    }
}