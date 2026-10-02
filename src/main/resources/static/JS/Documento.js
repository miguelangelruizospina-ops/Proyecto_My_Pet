// JavaScript encargado de gestionar el historial médico de una mascota.


// URL base de los servicios de la aplicación.
const API_BASE_HISTORIAL =
    "http://localhost:8082/api";


// URL del servicio que proporciona el token CSRF.
const API_CSRF =
    "http://localhost:8082/api/usuario/csrf";


// Almacena el historial médico actualmente seleccionado.
let historialActual = null;


// Almacena el ID de la mascota.
let idMascotaHistorial = null;


// Indica si el formulario se encuentra en modo edición.
let modoEdicion = false;


// ==========================================================
// INICIO
// ==========================================================

document.addEventListener(
    "DOMContentLoaded",
    function () {

        const parametros =
            new URLSearchParams(
                window.location.search
            );


        // Obtiene el ID de la mascota desde la URL.
        idMascotaHistorial =
            parametros.get("idMascota");


        if (!idMascotaHistorial) {

            console.error(
                "No se encontró el ID de la mascota."
            );

            return;
        }


        // Carga el historial médico existente.
        cargarHistorialMedico();


        // Botón para crear un nuevo historial.
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


        // Botón para editar el historial existente.
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


        // Botón para cancelar la edición.
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


        // Formulario principal.
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

    }
);


// ==========================================================
// OBTENER TOKEN CSRF
// ==========================================================

async function obtenerTokenCSRF() {

    const respuesta =
        await fetch(
            API_CSRF,
            {
                method: "GET",
                credentials: "same-origin"
            }
        );


    if (!respuesta.ok) {

        throw new Error(
            "No se pudo obtener el token de seguridad."
        );
    }


    const token =
        await respuesta.json();


    return token;
}


// ==========================================================
// CARGAR HISTORIAL MÉDICO
// ==========================================================

async function cargarHistorialMedico() {

    try {

        const respuesta =
            await fetch(
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


    // Ocultar mensaje de historial inexistente.
    const estadoSinHistorial =
        document.getElementById(
            "estadoSinHistorial"
        );


    if (estadoSinHistorial) {

        estadoSinHistorial.style.display =
            "none";
    }


    // Ocultar formulario.
    const formularioHistorial =
        document.getElementById(
            "formularioHistorial"
        );


    if (formularioHistorial) {

        formularioHistorial.style.display =
            "none";
    }


    // Mostrar información registrada.
    const informacionHistorial =
        document.getElementById(
            "informacionHistorial"
        );


    if (informacionHistorial) {

        informacionHistorial.style.display =
            "block";
    }


    // Mostrar cada dato.

    mostrarTexto(
        "mostrarAntecedentes",
        historialActual.antecedentesMedicos,
        "No se han registrado antecedentes médicos."
    );


    mostrarTexto(
        "mostrarAlergias",
        historialActual.alergias,
        "No se han registrado alergias."
    );


    mostrarTexto(
        "mostrarMedicamentos",
        historialActual.tratamientos,
        "No se han registrado medicamentos."
    );


    mostrarTexto(
        "mostrarVacunas",
        historialActual.vacunacion,
        "No se han registrado vacunas."
    );


    mostrarTexto(
        "mostrarCirugias",
        historialActual.cirugiasProcedimientos,
        "No se han registrado cirugías."
    );


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

        elemento.textContent =
            valor;

    } else {

        elemento.textContent =
            textoVacio;
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


    if (estadoSinHistorial) {

        estadoSinHistorial.style.display =
            "none";
    }


    if (informacionHistorial) {

        informacionHistorial.style.display =
            "none";
    }


    if (formularioHistorial) {

        formularioHistorial.style.display =
            "block";
    }


    if (btnCancelarEdicion) {

        btnCancelarEdicion.style.display =
            "none";
    }


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


    if (informacionHistorial) {

        informacionHistorial.style.display =
            "none";
    }


    if (estadoSinHistorial) {

        estadoSinHistorial.style.display =
            "none";
    }


    if (formularioHistorial) {

        formularioHistorial.style.display =
            "block";
    }


    if (btnCancelarEdicion) {

        btnCancelarEdicion.style.display =
            "inline-block";
    }


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
        historialActual.antecedentesMedicos ||
        "";


    document.getElementById(
        "alergias"
    ).value =
        historialActual.alergias ||
        "";


    document.getElementById(
        "medicamentos"
    ).value =
        historialActual.tratamientos ||
        "";


    document.getElementById(
        "vacunas"
    ).value =
        historialActual.vacunacion ||
        "";


    document.getElementById(
        "cirugias"
    ).value =
        historialActual.cirugiasProcedimientos ||
        "";


    document.getElementById(
        "observaciones"
    ).value =
        historialActual.consultasAtenciones ||
        "";
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
// GUARDAR HISTORIAL
// ==========================================================

async function guardarHistorial(evento) {

    evento.preventDefault();


    try {

        const datos = {

            diagnosticos:
                historialActual?.diagnosticos ||
                "",


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
                historialActual?.peso ||
                "",


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


        // Obtiene el token CSRF antes de modificar información.
        const tokenCSRF =
            await obtenerTokenCSRF();


        let respuesta;


        // ==================================================
        // ACTUALIZAR HISTORIAL EXISTENTE
        // ==================================================

        if (
            historialActual &&
            historialActual.idHistorial
        ) {

            respuesta =
                await fetch(
                    `${API_BASE_HISTORIAL}/historiales-medicos/actualizar/${historialActual.idHistorial}`,
                    {

                        method:
                            "PUT",


                        headers: {

                            "Content-Type":
                                "application/json",


                            [tokenCSRF.headerName]:
                                tokenCSRF.token
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

            respuesta =
                await fetch(
                    `${API_BASE_HISTORIAL}/historiales-medicos/guardar`,
                    {

                        method:
                            "POST",


                        headers: {

                            "Content-Type":
                                "application/json",


                            [tokenCSRF.headerName]:
                                tokenCSRF.token
                        },


                        credentials:
                            "same-origin",


                        body:
                            JSON.stringify(datos)
                    }
                );
        }


        if (!respuesta.ok) {

            const mensaje =
                await respuesta.text();


            throw new Error(
                mensaje ||
                "No se pudo guardar el historial médico."
            );
        }


        historialActual =
            await respuesta.json();


        alert(
            modoEdicion
                ? "Información actualizada correctamente."
                : "Historial médico creado correctamente."
        );


        mostrarHistorial();


    } catch (error) {

        console.error(
            "Error al guardar el historial médico:",
            error
        );


        alert(
            error.message ||
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


    return (
        campo.value ||
        ""
    ).trim();
}


// ==========================================================
// FUNCIONES PARA REGISTROS INDIVIDUALES
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


    if (!historialActual) {

        mostrarFormularioCrear();


        asignarValorFormulario(
            tipo,
            valor.trim()
        );


        return;
    }


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
        obtenerValorPorTipo(
            tipo
        );


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

            return historialActual.peso || "";


        case "Enfermedad":

            return historialActual.diagnosticos || "";


        case "Antecedente":

            return historialActual.antecedentesMedicos || "";


        case "Alergia":

            return historialActual.alergias || "";


        case "Medicamento":

            return historialActual.tratamientos || "";


        case "Cirugía":

            return historialActual.cirugiasProcedimientos || "";


        case "Vacuna":

            return historialActual.vacunacion || "";


        case "Consulta":

            return historialActual.consultasAtenciones || "";


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
            ).value =
                valor;

            break;


        case "Alergia":

            document.getElementById(
                "alergias"
            ).value =
                valor;

            break;


        case "Medicamento":

            document.getElementById(
                "medicamentos"
            ).value =
                valor;

            break;


        case "Vacuna":

            document.getElementById(
                "vacunas"
            ).value =
                valor;

            break;


        case "Cirugía":

            document.getElementById(
                "cirugias"
            ).value =
                valor;

            break;


        case "Consulta":

            document.getElementById(
                "observaciones"
            ).value =
                valor;

            break;
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

        const datos = {

            diagnosticos:
                historialActual.diagnosticos ||
                "",


            antecedentesMedicos:
                historialActual.antecedentesMedicos ||
                "",


            alergias:
                historialActual.alergias ||
                "",


            tratamientos:
                historialActual.tratamientos ||
                "",


            peso:
                historialActual.peso ||
                "",


            cirugiasProcedimientos:
                historialActual.cirugiasProcedimientos ||
                "",


            vacunacion:
                historialActual.vacunacion ||
                "",


            consultasAtenciones:
                historialActual.consultasAtenciones ||
                "",


            mascota: {

                idMascota:
                    parseInt(
                        idMascotaHistorial
                    )
            }
        };


        // Obtiene el token CSRF antes de actualizar.
        const tokenCSRF =
            await obtenerTokenCSRF();


        const respuesta =
            await fetch(
                `${API_BASE_HISTORIAL}/historiales-medicos/actualizar/${historialActual.idHistorial}`,
                {

                    method:
                        "PUT",


                    headers: {

                        "Content-Type":
                            "application/json",


                        [tokenCSRF.headerName]:
                            tokenCSRF.token
                    },


                    credentials:
                        "same-origin",


                    body:
                        JSON.stringify(datos)
                }
            );


        if (!respuesta.ok) {

            const mensaje =
                await respuesta.text();


            throw new Error(
                mensaje ||
                "No se pudo actualizar el historial."
            );
        }


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
            error.message ||
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

        // Obtiene el token CSRF antes de eliminar.
        const tokenCSRF =
            await obtenerTokenCSRF();


        const respuesta =
            await fetch(
                `${API_BASE_HISTORIAL}/historiales-medicos/eliminar/${historialActual.idHistorial}`,
                {

                    method:
                        "DELETE",


                    headers: {

                        [tokenCSRF.headerName]:
                            tokenCSRF.token
                    },


                    credentials:
                        "same-origin"
                }
            );


        if (!respuesta.ok) {

            const mensaje =
                await respuesta.text();


            throw new Error(
                mensaje ||
                "No se pudo eliminar el historial."
            );
        }


        historialActual =
            null;


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
            error.message ||
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