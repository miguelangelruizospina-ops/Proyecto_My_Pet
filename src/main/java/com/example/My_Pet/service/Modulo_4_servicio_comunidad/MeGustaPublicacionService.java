package com.example.My_Pet.service.Modulo_4_servicio_comunidad;

// Clases propias del proyecto.

import com.example.My_Pet.model.Modulo_1_gestion_usuario.Usuario;
import com.example.My_Pet.model.Modulo_4_servicio_comunidad.MeGustaPublicacion;
import com.example.My_Pet.model.Modulo_4_servicio_comunidad.PublicacionForo;
import com.example.My_Pet.repository.Modulo_1_gestion_usuario.UsuarioRepository;
import com.example.My_Pet.repository.Modulo_4_servicio_comunidad.MeGustaPublicacionRepository;
import com.example.My_Pet.repository.Modulo_4_servicio_comunidad.PublicacionForoRepository;

// Librerias del spring.

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Gestiona las operaciones de registro, consulta y eliminación de "Me gusta" en las publicaciones.

@Service
public class MeGustaPublicacionService {
    @Autowired
    private MeGustaPublicacionRepository meGustaRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PublicacionForoRepository publicacionRepository;

    // Agrega o elimina el "Me gusta" de una publicación.

    @Transactional
    public boolean cambiarMeGusta(
            Integer idUsuario,
            Integer idPublicacion) {

        // Verifica que el usuario exista

        Usuario usuario =
                usuarioRepository.findById(idUsuario)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "El usuario no existe."
                                )
                        );

        // Verifica que la publicación exista

        PublicacionForo publicacion =
                publicacionRepository.findById(idPublicacion)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "La publicación no existe."
                                )
                        );

        // Busca si el usuario ya había dado Me gusta

        var meGustaExistente =
                meGustaRepository
                        .findByUsuarioIdUsuarioAndPublicacionIdPublicacion(
                                idUsuario,
                                idPublicacion
                        );

        // Si ya existe el Me gusta lo elimina.

        if (meGustaExistente.isPresent()) {
            meGustaRepository.delete(
                    meGustaExistente.get()
            );
            return false;
        }

        // Si no existía el me gusta crea un nuevo Me gusta.

        MeGustaPublicacion meGusta =
                new MeGustaPublicacion();
        meGusta.setUsuario(usuario);
        meGusta.setPublicacion(publicacion);
        meGusta.setFecha(
                java.time.LocalDateTime.now()
        );


        // Guarda el nuevo Me gusta de la publicacion.

        meGustaRepository.save(meGusta);
        return true;
    }

    // Consulta la cantidad de "Me gusta" de una publicación.

    public long contarMeGustas(
            Integer idPublicacion) {

        // Verifica que la publicación exista

        if (!publicacionRepository.existsById(idPublicacion)) {
            throw new IllegalArgumentException(
                    "La publicación no existe."
            );
        }

        // Devuelve la cantidad total de Me gusta de la publicación

        return meGustaRepository
                .countByPublicacionIdPublicacion(
                        idPublicacion
                );
    }

    // Verifica si el usuario dio "Me gusta" a una publicación.

    public boolean usuarioDioMeGusta(
            Integer idUsuario,
            Integer idPublicacion) {

        // Comprueba si existe un registro para ese usuario y esa publicación.

        return meGustaRepository
                .findByUsuarioIdUsuarioAndPublicacionIdPublicacion(
                        idUsuario,
                        idPublicacion
                )
                .isPresent();
    }
}