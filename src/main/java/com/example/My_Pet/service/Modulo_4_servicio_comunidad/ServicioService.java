package com.example.My_Pet.service.Modulo_4_servicio_comunidad;

import com.example.My_Pet.model.Modulo_1_gestion_usuario.Usuario;
import com.example.My_Pet.model.Modulo_4_servicio_comunidad.Servicio;
import com.example.My_Pet.repository.Modulo_1_gestion_usuario.UsuarioRepository;
import com.example.My_Pet.repository.Modulo_4_servicio_comunidad.ServicioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicioService {

    @Autowired
    private ServicioRepository servicioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;


    // GET - Obtener todos los servicios
    public List<Servicio> obtenerTodos() {

        return servicioRepository.findAll();
    }


    // GET - Obtener servicios por tipo
    public List<Servicio> obtenerPorTipo(String tipo) {

        return servicioRepository.findByTipo(tipo);
    }


    // GET - Obtener un servicio por ID
    public Servicio obtenerPorId(Integer id) {

        return servicioRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El servicio con ID " + id +
                                " no existe."
                        )
                );
    }


    // POST - Crear un servicio
    public Servicio guardar(Servicio servicio) {

        // Verificar que tenga un usuario
        if (servicio.getUsuario() == null ||
            servicio.getUsuario().getIdUsuario() <= 0) {

            throw new IllegalArgumentException(
                    "El servicio debe estar asociado a un usuario válido."
            );
        }


        // Obtener el ID del usuario
        Integer idUsuario =
                servicio.getUsuario().getIdUsuario();


        // Verificar que el usuario exista
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
        servicio.setUsuario(usuarioExistente);


        // Guardar el servicio
        return servicioRepository.save(servicio);
    }


    // PUT - Actualizar un servicio
    public Servicio actualizar(
            Integer id,
            Servicio servicio) {

        // Buscar el servicio
        Servicio servicioExistente =
                servicioRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "El servicio con ID " +
                                        id +
                                        " no existe."
                                )
                        );


        // Actualizar nombre
        servicioExistente.setNombre(
                servicio.getNombre()
        );


        // Actualizar tipo
        servicioExistente.setTipo(
                servicio.getTipo()
        );


        // Actualizar ubicación
        servicioExistente.setUbicacion(
                servicio.getUbicacion()
        );


        // Actualizar descripción
        servicioExistente.setDescripcion(
                servicio.getDescripcion()
        );


        // Actualizar calificación
        servicioExistente.setCalificacion(
                servicio.getCalificacion()
        );


        // Actualizar usuario si se envía
        if (servicio.getUsuario() != null) {

            Integer idUsuario =
                    servicio.getUsuario().getIdUsuario();


            // Verificar que el usuario exista
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
            servicioExistente.setUsuario(
                    usuarioExistente
            );
        }


        // Guardar los cambios
        return servicioRepository.save(
                servicioExistente
        );
    }


    // DELETE - Eliminar un servicio
    public void eliminar(Integer id) {

        // Verificar que el servicio exista
        if (!servicioRepository.existsById(id)) {

            throw new IllegalArgumentException(
                    "El servicio con ID " +
                    id +
                    " no existe."
            );
        }


        // Eliminar el servicio
        servicioRepository.deleteById(id);
    }
}