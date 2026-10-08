package com.example.My_Pet.service.Modulo_4_servicio_comunidad;

// Clases propias del proyecto.

import com.example.My_Pet.model.Modulo_1_gestion_usuario.Usuario;
import com.example.My_Pet.model.Modulo_4_servicio_comunidad.PublicacionForo;
import com.example.My_Pet.repository.Modulo_1_gestion_usuario.UsuarioRepository;
import com.example.My_Pet.repository.Modulo_4_servicio_comunidad.PublicacionForoRepository;
import com.example.My_Pet.security.UsuarioPrincipal;

// Librerias del spring.

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Libreria de Java

import java.time.LocalDateTime;
import java.util.List;

// Gestiona las operaciones de consulta, registro, actualización y eliminación de publicaciones del foro en la base de datos.

@Service
public class PublicacionForoService {
    @Autowired
    private PublicacionForoRepository forumRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    // Lista todas las publicaciones registradas en el foro.

    public List<PublicacionForo> obtenerTodas() {
        return forumRepository.findAll();
    }

    // Obtiene todas las publicaciones asociadas a un usuario.

    public List<PublicacionForo> obtenerPorUsuario(Integer idUsuario) {
        return forumRepository.findByUsuarioIdUsuario(idUsuario);
    }

   // Registra una nueva publicación en el foro.

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

    // Actualiza el contenido de una publicación
   
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

        // Obtiene el usuario que inició sesión.

        Integer idUsuarioActual =
                usuarioPrincipal.idUsuario();
        
        // Identificar propietario de la publicación

        Integer idUsuarioPublicacion =
                publicacionExistente.getUsuario() != null
                        ? publicacionExistente
                                .getUsuario()
                                .getIdUsuario()
                        : null;
 
        // Valida si el usuario es administrador.
       
        boolean administrador =
                "ADMINISTRADOR".equalsIgnoreCase(
                        usuarioPrincipal.rol()
                );

        // Verifica si el usuario que inició sesión es el propietario.

        boolean propietario =
                idUsuarioPublicacion != null
                        && idUsuarioPublicacion.equals(
                                idUsuarioActual
                        );

        // Verifica que el usuario tenga permisos para realizar la acción.

        if (!administrador && !propietario) {
            throw new SecurityException(
                    "No tienes permiso para editar esta publicación."
            );
        }


       // Verifica que el título de la publicación sea válido.

        if (publicacion.getTitulo() == null ||
            publicacion.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El título no puede estar vacío."
            );
        }

        // Verifica que el contenido de la publicacion sea valido.

        if (publicacion.getContenido() == null ||
            publicacion.getContenido().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El contenido no puede estar vacío."
            );
        }

        // Actualizar título de la publicación.
        
        publicacionExistente.setTitulo(
                publicacion.getTitulo().trim()
        );

        // Actualizar contenido de la publicación.

        publicacionExistente.setContenido(
                publicacion.getContenido().trim()
        );

       // Mantiene el usuario propietario original de la publicación.

        return forumRepository.save(publicacionExistente);
    }

    // Eliminar una publicación del foro

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

        // Identifica que el  usuario este  autenticado

        Integer idUsuarioActual =
                usuarioPrincipal.idUsuario();

        // Identificar propietario de la publicacion.

        Integer idUsuarioPublicacion =
                publicacion.getUsuario() != null
                        ? publicacion
                                .getUsuario()
                                .getIdUsuario()
                        : null;

        // Verifica si el usuario que inició sesión es administrador.

        boolean administrador =
                "ADMINISTRADOR".equalsIgnoreCase(
                        usuarioPrincipal.rol()
                );

        //  Verifica si el usuario que inició sesión es propietario de la publicación.

        boolean propietario =
                idUsuarioPublicacion != null
                        && idUsuarioPublicacion.equals(
                                idUsuarioActual
                        );

        // Verifica los permisos

        if (!administrador && !propietario) {
            throw new SecurityException(
                    "No tienes permiso para eliminar esta publicación."
            );
        }
        
        // Elimina una publicacion del foro
       
        forumRepository.delete(publicacion);
    }
}