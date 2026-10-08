package com.example.My_Pet.service.Modulo_2_gestion_mascotas;

//  Clases propias del proyecto.

import com.example.My_Pet.model.Modulo_1_gestion_usuario.Usuario;
import com.example.My_Pet.model.Modulo_2_gestion_mascota.Documento;
import com.example.My_Pet.model.Modulo_2_gestion_mascota.Mascota;
import com.example.My_Pet.repository.Modulo_2_gestion_mascotas.DocumentoRepository;

// Librerias del spring.

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

// Librerias de Java

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

// Gestiona las operaciones de consulta, registro, actualización y eliminación de documentos en la base de datos.

@Service
public class DocumentoService {
    @Autowired
    private DocumentoRepository documentoRepository;

    // Define la carpeta donde se almacenarán los archivos.

    private final Path carpetaDocumentos =
            Paths.get("uploads", "documentos")
                    .toAbsolutePath()
                    .normalize();

    // Lista todos los documentos registrados en la base de datos.

    public List<Documento> obtenerTodos() {
        return documentoRepository.findAll();
    }

    // Lista los documentos asociados a un usuario.

    public List<Documento> obtenerPorUsuario(Integer idUsuario) {
        return documentoRepository.findByUsuario_IdUsuario(idUsuario);
    }

    // Lista los documentos asociados a una mascota.

    public List<Documento> obtenerPorMascota(Integer idMascota) {
        return documentoRepository.findByMascota_IdMascota(idMascota);
    }

    // Busca un documento guardado por su ID.

    public Documento obtenerPorId(Integer id) {
        return documentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Documento no encontrado."
                ));
    }

    // Valida y guarda el documento y su archivo.

    public Documento guardar(
            String tipoDocumento,
            String nombreDocumento,
            LocalDate fechaDocumento,
            MultipartFile archivo,
            Integer idUsuario,
            Integer idMascota) {

        // Verifica los datos obligatorios.

        if (tipoDocumento == null || tipoDocumento.isBlank()) {
            throw new IllegalArgumentException(
                    "El tipo de documento es obligatorio."
            );
        }
        if (nombreDocumento == null || nombreDocumento.isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre del documento es obligatorio."
            );
        }
        if (archivo == null || archivo.isEmpty()) {
            throw new IllegalArgumentException(
                    "El archivo es obligatorio."
            );
        }
        if (idUsuario == null) {
            throw new IllegalArgumentException(
                    "El usuario es obligatorio."
            );
        }
        if (idMascota == null) {
            throw new IllegalArgumentException(
                    "La mascota es obligatoria."
            );
        }
        try {

            // Crea la carpeta si todavía no existe.

            Files.createDirectories(carpetaDocumentos);
            String nombreOriginal =
                    archivo.getOriginalFilename();
            String extension = "";

            // Obtiene la extensión del archivo y la convierte a minúsculas.

            if (nombreOriginal != null &&
                    nombreOriginal.contains(".")) {
                extension =
                        nombreOriginal
                                .substring(
                                        nombreOriginal.lastIndexOf(".")
                                )
                                .toLowerCase();
            }

            // Valida los formatos permitidos.

            if (!extension.equals(".pdf") &&
                !extension.equals(".jpg") &&
                !extension.equals(".jpeg") &&
                !extension.equals(".png")) {
                throw new IllegalArgumentException(
                        "Solo se permiten archivos PDF, JPG, JPEG y PNG."
                );
            }

            // Genera un nombre único para el archivo.

            String nombreArchivo =
                    UUID.randomUUID() + extension;
            Path rutaArchivo =
                    carpetaDocumentos
                            .resolve(nombreArchivo)
                            .normalize();

            // Guarda físicamente el archivo.

            Files.copy(
                    archivo.getInputStream(),
                    rutaArchivo
            );

            // Crea el documento que será almacenado en MySQL.

            Documento documento = new Documento();
            documento.setTipoDocumento(
                    tipoDocumento.trim()
            );
            documento.setNombreDocumento(
                    nombreDocumento.trim()
            );
            documento.setFechaDocumento(
                    fechaDocumento
            );
            documento.setArchivo(
                    nombreArchivo
            );

            // Asocia el documento registrado con el usuario.

            Usuario usuario = new Usuario();
            usuario.setIdUsuario(idUsuario);
            documento.setUsuario(usuario);

            // Asocia el documento con la mascota.

            Mascota mascota = new Mascota();
            mascota.setIdMascota(idMascota);
            documento.setMascota(mascota);
            return documentoRepository.save(documento);
        } catch (IOException e) {
            throw new RuntimeException(
                    "No se pudo guardar el archivo.",
                    e
            );
        }
    }

    // Obtiene la ruta física de un archivo almacenado.

    public Path obtenerRutaArchivo(Integer id) {
        Documento documento =
                obtenerPorId(id);
        Path ruta =
                carpetaDocumentos
                        .resolve(documento.getArchivo())
                        .normalize();
        if (!ruta.startsWith(carpetaDocumentos)) {
            throw new RuntimeException(
                    "Ruta de archivo no válida."
            );
        }
        if (!Files.exists(ruta)) {
            throw new RuntimeException(
                    "El archivo no existe en el servidor."
            );
        }
        return ruta;
    }

    // Elimina el archivo y el registro correspondiente.

    public void eliminar(Integer id) {
        Documento documento =
                obtenerPorId(id);
        try {
            Path ruta =
                    carpetaDocumentos
                            .resolve(documento.getArchivo())
                            .normalize();

            if (Files.exists(ruta)) {
                Files.delete(ruta);
            }

        } catch (IOException e) {
            throw new RuntimeException(
                    "No se pudo eliminar el archivo.",
                    e
            );
        }
        documentoRepository.delete(documento);
    }
}
