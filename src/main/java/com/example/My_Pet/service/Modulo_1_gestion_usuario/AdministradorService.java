package com.example.My_Pet.service.Modulo_1_gestion_usuario;

// Clases propias del proyecto

import com.example.My_Pet.model.Modulo_1_gestion_usuario.Administrador;
import com.example.My_Pet.model.Modulo_1_gestion_usuario.Usuario;
import com.example.My_Pet.repository.Modulo_1_gestion_usuario.AdministradorRepository;
import com.example.My_Pet.repository.Modulo_1_gestion_usuario.UsuarioRepository;

// Librerias del spring

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Libreria de Java.

import java.util.List;

// Servicio y repositorios utilizados para gestionar administradores y usuarios en la base de datos.

@Service
public class AdministradorService {
    @Autowired
    private AdministradorRepository administradorRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    // Gestiona las operaciones de consulta, registro, actualización y eliminación de administradores en la base de datos.

    public List<Administrador> obtenerTodos() {
        return administradorRepository.findAll();
    }
    public Administrador obtenerPorId(Integer id) {
        return administradorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Administrador no encontrado con ID: " + id
                ));
    }
    public Administrador obtenerPorUsuario(Integer idUsuario) {
        return administradorRepository
                .findByUsuarioIdUsuario(idUsuario)
                .orElseThrow(() -> new RuntimeException(
                        "No existe un administrador asociado al usuario con ID: "
                                + idUsuario
                ));
    }
    public Administrador guardar(Administrador administrador) {

        // Verifica que exista un usuario válido.

        if (administrador.getUsuario() == null ||
                administrador.getUsuario().getIdUsuario() <= 0) {
            throw new IllegalArgumentException(
                    "El administrador debe estar ligado a un usuario válido."
            );
        }
        Integer idUsuario = administrador.getUsuario().getIdUsuario();
        Usuario usuarioExistente =
                usuarioRepository.findById(idUsuario)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "El usuario con ID " +
                                                idUsuario +
                                                " no existe."
                                )
                        );

        // Evita registrar dos administradores para el mismo usuario.

        if (administradorRepository
                .findByUsuarioIdUsuario(idUsuario)
                .isPresent()) {
            throw new IllegalArgumentException(
                    "El usuario con ID " + idUsuario +
                            " ya está registrado como administrador."
            );
        }
        administrador.setUsuario(usuarioExistente);
        return administradorRepository.save(administrador);
    }

    // Gestiona las operaciones de consulta, registro, actualización y eliminación de administradores en la base de datos.

    public Administrador actualizar(
            Integer id,
            Administrador datosAdministrador) {
        Administrador administradorExistente =
                obtenerPorId(id);

        // Verifica que los permisos hayan sido enviados.

        if (datosAdministrador.getPermisos() == null ||
                datosAdministrador.getPermisos().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Los permisos son obligatorios."
            );
        }
        administradorExistente.setPermisos(
                datosAdministrador.getPermisos()
        );
        return administradorRepository.save(
                administradorExistente
        );
    }
    public void eliminar(Integer id) {
        Administrador administradorExistente =
                obtenerPorId(id);
        administradorRepository.delete(administradorExistente);
    }
}