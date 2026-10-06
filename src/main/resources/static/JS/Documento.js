// JavaScript encargado de gestionar los documentos de una mascota.

// Configuración de los servicios.
const API_DOCUMENTOS =
    "http://localhost:8082/api/documentos";

const API_CSRF =
    "http://localhost:8082/api/usuario/csrf";

// ID de la mascota seleccionada.
let idMascotaDocumento = null;


// Inicializa la página de documentos.
document.addEventListener(
    "DOMContentLoaded",
    function () {

        const parametros =
            new URLSearchParams(
                window.location.search
            );

        idMascotaDocumento =
            parametros.get("idMascota");

        if (!idMascotaDocumento) {

            console.error(
                "No se encontró el ID de la mascota."
            );

            mostrarMensaje(
                "No se encontró la mascota."
            );

            return;
        }

        // Configura el regreso al perfil de la mascota.
        const flechaVolverPerfil =
            document.getElementById(
                "flechaVolverPerfil"
            );

        if (flechaVolverPerfil) {

            flechaVolverPerfil.href =
                `perfil_mascota.html?id=${encodeURIComponent(
                    idMascotaDocumento
                )}`;
        }

        // Configura el botón para agregar documentos.
        const btnAgregarDocumento =
            document.getElementById(
                "btnAgregarDocumento"
            );

        if (btnAgregarDocumento) {

            btnAgregarDocumento.addEventListener(
                "click",
                agregarDocumento
            );
        }

        cargarDocumentos();
    }
);


// Obtiene el token CSRF para las solicitudes protegidas.
async function obtenerTokenCSRF() {

    const respuesta =
        await fetch(
            API_CSRF,
            {
                method: "GET",
                credentials: "include"
            }
        );

    if (!respuesta.ok) {

        throw new Error(
            "No se pudo obtener el token de seguridad."
        );
    }

    return await respuesta.json();
}


// Obtiene el ID del usuario que inició sesión.
function obtenerIdUsuario() {

    const usuarioGuardado =
        localStorage.getItem(
            "usuarioLogueado"
        );

    if (usuarioGuardado) {

        try {

            const usuario =
                JSON.parse(
                    usuarioGuardado
                );

            return (
                usuario.idUsuario ||
                usuario.id_usuario ||
                null
            );

        } catch (error) {

            console.error(
                "No se pudo leer el usuario almacenado:",
                error
            );
        }
    }

    const idUsuario =
        localStorage.getItem(
            "idUsuario"
        );

    return idUsuario
        ? parseInt(idUsuario)
        : null;
}


// Carga los documentos registrados para la mascota.
async function cargarDocumentos() {

    const listaDocumentos =
        document.getElementById(
            "listaDocumentos"
        );

    if (!listaDocumentos) {

        return;
    }

    try {

        const respuesta =
            await fetch(
                `${API_DOCUMENTOS}/mascota/${idMascotaDocumento}`,
                {
                    method: "GET",
                    credentials: "include"
                }
            );

        if (!respuesta.ok) {

            throw new Error(
                "No se pudieron cargar los documentos."
            );
        }

        const documentos =
            await respuesta.json();

        mostrarDocumentos(
            documentos
        );

    } catch (error) {

        console.error(
            "Error al cargar documentos:",
            error
        );

        listaDocumentos.innerHTML = `
            <p class="text-center text-white">
                No se pudieron cargar los documentos.
            </p>
        `;
    }
}


// Muestra los documentos registrados en la interfaz.
function mostrarDocumentos(
    documentos
) {

    const listaDocumentos =
        document.getElementById(
            "listaDocumentos"
        );

    if (!listaDocumentos) {

        return;
    }

    if (
        !documentos ||
        documentos.length === 0
    ) {

        listaDocumentos.innerHTML = `
            <p class="text-center text-white">
                No hay documentos registrados para esta mascota.
            </p>
        `;

        return;
    }

    listaDocumentos.innerHTML = "";

    documentos.forEach(
        function (documento) {

            const tarjeta =
                document.createElement(
                    "div"
                );

            tarjeta.className =
                "documento-item mb-3 p-3";

            const idDocumento =
                documento.idDocumento;

            const tipoDocumento =
                documento.tipoDocumento ||
                "Sin tipo";

            const nombreDocumento =
                documento.nombreDocumento ||
                "Sin nombre";

            const fechaDocumento =
                documento.fechaDocumento ||
                "Sin fecha";

            const archivo =
                documento.archivo ||
                "";

            tarjeta.innerHTML = `
                <div class="documento-contenido">

                    <h3 class="h5">
                        ${escaparHTML(nombreDocumento)}
                    </h3>

                    <p>
                        <strong>Tipo:</strong>
                        ${escaparHTML(tipoDocumento)}
                    </p>

                    <p>
                        <strong>Fecha:</strong>
                        ${escaparHTML(fechaDocumento)}
                    </p>

                    ${
                        archivo
                            ? `
                                <p>
                                    <strong>Archivo:</strong>
                                    ${escaparHTML(archivo)}
                                </p>
                            `
                            : ""
                    }

                    <div class="text-center mt-3">

                        <a
                            href="${API_DOCUMENTOS}/ver/${idDocumento}"
                            target="_blank"
                            class="btn btn-primary me-2">

                            Ver

                        </a>

                        <a
                            href="${API_DOCUMENTOS}/descargar/${idDocumento}"
                            class="btn btn-success me-2">

                            Descargar

                        </a>

                        <button
                            type="button"
                            class="btn btn-danger"
                            onclick="eliminarDocumento(${idDocumento})">

                            Eliminar

                        </button>

                    </div>

                </div>
            `;

            listaDocumentos.appendChild(
                tarjeta
            );
        }
    );
}


// Registra un nuevo documento y su archivo.
async function agregarDocumento() {

    const tipoDocumento =
        document.getElementById(
            "tipoDocumento"
        ).value.trim();

    const nombreDocumento =
        document.getElementById(
            "nombreDocumento"
        ).value.trim();

    const fechaDocumento =
        document.getElementById(
            "fechaDocumento"
        ).value;

    const archivoInput =
        document.getElementById(
            "archivoDocumento"
        );

    if (!tipoDocumento) {

        alert(
            "Debe seleccionar el tipo de documento."
        );

        return;
    }

    if (!nombreDocumento) {

        alert(
            "Debe ingresar el nombre del documento."
        );

        return;
    }

    if (
        !archivoInput ||
        !archivoInput.files ||
        archivoInput.files.length === 0
    ) {

        alert(
            "Debe seleccionar un archivo."
        );

        return;
    }

    const archivo =
        archivoInput.files[0];

    // Valida los formatos permitidos.
    const extensionesPermitidas = [
        "pdf",
        "jpg",
        "jpeg",
        "png"
    ];

    const partes =
        archivo.name.split(".");

    const extension =
        partes.length > 1
            ? partes.pop().toLowerCase()
            : "";

    if (
        !extensionesPermitidas.includes(
            extension
        )
    ) {

        alert(
            "El archivo debe ser PDF, JPG, JPEG o PNG."
        );

        return;
    }

    const idUsuario =
        obtenerIdUsuario();

    if (!idUsuario) {

        alert(
            "No se pudo identificar al usuario."
        );

        return;
    }

    try {

        const tokenCSRF =
            await obtenerTokenCSRF();

        // Prepara los datos como formulario multipart.
        const formData =
            new FormData();

        formData.append(
            "tipoDocumento",
            tipoDocumento
        );

        formData.append(
            "nombreDocumento",
            nombreDocumento
        );

        if (fechaDocumento) {

            formData.append(
                "fechaDocumento",
                fechaDocumento
            );
        }

        formData.append(
            "archivo",
            archivo
        );

        formData.append(
            "idUsuario",
            idUsuario
        );

        formData.append(
            "idMascota",
            idMascotaDocumento
        );

        // Envía el documento al servidor.
        const respuesta =
            await fetch(
                `${API_DOCUMENTOS}/guardar`,
                {
                    method: "POST",

                    headers: {
                        [tokenCSRF.headerName]:
                            tokenCSRF.token
                    },

                    credentials: "include",

                    body: formData
                }
            );

        if (!respuesta.ok) {

            const mensaje =
                await respuesta.text();

            throw new Error(
                mensaje ||
                "No se pudo guardar el documento."
            );
        }

        // Limpia el formulario después de guardar.
        document.getElementById(
            "tipoDocumento"
        ).value = "";

        document.getElementById(
            "nombreDocumento"
        ).value = "";

        document.getElementById(
            "fechaDocumento"
        ).value = "";

        archivoInput.value = "";

        await cargarDocumentos();

        alert(
            "Documento agregado correctamente."
        );

    } catch (error) {

        console.error(
            "Error al agregar documento:",
            error
        );

        alert(
            error.message ||
            "No se pudo agregar el documento."
        );
    }
}


// Elimina un documento registrado.
async function eliminarDocumento(
    idDocumento
) {

    if (!idDocumento) {

        return;
    }

    const confirmar =
        confirm(
            "¿Está seguro de eliminar este documento?"
        );

    if (!confirmar) {

        return;
    }

    try {

        const tokenCSRF =
            await obtenerTokenCSRF();

        const respuesta =
            await fetch(
                `${API_DOCUMENTOS}/eliminar/${idDocumento}`,
                {
                    method: "DELETE",

                    headers: {
                        [tokenCSRF.headerName]:
                            tokenCSRF.token
                    },

                    credentials: "include"
                }
            );

        if (!respuesta.ok) {

            const mensaje =
                await respuesta.text();

            throw new Error(
                mensaje ||
                "No se pudo eliminar el documento."
            );
        }

        await cargarDocumentos();

        alert(
            "Documento eliminado correctamente."
        );

    } catch (error) {

        console.error(
            "Error al eliminar documento:",
            error
        );

        alert(
            error.message ||
            "No se pudo eliminar el documento."
        );
    }
}


// Evita que los datos mostrados se interpreten como HTML.
function escaparHTML(
    valor
) {

    const elemento =
        document.createElement(
            "div"
        );

    elemento.textContent =
        valor ?? "";

    return elemento.innerHTML;
}


// Muestra mensajes dentro de la lista de documentos.
function mostrarMensaje(
    mensaje
) {

    const listaDocumentos =
        document.getElementById(
            "listaDocumentos"
        );

    if (!listaDocumentos) {

        return;
    }

    listaDocumentos.innerHTML = `
        <p class="text-center text-white">
            ${escaparHTML(mensaje)}
        </p>
    `;
}


// Funciones disponibles para el HTML.
window.agregarDocumento =
    agregarDocumento;

window.eliminarDocumento =
    eliminarDocumento;