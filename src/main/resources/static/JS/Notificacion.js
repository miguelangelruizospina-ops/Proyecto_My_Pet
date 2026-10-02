const API_BASE = "http://localhost:8082/api";

const listaNotificaciones = document.getElementById("listaNotificaciones");
const mensajeSinNotificaciones = document.getElementById("mensajeSinNotificaciones");
const contadorNotificaciones = document.getElementById("contadorNotificaciones");
const btnEliminarTodas = document.getElementById("btnEliminarTodas");

/* ==========================================
   OBTENER ID DEL USUARIO
   ========================================== */

function obtenerIdUsuario() {

    const usuarioGuardado = localStorage.getItem("usuarioLogueado");

    if (usuarioGuardado) {

        try {

            const usuario = JSON.parse(usuarioGuardado);

            if (usuario.idUsuario) {
                return usuario.idUsuario;
            }

            if (usuario.id_usuario) {
                return usuario.id_usuario;
            }

        } catch (error) {

            console.error(
                "No se pudo leer usuarioLogueado:",
                error
            );

        }
    }

    const idUsuario = localStorage.getItem("idUsuario");

    if (idUsuario) {
        return Number(idUsuario);
    }

    return null;
}

/* ==========================================
   CARGAR NOTIFICACIONES
   ========================================== */

async function cargarNotificaciones() {

    const idUsuario = obtenerIdUsuario();

    if (!idUsuario) {

        console.warn(
            "No se encontró el usuario conectado."
        );

        mostrarSinNotificaciones();
        return;
    }

    try {

        const respuesta = await fetch(
            `${API_BASE}/notificaciones/usuario/${idUsuario}`
        );

        if (!respuesta.ok) {

            throw new Error(
                "No se pudieron cargar las notificaciones."
            );

        }

        const notificaciones = await respuesta.json();

        mostrarNotificaciones(notificaciones);

    } catch (error) {

        console.error(
            "Error al cargar notificaciones:",
            error
        );

        mostrarSinNotificaciones();
    }
}

/* ==========================================
   MOSTRAR NOTIFICACIONES
   ========================================== */

function mostrarNotificaciones(notificaciones) {

    listaNotificaciones.replaceChildren();

    if (
        !notificaciones ||
        notificaciones.length === 0
    ) {

        mostrarSinNotificaciones();
        actualizarContador(0);

        return;
    }

    mensajeSinNotificaciones.style.display = "none";

    let noLeidas = 0;

    notificaciones.forEach(notificacion => {

        const estado = obtenerEstado(
            notificacion.estado
        );

        if (estado !== "leida") {
            noLeidas++;
        }

        crearNotificacion(notificacion);

    });

    actualizarContador(noLeidas);
}

/* ==========================================
   CREAR TARJETA DE NOTIFICACIÓN
   ========================================== */

function crearNotificacion(notificacion) {

    const tarjeta = document.createElement("div");

    tarjeta.classList.add(
        "notificacion-item"
    );

    const estado = obtenerEstado(
        notificacion.estado
    );

    if (estado !== "leida") {

        tarjeta.classList.add(
            "notificacion-no-leida"
        );

    }

    /* MENSAJE */

    const mensaje = document.createElement("div");

    mensaje.classList.add(
        "notificacion-mensaje"
    );

    mensaje.textContent =
        notificacion.mensaje || "Sin mensaje";

    /* FECHA */

    const fecha = document.createElement("span");

    fecha.classList.add(
        "notificacion-fecha"
    );

    fecha.textContent =
        formatearFecha(notificacion.fecha);

    /* ESTADO */

    const estadoTexto = document.createElement("span");

    estadoTexto.classList.add(
        "notificacion-estado"
    );

    if (estado === "leida") {

        estadoTexto.classList.add(
            "estado-leida"
        );

        estadoTexto.textContent = "Leída";

    } else {

        estadoTexto.classList.add(
            "estado-no-leida"
        );

        estadoTexto.textContent = "No leída";
    }

    /* FECHA + ESTADO */

    const informacion = document.createElement("div");

    informacion.appendChild(fecha);
    informacion.appendChild(estadoTexto);

    /* ACCIONES */

    const acciones = document.createElement("div");

    acciones.classList.add(
        "notificacion-acciones"
    );

    /* BOTÓN ELIMINAR */

    const btnEliminar = document.createElement("button");

    btnEliminar.type = "button";

    btnEliminar.classList.add(
        "btn-eliminar-notificacion"
    );

    btnEliminar.textContent = "Eliminar";

    btnEliminar.addEventListener(
        "click",
        function () {

            eliminarNotificacion(
                notificacion.idNotificacion
            );

        }
    );

    acciones.appendChild(btnEliminar);

    tarjeta.appendChild(mensaje);
    tarjeta.appendChild(informacion);
    tarjeta.appendChild(acciones);

    listaNotificaciones.appendChild(tarjeta);
}

/* ==========================================
   OBTENER ESTADO
   ========================================== */

    function obtenerEstado(estado) {

    if (!estado) {
        return "no-leida";
    }

    const estadoNormalizado = estado
        .toString()
        .trim()
        .toLowerCase();

    if (
        estadoNormalizado === "no_leida" ||
        estadoNormalizado === "no-leida"
    ) {
        return "no-leida";
    }

    if (estadoNormalizado === "leida") {
        return "leida";
    }

    return estadoNormalizado;
}

/* ==========================================
   FORMATEAR FECHA
   ========================================== */

function formatearFecha(fecha) {

    if (!fecha) {
        return "Fecha no disponible";
    }

    try {

        const fechaConvertida = new Date(fecha);

        if (isNaN(fechaConvertida.getTime())) {
            return fecha;
        }

        return fechaConvertida.toLocaleString(
            "es-CO",
            {
                dateStyle: "medium",
                timeStyle: "short"
            }
        );

    } catch (error) {

        return fecha;
    }
}

/* ==========================================
   ACTUALIZAR CONTADOR
   ========================================== */

function actualizarContador(cantidad) {

    if (!contadorNotificaciones) {
        return;
    }

    if (cantidad === 0) {

        contadorNotificaciones.textContent =
            "No tienes notificaciones nuevas.";

    } else if (cantidad === 1) {

        contadorNotificaciones.textContent =
            "Tienes 1 notificación nueva.";

    } else {

        contadorNotificaciones.textContent =
            `Tienes ${cantidad} notificaciones nuevas.`;
    }
}

/* ==========================================
   MOSTRAR SIN NOTIFICACIONES
   ========================================== */

function mostrarSinNotificaciones() {

    listaNotificaciones.replaceChildren();

    mensajeSinNotificaciones.style.display =
        "block";
}

/* ==========================================
   ELIMINAR NOTIFICACIÓN
   ========================================== */

async function eliminarNotificacion(id) {

    if (!id) {
        return;
    }

    const confirmar = confirm(
        "¿Quieres eliminar esta notificación?"
    );

    if (!confirmar) {
        return;
    }

    try {

        const respuesta = await fetch(
            `${API_BASE}/notificaciones/${id}`,
            {
                method: "DELETE"
            }
        );

        if (!respuesta.ok) {

            const mensajeError =
                await respuesta.text();

            throw new Error(
                mensajeError ||
                "No se pudo eliminar la notificación."
            );
        }

        await cargarNotificaciones();

    } catch (error) {

        console.error(
            "Error al eliminar notificación:",
            error
        );

        alert(
            "No fue posible eliminar la notificación."
        );
    }
}

/* ==========================================
   INICIALIZAR
   ========================================== */

document.addEventListener(
    "DOMContentLoaded",
    function () {

        if (btnEliminarTodas) {
            btnEliminarTodas.addEventListener("click", async function () {
                const idUsuario = obtenerIdUsuario();
                if (!idUsuario) return;

                try {
                    const respuesta = await fetch(
                        `${API_BASE}/notificaciones/leer-todas/${idUsuario}`,
                        { method: "PUT" }
                    );
                    if (!respuesta.ok) {
                        throw new Error(await respuesta.text());
                    }
                    await cargarNotificaciones();
                } catch (error) {
                    console.error("Error marcando notificaciones:", error);
                    alert("No fue posible marcar las notificaciones como leídas.");
                }
            });
        }

        cargarNotificaciones();

    }
);