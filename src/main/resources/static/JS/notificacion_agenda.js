// Muestra el indicador de notificaciones pendientes de agenda.

// Inicializa el indicador de agenda cuando la página termina de cargar.

document.addEventListener("DOMContentLoaded", async function () {

    const indicadorAgenda =
        document.getElementById("indicadorAgenda");

    if (!indicadorAgenda) {
        return;
    }

    // Obtiene la información del usuario almacenada en el navegador.

    const usuarioGuardado =
        localStorage.getItem("usuarioLogueado");

    if (!usuarioGuardado) {
        indicadorAgenda.style.display = "none";
        return;
    }

    let usuario;

    try {

        usuario =
            JSON.parse(usuarioGuardado);

    } catch (error) {

        console.error(
            "No se pudo leer usuarioLogueado:",
            error
        );

        indicadorAgenda.style.display = "none";
        return;
    }

    const idUsuario =
        usuario.idUsuario ||
        usuario.id_usuario ||
        localStorage.getItem("idUsuario");

    if (!idUsuario) {

        indicadorAgenda.style.display = "none";
        return;
    }

    // Configura la ruta principal de la API.

    const apiBase =
        `${window.location.origin}/api`;

    try {

        // Obtiene las notificaciones asociadas al usuario.

        const respuesta =
            await fetch(
                `${apiBase}/notificaciones/usuario/${encodeURIComponent(idUsuario)}`,
                {
                    credentials: "same-origin"
                }
            );

        if (!respuesta.ok) {

            throw new Error(
                "No se pudieron obtener las notificaciones."
            );
        }

        const notificaciones =
            await respuesta.json();

        if (!Array.isArray(notificaciones)) {

            indicadorAgenda.style.display = "none";
            return;
        }

        // Filtra las notificaciones de agenda que todavía no han sido leídas.

        const notificacionesAgenda =
            notificaciones.filter(
                function (notificacion) {

                    const tipo =
                        notificacion.tipo
                            ? notificacion.tipo
                                .toString()
                                .trim()
                                .toUpperCase()
                            : "";

                    const estado =
                        notificacion.estado
                            ? notificacion.estado
                                .toString()
                                .trim()
                                .toUpperCase()
                            : "NO_LEIDA";

                    return (
                        tipo === "AGENDA" &&
                        (
                            estado === "NO_LEIDA" ||
                            estado === "NO-LEIDA"
                        )
                    );

                }
            );

        // Muestra u oculta el indicador según existan notificaciones pendientes.

        if (notificacionesAgenda.length > 0) {

            indicadorAgenda.style.display = "block";

        } else {

            indicadorAgenda.style.display = "none";

        }

    } catch (error) {

        console.error(
            "Error al actualizar indicador de agenda:",
            error
        );

        indicadorAgenda.style.display = "none";
    }

});