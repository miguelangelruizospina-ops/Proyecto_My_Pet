// ========================================
// REPORTES DE AYUDA - PANEL ADMINISTRADOR
// ========================================

let reporteSeleccionado = null;
let modalReporte = null;


// ========================================
// INICIO
// ========================================

document.addEventListener("DOMContentLoaded", () => {

    const modalElemento = document.getElementById("modalReporte");

    if (modalElemento) {
        modalReporte = new bootstrap.Modal(modalElemento);
    }

    cargarReportes();

    const btnGuardar = document.getElementById("btnGuardarReporte");

    if (btnGuardar) {
        btnGuardar.addEventListener("click", guardarCambiosReporte);
    }

});


// ========================================
// CARGAR TODOS LOS REPORTES
// ========================================

async function cargarReportes() {

    const mensajeCarga = document.getElementById("mensajeCarga");
    const mensajeError = document.getElementById("mensajeError");
    const mensajeSinReportes = document.getElementById("mensajeSinReportes");
    const contenedor = document.getElementById("contenedorReportes");

    mensajeCarga.classList.remove("d-none");
    mensajeError.classList.add("d-none");
    mensajeSinReportes.classList.add("d-none");

    contenedor.innerHTML = "";

    try {

        const respuesta = await fetch("/api/reportes/listar", {
            method: "GET",
            credentials: "include"
        });

        if (!respuesta.ok) {

            if (respuesta.status === 401 || respuesta.status === 403) {
                throw new Error(
                    "No tienes permisos para consultar los reportes."
                );
            }

            throw new Error(
                "No fue posible cargar los reportes."
            );
        }

        const reportes = await respuesta.json();

        mensajeCarga.classList.add("d-none");

        if (!Array.isArray(reportes) || reportes.length === 0) {

            mensajeSinReportes.classList.remove("d-none");
            return;
        }

        mostrarReportes(reportes);

    } catch (error) {

        console.error("Error al cargar reportes:", error);

        mensajeCarga.classList.add("d-none");

        mensajeError.textContent =
            error.message ||
            "Ocurrió un error al cargar los reportes.";

        mensajeError.classList.remove("d-none");
    }
}


// ========================================
// MOSTRAR REPORTES
// ========================================

function mostrarReportes(reportes) {

    const contenedor =
        document.getElementById("contenedorReportes");

    contenedor.innerHTML = "";

    reportes.forEach(reporte => {

        const tarjeta = crearTarjetaReporte(reporte);

        contenedor.appendChild(tarjeta);

    });
}


// ========================================
// CREAR TARJETA DE REPORTE
// ========================================

function crearTarjetaReporte(reporte) {

    const tarjeta = document.createElement("div");

    tarjeta.className =
        "tarjeta_agenda p-4 mb-4";

    const nombreUsuario =
        obtenerNombreUsuario(reporte);

    const fecha =
        formatearFecha(reporte.fecha);

    const estado =
        obtenerTextoEstado(reporte.estado);

    const claseEstado =
        obtenerClaseEstado(reporte.estado);

    const descripcion =
        escaparHTML(reporte.descripcion || "");

    const pagina =
        escaparHTML(reporte.pagina || "No especificada");

    tarjeta.innerHTML = `

        <div class="d-flex flex-column flex-md-row
                    justify-content-between
                    align-items-md-center
                    gap-2 mb-3">

            <div>

                <h3 class="titulo_seccion mb-1">
                    Reporte #${reporte.idReporte}
                </h3>

                <strong>
                    Usuario: ${nombreUsuario}
                </strong>

            </div>

            <span class="${claseEstado}">
                ${estado}
            </span>

        </div>


        <div class="row mb-3">

            <div class="col-md-6">

                <strong>Fecha:</strong>

                <span>
                    ${fecha}
                </span>

            </div>


            <div class="col-md-6">

                <strong>Página:</strong>

                <span>
                    ${pagina}
                </span>

            </div>

        </div>


        <div class="mb-3">

            <strong>
                Problema reportado:
            </strong>

            <p class="mt-2 mb-0">
                ${descripcion}
            </p>

        </div>


        ${
            reporte.respuesta
                ? `
                    <div class="mb-3">

                        <strong>
                            Respuesta actual:
                        </strong>

                        <p class="mt-2 mb-0">
                            ${escaparHTML(reporte.respuesta)}
                        </p>

                    </div>
                  `
                : ""
        }


        <div class="text-center text-md-end">

            <button
                type="button"
                class="btn_mascota_card"
                data-reporte-id="${reporte.idReporte}"
            >
                Gestionar reporte
            </button>

        </div>

    `;


    const boton =
        tarjeta.querySelector("button");

    boton.addEventListener("click", () => {

        abrirReporte(reporte);

    });


    return tarjeta;
}


// ========================================
// ABRIR REPORTE
// ========================================

function abrirReporte(reporte) {

    reporteSeleccionado = reporte;

    document.getElementById("reporteUsuario").value =
        obtenerNombreUsuario(reporte);

    document.getElementById("reporteFecha").value =
        formatearFecha(reporte.fecha);

    document.getElementById("reportePagina").value =
        reporte.pagina || "No especificada";

    document.getElementById("reporteNavegador").value =
        reporte.navegador || "No disponible";

    document.getElementById("reporteDescripcion").value =
        reporte.descripcion || "";

    document.getElementById("reporteEstado").value =
        reporte.estado || "pendiente";

    document.getElementById("reporteRespuesta").value =
        reporte.respuesta || "";

    ocultarMensajeModal();

    if (modalReporte) {
        modalReporte.show();
    }
}


// ========================================
// GUARDAR CAMBIOS
// ========================================

async function guardarCambiosReporte() {

    if (!reporteSeleccionado) {
        return;
    }

    const estado =
        document.getElementById("reporteEstado").value;

    const respuesta =
        document.getElementById("reporteRespuesta").value.trim();

    const boton =
        document.getElementById("btnGuardarReporte");

    ocultarMensajeModal();

    boton.disabled = true;
    boton.textContent = "Guardando...";


    try {

        const id =
            reporteSeleccionado.idReporte;

        const parametros = new URLSearchParams();

        parametros.append("estado", estado);

        if (respuesta.length > 0) {
            parametros.append("respuesta", respuesta);
        }


        const response = await fetch(
            `/api/reportes/actualizar/${id}?${parametros.toString()}`,
            {
                method: "PUT",
                credentials: "include"
            }
        );


        if (!response.ok) {

            if (response.status === 401 ||
                response.status === 403) {

                throw new Error(
                    "No tienes permisos para actualizar este reporte."
                );
            }

            throw new Error(
                "No fue posible guardar los cambios."
            );
        }


        await response.json();


        mostrarMensajeModal(
            "Reporte actualizado correctamente.",
            "success"
        );


        setTimeout(() => {

            if (modalReporte) {
                modalReporte.hide();
            }

            cargarReportes();

        }, 800);


    } catch (error) {

        console.error(
            "Error al actualizar reporte:",
            error
        );

        mostrarMensajeModal(
            error.message ||
            "Ocurrió un error al guardar los cambios.",
            "danger"
        );

    } finally {

        boton.disabled = false;
        boton.textContent = "Guardar cambios";

    }
}


// ========================================
// OBTENER NOMBRE DEL USUARIO
// ========================================

function obtenerNombreUsuario(reporte) {

    if (!reporte.usuario) {
        return "Usuario no disponible";
    }

    const nombre =
        reporte.usuario.nombre || "";

    const apellidos =
        reporte.usuario.apellidos || "";

    const usuario =
        reporte.usuario.usuario || "";

    const nombreCompleto =
        `${nombre} ${apellidos}`.trim();

    if (nombreCompleto.length > 0) {
        return escaparHTML(nombreCompleto);
    }

    if (usuario.length > 0) {
        return escaparHTML(usuario);
    }

    return "Usuario no disponible";
}


// ========================================
// FORMATEAR FECHA
// ========================================

function formatearFecha(fecha) {

    if (!fecha) {
        return "No disponible";
    }

    try {

        const fechaObjeto =
            new Date(fecha);

        if (Number.isNaN(fechaObjeto.getTime())) {
            return fecha;
        }

        return fechaObjeto.toLocaleString(
            "es-CO",
            {
                dateStyle: "short",
                timeStyle: "short"
            }
        );

    } catch (error) {

        return fecha;

    }
}


// ========================================
// ESTADO
// ========================================

function obtenerTextoEstado(estado) {

    switch (estado) {

        case "pendiente":
            return "Pendiente";

        case "en_revision":
            return "En revisión";

        case "solucionado":
            return "Solucionado";

        default:
            return estado || "Sin estado";
    }
}


function obtenerClaseEstado(estado) {

    switch (estado) {

        case "pendiente":
            return "badge bg-warning text-dark";

        case "en_revision":
            return "badge bg-info text-dark";

        case "solucionado":
            return "badge bg-success";

        default:
            return "badge bg-secondary";
    }
}


// ========================================
// MENSAJES DEL MODAL
// ========================================

function mostrarMensajeModal(mensaje, tipo) {

    const elemento =
        document.getElementById("mensajeModal");

    elemento.textContent = mensaje;

    elemento.className =
        `alert alert-${tipo}`;

}


function ocultarMensajeModal() {

    const elemento =
        document.getElementById("mensajeModal");

    elemento.textContent = "";

    elemento.className =
        "alert d-none";

}


// ========================================
// SEGURIDAD PARA MOSTRAR TEXTO HTML
// ========================================

function escaparHTML(texto) {

    const elemento =
        document.createElement("div");

    elemento.textContent =
        texto;

    return elemento.innerHTML;
}