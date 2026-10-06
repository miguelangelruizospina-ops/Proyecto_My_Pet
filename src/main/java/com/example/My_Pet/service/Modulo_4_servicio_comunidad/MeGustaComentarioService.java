package com.example.My_Pet.service.Modulo_4_servicio_comunidad;

import com.example.My_Pet.model.Modulo_1_gestion_usuario.Usuario;
import com.example.My_Pet.model.Modulo_4_servicio_comunidad.ComentarioForo;
import com.example.My_Pet.model.Modulo_4_servicio_comunidad.MeGustaComentario;
import com.example.My_Pet.repository.Modulo_1_gestion_usuario.UsuarioRepository;
import com.example.My_Pet.repository.Modulo_4_servicio_comunidad.ComentarioForoRepository;
import com.example.My_Pet.repository.Modulo_4_servicio_comunidad.MeGustaComentarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MeGustaComentarioService {

    @Autowired
    private MeGustaComentarioRepository meGustaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ComentarioForoRepository comentarioRepository;


    // ==========================================
    // CAMBIAR ME GUSTA
    // ==========================================

    @Transactional
    public boolean cambiarMeGusta(
            Integer idUsuario,
            Integer idComentario) {

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El usuario no existe."
                        )
                );

        ComentarioForo comentario =
                comentarioRepository.findById(idComentario)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "El comentario no existe."
                                )
                        );

        var meGustaExistente =
                meGustaRepository
                        .findByUsuarioIdUsuarioAndComentarioIdComentario(
                                idUsuario,
                                idComentario
                        );

        // Si ya existe, se elimina
        if (meGustaExistente.isPresent()) {

            meGustaRepository.delete(
                    meGustaExistente.get()
            );

            return false;
        }

        // Si no existe, se crea
        MeGustaComentario meGusta =
                new MeGustaComentario();

        meGusta.setUsuario(usuario);
        meGusta.setComentario(comentario);
        meGusta.setFecha(java.time.LocalDateTime.now());

        meGustaRepository.save(meGusta);

        return true;
    }


    // ==========================================
    // CONTAR ME GUSTAS
    // ==========================================

    public long contarMeGustas(
            Integer idComentario) {

        if (!comentarioRepository.existsById(idComentario)) {
            throw new IllegalArgumentException(
                    "El comentario no existe."
            );
        }

        return meGustaRepository
                .countByComentarioIdComentario(
                        idComentario
                );
    }


    // ==========================================
    // VERIFICAR SI EL USUARIO DIO ME GUSTA
    // ==========================================

    public boolean usuarioDioMeGusta(
            Integer idUsuario,
            Integer idComentario) {

        return meGustaRepository
                .findByUsuarioIdUsuarioAndComentarioIdComentario(
                        idUsuario,
                        idComentario
                )
                .isPresent();
    }
}