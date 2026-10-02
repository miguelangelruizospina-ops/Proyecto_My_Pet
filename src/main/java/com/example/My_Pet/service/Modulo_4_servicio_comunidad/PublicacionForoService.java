package com.example.My_Pet.service.Modulo_4_servicio_comunidad;

import com.example.My_Pet.model.Modulo_1_gestion_usuario.Usuario;
import com.example.My_Pet.model.Modulo_4_servicio_comunidad.PublicacionForo;
import com.example.My_Pet.repository.Modulo_1_gestion_usuario.UsuarioRepository;
import com.example.My_Pet.repository.Modulo_4_servicio_comunidad.PublicacionForoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PublicacionForoService {

    @Autowired
    private PublicacionForoRepository forumRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Obtener todas las publicaciones
    public List<PublicacionForo> obtenerTodas() {
        return forumRepository.findAll();
    }

    // Obtener publicaciones de un usuario
    public List<PublicacionForo> obtenerPorUsuario(Integer idUsuario) {
        return forumRepository.findByUsuarioIdUsuario(idUsuario);
    }

    // Crear una publicación
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

    // Actualizar una publicación
    public PublicacionForo actualizar(
            Integer id,
            PublicacionForo publicacion) {

        PublicacionForo publicacionExistente =
                forumRepository.findById(id)
                .orElseThrow(() ->
                    new IllegalArgumentException(
                        "La publicación con ID " + id +
                        " no existe."
                    )
                );

        // Actualizar título
        publicacionExistente.setTitulo(
                publicacion.getTitulo()
        );

        // Actualizar contenido
        publicacionExistente.setContenido(
                publicacion.getContenido()
        );

        // Actualizar usuario si se envía
        if (publicacion.getUsuario() != null) {

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

            publicacionExistente.setUsuario(
                    usuarioExistente
            );
        }

        return forumRepository.save(publicacionExistente);
    }

    // Eliminar una publicación
    public void eliminar(Integer id) {

        if (!forumRepository.existsById(id)) {

            throw new IllegalArgumentException(
                "La publicación con ID " + id +
                " no existe."
            );
        }

        forumRepository.deleteById(id);
    }
}