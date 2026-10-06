document.addEventListener("DOMContentLoaded", function () {
    const estadoConexion = document.getElementById("estadoConexion");
    const estadoReporte = document.getElementById("estadoReporte");
    const botonConexion = document.getElementById("btnProbarConexion");
    const botonEnviarReporte = document.getElementById("btnEnviarReporte");
    const botonCopiarReporte = document.getElementById("btnCopiarReporte");
    const descripcionProblema = document.getElementById("descripcionProblema");
    const listaMisReportes = document.getElementById("listaMisReportes");

    function obtenerUsuario() {
        try {
            return JSON.parse(
                localStorage.getItem("usuarioLogueado") || "null"
            );
        } catch (error) {
            return null;
        }
    }

    // Obtiene el ID del usuario que inició sesión.
    function obtenerIdUsuario() {
        const usuario = obtenerUsuario();

        return usuario && (
            usuario.idUsuario ||
            usuario.id_usuario ||
            localStorage.getItem("idUsuario")
        );
    }

    // Convierte el estado interno del reporte en el texto mostrado al usuario.
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

    // Asigna una clase de Bootstrap según el estado del reporte.
    function obtenerClaseEstado(estado) {
        switch (estado) {
            case "pendiente":
                return "text-bg-warning";

            case "en_revision":
                return "text-bg-primary";

            case "solucionado":
                return "text-bg-success";

            default:
                return "text-bg-secondary";
        }
    }

    // Evita insertar directamente contenido enviado desde la base de datos en HTML.
    function escaparHTML(texto) {
        return String(texto ?? "")
            .replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;")
            .replace(/"/g, "&quot;")
            .replace(/'/g, "&#039;");
    }

    // Carga los reportes realizados por el usuario autenticado.
    async function cargarMisReportes() {
        const idUsuario = obtenerIdUsuario();

        if (!idUsuario) {
            listaMisReportes.innerHTML = `
                <p class="text-muted mb-0">
                    Inicia sesión para consultar tus reportes.
                </p>
            `;
            return;
        }

        listaMisReportes.innerHTML = `
            <p class="text-muted mb-0">
                Cargando reportes...
            </p>
        `;

        try {
            const respuesta = await fetch(
                `/api/reportes/usuario/${encodeURIComponent(idUsuario)}`
            );

            if (!respuesta.ok) {
                throw new Error(
                    `El servidor respondió ${respuesta.status}.`
                );
            }

            const reportes = await respuesta.json();

            if (!Array.isArray(reportes) || reportes.length === 0) {
                listaMisReportes.innerHTML = `
                    <p class="text-muted mb-0">
                        Aún no has realizado ningún reporte.
                    </p>
                `;
                return;
            }

            listaMisReportes.innerHTML = reportes
                .slice()
                .reverse()
                .map(function (reporte) {

                    const estado = reporte.estado || "pendiente";

                    const fecha = reporte.fecha
                        ? new Date(reporte.fecha).toLocaleString("es-CO")
                        : "Fecha no disponible";

                    const respuestaAdministrador =
                        reporte.respuesta &&
                        reporte.respuesta.trim()
                            ? reporte.respuesta
                            : "El administrador aún no ha agregado una respuesta.";

                    return `
                        <article class="border rounded p-3 mb-3">

                            <div class="d-flex justify-content-between align-items-start gap-3 mb-3">

                                <h3 class="h6 mb-0">
                                    Reporte #${escaparHTML(reporte.idReporte)}
                                </h3>

                                <span class="badge ${obtenerClaseEstado(estado)}">
                                    ${escaparHTML(obtenerTextoEstado(estado))}
                                </span>

                            </div>

                            <p class="small text-muted mb-2">
                                ${escaparHTML(fecha)}
                            </p>

                            <p class="mb-3">
                                <strong>Problema reportado:</strong><br>
                                ${escaparHTML(reporte.descripcion)}
                            </p>

                            <div class="bg-light rounded p-3">
                                <strong>Respuesta del administrador:</strong>

                                <p class="mb-0 mt-2">
                                    ${escaparHTML(respuestaAdministrador)}
                                </p>
                            </div>

                        </article>
                    `;
                })
                .join("");

        } catch (error) {
            console.error(
                "No se pudieron cargar los reportes:",
                error
            );

            listaMisReportes.innerHTML = `
                <p class="text-danger mb-0">
                    No se pudieron cargar tus reportes.
                </p>
            `;
        }
    }


    // Comprueba que el servidor y la sesión respondan correctamente.
    botonConexion.addEventListener("click", async function () {
        botonConexion.disabled = true;
        estadoConexion.textContent = "Comprobando...";

        try {
            const idUsuario = obtenerIdUsuario();

            const ruta = idUsuario
                ? `/api/mascotas/usuario/${encodeURIComponent(idUsuario)}`
                : "/api/usuario/csrf";

            const respuesta = await fetch(ruta);

            if (!respuesta.ok) {
                throw new Error(
                    `El servidor respondió ${respuesta.status}.`
                );
            }

            estadoConexion.textContent = idUsuario
                ? "Servidor, sesión y consulta de datos responden correctamente."
                : "El servidor responde. Inicia sesión para comprobar también la consulta de datos.";

        } catch (error) {
            console.error(
                "Fallo comprobando la conexión:",
                error
            );

            estadoConexion.textContent =
                "No se pudo conectar. Comprueba que Spring Boot y MySQL estén iniciados.";

        } finally {
            botonConexion.disabled = false;
        }
    });


    // Envía el reporte al backend.
    botonEnviarReporte.addEventListener("click", async function () {
        const descripcion = descripcionProblema.value.trim();

        if (!descripcion) {
            estadoReporte.textContent =
                "Escribe una descripción del problema antes de enviar el reporte.";
            return;
        }

        botonEnviarReporte.disabled = true;
        estadoReporte.textContent = "Enviando reporte...";

        const reporte = {
            descripcion: descripcion,
            pagina: window.location.href,
            navegador: navigator.userAgent
        };

        try {
            const respuesta = await fetch("/api/reportes/crear", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(reporte)
            });

            if (!respuesta.ok) {
                let mensaje = `El servidor respondió ${respuesta.status}.`;

                try {
                    const datos = await respuesta.json();

                    if (datos.message) {
                        mensaje = datos.message;
                    }
                } catch (error) {
                    // La respuesta no contenía JSON.
                }

                throw new Error(mensaje);
            }

            descripcionProblema.value = "";

            estadoReporte.textContent =
                "Reporte enviado correctamente. El administrador podrá revisarlo.";

            // Actualiza inmediatamente la lista con el nuevo reporte.
            await cargarMisReportes();

        } catch (error) {
            console.error(
                "No se pudo enviar el reporte:",
                error
            );

            estadoReporte.textContent =
                `No se pudo enviar el reporte: ${error.message}`;

        } finally {
            botonEnviarReporte.disabled = false;
        }
    });


    // Copia la información técnica del problema.
    botonCopiarReporte.addEventListener("click", async function () {
        const usuario = obtenerUsuario();

        const reporte = [
            `Problema: ${descripcionProblema.value.trim() || "Sin descripción"}`,
            `Página: ${window.location.href}`,
            `Fecha: ${new Date().toLocaleString("es")}`,
            `Navegador: ${navigator.userAgent}`,
            `Sesión: ${usuario ? "iniciada" : "no iniciada"}`,
            `Estado de conexión: ${estadoConexion.textContent}`
        ].join("\n");

        try {
            await navigator.clipboard.writeText(reporte);

            estadoReporte.textContent =
                "Reporte copiado.";

        } catch (error) {
            console.error(
                "No se pudo copiar el reporte:",
                error
            );

            estadoReporte.textContent =
                "El navegador no permitió copiarlo. Selecciona y copia manualmente el texto de descripción.";
        }
    });


    // Carga los reportes cuando se abre la página de ayuda.
    cargarMisReportes();
});