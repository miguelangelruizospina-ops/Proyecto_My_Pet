// Configuración de las rutas principales de la API del historial médico.

const API_BASE_HISTORIAL = "http://localhost:8082/api";

const API_CSRF =
    "http://localhost:8082/api/usuario/csrf";

// Variables utilizadas para controlar el historial médico actual y su modo de edición.

let historialActual = null;

let idMascotaHistorial = null;

let modoEdicion = false;

// Inicializa la página del historial médico cuando termina de cargar.

document.addEventListener("DOMContentLoaded", function () {

    const parametros =
        new URLSearchParams(window.location.search);

    // Obtiene el ID de la mascota desde la URL.

    idMascotaHistorial =
        parametros.get("idMascota");

    console.log(
        "ID de mascota recibido:",
        idMascotaHistorial
    );

    // Verifica que exista el ID de la mascota.

    if (!idMascotaHistorial) {

        console.error(
            "No se encontró el ID de la mascota."
        );

        return;
    }

    // Configura la flecha para regresar al perfil de la mascota.

    const flechaVolverPerfil =
        document.getElementById(
            "flechaVolverPerfil"
         );

    if (flechaVolverPerfil) {

        flechaVolverPerfil.href =
            `perfil_mascota.html?id=${encodeURIComponent(
                 idMascotaHistorial
            )}`;
    }

    // Carga el historial médico de la mascota.

    cargarHistorialMedico();

    // Configura el botón para crear un nuevo historial.

    const btnCrearHistorial =
        document.getElementById(
            "btnCrearHistorial"
        );

    if (btnCrearHistorial) {

        btnCrearHistorial.addEventListener(
            "click",
            mostrarFormularioCrear
        );
    }

    // Configura el botón para editar la información del historial.

    const btnEditarInformacion =
        document.getElementById(
            "btnEditarInformacion"
        );

    if (btnEditarInformacion) {

        btnEditarInformacion.addEventListener(
            "click",
            mostrarFormularioEditar
        );
    }

    // Configura el botón para cancelar la edición.

    const btnCancelarEdicion =
        document.getElementById(
            "btnCancelarEdicion"
        );

    if (btnCancelarEdicion) {

        btnCancelarEdicion.addEventListener(
            "click",
            cancelarEdicion
        );
    }

    // Configura el formulario principal para guardar el historial.

    const formulario =
        document.getElementById(
            "formHistorial"
        );

    if (formulario) {

        formulario.addEventListener(
            "submit",
            guardarHistorial
        );
    }

});

// Obtiene el token CSRF necesario para realizar solicitudes protegidas.

async function obtenerTokenCSRF() {

    const respuesta = await fetch(
        API_CSRF,
        {
            method: "GET",

            credentials: "same-origin"
        }
    );

    if (!respuesta.ok) {

        throw new Error(
            "No se pudo obtener el token CSRF."
        );
    }

    return await respuesta.json();
}

// Carga el historial médico asociado a la mascota.

async function cargarHistorialMedico() {

    try {

        const respuesta = await fetch(
            `${API_BASE_HISTORIAL}/historiales-medicos/mascota/${idMascotaHistorial}`,
            {
                method: "GET",

                credentials: "same-origin"
            }
        );

        if (!respuesta.ok) {

            throw new Error(
                "No se pudo cargar el historial médico."
            );
        }

        const historiales =
            await respuesta.json();

        // Muestra el estado correspondiente cuando la mascota no tiene historial.

        if (
            !historiales ||
            historiales.length === 0
        ) {

            historialActual = null;

            mostrarEstadoSinHistorial();

            return;
        }

        // Selecciona el historial más reciente cuando existen varios registros.

        historialActual =
            historiales.reduce(
                function (actual, historial) {

                    if (!actual) {

                        return historial;
                    }

                    return historial.idHistorial >
                        actual.idHistorial
                        ? historial
                        : actual;

                },
                null
            );

        mostrarHistorial();

    } catch (error) {

        console.error(
            "Error al cargar el historial médico:",
            error
        );

        mostrarEstadoSinHistorial();
    }
}

// Muestra la interfaz correspondiente cuando no existe un historial médico.

function mostrarEstadoSinHistorial() {

    const estadoSinHistorial =
        document.getElementById(
            "estadoSinHistorial"
        );

    const formularioHistorial =
        document.getElementById(
            "formularioHistorial"
        );

    const informacionHistorial =
        document.getElementById(
            "informacionHistorial"
        );

    if (estadoSinHistorial) {

        estadoSinHistorial.style.display =
            "block";
    }

    if (formularioHistorial) {

        formularioHistorial.style.display =
            "none";
    }

    if (informacionHistorial) {

        informacionHistorial.style.display =
            "none";
    }

    modoEdicion = false;
}

// Muestra la información registrada en el historial médico.

function mostrarHistorial() {

    if (!historialActual) {

        mostrarEstadoSinHistorial();

        return;
    }

    // Oculta el mensaje que indica que no existe historial.

    const estadoSinHistorial =
        document.getElementById(
            "estadoSinHistorial"
        );

    if (estadoSinHistorial) {

        estadoSinHistorial.style.display =
            "none";
    }

    // Oculta el formulario del historial.

    const formularioHistorial =
        document.getElementById(
            "formularioHistorial"
        );

    if (formularioHistorial) {

        formularioHistorial.style.display =
            "none";
    }

    // Muestra la información registrada del historial.

    const informacionHistorial =
        document.getElementById(
            "informacionHistorial"
        );

    if (informacionHistorial) {

        informacionHistorial.style.display =
            "block";
    }

    // Muestra los antecedentes médicos registrados.

    mostrarTexto(
        "mostrarAntecedentes",
        historialActual.antecedentesMedicos,
        "No se han registrado antecedentes médicos."
    );

    // Muestra las alergias registradas.

    mostrarTexto(
        "mostrarAlergias",
        historialActual.alergias,
        "No se han registrado alergias."
    );

    // Muestra los medicamentos o tratamientos registrados.

    mostrarTexto(
        "mostrarMedicamentos",
        historialActual.tratamientos,
        "No se han registrado medicamentos."
    );

    // Muestra las vacunas registradas.

    mostrarTexto(
        "mostrarVacunas",
        historialActual.vacunacion,
        "No se han registrado vacunas."
    );

    // Muestra las cirugías y procedimientos registrados.

    mostrarTexto(
        "mostrarCirugias",
        historialActual.cirugiasProcedimientos,
        "No se han registrado cirugías."
    );

    // Muestra las consultas y observaciones registradas.

    mostrarTexto(
        "mostrarObservaciones",
        historialActual.consultasAtenciones,
        "No se han registrado observaciones."
    );

    modoEdicion = false;
}

// Muestra un valor del historial o un texto cuando el campo está vacío.

function mostrarTexto(
    idElemento,
    valor,
    textoVacio
) {

    const elemento =
        document.getElementById(
            idElemento
        );

    if (!elemento) {

        return;
    }

    if (
        valor !== null &&
        valor !== undefined &&
        String(valor).trim() !== ""
    ) {

        elemento.textContent = valor;

    } else {

        elemento.textContent = textoVacio;
    }
}

// Muestra el formulario para crear un nuevo historial médico.

function mostrarFormularioCrear() {

    modoEdicion = false;

    historialActual = null;

    const estadoSinHistorial =
        document.getElementById(
            "estadoSinHistorial"
        );

    const informacionHistorial =
        document.getElementById(
            "informacionHistorial"
        );

    const formularioHistorial =
        document.getElementById(
            "formularioHistorial"
        );

    const btnCancelarEdicion =
        document.getElementById(
            "btnCancelarEdicion"
        );

    // Oculta el estado que indica que no existe historial.

    if (estadoSinHistorial) {

        estadoSinHistorial.style.display =
            "none";
    }

    // Oculta la información existente.

    if (informacionHistorial) {

        informacionHistorial.style.display =
            "none";
    }

    // Muestra el formulario del historial.

    if (formularioHistorial) {

        formularioHistorial.style.display =
            "block";
    }

    // Oculta el botón cancelar mientras se crea un historial.

    if (btnCancelarEdicion) {

        btnCancelarEdicion.style.display =
            "none";
    }

    // Limpia los campos del formulario.

    limpiarFormulario();
}

// Muestra el formulario con la información del historial para editarla.

function mostrarFormularioEditar() {

    if (!historialActual) {

        return;
    }

    modoEdicion = true;

    const informacionHistorial =
        document.getElementById(
            "informacionHistorial"
        );

    const formularioHistorial =
        document.getElementById(
            "formularioHistorial"
        );

    const estadoSinHistorial =
        document.getElementById(
            "estadoSinHistorial"
        );

    const btnCancelarEdicion =
        document.getElementById(
            "btnCancelarEdicion"
        );

    // Oculta la información registrada.

    if (informacionHistorial) {

        informacionHistorial.style.display =
            "none";
    }

    // Oculta el estado sin historial.

    if (estadoSinHistorial) {

        estadoSinHistorial.style.display =
            "none";
    }

    // Muestra el formulario para editar la información.

    if (formularioHistorial) {

        formularioHistorial.style.display =
            "block";
    }

    // Muestra el botón para cancelar la edición.

    if (btnCancelarEdicion) {

        btnCancelarEdicion.style.display =
            "inline-block";
    }

    // Carga en el formulario la información existente.

    cargarDatosFormulario();
}

// Carga los datos del historial actual en los campos del formulario.

function cargarDatosFormulario() {

    if (!historialActual) {

        return;
    }

    document.getElementById(
        "antecedentes"
    ).value =
        historialActual.antecedentesMedicos || "";

    document.getElementById(
        "alergias"
    ).value =
        historialActual.alergias || "";

    document.getElementById(
        "medicamentos"
    ).value =
        historialActual.tratamientos || "";

    document.getElementById(
        "vacunas"
    ).value =
        historialActual.vacunacion || "";

    document.getElementById(
        "cirugias"
    ).value =
        historialActual.cirugiasProcedimientos || "";

    document.getElementById(
        "observaciones"
    ).value =
        historialActual.consultasAtenciones || "";
}

// Limpia todos los campos del formulario del historial.

function limpiarFormulario() {

    document.getElementById(
        "antecedentes"
    ).value = "";

    document.getElementById(
        "alergias"
    ).value = "";

    document.getElementById(
        "medicamentos"
    ).value = "";

    document.getElementById(
        "vacunas"
    ).value = "";

    document.getElementById(
        "cirugias"
    ).value = "";

    document.getElementById(
        "observaciones"
    ).value = "";
}

// Cancela la edición y regresa a la información disponible.

function cancelarEdicion() {

    if (historialActual) {

        mostrarHistorial();

    } else {

        mostrarEstadoSinHistorial();
    }
}

// Guarda un nuevo historial o actualiza el historial existente.

async function guardarHistorial(evento) {

    evento.preventDefault();

    try {

        // Obtiene el token CSRF para proteger la solicitud.

        const csrf =
            await obtenerTokenCSRF();

        // Construye los datos que serán enviados al backend.

        const datos = {

            diagnosticos:
                historialActual?.diagnosticos || "",

            antecedentesMedicos:
                obtenerValor(
                    "antecedentes"
                ),

            alergias:
                obtenerValor(
                    "alergias"
                ),

            tratamientos:
                obtenerValor(
                    "medicamentos"
                ),

            peso:
                historialActual?.peso || "",

            cirugiasProcedimientos:
                obtenerValor(
                    "cirugias"
                ),

            vacunacion:
                obtenerValor(
                    "vacunas"
                ),

            consultasAtenciones:
                obtenerValor(
                    "observaciones"
                ),

            mascota: {

                idMascota:
                    parseInt(
                        idMascotaHistorial
                    )
            }
        };

        let respuesta;

        // Actualiza el historial existente cuando ya tiene un identificador.

        if (
            historialActual &&
            historialActual.idHistorial
        ) {

            respuesta = await fetch(
                `${API_BASE_HISTORIAL}/historiales-medicos/actualizar/${historialActual.idHistorial}`,
                {
                    method: "PUT",

                    headers: {

                        "Content-Type":
                            "application/json",

                        [csrf.headerName]:
                            csrf.token
                    },

                    credentials:
                        "same-origin",

                    body:
                        JSON.stringify(datos)
                }
            );

        // Crea un nuevo historial cuando todavía no existe uno.

        } else {

            respuesta = await fetch(
                `${API_BASE_HISTORIAL}/historiales-medicos/guardar`,
                {
                    method: "POST",

                    headers: {

                        "Content-Type":
                            "application/json",

                        [csrf.headerName]:
                            csrf.token
                    },

                    credentials:
                        "same-origin",

                    body:
                        JSON.stringify(datos)
                }
            );
        }

        // Verifica que el backend haya procesado correctamente la solicitud.

        if (!respuesta.ok) {

            const mensaje =
                await respuesta.text();

            throw new Error(
                mensaje ||
                "No se pudo guardar el historial médico."
            );
        }

        // Actualiza el historial local con la respuesta del servidor.

        historialActual =
            await respuesta.json();

        // Informa al usuario que la operación terminó correctamente.

        alert(
            modoEdicion
                ? "Información actualizada correctamente."
                : "Historial médico creado correctamente."
        );

        // Muestra nuevamente la información registrada.

        mostrarHistorial();

    } catch (error) {

        console.error(
            "Error al guardar el historial médico:",
            error
        );

        alert(
            "No se pudo guardar el historial médico."
        );
    }
}

// Obtiene y limpia el valor de un campo del formulario.

function obtenerValor(idCampo) {

    const campo =
        document.getElementById(
            idCampo
        );

    if (!campo) {

        return "";
    }

    return campo.value.trim();
}

// Agrega un registro individual desde otras partes de My Pet.

async function agregarRegistro(tipo) {

    const valor =
        prompt(
            "Ingrese la información de: " +
            tipo
        );

    if (valor === null) {

        return;
    }

    if (valor.trim() === "") {

        alert(
            "Debe ingresar información."
        );

        return;
    }

    // Abre el formulario si todavía no existe un historial.

    if (!historialActual) {

        mostrarFormularioCrear();

        asignarValorFormulario(
            tipo,
            valor.trim()
        );

        return;
    }

    // Actualiza el campo correspondiente del historial existente.

    asignarValorHistorial(
        tipo,
        valor.trim()
    );

    await actualizarHistorial();
}

// Permite editar individualmente un registro del historial.

async function editarRegistro(tipo) {

    if (!historialActual) {

        return;
    }

    const valorActual =
        obtenerValorPorTipo(tipo);

    const nuevoValor =
        prompt(
            "Editar información de " +
            tipo,
            valorActual
        );

    if (nuevoValor === null) {

        return;
    }

    if (nuevoValor.trim() === "") {

        alert(
            "Debe ingresar información."
        );

        return;
    }

    asignarValorHistorial(
        tipo,
        nuevoValor.trim()
    );

    await actualizarHistorial();
}

// Obtiene el valor actual del historial según el tipo de registro.

function obtenerValorPorTipo(tipo) {

    if (!historialActual) {

        return "";
    }

    switch (tipo) {

        case "Peso":

            return
                historialActual.peso || "";

        case "Enfermedad":

            return
                historialActual.diagnosticos || "";

        case "Antecedente":

            return
                historialActual.antecedentesMedicos || "";

        case "Alergia":

            return
                historialActual.alergias || "";

        case "Medicamento":

            return
                historialActual.tratamientos || "";

        case "Cirugía":

            return
                historialActual.cirugiasProcedimientos || "";

        case "Vacuna":

            return
                historialActual.vacunacion || "";

        case "Consulta":

            return
                historialActual.consultasAtenciones || "";

        default:

            return "";
    }
}

// Asigna un valor al campo correspondiente del historial actual.

function asignarValorHistorial(
    tipo,
    valor
) {

    switch (tipo) {

        case "Peso":

            historialActual.peso =
                valor;

            break;

        case "Enfermedad":

            historialActual.diagnosticos =
                valor;

            break;

        case "Antecedente":

            historialActual.antecedentesMedicos =
                valor;

            break;

        case "Alergia":

            historialActual.alergias =
                valor;

            break;

        case "Medicamento":

            historialActual.tratamientos =
                valor;

            break;

        case "Cirugía":

            historialActual.cirugiasProcedimientos =
                valor;

            break;

        case "Vacuna":

            historialActual.vacunacion =
                valor;

            break;

        case "Consulta":

            historialActual.consultasAtenciones =
                valor;

            break;

        default:

            console.error(
                "Tipo de registro no reconocido:",
                tipo
            );
    }
}

// Asigna un valor al campo correspondiente del formulario.

function asignarValorFormulario(
    tipo,
    valor
) {

    switch (tipo) {

        case "Antecedente":

            document.getElementById(
                "antecedentes"
            ).value = valor;

            break;

        case "Alergia":

            document.getElementById(
                "alergias"
            ).value = valor;

            break;

        case "Medicamento":

            document.getElementById(
                "medicamentos"
            ).value = valor;

            break;

        case "Vacuna":

            document.getElementById(
                "vacunas"
            ).value = valor;

            break;

        case "Cirugía":

            document.getElementById(
                "cirugias"
            ).value = valor;

            break;

        case "Consulta":

            document.getElementById(
                "observaciones"
            ).value = valor;

            break;

        default:

            console.error(
                "Tipo de formulario no reconocido:",
                tipo
            );
    }
}

// Actualiza el historial médico existente mediante el backend.

async function actualizarHistorial() {

    if (!historialActual) {

        return;
    }

    try {

        // Obtiene el token CSRF para proteger la solicitud.

        const csrf =
            await obtenerTokenCSRF();

        // Construye los datos actuales del historial.

        const datos = {

            diagnosticos:
                historialActual.diagnosticos || "",

            antecedentesMedicos:
                historialActual.antecedentesMedicos || "",

            alergias:
                historialActual.alergias || "",

            tratamientos:
                historialActual.tratamientos || "",

            peso:
                historialActual.peso || "",

            cirugiasProcedimientos:
                historialActual.cirugiasProcedimientos || "",

            vacunacion:
                historialActual.vacunacion || "",

            consultasAtenciones:
                historialActual.consultasAtenciones || "",

            mascota: {

                idMascota:
                    parseInt(
                        idMascotaHistorial
                    )
            }
        };

        // Envía la actualización del historial al backend.

        const respuesta =
            await fetch(
                `${API_BASE_HISTORIAL}/historiales-medicos/actualizar/${historialActual.idHistorial}`,
                {
                    method: "PUT",

                    headers: {

                        "Content-Type":
                            "application/json",

                        [csrf.headerName]:
                            csrf.token
                    },

                    credentials:
                        "same-origin",

                    body:
                        JSON.stringify(datos)
                }
            );

        // Verifica que la actualización haya sido procesada correctamente.

        if (!respuesta.ok) {

            const mensaje =
                await respuesta.text();

            throw new Error(
                mensaje ||
                "No se pudo actualizar el historial."
            );
        }

        // Actualiza la información local con la respuesta del servidor.

        historialActual =
            await respuesta.json();

        mostrarHistorial();

        alert(
            "Información actualizada correctamente."
        );

    } catch (error) {

        console.error(
            "Error al actualizar historial:",
            error
        );

        alert(
            "No se pudo actualizar la información."
        );
    }
}

// Elimina el historial médico después de confirmar la acción.

async function eliminarHistorial() {

    if (!historialActual) {

        alert(
            "No existe un historial médico para eliminar."
        );

        return;
    }

    const confirmar =
        confirm(
            "¿Está seguro de eliminar el historial médico?"
        );

    if (!confirmar) {

        return;
    }

    try {

        // Obtiene el token CSRF para proteger la solicitud.

        const csrf =
            await obtenerTokenCSRF();

        // Envía la solicitud para eliminar el historial.

        const respuesta =
            await fetch(
                `${API_BASE_HISTORIAL}/historiales-medicos/eliminar/${historialActual.idHistorial}`,
                {
                    method: "DELETE",

                    headers: {

                        [csrf.headerName]:
                            csrf.token
                    },

                    credentials:
                        "same-origin"
                }
            );

        // Verifica que la eliminación haya sido procesada correctamente.

        if (!respuesta.ok) {

            const mensaje =
                await respuesta.text();

            throw new Error(
                mensaje ||
                "No se pudo eliminar el historial."
            );
        }

        // Actualiza la interfaz después de eliminar el historial.

        historialActual = null;

        mostrarEstadoSinHistorial();

        limpiarFormulario();

        alert(
            "Historial médico eliminado correctamente."
        );

    } catch (error) {

        console.error(
            "Error al eliminar historial:",
            error
        );

        alert(
            "No se pudo eliminar el historial médico."
        );
    }
}

// Expone las funciones utilizadas directamente desde el HTML.

window.agregarRegistro =
    agregarRegistro;

window.editarRegistro =
    editarRegistro;

window.eliminarHistorial =
    eliminarHistorial;