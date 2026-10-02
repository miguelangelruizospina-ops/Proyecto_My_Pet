package com.example.My_Pet.service.Modulo_3_Agenda_Recordatorio;

import com.example.My_Pet.model.Modulo_1_gestion_usuario.Usuario;
import com.example.My_Pet.model.Modulo_3_Agenda_Recordatorio.Recordatorio;
import com.example.My_Pet.repository.Modulo_1_gestion_usuario.UsuarioRepository;
import com.example.My_Pet.repository.Modulo_3_Agenda_Recordatorio.RecordatorioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecordatorioService {

    @Autowired
    private RecordatorioRepository recordatorioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;


    // GET - Obtener todos
    public List<Recordatorio> obtenerTodos() {

        return recordatorioRepository.findAll();
    }


    // GET - Obtener por usuario
    public List<Recordatorio> obtenerPorUsuario(Integer idUsuario) {

        return recordatorioRepository.findByUsuarioIdUsuario(idUsuario);
    }


    // Crear recordatorio
    // Este método también puede ser utilizado por EventoService
    public Recordatorio guardar(Recordatorio recordatorio) {

        // Verificar que exista un usuario
        if (recordatorio.getUsuario() == null ||
            recordatorio.getUsuario().getIdUsuario() <= 0) {

            throw new IllegalArgumentException(
                    "No se puede crear un recordatorio sin asociarlo a un usuario válido."
            );
        }


        // Obtener el ID del usuario
        int idUsuario =
                recordatorio.getUsuario().getIdUsuario();


        // Buscar el usuario en la base de datos
        Usuario usuarioExistente =
                usuarioRepository.findById(idUsuario)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "El usuario con ID " +
                                        idUsuario +
                                        " no existe."
                                )
                        );


        // Asociar el usuario existente
        recordatorio.setUsuario(usuarioExistente);


        // Si no se envía estado, colocar Pendiente
        if (recordatorio.getEstado() == null ||
            recordatorio.getEstado().isEmpty()) {

            recordatorio.setEstado("Pendiente");
        }


        // Guardar recordatorio
        return recordatorioRepository.save(recordatorio);
    }


    // PUT - Actualizar
    public Recordatorio actualizar(
            Integer id,
            Recordatorio recordatorio) {

        // Buscar el recordatorio existente
        Recordatorio recordatorioExistente =
                recordatorioRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "El recordatorio con ID " +
                                        id +
                                        " no existe."
                                )
                        );


        // Actualizar mensaje
        recordatorioExistente.setMensaje(
                recordatorio.getMensaje()
        );


        // Actualizar estado
        recordatorioExistente.setEstado(
                recordatorio.getEstado()
        );


        // Actualizar usuario si se envía
        if (recordatorio.getUsuario() != null) {

            int idUsuario =
                    recordatorio.getUsuario().getIdUsuario();


            Usuario usuarioExistente =
                    usuarioRepository.findById(idUsuario)
                            .orElseThrow(() ->
                                    new IllegalArgumentException(
                                            "El usuario con ID " +
                                            idUsuario +
                                            " no existe."
                                    )
                            );


            recordatorioExistente.setUsuario(
                    usuarioExistente
            );
        }


        // Guardar cambios
        return recordatorioRepository.save(
                recordatorioExistente
        );
    }


    // DELETE - Eliminar
    public void eliminar(Integer id) {

        // Verificar que exista
        if (!recordatorioRepository.existsById(id)) {

            throw new IllegalArgumentException(
                    "El recordatorio con ID " +
                    id +
                    " no existe."
            );
        }


        // Eliminar
        recordatorioRepository.deleteById(id);
    }
}