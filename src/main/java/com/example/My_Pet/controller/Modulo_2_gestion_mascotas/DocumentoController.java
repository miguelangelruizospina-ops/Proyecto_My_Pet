
// Controlador para la gestión de documentos de las mascotas.

package com.example.My_Pet.controller.Modulo_2_gestion_mascotas;

// Modelos y servicios.

import com.example.My_Pet.model.Modulo_2_gestion_mascota.Documento;
import com.example.My_Pet.service.Modulo_2_gestion_mascotas.DocumentoService;

// Librerías de Spring.

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

// Librerías de Java.

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;


// Define el controlador REST de documentos.

@RestController
@RequestMapping("/api/documentos")
public class DocumentoController {

    @Autowired
    private DocumentoService documentoService;


    // Lista todos los documentos. Solo para administradores.

    @GetMapping("/listar")
    @PreAuthorize("@autorizacion.esAdministrador()")
    public List<Documento> listarTodo() {

        return documentoService.obtenerTodos();
    }


    // Lista los documentos asociados a un usuario.

    @GetMapping("/usuario/{idUsuario}")
    @PreAuthorize(
            "@autorizacion.esUsuarioPropioOAdministrador(#idUsuario)"
    )
    public ResponseEntity<List<Documento>> listarPorUsuario(

            @PathVariable("idUsuario")
            Integer idUsuario) {

        return ResponseEntity.ok(
                documentoService.obtenerPorUsuario(idUsuario)
        );
    }


    // Lista los documentos asociados a una mascota.

    @GetMapping("/mascota/{idMascota}")
    @PreAuthorize(
            "@autorizacion.esMascotaPropiaOAdministrador(#idMascota)"
    )
    public ResponseEntity<List<Documento>> listarPorMascota(

            @PathVariable("idMascota")
            @P("idMascota")
            Integer idMascota) {

        return ResponseEntity.ok(
                documentoService.obtenerPorMascota(idMascota)
        );
    }


    // Registra un documento y almacena el archivo seleccionado.

    @PostMapping(
            value = "/guardar",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @PreAuthorize(
            "@autorizacion.puedeGuardarDocumento(#idUsuario, #idMascota)"
    )
    public ResponseEntity<?> crearDocumento(

            @RequestParam("tipoDocumento")
            String tipoDocumento,

            @RequestParam("nombreDocumento")
            String nombreDocumento,

            @RequestParam(
                    value = "fechaDocumento",
                    required = false
            )
            String fechaDocumento,

            @RequestParam("archivo")
            MultipartFile archivo,

            @RequestParam("idUsuario")
            @P("idUsuario")
            Integer idUsuario,

            @RequestParam("idMascota")
            @P("idMascota")
            Integer idMascota) {

        try {

            LocalDate fecha = null;

            if (fechaDocumento != null &&
                    !fechaDocumento.isBlank()) {

                fecha = LocalDate.parse(fechaDocumento);
            }

            Documento nuevoDocumento =
                    documentoService.guardar(
                            tipoDocumento,
                            nombreDocumento,
                            fecha,
                            archivo,
                            idUsuario,
                            idMascota
                    );

            return ResponseEntity.ok(nuevoDocumento);

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());

        } catch (RuntimeException e) {

            return ResponseEntity
                    .internalServerError()
                    .body(e.getMessage());
        }
    }


    // Permite visualizar un documento en el navegador.

    @GetMapping("/ver/{id}")
    @PreAuthorize(
            "@autorizacion.esDocumentoPropioOAdministrador(#id)"
    )
    public ResponseEntity<Resource> verDocumento(

            @PathVariable("id")
            @P("id")
            Integer id) {

        try {

            Documento documento =
                    documentoService.obtenerPorId(id);

            Path ruta =
                    documentoService.obtenerRutaArchivo(id);

            Resource recurso =
                    new UrlResource(ruta.toUri());

            String tipoContenido =
                    Files.probeContentType(ruta);

            if (tipoContenido == null) {

                tipoContenido =
                        MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }

            return ResponseEntity.ok()
                    .contentType(
                            MediaType.parseMediaType(
                                    tipoContenido
                            )
                    )
                    .header(
                            HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename=\"" +
                                    documento.getNombreDocumento() +
                                    "\""
                    )
                    .body(recurso);

        } catch (Exception e) {

            return ResponseEntity
                    .notFound()
                    .build();
        }
    }


    // Permite descargar un documento almacenado.

    @GetMapping("/descargar/{id}")
    @PreAuthorize(
            "@autorizacion.esDocumentoPropioOAdministrador(#id)"
    )
    public ResponseEntity<Resource> descargarDocumento(

            @PathVariable("id")
            @P("id")
            Integer id) {

        try {

            Documento documento =
                    documentoService.obtenerPorId(id);

            Path ruta =
                    documentoService.obtenerRutaArchivo(id);

            Resource recurso =
                    new UrlResource(ruta.toUri());

            String tipoContenido =
                    Files.probeContentType(ruta);

            if (tipoContenido == null) {

                tipoContenido =
                        MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }

            return ResponseEntity.ok()
                    .contentType(
                            MediaType.parseMediaType(
                                    tipoContenido
                            )
                    )
                    .header(
                            HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" +
                                    documento.getNombreDocumento() +
                                    "\""
                    )
                    .body(recurso);

        } catch (Exception e) {

            return ResponseEntity
                    .notFound()
                    .build();
        }
    }


    // Elimina el documento y su archivo almacenado.

    @DeleteMapping("/eliminar/{id}")
    @PreAuthorize(
            "@autorizacion.esDocumentoPropioOAdministrador(#id)"
    )
    public ResponseEntity<String> eliminarDocumento(

            @PathVariable("id")
            @P("id")
            Integer id) {

        try {

            documentoService.eliminar(id);

            return ResponseEntity.ok(
                    "Documento eliminado correctamente"
            );

        } catch (RuntimeException e) {

            return ResponseEntity
                    .notFound()
                    .build();
        }
    }
}
