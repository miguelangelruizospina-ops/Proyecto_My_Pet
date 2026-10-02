// Servicio encargado de verificar los permisos de acceso de los usuarios.

// Paquete donde se encuentra el servicio.

package com.example.My_Pet.security;

// Librerías necesarias para los modelos y repositorios.

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


// Permite utilizar este servicio para controlar la autorización.

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


    // Constructor que recibe los repositorios necesarios para verificar permisos.

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

            ServicioRepository servicioRepository) {

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

    }


    // Verifica si el usuario autenticado tiene rol de administrador.

    public boolean esAdministrador() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        return authentication != null
                && authentication.getAuthorities()
                        .stream()
                        .anyMatch(
                                authority ->
                                        authority.getAuthority()
                                                .equals("ROLE_ADMINISTRADOR")
                        );

    }


    // Verifica si el usuario es el propietario del recurso o un administrador.

    public boolean esUsuarioPropioOAdministrador(Integer idUsuario) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null || idUsuario == null) {

            return false;

        }

        if (esAdministrador()) {

            return true;

        }

        Object principal =
                authentication.getPrincipal();

        return principal instanceof UsuarioPrincipal usuario
                && usuario.idUsuario() == idUsuario;

    }


    // Verifica si una mascota pertenece al usuario o si es administrador.

    public boolean esMascotaPropiaOAdministrador(Integer id) {

        return mascotaRepository.findById(id)

                .map(
                        mascota ->
                                mascota.getUsuario() != null
                                        && esUsuarioPropioOAdministrador(
                                                mascota.getUsuario()
                                                        .getIdUsuario()
                                        )
                )

                .orElse(false);

    }


    // Verifica si el perfil de una mascota pertenece al usuario o si es administrador.

    public boolean esPerfilMascotaPropioOAdministrador(Integer id) {

        return perfilMascotaRepository.findById(id)

                .map(
                        perfil ->
                                perfil.getMascota() != null
                                        && esMascotaPropiaOAdministrador(
                                                perfil.getMascota()
                                                        .getIdMascota()
                                        )
                )

                .orElse(false);

    }


    // Verifica si un documento pertenece al usuario o a una de sus mascotas.

    public boolean esDocumentoPropioOAdministrador(Integer id) {

        return documentoRepository.findById(id)

                .map(
                        documento ->
                                perteneceDocumento(documento)
                )

                .orElse(false);

    }


    // Verifica si el historial médico pertenece al usuario o si es administrador.

    public boolean esHistorialPropioOAdministrador(Integer id) {

        return historialMedicoRepository.findById(id)

                .map(
                        historial ->
                                historial.getMascota() != null
                                        && esMascotaPropiaOAdministrador(
                                                historial.getMascota()
                                                        .getIdMascota()
                                        )
                )

                .orElse(false);

    }


    // Verifica si un evento pertenece al usuario o si es administrador.

    public boolean esEventoPropioOAdministrador(Integer id) {

        return eventoRepository.findById(id)

                .map(
                        evento ->
                                evento.getMascota() != null
                                        && esMascotaPropiaOAdministrador(
                                                evento.getMascota()
                                                        .getIdMascota()
                                        )
                )

                .orElse(false);

    }


    // Verifica si un recordatorio pertenece al usuario o si es administrador.

    public boolean esRecordatorioPropioOAdministrador(Integer id) {

        return recordatorioRepository.findById(id)

                .map(
                        recordatorio ->
                                recordatorio.getUsuario() != null
                                        && esUsuarioPropioOAdministrador(
                                                recordatorio.getUsuario()
                                                        .getIdUsuario()
                                        )
                )

                .orElse(false);

    }


    // Verifica si una notificación pertenece al usuario o si es administrador.

    public boolean esNotificacionPropiaOAdministrador(Integer id) {

        return notificacionRepository.findById(id)

                .map(
                        notificacion ->
                                notificacion.getUsuario() != null
                                        && esUsuarioPropioOAdministrador(
                                                notificacion.getUsuario()
                                                        .getIdUsuario()
                                        )
                )

                .orElse(false);

    }


    // Verifica si una emergencia pertenece al usuario o si es administrador.

    public boolean esEmergenciaPropiaOAdministrador(Integer id) {

        return emergenciaRepository.findById(id)

                .map(
                        emergencia ->
                                emergencia.getUsuario() != null
                                        && esUsuarioPropioOAdministrador(
                                                emergencia.getUsuario()
                                                        .getIdUsuario()
                                        )
                )

                .orElse(false);

    }


    // Verifica si un chatbot pertenece al usuario o si es administrador.

    public boolean esChatbotPropioOAdministrador(Integer id) {

        return chatbotRepository.findById(id)

                .map(
                        chatbot ->
                                chatbot.getUsuario() != null
                                        && esUsuarioPropioOAdministrador(
                                                chatbot.getUsuario()
                                                        .getIdUsuario()
                                        )
                )

                .orElse(false);

    }


    // Verifica si una publicación del foro pertenece al usuario o si es administrador.

    public boolean esPublicacionPropiaOAdministrador(Integer id) {

        return publicacionForoRepository.findById(id)

                .map(
                        publicacion ->
                                publicacion.getUsuario() != null
                                        && esUsuarioPropioOAdministrador(
                                                publicacion.getUsuario()
                                                        .getIdUsuario()
                                        )
                )

                .orElse(false);

    }


    // Verifica si un servicio pertenece al usuario o si es administrador.

    public boolean esServicioPropioOAdministrador(Integer id) {

        return servicioRepository.findById(id)

                .map(
                        servicio ->
                                servicio.getUsuario() != null
                                        && esUsuarioPropioOAdministrador(
                                                servicio.getUsuario()
                                                        .getIdUsuario()
                                        )
                )

                .orElse(false);

    }


    // Verifica si el usuario puede guardar un documento asociado a una mascota.

    public boolean puedeGuardarDocumento(
            Integer idUsuario,
            Integer idMascota) {

        boolean usuarioAutorizado =
                esUsuarioPropioOAdministrador(
                        idUsuario
                );

        boolean mascotaAutorizada =
                idMascota == null
                        || esMascotaPropiaOAdministrador(
                                idMascota
                        );


        // Muestra en consola los datos utilizados para verificar la autorización.

        System.out.println(
                "ID usuario recibido: "
                        + idUsuario
        );

        System.out.println(
                "ID mascota recibida: "
                        + idMascota
        );

        System.out.println(
                "Usuario autorizado: "
                        + usuarioAutorizado
        );

        System.out.println(
                "Mascota autorizada: "
                        + mascotaAutorizada
        );


        return usuarioAutorizado
                && mascotaAutorizada;

    }


    // Verifica si un documento pertenece al usuario o a una de sus mascotas.

    private boolean perteneceDocumento(
            Documento documento) {

        boolean usuarioPropio =
                documento.getUsuario() != null
                        && esUsuarioPropioOAdministrador(
                                documento.getUsuario()
                                        .getIdUsuario()
                        );

        boolean mascotaPropia =
                documento.getMascota() != null
                        && esMascotaPropiaOAdministrador(
                                documento.getMascota()
                                        .getIdMascota()
                        );

        return usuarioPropio
                || mascotaPropia;

    }

}