package com.example.My_Pet.service.Modulo_4_servicio_comunidad;

import com.example.My_Pet.model.Modulo_1_gestion_usuario.Usuario;
import com.example.My_Pet.model.Modulo_4_servicio_comunidad.ComentarioForo;
import com.example.My_Pet.model.Modulo_4_servicio_comunidad.PublicacionForo;
import com.example.My_Pet.repository.Modulo_1_gestion_usuario.UsuarioRepository;
import com.example.My_Pet.repository.Modulo_4_servicio_comunidad.ComentarioForoRepository;
import com.example.My_Pet.repository.Modulo_4_servicio_comunidad.PublicacionForoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ComentarioForoService {

    @Autowired
    private ComentarioForoRepository comentarioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PublicacionForoRepository publicacionRepository;

    // ==========================================
    // OBTENER COMENTARIOS DE UNA PUBLICACIÓN
    // ==========================================

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

    // ==========================================
    // CREAR COMENTARIO
    // ==========================================

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

    // ==========================================
    // CREAR RESPUESTA A UN COMENTARIO
    // ==========================================

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

    // ==========================================
    // OBTENER RESPUESTAS DE UN COMENTARIO
    // ==========================================

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

    // ==========================================
    // ACTUALIZAR COMENTARIO O RESPUESTA
    // ==========================================

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

    // ==========================================
    // ELIMINAR COMENTARIO
    // ==========================================

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