package com.example.My_Pet.service.Modulo_2_gestion_mascotas;

// Clase principal del proyecto

import com.example.My_Pet.model.Modulo_1_gestion_usuario.Usuario;
import com.example.My_Pet.model.Modulo_2_gestion_mascota.Mascota;
import com.example.My_Pet.model.Modulo_2_gestion_mascota.PerfilMascota;
import com.example.My_Pet.repository.Modulo_1_gestion_usuario.UsuarioRepository;
import com.example.My_Pet.repository.Modulo_2_gestion_mascotas.MascotaRepository;
import com.example.My_Pet.repository.Modulo_2_gestion_mascotas.PerfilMascotaRepository;

// Libreria del spring.

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Liberia de Java

import java.util.List;

// Gestiona las operaciones de consulta, registro, actualización y eliminación de mascotas en la base de datos.

@Service
public class MascotaService {
    @Autowired
    private MascotaRepository mascotaRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
        @Autowired
        private PerfilMascotaRepository perfilMascotaRepository;

    // Método para listar todas las mascotas registradas en la base de datos.

    public List<Mascota> obtenerTodasLasMascotas() {
        return mascotaRepository.findAll();
    }

    // Método para listar las mascotas de un usuario específico que estan registrados en la base de datos.

    public List<Mascota> obtenerMascotasPorUsuario(Integer idUsuario) {
        return mascotaRepository.findByUsuarioIdUsuario(idUsuario);
    }

    // Método para buscar una mascota por su ID

    public Mascota obtenerMascotaPorId(Integer idMascota) {
        return mascotaRepository.findById(idMascota)
                .orElseThrow(() -> new RuntimeException(
                        "Mascota no encontrada con ID: " + idMascota
                ));
    }

    // Método para guardar una mascota nueva en la base de datos.

        @Transactional
    public Mascota guardarMascota(Mascota mascota) {
        if (mascota.getUsuario() == null) {
            throw new RuntimeException(
                    "La mascota debe tener un usuario"
            );
        }
        Integer idUsuario = mascota.getUsuario().getIdUsuario();

        // Verifica que el usuario exista en la base de datos.

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException(
                        "El usuario con ID " + idUsuario + " no existe"
                ));

       // Asocia la mascota con el usuario existente en la base de datos.

        mascota.setUsuario(usuario);
        Mascota mascotaGuardada = mascotaRepository.save(mascota);
        PerfilMascota perfil = new PerfilMascota();
        perfil.setMascota(mascotaGuardada);
        perfilMascotaRepository.save(perfil);
        return mascotaGuardada;
    }

    // Método para actualizar una mascota registrada.

    public Mascota actualizarMascota(
            Integer idMascota,
            Mascota datosMascota) {
        Mascota mascotaExistente =
                obtenerMascotaPorId(idMascota);

        // Actualizamos los datos de la mascota registrada en la base de datos.

        mascotaExistente.setNombre(
                datosMascota.getNombre()
        );
        mascotaExistente.setEspecie(
                datosMascota.getEspecie()
        );
        mascotaExistente.setRaza(
                datosMascota.getRaza()
        );
        mascotaExistente.setFechaNacimiento(
                datosMascota.getFechaNacimiento()
        );
        mascotaExistente.setFoto(
                datosMascota.getFoto()
        );

        // Mantiene el usuario propietario original durante la actualización.

        return mascotaRepository.save(mascotaExistente);
    }

    // Método para eliminar una mascota de la base de datos.

    public void eliminarMascota(Integer idMascota) {
        Mascota mascotaExistente =
                obtenerMascotaPorId(idMascota);
        mascotaRepository.delete(mascotaExistente);
    }
}