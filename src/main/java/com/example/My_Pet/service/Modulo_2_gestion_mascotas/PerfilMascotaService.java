package com.example.My_Pet.service.Modulo_2_gestion_mascotas;

// Clases propias del proyecto.

import com.example.My_Pet.model.Modulo_2_gestion_mascota.Mascota;
import com.example.My_Pet.model.Modulo_2_gestion_mascota.PerfilMascota;
import com.example.My_Pet.repository.Modulo_2_gestion_mascotas.MascotaRepository;
import com.example.My_Pet.repository.Modulo_2_gestion_mascotas.PerfilMascotaRepository;

// Librerias del spring.

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Libreria de Java

import java.util.List;

// Gestiona las operaciones de consulta, registro, actualización y eliminación de perfiles de mascotas en la base de datos.

@Service
public class PerfilMascotaService {
    @Autowired
    private PerfilMascotaRepository perfilMascotaRepository;
    @Autowired
    private MascotaRepository mascotaRepository;

    // Obtener todos los perfiles registrados en la basa de datos.

    public List<PerfilMascota> obtenerTodos() {
        return perfilMascotaRepository.findAll();
    }

    // Busca un perfil de mascota registrado por su ID.

    public PerfilMascota obtenerPorId(Integer id) {
        return perfilMascotaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Perfil de mascota no encontrado con ID: " + id
                ));
    }

    // Busca un perfil de mascota mediante el ID de la mascota.

    public PerfilMascota obtenerPorMascota(Integer idMascota) {
        return perfilMascotaRepository.findByMascotaIdMascota(idMascota)
                .orElseThrow(() -> new RuntimeException(
                        "No existe un perfil para la mascota con ID: " + idMascota
                ));
    }

    // Guardar un nuevo perfil de mascota registrado.

    public PerfilMascota guardar(PerfilMascota perfil) {
        if (perfil.getMascota() == null ||
                perfil.getMascota().getIdMascota() == null) {
            throw new IllegalArgumentException(
                    "No se puede crear un perfil sin asociarlo a una mascota."
            );
        }
        Integer idMascota = perfil.getMascota().getIdMascota();

        // Busca la mascota existente en la base de datos.

        Mascota mascotaExistente = mascotaRepository.findById(idMascota)
                .orElseThrow(() -> new IllegalArgumentException(
                        "La mascota con ID " + idMascota + " no existe."
                ));

        // Verificar que la mascota no tenga otro perfil

        if (perfilMascotaRepository.findByMascotaIdMascota(idMascota).isPresent()) {
            throw new IllegalArgumentException(
                    "La mascota con ID " + idMascota +
                    " ya tiene un perfil."
            );
        }

        // Asociar la mascota existente al perfil de mascota creado.

        perfil.setMascota(mascotaExistente);
        return perfilMascotaRepository.save(perfil);
    }

    // Actualizar un perfil de mascota creado. 

    public PerfilMascota actualizar(
            Integer id,
            PerfilMascota datosPerfil) {
        PerfilMascota perfilExistente = obtenerPorId(id);
        perfilExistente.setCaracteristicas(
                datosPerfil.getCaracteristicas()
        );
        perfilExistente.setComportamiento(
                datosPerfil.getComportamiento()
        );
        perfilExistente.setGustos(
                datosPerfil.getGustos()
        );
        perfilExistente.setCuidadosEspeciales(
                datosPerfil.getCuidadosEspeciales()
        );

        // Mantiene la mascota relacionada durante la actualización.

        return perfilMascotaRepository.save(perfilExistente);
    }

    // Eliminar un perfil de mascota registrado.

    public void eliminar(Integer id) {
        PerfilMascota perfilExistente = obtenerPorId(id);
        perfilMascotaRepository.delete(perfilExistente);
    }
}