package com.example.My_Pet.service.Modulo_2_gestion_mascotas;

import com.example.My_Pet.model.Modulo_1_gestion_usuario.Usuario;
import com.example.My_Pet.model.Modulo_2_gestion_mascota.Mascota;
import com.example.My_Pet.model.Modulo_2_gestion_mascota.PerfilMascota;
import com.example.My_Pet.repository.Modulo_1_gestion_usuario.UsuarioRepository;
import com.example.My_Pet.repository.Modulo_2_gestion_mascotas.MascotaRepository;
import com.example.My_Pet.repository.Modulo_2_gestion_mascotas.PerfilMascotaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MascotaService {

    @Autowired
    private MascotaRepository mascotaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

        @Autowired
        private PerfilMascotaRepository perfilMascotaRepository;

    // Método para listar todas las mascotas
    public List<Mascota> obtenerTodasLasMascotas() {
        return mascotaRepository.findAll();
    }

    // Método para listar las mascotas de un usuario específico
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

    // Método para guardar una mascota
        @Transactional
    public Mascota guardarMascota(Mascota mascota) {

        if (mascota.getUsuario() == null) {
            throw new RuntimeException(
                    "La mascota debe tener un usuario"
            );
        }

        Integer idUsuario = mascota.getUsuario().getIdUsuario();

        // Buscamos que el usuario exista
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException(
                        "El usuario con ID " + idUsuario + " no existe"
                ));

        // Asociamos el usuario real de la base de datos
        mascota.setUsuario(usuario);

        Mascota mascotaGuardada = mascotaRepository.save(mascota);

        PerfilMascota perfil = new PerfilMascota();
        perfil.setMascota(mascotaGuardada);
        perfilMascotaRepository.save(perfil);

        return mascotaGuardada;
    }

    // Método para actualizar una mascota
    public Mascota actualizarMascota(
            Integer idMascota,
            Mascota datosMascota) {

        Mascota mascotaExistente =
                obtenerMascotaPorId(idMascota);

        // Actualizamos los datos de la mascota
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

        // No cambiamos el usuario dueño
        // durante la actualización.
        return mascotaRepository.save(mascotaExistente);
    }

    // Método para eliminar una mascota
    public void eliminarMascota(Integer idMascota) {

        Mascota mascotaExistente =
                obtenerMascotaPorId(idMascota);

        mascotaRepository.delete(mascotaExistente);
    }
}