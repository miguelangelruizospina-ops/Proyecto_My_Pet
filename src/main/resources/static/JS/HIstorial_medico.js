// ==========================================================
// CONFIGURACIÓN
// ==========================================================

const API_BASE_HISTORIAL = "http://localhost:8082/api";

const API_CSRF =
    "http://localhost:8082/api/usuario/csrf";


// ==========================================================
// VARIABLES GLOBALES
// ==========================================================

let historialActual = null;

let idMascotaHistorial = null;

let modoEdicion = false;


// ==========================================================
// INICIO
// ==========================================================

document.addEventListener("DOMContentLoaded", function () {

    const parametros =
        new URLSearchParams(window.location.search);


    // Obtener el ID de la mascota desde la URL

    idMascotaHistorial =
        parametros.get("idMascota");


    console.log(
        "ID de mascota recibido:",
        idMascotaHistorial
    );


    // Verificar que exista el ID de la mascota

    if (!idMascotaHistorial) {

        console.error(
            "No se encontró el ID de la mascota."
        );

        return;
    }
// ======================================================
// FLECHA PARA REGRESAR AL PERFIL DE LA MASCOTA
// ======================================================

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


    // Cargar el historial médico

    cargarHistorialMedico();


    // ======================================================
    // BOTÓN CREAR HISTORIAL
    // ======================================================

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


    // ======================================================
    // BOTÓN EDITAR INFORMACIÓN
    // ======================================================

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


    // ======================================================
    // BOTÓN CANCELAR EDICIÓN
    // ======================================================

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


    // ======================================================
    // FORMULARIO PRINCIPAL
    // ======================================================

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


// ==========================================================
// OBTENER TOKEN CSRF
// ==========================================================

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


// ==========================================================
// CARGAR HISTORIAL MÉDICO
// ==========================================================

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


        // ==================================================
        // NO EXISTE HISTORIAL
        // ==================================================

        if (
            !historiales ||
            historiales.length === 0
        ) {

            historialActual = null;

            mostrarEstadoSinHistorial();

            return;
        }


        // ==================================================
        // EXISTE HISTORIAL
        // ==================================================

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


// ==========================================================
// MOSTRAR ESTADO SIN HISTORIAL
// ==========================================================

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


// ==========================================================
// MOSTRAR HISTORIAL
// ==========================================================

function mostrarHistorial() {

    if (!historialActual) {

        mostrarEstadoSinHistorial();

        return;
    }


    // ======================================================
    // OCULTAR ESTADO SIN HISTORIAL
    // ======================================================

    const estadoSinHistorial =
        document.getElementById(
            "estadoSinHistorial"
        );


    if (estadoSinHistorial) {

        estadoSinHistorial.style.display =
            "none";
    }


    // ======================================================
    // OCULTAR FORMULARIO
    // ======================================================

    const formularioHistorial =
        document.getElementById(
            "formularioHistorial"
        );


    if (formularioHistorial) {

        formularioHistorial.style.display =
            "none";
    }


    // ======================================================
    // MOSTRAR INFORMACIÓN REGISTRADA
    // ======================================================

    const informacionHistorial =
        document.getElementById(
            "informacionHistorial"
        );


    if (informacionHistorial) {

        informacionHistorial.style.display =
            "block";
    }


    // ======================================================
    // MOSTRAR ANTECEDENTES
    // ======================================================

    mostrarTexto(
        "mostrarAntecedentes",
        historialActual.antecedentesMedicos,
        "No se han registrado antecedentes médicos."
    );


    // ======================================================
    // MOSTRAR ALERGIAS
    // ======================================================

    mostrarTexto(
        "mostrarAlergias",
        historialActual.alergias,
        "No se han registrado alergias."
    );


    // ======================================================
    // MOSTRAR MEDICAMENTOS
    // ======================================================

    mostrarTexto(
        "mostrarMedicamentos",
        historialActual.tratamientos,
        "No se han registrado medicamentos."
    );


    // ======================================================
    // MOSTRAR VACUNAS
    // ======================================================

    mostrarTexto(
        "mostrarVacunas",
        historialActual.vacunacion,
        "No se han registrado vacunas."
    );


    // ======================================================
    // MOSTRAR CIRUGÍAS
    // ======================================================

    mostrarTexto(
        "mostrarCirugias",
        historialActual.cirugiasProcedimientos,
        "No se han registrado cirugías."
    );


    // ======================================================
    // MOSTRAR OBSERVACIONES
    // ======================================================

    mostrarTexto(
        "mostrarObservaciones",
        historialActual.consultasAtenciones,
        "No se han registrado observaciones."
    );


    modoEdicion = false;
}


// ==========================================================
// MOSTRAR TEXTO
// ==========================================================

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


// ==========================================================
// MOSTRAR FORMULARIO PARA CREAR
// ==========================================================

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


    // Ocultar estado sin historial

    if (estadoSinHistorial) {

        estadoSinHistorial.style.display =
            "none";
    }


    // Ocultar información existente

    if (informacionHistorial) {

        informacionHistorial.style.display =
            "none";
    }


    // Mostrar formulario

    if (formularioHistorial) {

        formularioHistorial.style.display =
            "block";
    }


    // Ocultar botón cancelar al crear

    if (btnCancelarEdicion) {

        btnCancelarEdicion.style.display =
            "none";
    }


    // Limpiar formulario

    limpiarFormulario();
}


// ==========================================================
// MOSTRAR FORMULARIO PARA EDITAR
// ==========================================================

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


    // Ocultar información registrada

    if (informacionHistorial) {

        informacionHistorial.style.display =
            "none";
    }


    // Ocultar estado sin historial

    if (estadoSinHistorial) {

        estadoSinHistorial.style.display =
            "none";
    }


    // Mostrar formulario

    if (formularioHistorial) {

        formularioHistorial.style.display =
            "block";
    }


    // Mostrar botón cancelar

    if (btnCancelarEdicion) {

        btnCancelarEdicion.style.display =
            "inline-block";
    }


    // Cargar información existente

    cargarDatosFormulario();
}


// ==========================================================
// CARGAR DATOS EN EL FORMULARIO
// ==========================================================

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


// ==========================================================
// LIMPIAR FORMULARIO
// ==========================================================

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


// ==========================================================
// CANCELAR EDICIÓN
// ==========================================================

function cancelarEdicion() {

    if (historialActual) {

        mostrarHistorial();

    } else {

        mostrarEstadoSinHistorial();
    }
}


// ==========================================================
// GUARDAR HISTORIAL MÉDICO
// ==========================================================

async function guardarHistorial(evento) {

    evento.preventDefault();


    try {

        // ==================================================
        // OBTENER TOKEN CSRF
        // ==================================================

        const csrf =
            await obtenerTokenCSRF();


        // ==================================================
        // CONSTRUIR DATOS
        // ==================================================

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


        // ==================================================
        // ACTUALIZAR HISTORIAL EXISTENTE
        // ==================================================

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


        // ==================================================
        // CREAR HISTORIAL
        // ==================================================

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


        // ==================================================
        // VALIDAR RESPUESTA
        // ==================================================

        if (!respuesta.ok) {

            const mensaje =
                await respuesta.text();


            throw new Error(
                mensaje ||
                "No se pudo guardar el historial médico."
            );
        }


        // ==================================================
        // ACTUALIZAR HISTORIAL ACTUAL
        // ==================================================

        historialActual =
            await respuesta.json();


        // ==================================================
        // MOSTRAR MENSAJE
        // ==================================================

        alert(
            modoEdicion
                ? "Información actualizada correctamente."
                : "Historial médico creado correctamente."
        );


        // ==================================================
        // MOSTRAR INFORMACIÓN
        // ==================================================

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


// ==========================================================
// OBTENER VALOR DEL CAMPO
// ==========================================================

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


// ==========================================================
// AGREGAR REGISTRO INDIVIDUAL
// Compatibilidad con otras partes de My Pet
// ==========================================================

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


    // Si no existe historial, abrir formulario

    if (!historialActual) {

        mostrarFormularioCrear();


        asignarValorFormulario(
            tipo,
            valor.trim()
        );


        return;
    }


    // Actualizar historial existente

    asignarValorHistorial(
        tipo,
        valor.trim()
    );


    await actualizarHistorial();
}


// ==========================================================
// EDITAR REGISTRO INDIVIDUAL
// ==========================================================

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


// ==========================================================
// OBTENER VALOR ACTUAL SEGÚN TIPO
// ==========================================================

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


// ==========================================================
// ASIGNAR VALOR AL HISTORIAL
// ==========================================================

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


// ==========================================================
// ASIGNAR VALOR AL FORMULARIO
// ==========================================================

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


// ==========================================================
// ACTUALIZAR HISTORIAL
// ==========================================================

async function actualizarHistorial() {

    if (!historialActual) {

        return;
    }


    try {

        // ==================================================
        // OBTENER TOKEN CSRF
        // ==================================================

        const csrf =
            await obtenerTokenCSRF();


        // ==================================================
        // CONSTRUIR DATOS
        // ==================================================

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


        // ==================================================
        // ACTUALIZAR
        // ==================================================

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


        // ==================================================
        // VALIDAR RESPUESTA
        // ==================================================

        if (!respuesta.ok) {

            const mensaje =
                await respuesta.text();


            throw new Error(
                mensaje ||
                "No se pudo actualizar el historial."
            );
        }


        // ==================================================
        // ACTUALIZAR INFORMACIÓN LOCAL
        // ==================================================

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


// ==========================================================
// ELIMINAR HISTORIAL
// ==========================================================

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

        // ==================================================
        // OBTENER TOKEN CSRF
        // ==================================================

        const csrf =
            await obtenerTokenCSRF();


        // ==================================================
        // ELIMINAR
        // ==================================================

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


        // ==================================================
        // VALIDAR RESPUESTA
        // ==================================================

        if (!respuesta.ok) {

            const mensaje =
                await respuesta.text();


            throw new Error(
                mensaje ||
                "No se pudo eliminar el historial."
            );
        }


        // ==================================================
        // ACTUALIZAR INTERFAZ
        // ==================================================

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


// ==========================================================
// FUNCIONES DISPONIBLES PARA EL HTML
// ==========================================================

window.agregarRegistro =
    agregarRegistro;


window.editarRegistro =
    editarRegistro;


window.eliminarHistorial =
    eliminarHistorial;