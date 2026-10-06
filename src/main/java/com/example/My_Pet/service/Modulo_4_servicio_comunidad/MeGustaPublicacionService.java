package com.example.My_Pet.service.Modulo_4_servicio_comunidad;

import com.example.My_Pet.model.Modulo_1_gestion_usuario.Usuario;
import com.example.My_Pet.model.Modulo_4_servicio_comunidad.MeGustaPublicacion;
import com.example.My_Pet.model.Modulo_4_servicio_comunidad.PublicacionForo;
import com.example.My_Pet.repository.Modulo_1_gestion_usuario.UsuarioRepository;
import com.example.My_Pet.repository.Modulo_4_servicio_comunidad.MeGustaPublicacionRepository;
import com.example.My_Pet.repository.Modulo_4_servicio_comunidad.PublicacionForoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MeGustaPublicacionService {

    // Repositorio de los Me gusta de las publicaciones
    @Autowired
    private MeGustaPublicacionRepository meGustaRepository;

    // Repositorio de usuarios
    @Autowired
    private UsuarioRepository usuarioRepository;

    // Repositorio de publicaciones
    @Autowired
    private PublicacionForoRepository publicacionRepository;


    // =========================================================
    // AGREGAR O QUITAR ME GUSTA
    // =========================================================

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


        // Si ya existe el Me gusta,
        // lo elimina.
        if (meGustaExistente.isPresent()) {

            meGustaRepository.delete(
                    meGustaExistente.get()
            );

            // false significa que el Me gusta
            // quedó desactivado.
            return false;
        }


        // Si no existía, crea un nuevo Me gusta
        MeGustaPublicacion meGusta =
                new MeGustaPublicacion();

        meGusta.setUsuario(usuario);

        meGusta.setPublicacion(publicacion);

        meGusta.setFecha(
                java.time.LocalDateTime.now()
        );


        // Guarda el nuevo Me gusta
        meGustaRepository.save(meGusta);


        // true significa que el Me gusta
        // quedó activado.
        return true;
    }


    // =========================================================
    // CONTAR ME GUSTAS
    // =========================================================

    public long contarMeGustas(
            Integer idPublicacion) {

        // Verifica que la publicación exista
        if (!publicacionRepository.existsById(idPublicacion)) {

            throw new IllegalArgumentException(
                    "La publicación no existe."
            );
        }


        // Devuelve la cantidad total
        // de Me gusta de la publicación
        return meGustaRepository
                .countByPublicacionIdPublicacion(
                        idPublicacion
                );
    }


    // =========================================================
    // VERIFICAR ME GUSTA DEL USUARIO
    // =========================================================

    public boolean usuarioDioMeGusta(
            Integer idUsuario,
            Integer idPublicacion) {

        // Comprueba si existe un registro
        // para ese usuario y esa publicación.
        return meGustaRepository
                .findByUsuarioIdUsuarioAndPublicacionIdPublicacion(
                        idUsuario,
                        idPublicacion
                )
                .isPresent();
    }
}