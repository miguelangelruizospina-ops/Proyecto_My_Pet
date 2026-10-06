// Servicio encargado de consultar y gestionar las notificaciones de My Pet.

const API_BASE = `${window.location.origin}/api`;

const listaNotificaciones =
    document.getElementById("listaNotificaciones");

const mensajeSinNotificaciones =
    document.getElementById("mensajeSinNotificaciones");

const contadorNotificaciones =
    document.getElementById("contadorNotificaciones");

const btnMarcarTodasComoLeidas =
    document.getElementById("btnMarcarTodasComoLeidas");

const btnEliminarTodas =
    document.getElementById("btnEliminarTodas");


// =========================================================
// OBTENCIÓN DEL USUARIO
// =========================================================

// Obtiene el ID del usuario actualmente autenticado.
function obtenerIdUsuario() {

    const usuarioGuardado =
        localStorage.getItem("usuarioLogueado");

    if (!usuarioGuardado) {
        return localStorage.getItem("idUsuario");
    }

    try {

        const usuario =
            JSON.parse(usuarioGuardado);

        return (
            usuario.idUsuario ||
            usuario.id_usuario ||
            localStorage.getItem("idUsuario")
        );

    } catch (error) {

        console.error(
            "No se pudo leer el usuario guardado:",
            error
        );

        return localStorage.getItem("idUsuario");
    }
}


// =========================================================
// PETICIONES AL BACKEND
// =========================================================

// Realiza las solicitudes al backend de notificaciones.
async function solicitar(url, opciones = {}) {

    const headers = {
        ...(opciones.body
            ? { "Content-Type": "application/json" }
            : {}),
        ...(opciones.headers || {})
    };

    const respuesta =
        await fetch(
            `${API_BASE}${url}`,
            {
                ...opciones,
                cache: "no-store",
                credentials: "include",
                headers
            }
        );

    if (!respuesta.ok) {

        const mensaje =
            await respuesta.text();

        throw new Error(
            mensaje ||
            `Error HTTP ${respuesta.status}`
        );
    }

    const texto =
        await respuesta.text();

    if (!texto) {
        return null;
    }

    const contentType =
        respuesta.headers.get("content-type") || "";

    if (
        contentType.includes("application/json")
    ) {

        return JSON.parse(texto);
    }

    return texto;
}


// =========================================================
// CARGA DE NOTIFICACIONES
// =========================================================

// Consulta las notificaciones actuales del usuario.
async function cargarNotificaciones() {

    const idUsuario =
        obtenerIdUsuario();

    if (!idUsuario) {

        mostrarSinNotificaciones();

        return;
    }

    try {

        const notificaciones =
            await solicitar(
                `/notificaciones/usuario/${encodeURIComponent(idUsuario)}`
            );

        mostrarNotificaciones(
            notificaciones
        );

    } catch (error) {

        console.error(
            "Error al cargar notificaciones:",
            error
        );

        mostrarSinNotificaciones();
    }
}


// =========================================================
// MOSTRAR NOTIFICACIONES
// =========================================================

// Construye visualmente la lista de notificaciones.
function mostrarNotificaciones(
    notificaciones
) {

    listaNotificaciones.replaceChildren();

    if (
        !Array.isArray(notificaciones) ||
        notificaciones.length === 0
    ) {

        mostrarSinNotificaciones();

        actualizarContador(0);

        return;
    }

    if (mensajeSinNotificaciones) {

        mensajeSinNotificaciones.style.display =
            "none";
    }

    let noLeidas = 0;

    notificaciones.forEach(
        function (notificacion) {

            const estado =
                obtenerEstado(
                    notificacion.estado
                );

            if (
                estado === "no-leida"
            ) {

                noLeidas++;
            }

            crearNotificacion(
                notificacion
            );
        }
    );

    actualizarContador(
        noLeidas
    );
}


// =========================================================
// CREACIÓN DE TARJETAS
// =========================================================

// Crea la tarjeta visual de cada notificación.
function crearNotificacion(
    notificacion
) {

    const tarjeta =
        document.createElement("div");

    tarjeta.classList.add(
        "notificacion-item"
    );

    tarjeta.dataset.idNotificacion =
        notificacion.idNotificacion;


    const estado =
        obtenerEstado(
            notificacion.estado
        );

    if (
        estado === "no-leida"
    ) {

        tarjeta.classList.add(
            "notificacion-no-leida"
        );
    }


    const mensaje =
        document.createElement("div");

    mensaje.classList.add(
        "notificacion-mensaje"
    );

    mensaje.textContent =
        notificacion.mensaje ||
        "Sin mensaje";


    const fecha =
        document.createElement("span");

    fecha.classList.add(
        "notificacion-fecha"
    );

    fecha.textContent =
        formatearFecha(
            notificacion.fecha
        );


    const estadoTexto =
        document.createElement("span");

    estadoTexto.classList.add(
        "notificacion-estado"
    );


    if (
        estado === "leida"
    ) {

        estadoTexto.classList.add(
            "estado-leida"
        );

        estadoTexto.textContent =
            "Leída";

    } else {

        estadoTexto.classList.add(
            "estado-no-leida"
        );

        estadoTexto.textContent =
            "No leída";
    }


    const informacion =
        document.createElement("div");

    informacion.appendChild(
        fecha
    );

    informacion.appendChild(
        estadoTexto
    );


    const acciones =
        document.createElement("div");

    acciones.classList.add(
        "notificacion-acciones"
    );


    // Botón para marcar una notificación individual como leída.
    if (
        estado === "no-leida"
    ) {

        const btnLeer =
            document.createElement("button");

        btnLeer.type =
            "button";

        btnLeer.classList.add(
            "btn",
            "btn-success",
            "btn-sm"
        );

        btnLeer.textContent =
            "Marcar como leída";


        btnLeer.addEventListener(
            "click",
            async function () {

                btnLeer.disabled =
                    true;

                const resultado =
                    await marcarComoLeida(
                        notificacion.idNotificacion,
                        tarjeta
                    );

                if (!resultado) {

                    btnLeer.disabled =
                        false;
                }
            }
        );

        acciones.appendChild(
            btnLeer
        );
    }


    // Botón para eliminar la notificación.
    const btnEliminar =
        document.createElement("button");

    btnEliminar.type =
        "button";

    btnEliminar.classList.add(
        "btn-eliminar-notificacion"
    );

    btnEliminar.textContent =
        "Eliminar";


    btnEliminar.addEventListener(
        "click",
        async function () {

            btnEliminar.disabled =
                true;

            const resultado =
                await eliminarNotificacion(
                    notificacion.idNotificacion,
                    tarjeta
                );

            if (
                !resultado &&
                document.body.contains(tarjeta)
            ) {

                btnEliminar.disabled =
                    false;
            }
        }
    );

    acciones.appendChild(
        btnEliminar
    );


    tarjeta.appendChild(
        mensaje
    );

    tarjeta.appendChild(
        informacion
    );

    tarjeta.appendChild(
        acciones
    );


    listaNotificaciones.appendChild(
        tarjeta
    );
}


// =========================================================
// ESTADO DE LAS NOTIFICACIONES
// =========================================================

// Normaliza los diferentes formatos de estado.
function obtenerEstado(
    estado
) {

    const valor =
        estado
            ? estado
                .toString()
                .trim()
                .toUpperCase()
            : "NO_LEIDA";


    if (
        valor === "LEIDA" ||
        valor === "LEÍDA"
    ) {

        return "leida";
    }

    return "no-leida";
}


// =========================================================
// FORMATO DE FECHA
// =========================================================

// Convierte la fecha recibida del backend a formato colombiano.
function formatearFecha(
    fecha
) {

    if (!fecha) {

        return "Fecha no disponible";
    }


    const fechaObjeto =
        new Date(fecha);


    if (
        Number.isNaN(
            fechaObjeto.getTime()
        )
    ) {

        return fecha;
    }


    return fechaObjeto.toLocaleString(
        "es-CO"
    );
}


// =========================================================
// CONTADOR
// =========================================================

// Actualiza el contador de notificaciones no leídas.
function actualizarContador(
    cantidad
) {

    if (
        !contadorNotificaciones
    ) {

        return;
    }


    contadorNotificaciones.textContent =
        cantidad === 1
            ? "1 notificación"
            : `${cantidad} notificaciones`;
}


// =========================================================
// MARCAR COMO LEÍDA
// =========================================================

// Marca una notificación individual como leída.
async function marcarComoLeida(
    id,
    tarjeta
) {

    if (!id) {
        return false;
    }


    try {

        await solicitar(
            `/notificaciones/leer/${encodeURIComponent(id)}`,
            {
                method: "PUT"
            }
        );


        // Actualiza inmediatamente la tarjeta.
        if (tarjeta) {

            tarjeta.classList.remove(
                "notificacion-no-leida"
            );


            const estadoTexto =
                tarjeta.querySelector(
                    ".notificacion-estado"
                );


            if (estadoTexto) {

                estadoTexto.classList.remove(
                    "estado-no-leida"
                );

                estadoTexto.classList.add(
                    "estado-leida"
                );

                estadoTexto.textContent =
                    "Leída";
            }


            const botonLeer =
                tarjeta.querySelector(
                    ".btn-success"
                );


            if (botonLeer) {

                botonLeer.remove();
            }
        }


        const noLeidas =
            listaNotificaciones.querySelectorAll(
                ".notificacion-no-leida"
            ).length;


        actualizarContador(
            noLeidas
        );


        return true;

    } catch (error) {

        console.error(
            "Error al marcar la notificación como leída:",
            error
        );

        alert(
            "No fue posible marcar la notificación como leída."
        );

        return false;
    }
}


// =========================================================
// ELIMINACIÓN INDIVIDUAL
// =========================================================

// Elimina una notificación de la base de datos
// y posteriormente la quita de la interfaz.
async function eliminarNotificacion(
    id,
    tarjeta
) {

    if (
        !id ||
        !tarjeta
    ) {

        return false;
    }


    const confirmar =
        window.confirm(
            "¿Quieres eliminar esta notificación?"
        );


    if (!confirmar) {

        return false;
    }


    try {

        const respuesta =
            await fetch(
                `${API_BASE}/notificaciones/${encodeURIComponent(id)}`,
                {
                    method: "DELETE",
                    cache: "no-store",
                    credentials: "include"
                }
            );


        if (!respuesta.ok) {

            const mensaje =
                await respuesta.text();

            throw new Error(
                mensaje ||
                `Error HTTP ${respuesta.status}`
            );
        }


        // El backend confirmó la eliminación.
        // Ahora se elimina la tarjeta de la interfaz.
        tarjeta.remove();


        // Actualiza el contador de no leídas.
        const noLeidas =
            listaNotificaciones.querySelectorAll(
                ".notificacion-no-leida"
            ).length;


        actualizarContador(
            noLeidas
        );


        // Comprueba si ya no quedan notificaciones.
        const cantidadTarjetas =
            listaNotificaciones.querySelectorAll(
                ".notificacion-item"
            ).length;


        if (
            cantidadTarjetas === 0
        ) {

            mostrarSinNotificaciones();

            actualizarContador(0);
        }


        return true;

    } catch (error) {

        console.error(
            "Error al eliminar la notificación:",
            error
        );

        alert(
            "No fue posible eliminar la notificación."
        );

        return false;
    }
}


// =========================================================
// MARCAR TODAS COMO LEÍDAS
// =========================================================

// Marca todas las notificaciones del usuario como leídas.
async function marcarTodasComoLeidas() {

    const idUsuario =
        obtenerIdUsuario();


    if (!idUsuario) {
        return;
    }


    try {

        await solicitar(
            `/notificaciones/leer-todas/${encodeURIComponent(idUsuario)}`,
            {
                method: "PUT"
            }
        );


        // Actualiza todas las tarjetas directamente.
        const tarjetas =
            listaNotificaciones.querySelectorAll(
                ".notificacion-item"
            );


        tarjetas.forEach(
            function (tarjeta) {

                tarjeta.classList.remove(
                    "notificacion-no-leida"
                );


                const estadoTexto =
                    tarjeta.querySelector(
                        ".notificacion-estado"
                    );


                if (estadoTexto) {

                    estadoTexto.classList.remove(
                        "estado-no-leida"
                    );

                    estadoTexto.classList.add(
                        "estado-leida"
                    );

                    estadoTexto.textContent =
                        "Leída";
                }


                const botonLeer =
                    tarjeta.querySelector(
                        ".btn-success"
                    );


                if (botonLeer) {

                    botonLeer.remove();
                }
            }
        );


        actualizarContador(0);

    } catch (error) {

        console.error(
            "Error al marcar todas las notificaciones como leídas:",
            error
        );

        alert(
            "No fue posible marcar las notificaciones como leídas."
        );
    }
}


// =========================================================
// ELIMINAR TODAS
// =========================================================

// Elimina todas las notificaciones del usuario.
async function eliminarTodas() {

    const idUsuario =
        obtenerIdUsuario();


    if (!idUsuario) {
        return;
    }


    const confirmar =
        window.confirm(
            "¿Quieres eliminar todas las notificaciones? Esta acción no se puede deshacer."
        );


    if (!confirmar) {
        return;
    }


    try {

        const respuesta =
            await fetch(
                `${API_BASE}/notificaciones/usuario/${encodeURIComponent(idUsuario)}`,
                {
                    method: "DELETE",
                    cache: "no-store",
                    credentials: "include"
                }
            );


        if (!respuesta.ok) {

            const mensaje =
                await respuesta.text();

            throw new Error(
                mensaje ||
                `Error HTTP ${respuesta.status}`
            );
        }


        // El backend confirmó la eliminación.
        // Se limpia la lista inmediatamente.
        mostrarSinNotificaciones();

        actualizarContador(0);


    } catch (error) {

        console.error(
            "Error al eliminar todas las notificaciones:",
            error
        );

        alert(
            "No fue posible eliminar las notificaciones."
        );
    }
}


// =========================================================
// ESTADO VACÍO
// =========================================================

// Muestra el mensaje cuando no existen notificaciones.
function mostrarSinNotificaciones() {

    if (
        listaNotificaciones
    ) {

        listaNotificaciones.replaceChildren();
    }


    if (
        mensajeSinNotificaciones
    ) {

        mensajeSinNotificaciones.style.display =
            "block";
    }
}


// =========================================================
// INICIALIZACIÓN
// =========================================================

// Carga las notificaciones cuando la página está lista.
document.addEventListener(
    "DOMContentLoaded",
    function () {

        // Botón para marcar todas como leídas.
        if (
            btnMarcarTodasComoLeidas
        ) {

            btnMarcarTodasComoLeidas.addEventListener(
                "click",
                marcarTodasComoLeidas
            );
        }


        // Botón para eliminar todas definitivamente.
        if (
            btnEliminarTodas
        ) {

            btnEliminarTodas.addEventListener(
                "click",
                eliminarTodas
            );
        }


        cargarNotificaciones();
    }
);