package com.example.My_Pet.service.Modulo_4_servicio_comunidad;

import com.example.My_Pet.model.Modulo_1_gestion_usuario.Usuario;
import com.example.My_Pet.model.Modulo_4_servicio_comunidad.PublicacionForo;
import com.example.My_Pet.repository.Modulo_1_gestion_usuario.UsuarioRepository;
import com.example.My_Pet.repository.Modulo_4_servicio_comunidad.PublicacionForoRepository;
import com.example.My_Pet.security.UsuarioPrincipal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PublicacionForoService {

    @Autowired
    private PublicacionForoRepository forumRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;


    // ============================================================
    // Obtener todas las publicaciones
    // ============================================================

    public List<PublicacionForo> obtenerTodas() {
        return forumRepository.findAll();
    }


    // ============================================================
    // Obtener publicaciones de un usuario
    // ============================================================

    public List<PublicacionForo> obtenerPorUsuario(Integer idUsuario) {
        return forumRepository.findByUsuarioIdUsuario(idUsuario);
    }


    // ============================================================
    // Crear una publicación
    // ============================================================

    public PublicacionForo guardar(PublicacionForo publicacion) {

        if (publicacion.getUsuario() == null ||
            publicacion.getUsuario().getIdUsuario() <= 0) {

            throw new IllegalArgumentException(
                "La publicación debe estar asociada a un usuario válido."
            );
        }

        Integer idUsuario =
                publicacion.getUsuario().getIdUsuario();

        Usuario usuarioExistente =
                usuarioRepository.findById(idUsuario)
                .orElseThrow(() ->
                    new IllegalArgumentException(
                        "El usuario con ID " + idUsuario +
                        " no existe."
                    )
                );

        publicacion.setUsuario(usuarioExistente);

        // Coloca la fecha automáticamente al crear

        if (publicacion.getIdPublicacion() == null) {
            publicacion.setFecha(LocalDateTime.now());
        }

        return forumRepository.save(publicacion);
    }


    // ============================================================
    // Actualizar una publicación
    // ============================================================

    @Transactional
    public PublicacionForo actualizar(
            Integer id,
            PublicacionForo publicacion,
            UsuarioPrincipal usuarioPrincipal) {

        PublicacionForo publicacionExistente =
                forumRepository.findById(id)
                .orElseThrow(() ->
                    new IllegalArgumentException(
                        "La publicación con ID " + id +
                        " no existe."
                    )
                );


        // ========================================================
        // Identificar usuario autenticado
        // ========================================================

        Integer idUsuarioActual =
                usuarioPrincipal.idUsuario();


        // ========================================================
        // Identificar propietario de la publicación
        // ========================================================

        Integer idUsuarioPublicacion =
                publicacionExistente.getUsuario() != null
                        ? publicacionExistente
                                .getUsuario()
                                .getIdUsuario()
                        : null;


        // ========================================================
        // Comprobar si es administrador
        // ========================================================

        boolean administrador =
                "ADMINISTRADOR".equalsIgnoreCase(
                        usuarioPrincipal.rol()
                );


        // ========================================================
        // Comprobar si es propietario
        // ========================================================

        boolean propietario =
                idUsuarioPublicacion != null
                        && idUsuarioPublicacion.equals(
                                idUsuarioActual
                        );


        // ========================================================
        // Validar permisos
        // ========================================================

        if (!administrador && !propietario) {

            throw new SecurityException(
                    "No tienes permiso para editar esta publicación."
            );
        }


        // ========================================================
        // Validar título
        // ========================================================

        if (publicacion.getTitulo() == null ||
            publicacion.getTitulo().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "El título no puede estar vacío."
            );
        }


        // ========================================================
        // Validar contenido
        // ========================================================

        if (publicacion.getContenido() == null ||
            publicacion.getContenido().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "El contenido no puede estar vacío."
            );
        }


        // ========================================================
        // Actualizar título
        // ========================================================

        publicacionExistente.setTitulo(
                publicacion.getTitulo().trim()
        );


        // ========================================================
        // Actualizar contenido
        // ========================================================

        publicacionExistente.setContenido(
                publicacion.getContenido().trim()
        );


        /*
         * No modificamos el usuario de la publicación.
         *
         * El propietario original debe mantenerse.
         */

        return forumRepository.save(publicacionExistente);
    }


    // ============================================================
    // Eliminar una publicación
    // ============================================================

    @Transactional
    public void eliminar(
            Integer id,
            UsuarioPrincipal usuarioPrincipal) {

        PublicacionForo publicacion =
                forumRepository.findById(id)
                .orElseThrow(() ->
                    new IllegalArgumentException(
                        "La publicación con ID " + id +
                        " no existe."
                    )
                );


        // ========================================================
        // Identificar usuario autenticado
        // ========================================================

        Integer idUsuarioActual =
                usuarioPrincipal.idUsuario();


        // ========================================================
        // Identificar propietario
        // ========================================================

        Integer idUsuarioPublicacion =
                publicacion.getUsuario() != null
                        ? publicacion
                                .getUsuario()
                                .getIdUsuario()
                        : null;


        // ========================================================
        // Comprobar administrador
        // ========================================================

        boolean administrador =
                "ADMINISTRADOR".equalsIgnoreCase(
                        usuarioPrincipal.rol()
                );


        // ========================================================
        // Comprobar propietario
        // ========================================================

        boolean propietario =
                idUsuarioPublicacion != null
                        && idUsuarioPublicacion.equals(
                                idUsuarioActual
                        );


        // ========================================================
        // Validar permisos
        // ========================================================

        if (!administrador && !propietario) {

            throw new SecurityException(
                    "No tienes permiso para eliminar esta publicación."
            );
        }


        // ========================================================
        // Eliminar publicación
        // ========================================================

        forumRepository.delete(publicacion);
    }
}