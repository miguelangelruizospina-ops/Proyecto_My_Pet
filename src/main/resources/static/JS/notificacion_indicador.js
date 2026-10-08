// Configuración de la ruta principal de la API.

const API_BASE = `${window.location.origin}/api`;

// Obtiene el ID del usuario almacenado en la sesión local.

function obtenerIdUsuarioIndicador() {

    const usuarioGuardado =
        localStorage.getItem("usuarioLogueado");

    if (usuarioGuardado) {

        try {

            const usuario =
                JSON.parse(usuarioGuardado);

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

    const idUsuario =
        localStorage.getItem("idUsuario");

    if (idUsuario) {
        return Number(idUsuario);
    }

    return null;
}

// Actualiza el indicador según las notificaciones no leídas del usuario.

async function actualizarIndicadorNotificaciones() {

    const indicador =
        document.getElementById("indicadorNotificaciones");

    if (!indicador) {
        return;
    }

    const idUsuario =
        obtenerIdUsuarioIndicador();

    if (!idUsuario) {

        indicador.style.display = "none";
        return;

    }

    try {

        const respuesta =
            await fetch(
                `${API_BASE}/notificaciones/usuario/${idUsuario}`,
                {
                    credentials: "same-origin"
                }
            );

        if (!respuesta.ok) {

            throw new Error(
                "No se pudieron consultar las notificaciones."
            );

        }

        const notificaciones =
            await respuesta.json();

        if (!Array.isArray(notificaciones)) {

            indicador.style.display = "none";
            return;

        }

        // Cuenta las notificaciones que todavía no han sido leídas.

        const noLeidas =
            notificaciones.filter(
                function (notificacion) {

                    return obtenerEstadoIndicador(
                        notificacion.estado
                    ) === "no-leida";

                }
            ).length;

        // Muestra u oculta el indicador según existan notificaciones pendientes.

        if (noLeidas > 0) {

            indicador.style.display = "block";

        } else {

            indicador.style.display = "none";

        }

    } catch (error) {

        console.error(
            "Error al actualizar indicador de notificaciones:",
            error
        );

        indicador.style.display = "none";

    }

}

// Normaliza el estado de una notificación para facilitar su comparación.

function obtenerEstadoIndicador(estado) {

    if (!estado) {
        return "no-leida";
    }

    const estadoNormalizado =
        estado
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

// Actualiza el indicador de notificaciones cuando termina de cargar la página.

document.addEventListener(
    "DOMContentLoaded",
    function () {

        actualizarIndicadorNotificaciones();

    }
);