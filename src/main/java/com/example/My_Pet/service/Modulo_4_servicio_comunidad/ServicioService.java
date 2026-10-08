package com.example.My_Pet.service.Modulo_4_servicio_comunidad;

// Clases propias del proyecto.

import com.example.My_Pet.model.Modulo_1_gestion_usuario.Usuario;
import com.example.My_Pet.model.Modulo_4_servicio_comunidad.Servicio;
import com.example.My_Pet.repository.Modulo_1_gestion_usuario.UsuarioRepository;
import com.example.My_Pet.repository.Modulo_4_servicio_comunidad.ServicioRepository;

// Librerias del spring.

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Librerias de Java

import java.util.List;

// Gestiona las operaciones de consulta, registro, actualización y eliminación de servicios en la base de datos.

@Service
public class ServicioService {
    @Autowired
    private ServicioRepository servicioRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    //Obteniene todos los servicios registrados

    public List<Servicio> obtenerTodos() {
        return servicioRepository.findAll();
    }

    // Obtiene los  servicios  reigstrados por tipo.

    public List<Servicio> obtenerPorTipo(String tipo) {
        return servicioRepository.findByTipo(tipo);
    }


    // Obtiene un servicio registrado por su ID.

    public Servicio obtenerPorId(Integer id) {
        return servicioRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El servicio con ID " + id +
                                " no existe."
                        )
                );
    }


    // Crear un servicio
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

       // Asocia el servicio con el usuario existente en la base de datos.

        servicio.setUsuario(usuarioExistente);
        return servicioRepository.save(servicio);
    }

 // Actualiza un servicio existente.

    public Servicio actualizar(
            Integer id,
            Servicio servicio) {

         // Busca el servicio existente en la base de datos.

        Servicio servicioExistente =
                servicioRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "El servicio con ID " +
                                        id +
                                        " no existe."
                                )
                        );


        // Actualiza los datos del servicio

        servicioExistente.setNombre(
                servicio.getNombre()
        );
        servicioExistente.setTipo(
                servicio.getTipo()
        );
        servicioExistente.setUbicacion(
                servicio.getUbicacion()
        );
        servicioExistente.setDescripcion(
                servicio.getDescripcion()
        );
        servicioExistente.setCalificacion(
                servicio.getCalificacion()
        );
        if (servicio.getUsuario() != null) {
            Integer idUsuario =
                    servicio.getUsuario().getIdUsuario();

             // Verifica que el usuario exista en la base de datos.

            Usuario usuarioExistente =
                    usuarioRepository.findById(idUsuario)
                            .orElseThrow(() ->
                                    new IllegalArgumentException(
                                            "El usuario con ID " +
                                            idUsuario +
                                            " no existe."
                                    )
                            );

             // Asocia el usuario existente al servicio.

            servicioExistente.setUsuario(
                    usuarioExistente
            );
        }
        return servicioRepository.save(
                servicioExistente
        );
    }

    // Elimina un servicio existente.

    public void eliminar(Integer id) {

        // Verificar que el servicio exista antes de eliminarlo.

        if (!servicioRepository.existsById(id)) {
            throw new IllegalArgumentException(
                    "El servicio con ID " +
                    id +
                    " no existe."
            );
        }

        // Elimina el servicio de la base de datos.

        servicioRepository.deleteById(id);
    }
}