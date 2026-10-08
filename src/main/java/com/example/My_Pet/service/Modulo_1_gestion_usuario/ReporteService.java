package com.example.My_Pet.service.Modulo_1_gestion_usuario;

// Clases propias del proyecto.

import com.example.My_Pet.ReporteAdminDTO;
import com.example.My_Pet.model.Modulo_1_gestion_usuario.Reporte;
import com.example.My_Pet.model.Modulo_1_gestion_usuario.Usuario;
import com.example.My_Pet.repository.Modulo_1_gestion_usuario.ReporteRepository;
import com.example.My_Pet.repository.Modulo_1_gestion_usuario.UsuarioRepository;

// Librerias del sprgin.

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Librerias de Java

import java.time.LocalDateTime;
import java.util.List;

// Gestiona las operaciones de consulta, registro, actualización y eliminación de reportes en la base de 

@Service
public class ReporteService {
    @Autowired
    private ReporteRepository reporteRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

     // Lista todos los reportes registrados solo *** PARA LOS ADMINISTRADORES***
    public List<ReporteAdminDTO> obtenerTodosLosReportes() {
        return reporteRepository.findAll()
                .stream()
                .map(ReporteAdminDTO::new)
                .toList();
    }

     // Lista los reportes asociados a un usuario.

    public List<Reporte> obtenerReportesPorUsuario(Integer idUsuario) {
        return reporteRepository.findByUsuarioIdUsuario(idUsuario);
    }

    // Busca un reporte por su ID

    public Reporte obtenerReportePorId(Integer idReporte) {
        return reporteRepository.findById(idReporte)
                .orElseThrow(() -> new RuntimeException(
                        "Reporte no encontrado con ID: " + idReporte
                ));
    }

     // Registra un nuevo reporte asociado a un usuario.

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

       // Reportes nuevos siempre su estado es pendiente.

        reporte.setEstado("pendiente");

        // El administrador todavía no ha dado una respuesta.

        reporte.setRespuesta(null);
        return reporteRepository.save(reporte);
    }

       // Actualiza el estado y la respuesta de un reporte existente.

    @Transactional
    public Reporte actualizarReporte(
            Integer idReporte,
            String estado,
            String respuesta) {
        Reporte reporte = obtenerReportePorId(idReporte);

          // Verifica que el estado recibido sea válido.

        if (!estado.equals("pendiente")
                && !estado.equals("en_revision")
                && !estado.equals("solucionado")) {
            throw new IllegalArgumentException(
                    "Estado de reporte no válido: " + estado
            );
        }

         // Actualiza el estado del reporte.

        reporte.setEstado(estado);

        // Guarda la respuesta del administrador

        if (respuesta != null && !respuesta.trim().isEmpty()) {
            reporte.setRespuesta(respuesta.trim());
        } else {
            reporte.setRespuesta(null);
        }
        return reporteRepository.save(reporte);
    }
}