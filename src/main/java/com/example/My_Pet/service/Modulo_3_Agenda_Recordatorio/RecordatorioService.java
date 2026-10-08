package com.example.My_Pet.service.Modulo_3_Agenda_Recordatorio;

// Clases propias del proyecto.

import com.example.My_Pet.model.Modulo_1_gestion_usuario.Usuario;
import com.example.My_Pet.model.Modulo_2_gestion_mascota.Mascota;
import com.example.My_Pet.model.Modulo_3_Agenda_Recordatorio.Recordatorio;
import com.example.My_Pet.repository.Modulo_1_gestion_usuario.UsuarioRepository;
import com.example.My_Pet.repository.Modulo_2_gestion_mascotas.MascotaRepository;
import com.example.My_Pet.repository.Modulo_3_Agenda_Recordatorio.RecordatorioRepository;

// Librerías del Spring.

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Librerias de Java

import java.time.LocalDateTime;
import java.util.List;

// Gestiona las operaciones de consulta, registro, actualización y eliminación de recordatorios en la base de datos.

@Service
public class RecordatorioService {
    @Autowired
    private RecordatorioRepository recordatorioRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private MascotaRepository mascotaRepository;

    // Obtiene todos los recordatorios registrdos en la base de datos.

    public List<Recordatorio> obtenerTodos() {
        return recordatorioRepository.findAll();
    }

    // Obtiene los recordatorios registrados de un usuario existente.

    public List<Recordatorio> obtenerPorUsuario(Integer idUsuario) {
        return recordatorioRepository.findByUsuarioIdUsuario(idUsuario);
    }

    // Obtiene los recordatorios de una mascota.

    public List<Recordatorio> obtenerPorMascota(Integer idMascota) {
        return recordatorioRepository.findByMascotaIdMascota(idMascota);
    }

   // Lista los recordatorios pendientes cuya fecha ya se cumplió.

    public List<Recordatorio> obtenerPendientesParaNotificar() {
        return recordatorioRepository.findByEstadoAndFechaLessThanEqual(
                "PENDIENTE",
                LocalDateTime.now()
        );
    }

 // Registra un nuevo recordatorio en la base de datos.

    public Recordatorio guardar(Recordatorio recordatorio) {
        if (recordatorio == null) {
            throw new IllegalArgumentException(
                "El recordatorio no puede estar vacío."
            );
        }

        // Verifica que el usuario esté asociado al recordatorio.

        if (recordatorio.getUsuario() == null ||
            recordatorio.getUsuario().getIdUsuario() <= 0) {
            throw new IllegalArgumentException(
                "El recordatorio debe estar asociado a un usuario válido."
            );
        }

        // Verifica que el recordatorio tenga fecha y hora.

        if (recordatorio.getFecha() == null) {
            throw new IllegalArgumentException(
                "El recordatorio debe tener una fecha y hora."
            );
        }

        // Verifica que el recordatorio tenga una mascota.

        if (recordatorio.getMascota() == null ||
            recordatorio.getMascota().getIdMascota() == null ||
            recordatorio.getMascota().getIdMascota() <= 0) {
            throw new IllegalArgumentException(
                "El recordatorio debe estar asociado a una mascota válida."
            );
        }
        Integer idUsuario =
                recordatorio.getUsuario().getIdUsuario();
        Integer idMascota =
                recordatorio.getMascota().getIdMascota();

        // Busca el usuario existente en la base de datos.

        Usuario usuarioExistente =
                usuarioRepository.findById(idUsuario)
                    .orElseThrow(() -> new IllegalArgumentException(
                        "El usuario con ID " + idUsuario + " no existe."
                    ));

        // Busca la mascota existente en la base de datos.

        Mascota mascotaExistente =
                mascotaRepository.findById(idMascota)
                    .orElseThrow(() -> new IllegalArgumentException(
                        "La mascota con ID " + idMascota + " no existe."
                    ));

        // Verifica que la mascota pertenezca al usuario.

        if (mascotaExistente.getUsuario() == null ||
            !idUsuario.equals(
                mascotaExistente.getUsuario().getIdUsuario()
            )) {
            throw new IllegalArgumentException(
                "El recordatorio debe estar asociado a una mascota del usuario."
            );
        }
        recordatorio.setUsuario(usuarioExistente);
        recordatorio.setMascota(mascotaExistente);

        // Todo recordatorio nuevo comienza pendiente.

        recordatorio.setEstado("PENDIENTE");
        return recordatorioRepository.save(recordatorio);
    }

    // Actualiza un recordatorio existente.

    public Recordatorio actualizar(
            Integer id,
            Recordatorio recordatorio) {
        if (recordatorio == null) {
            throw new IllegalArgumentException(
                "El recordatorio no puede estar vacío."
            );
        }
        if (recordatorio.getFecha() == null) {
            throw new IllegalArgumentException(
                "El recordatorio debe tener una fecha y hora."
            );
        }

        // Busca el recordatorio que se desea actualizar.

        Recordatorio existente =
                recordatorioRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException(
                        "El recordatorio con ID " + id + " no existe."
                    ));

        // Actualiza los datos del recordatorio.

        existente.setMensaje(recordatorio.getMensaje());
        existente.setFecha(recordatorio.getFecha());

        // Al modificarlo vuelve a quedar pendiente.

        existente.setEstado("PENDIENTE");
        return recordatorioRepository.save(existente);
    }

    // Marca el recordatorio como notificado.

    public Recordatorio marcarComoNotificado(Integer id) {
        Recordatorio existente =
                recordatorioRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException(
                        "El recordatorio con ID " + id + " no existe."
                    ));
        existente.setEstado("NOTIFICADO");
        return recordatorioRepository.save(existente);
    }

    // Elimina un recordatorio registrado en la base de datos.

    public void eliminar(Integer id) {
        if (!recordatorioRepository.existsById(id)) {
            throw new IllegalArgumentException(
                "El recordatorio con ID " + id + " no existe."
            );
        }
        recordatorioRepository.deleteById(id);
    }
}