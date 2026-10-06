
const API_BASE = `${window.location.origin}/api`;


/* ==========================================
   OBTENER ID DEL USUARIO
   ========================================== */

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


/* ==========================================
   ACTUALIZAR INDICADOR DE NOTIFICACIONES
   ========================================== */

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


        /* ==========================================
           CONTAR NOTIFICACIONES NO LEÍDAS
           ========================================== */

        const noLeidas =
            notificaciones.filter(
                function (notificacion) {

                    return obtenerEstadoIndicador(
                        notificacion.estado
                    ) === "no-leida";

                }
            ).length;


        /* ==========================================
           MOSTRAR U OCULTAR INDICADOR
           ========================================== */

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


/* ==========================================
   NORMALIZAR ESTADO
   ========================================== */

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


/* ==========================================
   INICIALIZAR
   ========================================== */

document.addEventListener(
    "DOMContentLoaded",
    function () {

        actualizarIndicadorNotificaciones();

    }
);
