package com.example.My_Pet.security;

import com.example.My_Pet.model.Modulo_2_gestion_mascota.Documento;
import com.example.My_Pet.model.Modulo_2_gestion_mascota.HistorialMedico;
import com.example.My_Pet.model.Modulo_2_gestion_mascota.Mascota;
import com.example.My_Pet.model.Modulo_2_gestion_mascota.PerfilMascota;
import com.example.My_Pet.model.Modulo_3_Agenda_Recordatorio.Evento;
import com.example.My_Pet.model.Modulo_3_Agenda_Recordatorio.Notificacion;
import com.example.My_Pet.model.Modulo_3_Agenda_Recordatorio.Recordatorio;
import com.example.My_Pet.model.Modulo_4_servicio_comunidad.Chatbot;
import com.example.My_Pet.model.Modulo_4_servicio_comunidad.PublicacionForo;
import com.example.My_Pet.model.Modulo_4_servicio_comunidad.Servicio;
import com.example.My_Pet.model.Modulo_5_emergencia.Emergencia;

import com.example.My_Pet.repository.Modulo_1_gestion_usuario.ReporteRepository;
import com.example.My_Pet.repository.Modulo_2_gestion_mascotas.DocumentoRepository;
import com.example.My_Pet.repository.Modulo_2_gestion_mascotas.HistorialMedicoRepository;
import com.example.My_Pet.repository.Modulo_2_gestion_mascotas.MascotaRepository;
import com.example.My_Pet.repository.Modulo_2_gestion_mascotas.PerfilMascotaRepository;
import com.example.My_Pet.repository.Modulo_3_Agenda_Recordatorio.EventoRepository;
import com.example.My_Pet.repository.Modulo_3_Agenda_Recordatorio.NotificacionRepository;
import com.example.My_Pet.repository.Modulo_3_Agenda_Recordatorio.RecordatorioRepository;
import com.example.My_Pet.repository.Modulo_4_servicio_comunidad.ChatbotRepository;
import com.example.My_Pet.repository.Modulo_4_servicio_comunidad.PublicacionForoRepository;
import com.example.My_Pet.repository.Modulo_4_servicio_comunidad.ServicioRepository;
import com.example.My_Pet.repository.Modulo_5_emergencia.EmergenciaRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component("autorizacion")
public class AutorizacionService {

    private final MascotaRepository mascotaRepository;
    private final PerfilMascotaRepository perfilMascotaRepository;
    private final DocumentoRepository documentoRepository;
    private final HistorialMedicoRepository historialMedicoRepository;
    private final EventoRepository eventoRepository;
    private final RecordatorioRepository recordatorioRepository;
    private final NotificacionRepository notificacionRepository;
    private final EmergenciaRepository emergenciaRepository;
    private final ChatbotRepository chatbotRepository;
    private final PublicacionForoRepository publicacionForoRepository;
    private final ServicioRepository servicioRepository;
    private final ReporteRepository reporteRepository;

    public AutorizacionService(
            MascotaRepository mascotaRepository,
            PerfilMascotaRepository perfilMascotaRepository,
            DocumentoRepository documentoRepository,
            HistorialMedicoRepository historialMedicoRepository,
            EventoRepository eventoRepository,
            RecordatorioRepository recordatorioRepository,
            NotificacionRepository notificacionRepository,
            EmergenciaRepository emergenciaRepository,
            ChatbotRepository chatbotRepository,
            PublicacionForoRepository publicacionForoRepository,
            ServicioRepository servicioRepository,
            ReporteRepository reporteRepository) {

        this.mascotaRepository = mascotaRepository;
        this.perfilMascotaRepository = perfilMascotaRepository;
        this.documentoRepository = documentoRepository;
        this.historialMedicoRepository = historialMedicoRepository;
        this.eventoRepository = eventoRepository;
        this.recordatorioRepository = recordatorioRepository;
        this.notificacionRepository = notificacionRepository;
        this.emergenciaRepository = emergenciaRepository;
        this.chatbotRepository = chatbotRepository;
        this.publicacionForoRepository = publicacionForoRepository;
        this.servicioRepository = servicioRepository;
        this.reporteRepository = reporteRepository;
    }

    public boolean esAdministrador() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        return authentication != null
                && authentication.getAuthorities()
                        .stream()
                        .anyMatch(authority ->
                                authority.getAuthority()
                                        .equals("ROLE_ADMINISTRADOR"));
    }

    public boolean esUsuarioPropioOAdministrador(Integer idUsuario) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || idUsuario == null) {
            return false;
        }

        if (esAdministrador()) {
            return true;
        }

        Object principal = authentication.getPrincipal();

        return principal instanceof UsuarioPrincipal usuario
                && Objects.equals(usuario.idUsuario(), idUsuario);
    }

    public boolean esMascotaPropiaOAdministrador(Integer id) {
        if (id == null) return false;

        return mascotaRepository.findById(id)
                .map(mascota ->
                        mascota.getUsuario() != null
                                && esUsuarioPropioOAdministrador(
                                        mascota.getUsuario().getIdUsuario()))
                .orElse(false);
    }

    public boolean puedeGuardarEvento(Evento evento) {
        if (evento == null || evento.getMascota() == null) return false;

        Integer idMascota = evento.getMascota().getIdMascota();

        if (idMascota == null || idMascota <= 0) return false;

        return esMascotaPropiaOAdministrador(idMascota);
    }

    public boolean esPerfilMascotaPropioOAdministrador(Integer id) {
        if (id == null) return false;

        return perfilMascotaRepository.findById(id)
                .map(perfil ->
                        perfil.getMascota() != null
                                && esMascotaPropiaOAdministrador(
                                        perfil.getMascota().getIdMascota()))
                .orElse(false);
    }

    public boolean esDocumentoPropioOAdministrador(Integer id) {
        if (id == null) return false;

        return documentoRepository.findById(id)
                .map(this::perteneceDocumento)
                .orElse(false);
    }

    public boolean esHistorialPropioOAdministrador(Integer id) {
        if (id == null) return false;

        return historialMedicoRepository.findById(id)
                .map(historial ->
                        historial.getMascota() != null
                                && esMascotaPropiaOAdministrador(
                                        historial.getMascota().getIdMascota()))
                .orElse(false);
    }

    public boolean esEventoPropioOAdministrador(Integer id) {
        if (id == null) return false;

        return eventoRepository.findById(id)
                .map(evento ->
                        evento.getMascota() != null
                                && esMascotaPropiaOAdministrador(
                                        evento.getMascota().getIdMascota()))
                .orElse(false);
    }

    public boolean esRecordatorioPropioOAdministrador(Integer id) {
        if (id == null) return false;

        return recordatorioRepository.findById(id)
                .map(recordatorio ->
                        recordatorio.getUsuario() != null
                                && esUsuarioPropioOAdministrador(
                                        recordatorio.getUsuario().getIdUsuario()))
                .orElse(false);
    }

    public boolean esNotificacionPropiaOAdministrador(Integer id) {
        if (id == null) return false;

        if (esAdministrador()) return true;

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) return false;

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof UsuarioPrincipal usuario)) return false;

        Integer idUsuarioAutenticado = usuario.idUsuario();

        if (idUsuarioAutenticado == null) return false;

        return notificacionRepository.findById(id)
                .map(notificacion ->
                        notificacion.getUsuario() != null
                                && Objects.equals(
                                        notificacion.getUsuario().getIdUsuario(),
                                        idUsuarioAutenticado))
                .orElse(false);
    }

    public boolean esEmergenciaPropiaOAdministrador(Integer id) {
        if (id == null) return false;

        return emergenciaRepository.findById(id)
                .map(emergencia ->
                        emergencia.getUsuario() != null
                                && esUsuarioPropioOAdministrador(
                                        emergencia.getUsuario().getIdUsuario()))
                .orElse(false);
    }

    public boolean esChatbotPropioOAdministrador(Integer id) {
        if (id == null) return false;

        return chatbotRepository.findById(id)
                .map(chatbot ->
                        chatbot.getUsuario() != null
                                && esUsuarioPropioOAdministrador(
                                        chatbot.getUsuario().getIdUsuario()))
                .orElse(false);
    }

    public boolean esPublicacionPropiaOAdministrador(Integer id) {
        if (id == null) return false;

        return publicacionForoRepository.findById(id)
                .map(publicacion ->
                        publicacion.getUsuario() != null
                                && esUsuarioPropioOAdministrador(
                                        publicacion.getUsuario().getIdUsuario()))
                .orElse(false);
    }

    public boolean esServicioPropioOAdministrador(Integer id) {
        if (id == null) return false;

        return servicioRepository.findById(id)
                .map(servicio ->
                        servicio.getUsuario() != null
                                && esUsuarioPropioOAdministrador(
                                        servicio.getUsuario().getIdUsuario()))
                .orElse(false);
    }

    public boolean puedeGuardarDocumento(
            Integer idUsuario,
            Integer idMascota) {

        boolean usuarioAutorizado =
                esUsuarioPropioOAdministrador(idUsuario);

        boolean mascotaAutorizada =
                idMascota == null
                        || esMascotaPropiaOAdministrador(idMascota);

        return usuarioAutorizado && mascotaAutorizada;
    }

    /**
     * Verifica que el reporte pertenezca al usuario autenticado
     * o que el usuario sea administrador.
     */
    public boolean esReportePropioOAdministrador(Integer id) {
        if (id == null) {
            return false;
        }

        return reporteRepository.findById(id)
                .map(reporte ->
                        reporte.getUsuario() != null
                                && esUsuarioPropioOAdministrador(
                                        reporte.getUsuario().getIdUsuario()
                                )
                )
                .orElse(false);
    }

    private boolean perteneceDocumento(Documento documento) {

        boolean usuarioPropio =
                documento.getUsuario() != null
                        && esUsuarioPropioOAdministrador(
                                documento.getUsuario().getIdUsuario());

        boolean mascotaPropia =
                documento.getMascota() != null
                        && esMascotaPropiaOAdministrador(
                                documento.getMascota().getIdMascota());

        return usuarioPropio || mascotaPropia;
    }
}