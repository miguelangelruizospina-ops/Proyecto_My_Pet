
// Servicio encargado de gestionar la información del historial médico
// de las mascotas utilizada por la interfaz de historial médico.

// Paquete donde se encuentra el servicio.

package com.example.My_Pet.service.Modulo_2_gestion_mascotas;

// Librerías necesarias para gestionar el historial médico,
// las mascotas y las operaciones de la base de datos.

import com.example.My_Pet.model.Modulo_2_gestion_mascota.HistorialMedico;

import com.example.My_Pet.model.Modulo_2_gestion_mascota.Mascota;

import com.example.My_Pet.repository.Modulo_2_gestion_mascotas.HistorialMedicoRepository;

import com.example.My_Pet.repository.Modulo_2_gestion_mascotas.MascotaRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// Define el servicio para la gestión de historiales médicos.

@Service

public class HistorialMedicoService {

    @Autowired
    private HistorialMedicoRepository historialMedicoRepository;

    @Autowired
    private MascotaRepository mascotaRepository;

    // Consulta los historiales médicos registrados.

    public List<HistorialMedico> obtenerTodos() {

        return historialMedicoRepository.findAll();

    }

    // Consulta los historiales médicos asociados a una mascota.

    public List<HistorialMedico> obtenerPorMascota(Integer idMascota) {

        return historialMedicoRepository.findByMascotaIdMascota(idMascota);

    }

    // Registra un nuevo historial y lo relaciona con una mascota existente.

    @Transactional

    public HistorialMedico guardar(HistorialMedico historial) {

        if (historial.getMascota() == null
                || historial.getMascota().getIdMascota() == null) {

            throw new IllegalArgumentException(
                    "No se puede registrar un historial sin asociarlo a una mascota."
            );
        }

        Integer idMascota =
                historial.getMascota().getIdMascota();

        Mascota mascotaExistente =
                mascotaRepository.findById(idMascota)

                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "La mascota con ID "
                                                + idMascota
                                                + " no existe."
                                )
                        );

        historial.setMascota(mascotaExistente);

        return historialMedicoRepository.save(historial);

    }

    // Actualiza la información de un historial médico existente.

    @Transactional

    public HistorialMedico actualizar(
            Integer id,
            HistorialMedico datos) {

        HistorialMedico historialExistente =
                historialMedicoRepository.findById(id)

                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "El historial médico con ID "
                                                + id
                                                + " no existe."
                                )
                        );

        if (datos.getMascota() == null
                || datos.getMascota().getIdMascota() == null) {

            throw new IllegalArgumentException(
                    "La mascota es obligatoria."
            );
        }

        Integer idMascota =
                datos.getMascota().getIdMascota();

        Mascota mascotaExistente =
                mascotaRepository.findById(idMascota)

                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "La mascota con ID "
                                                + idMascota
                                                + " no existe."
                                )
                        );

        historialExistente.setDiagnosticos(
                datos.getDiagnosticos()
        );

        historialExistente.setAntecedentesMedicos(
                datos.getAntecedentesMedicos()
        );

        historialExistente.setAlergias(
                datos.getAlergias()
        );

        historialExistente.setTratamientos(
                datos.getTratamientos()
        );

        historialExistente.setPeso(
                datos.getPeso()
        );

        historialExistente.setCirugiasProcedimientos(
                datos.getCirugiasProcedimientos()
        );

        historialExistente.setVacunacion(
                datos.getVacunacion()
        );

        historialExistente.setConsultasAtenciones(
                datos.getConsultasAtenciones()
        );

        historialExistente.setEnfermedadesActuales(
                datos.getEnfermedadesActuales()
        );

        historialExistente.setMascota(mascotaExistente);

        return historialMedicoRepository.save(
                historialExistente
        );

    }

    // Elimina un historial médico existente.

    @Transactional

    public void eliminar(Integer id) {

        HistorialMedico historialExistente =
                historialMedicoRepository.findById(id)

                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "El historial médico con ID "
                                                + id
                                                + " no existe."
                                )
                        );

        historialMedicoRepository.delete(
                historialExistente
        );

    }

}
