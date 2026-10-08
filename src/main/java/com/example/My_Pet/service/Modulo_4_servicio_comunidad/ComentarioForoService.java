package com.example.My_Pet.service.Modulo_4_servicio_comunidad;

// Clases propias del proyecto.

import com.example.My_Pet.model.Modulo_1_gestion_usuario.Usuario;
import com.example.My_Pet.model.Modulo_4_servicio_comunidad.ComentarioForo;
import com.example.My_Pet.model.Modulo_4_servicio_comunidad.PublicacionForo;
import com.example.My_Pet.repository.Modulo_1_gestion_usuario.UsuarioRepository;
import com.example.My_Pet.repository.Modulo_4_servicio_comunidad.ComentarioForoRepository;
import com.example.My_Pet.repository.Modulo_4_servicio_comunidad.PublicacionForoRepository;

// Clases del spring.

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Librerias de Java

import java.time.LocalDateTime;
import java.util.List;

// Gestiona las operaciones de consulta, registro, actualización y eliminación de comentarios del foro en la base de datos.

@Service
public class ComentarioForoService {
    @Autowired
    private ComentarioForoRepository comentarioRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PublicacionForoRepository publicacionRepository;

    // Lista los comentarios asociados a una publicación.

    public List<ComentarioForo> obtenerPorPublicacion(
            Integer idPublicacion) {
        if (!publicacionRepository.existsById(idPublicacion)) {
            throw new IllegalArgumentException(
                    "La publicación no existe."
            );
        }
        return comentarioRepository
                .findByPublicacionIdPublicacion(idPublicacion);
    }

    // Registra un nuevo comentario en una publicación.

    @Transactional
    public ComentarioForo guardar(
            Integer idUsuario,
            Integer idPublicacion,
            String contenido) {
        if (contenido == null || contenido.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El comentario no puede estar vacío."
            );
        }
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException(
                        "El usuario no existe."
                ));
        PublicacionForo publicacion =
                publicacionRepository.findById(idPublicacion)
                        .orElseThrow(() -> new IllegalArgumentException(
                                "La publicación no existe."
                        ));
        ComentarioForo comentario = new ComentarioForo();
        comentario.setContenido(contenido.trim());
        comentario.setFecha(LocalDateTime.now());
        comentario.setUsuario(usuario);
        comentario.setPublicacion(publicacion);
        return comentarioRepository.save(comentario);
    }

    // Registra una nueva respuesta a un comentario.

    @Transactional
    public ComentarioForo responder(
            Integer idUsuario,
            Integer idComentarioPadre,
            String contenido) {
        if (contenido == null || contenido.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "La respuesta no puede estar vacía."
            );
        }
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException(
                        "El usuario no existe."
                ));
        ComentarioForo comentarioPadre =
                comentarioRepository.findById(idComentarioPadre)
                        .orElseThrow(() -> new IllegalArgumentException(
                                "El comentario al que se desea responder no existe."
                        ));
        ComentarioForo respuesta = new ComentarioForo();
        respuesta.setContenido(contenido.trim());
        respuesta.setFecha(LocalDateTime.now());
        respuesta.setUsuario(usuario);
        respuesta.setPublicacion(
                comentarioPadre.getPublicacion()
        );
        respuesta.setComentarioPadre(
                comentarioPadre
        );
        return comentarioRepository.save(respuesta);
    }

    // Lista las respuestas asociadas a un comentario.

    public List<ComentarioForo> obtenerRespuestas(
            Integer idComentarioPadre) {
        if (!comentarioRepository.existsById(idComentarioPadre)) {
            throw new IllegalArgumentException(
                    "El comentario no existe."
            );
        }
        return comentarioRepository
                .findByComentarioPadreIdComentario(
                        idComentarioPadre
                );
    }

   // Actualiza un comentario o una respuesta existente.

    @Transactional
    public ComentarioForo actualizar(
            Integer idComentario,
            String contenido) {
        if (contenido == null || contenido.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El comentario no puede estar vacío."
            );
        }
        ComentarioForo comentario =
                comentarioRepository.findById(idComentario)
                        .orElseThrow(() -> new IllegalArgumentException(
                                "El comentario no existe."
                        ));
        comentario.setContenido(contenido.trim());
        return comentarioRepository.save(comentario);
    }

    // Elimina un comentario existente.

    @Transactional
    public void eliminar(Integer idComentario) {
        if (!comentarioRepository.existsById(idComentario)) {
            throw new IllegalArgumentException(
                    "El comentario no existe."
            );
        }
        comentarioRepository.deleteById(idComentario);
    }
}