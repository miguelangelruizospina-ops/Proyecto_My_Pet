// Archivo encargado de gestionar las emergencias de la mascota.

// Ejecuta el código cuando la página haya terminado de cargar.

document.addEventListener("DOMContentLoaded", function () {

    // Configuración y elementos principales de la página.

    const apiBase = `${window.location.origin}/api`;

    // Obtiene la información del usuario guardada en la sesión.

    const usuarioGuardado =
        localStorage.getItem("usuarioLogueado");

    // Obtiene los elementos principales de la pantalla.

    const formulario =
        document.getElementById("formEmergenciaUsuario");

    const lista =
        document.getElementById("listaEmergencias");

    const estado =
        document.getElementById("estadoEmergencia");

    const boton =
        document.getElementById("btnEnviarEmergencia");

    const selectorMascota =
        document.getElementById("mascotaEmergenciaUsuario");

    // Obtiene el ID de la mascota enviado en la URL.

    const idMascotaUrl =
        new URLSearchParams(window.location.search)
            .get("idMascota");

    // Verifica que exista una sesión de usuario.
    
    if (!usuarioGuardado) {

        window.location.href =
            "/Front%20end/Modulo_1_gestion_usuario/iniciar_sesion.html";

        return;
    }

    // Variable donde se almacenará la información del usuario.

    let usuario;

    try {

        usuario =
            JSON.parse(usuarioGuardado);

    } catch (error) {

        localStorage.removeItem("usuarioLogueado");
        localStorage.removeItem("idUsuario");

        window.location.href =
            "/Front%20end/Modulo_1_gestion_usuario/iniciar_sesion.html";

        return;
    }

    // Obtiene el ID del usuario que inició sesión.

    const idUsuario =
        usuario.idUsuario ||
        usuario.id_usuario ||
        localStorage.getItem("idUsuario");

    if (!idUsuario) {

        estado.textContent =
            "No se pudo identificar tu cuenta.";

        return;
    }

    // Obtiene el token CSRF almacenado en las cookies.

    function obtenerTokenCsrf() {

        const nombreCookie =
            "XSRF-TOKEN";

        const cookies =
            document.cookie.split(";");

        for (const cookie of cookies) {

            const parte =
                cookie.trim();

            if (
                parte.startsWith(
                    nombreCookie + "="
                )
            ) {

                return decodeURIComponent(
                    parte.substring(
                        nombreCookie.length + 1
                    )
                );
            }
        }

        return null;
    }

    // Solicita un nuevo token CSRF al backend.

    async function inicializarCsrf() {

        const respuesta =
            await fetch(
                `${apiBase}/usuario/csrf`,
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

        return obtenerTokenCsrf();
    }

    // Gestiona las solicitudes a la API y agrega el token CSRF cuando corresponde.

    async function solicitar(
        url,
        opciones = {}
    ) {

        const metodo =
            (
                opciones.method ||
                "GET"
            ).toUpperCase();

        const headers = {

            ...(opciones.body
                ? {
                    "Content-Type":
                        "application/json"
                }
                : {}),

            ...(opciones.headers || {})
        };

        // Agrega el token CSRF a las operaciones que modifican datos.

        if (
            metodo !== "GET" &&
            metodo !== "HEAD" &&
            metodo !== "OPTIONS"
        ) {

            let tokenCsrf =
                obtenerTokenCsrf();

            if (!tokenCsrf) {

                tokenCsrf =
                    await inicializarCsrf();
            }

            if (tokenCsrf) {

                headers["X-XSRF-TOKEN"] =
                    tokenCsrf;

            } else {

                throw new Error(
                    "No se pudo obtener el token de seguridad CSRF."
                );
            }
        }

        const respuesta =
            await fetch(
                `${apiBase}${url}`,
                {
                    ...opciones,
                    credentials: "same-origin",
                    headers
                }
            );

        if (!respuesta.ok) {

            const mensaje =
                await respuesta.text();

            throw new Error(
                mensaje ||
                "No se pudo completar la solicitud."
            );
        }

        const texto =
            await respuesta.text();

        return texto
            ? JSON.parse(texto)
            : null;
    }

    // Carga las mascotas pertenecientes al usuario autenticado.

    async function cargarMascotas() {

        if (!selectorMascota) {
            return;
        }

        try {

            selectorMascota.replaceChildren();

            const opcionInicial =
                document.createElement("option");

            opcionInicial.value = "";
            opcionInicial.textContent =
                "Selecciona una mascota";

            selectorMascota.appendChild(
                opcionInicial
            );

            // Obtiene únicamente las mascotas pertenecientes al usuario autenticado.

            const mascotas =
                await solicitar(
                    `/mascotas/usuario/${encodeURIComponent(idUsuario)}`
                );

            if (
                !Array.isArray(mascotas) ||
                mascotas.length === 0
            ) {

                const opcion =
                    document.createElement("option");

                opcion.value = "";
                opcion.textContent =
                    "No tienes mascotas registradas";

                opcion.disabled = true;

                selectorMascota.appendChild(
                    opcion
                );

                return;
            }

            // Agrega cada mascota al selector.

            mascotas.forEach(
                function (mascota) {

                    const opcion =
                        document.createElement("option");

                    opcion.value =
                        mascota.idMascota;

                    opcion.textContent =
                        mascota.nombre ||
                        "Mascota sin nombre";

                    selectorMascota.appendChild(
                        opcion
                    );
                }
            );

            // Selecciona automáticamente la mascota indicada en la URL.

            if (idMascotaUrl) {

                const mascotaExiste =
                    mascotas.some(
                        function (mascota) {

                            return String(
                                mascota.idMascota
                            ) === String(
                                idMascotaUrl
                            );
                        }
                    );

                if (mascotaExiste) {

                    selectorMascota.value =
                        String(idMascotaUrl);

                } else {

                    console.warn(
                        "La mascota indicada en la URL no pertenece al usuario."
                    );
                }
            }

        } catch (error) {

            console.error(
                "Error cargando las mascotas:",
                error
            );

            selectorMascota.replaceChildren();

            const opcionError =
                document.createElement("option");

            opcionError.value = "";
            opcionError.textContent =
                "No se pudieron cargar las mascotas";

            selectorMascota.appendChild(
                opcionError
            );

            estado.textContent =
                "No se pudieron cargar tus mascotas.";
        }
    }

    // Marca una emergencia como solucionada en el backend.

    async function solucionarEmergencia(
        idEmergencia
    ) {

        try {

            await solicitar(
                `/emergencias/resolver/${encodeURIComponent(idEmergencia)}`,
                {
                    method: "PUT"
                }
            );

            estado.textContent =
                "La emergencia fue marcada como solucionada.";

            await cargarEmergencias();

        } catch (error) {

            console.error(
                "Error solucionando la emergencia:",
                error
            );

            estado.textContent =
                error.message ||
                "No se pudo marcar la emergencia como solucionada.";
        }
    }

    // Muestra las emergencias pendientes registradas para el usuario.

    function mostrarEmergencias(
        emergencias
    ) {

        lista.replaceChildren();

        const emergenciasPendientes =
            emergencias.filter(
                function (emergencia) {

                    return (
                        emergencia.estado &&
                        emergencia.estado
                            .toUpperCase() ===
                        "PENDIENTE"
                    );
                }
            );

        if (!emergenciasPendientes.length) {

            lista.textContent =
                "No tienes emergencias pendientes.";

            return;
        }

        emergenciasPendientes.forEach(
            function (emergencia) {

                const articulo =
                    document.createElement(
                        "article"
                    );

                // Configura el recuadro de la emergencia.

                articulo.className =
                    "p-3 rounded shadow-sm mb-3";

                articulo.style.backgroundColor =
                    "rgba(255, 255, 255, 0.78)";

                articulo.style.backdropFilter =
                    "blur(3px)";

                articulo.style.webkitBackdropFilter =
                    "blur(3px)";

                // Configura el título de la sección.

                const titulo =
                    document.createElement(
                        "h3"
                    );

                titulo.className =
                    "h6 mb-2";

                titulo.textContent =
                    emergencia.tipo ||
                    "Emergencia";

                // Configura el contenido de la emergencia.

                const descripcion =
                    document.createElement(
                        "p"
                    );

                descripcion.className =
                    "mb-1";

                descripcion.textContent =
                    emergencia.descripcion ||
                    "Sin descripción";

                // Muestra la información adicional de la emergencia.

                const detalle =
                    document.createElement(
                        "small"
                    );

                const nombreMascota =
                    emergencia.mascota?.nombre;

                detalle.textContent =
                    [
                        nombreMascota,
                        emergencia.fecha
                    ]
                    .filter(Boolean)
                    .join(" · ");

                // Configura el botón para solucionar la emergencia.

                const botonResolver =
                    document.createElement(
                        "button"
                    );

                botonResolver.type =
                    "button";

                botonResolver.className =
                    "btn btn-sm mt-3";

                botonResolver.style.backgroundColor =
                    "#F7931E";

                botonResolver.style.borderColor =
                    "#F7931E";

                botonResolver.style.color =
                    "#ffffff";

                botonResolver.textContent =
                    "Marcar como solucionada";

                botonResolver.addEventListener(
                    "mouseenter",
                    function () {

                        botonResolver.style.backgroundColor =
                            "#d9790d";

                        botonResolver.style.borderColor =
                            "#d9790d";
                    }
                );

                botonResolver.addEventListener(
                    "mouseleave",
                    function () {

                        botonResolver.style.backgroundColor =
                            "#F7931E";

                        botonResolver.style.borderColor =
                            "#F7931E";
                    }
                );

                botonResolver.addEventListener(
                    "click",
                    async function () {

                        const confirmar =
                            window.confirm(
                                "¿La emergencia ya fue atendida o solucionada?"
                            );

                        if (!confirmar) {
                            return;
                        }

                        botonResolver.disabled =
                            true;

                        botonResolver.textContent =
                            "Guardando...";

                        await solucionarEmergencia(
                            emergencia.idEmergencia
                        );
                    }
                );

                articulo.append(
                    titulo,
                    descripcion,
                    detalle,
                    botonResolver
                );

                lista.append(
                    articulo
                );
            }
        );
    }

    // Carga las emergencias registradas para el usuario.

    async function cargarEmergencias() {

        try {

            const emergencias =
                await solicitar(
                    `/emergencias/usuario/${encodeURIComponent(idUsuario)}`
                );

            mostrarEmergencias(
                emergencias
            );

        } catch (error) {

            console.error(
                "Error cargando reportes de emergencia:",
                error
            );

            estado.textContent =
                "No se pudieron cargar tus reportes.";
        }
    }

    // Configura el regreso al perfil de la mascota cuando fue indicada en la URL.

    if (idMascotaUrl) {

        const enlaceVolver =
            document.getElementById(
                "volverEmergencia"
            );

        if (enlaceVolver) {

            enlaceVolver.href =
                `/Front%20end/Modulo_2_gestion_mascotas/perfil_mascota.html?id=${encodeURIComponent(idMascotaUrl)}`;
        }

        // Consulta la información de la mascota para mostrarla en el encabezado.
        solicitar(
            `/mascotas/${encodeURIComponent(idMascotaUrl)}`
        )
            .then(
                mascota => {

                    const elementoMascota =
                        document.getElementById(
                            "mascotaEmergencia"
                        );

                    if (elementoMascota) {

                        elementoMascota.textContent =
                            `Mascota: ${mascota.nombre}`;
                    }
                }
            )
            .catch(
                error => {

                    console.error(
                        "No se pudo cargar la mascota:",
                        error
                    );

                    estado.textContent =
                        "No se pudo verificar la mascota seleccionada.";
                }
            );
    }

    // Registra una nueva emergencia asociada a la mascota seleccionada.

    formulario.addEventListener(
        "submit",
        async function (event) {

            event.preventDefault();

            estado.textContent =
                "";

            boton.disabled =
                true;

            const descripcion =
                document
                    .getElementById(
                        "descripcionEmergenciaUsuario"
                    )
                    .value
                    .trim();

            // Obtiene la mascota seleccionada en el formulario.

            const idMascotaSeleccionada =
                selectorMascota
                    ? selectorMascota.value
                    : "";

            // Verifica que se haya seleccionado una mascota.

            if (!idMascotaSeleccionada) {

                estado.textContent =
                    "Selecciona la mascota relacionada con la emergencia.";

                boton.disabled =
                    false;

                if (selectorMascota) {
                    selectorMascota.focus();
                }

                return;
            }

            // Construye el objeto de la emergencia.

            const emergencia = {

                tipo:
                    document.getElementById(
                        "tipoEmergenciaUsuario"
                    ).value,

                descripcion,

                usuario: {
                    idUsuario:
                        Number(idUsuario)
                },

                mascota: {
                    idMascota:
                        Number(idMascotaSeleccionada)
                }
            };

            try {

                // Envía la emergencia al backend.

                await solicitar(
                    "/emergencias/guardar",
                    {
                        method: "POST",
                        body:
                            JSON.stringify(
                                emergencia
                            )
                    }
                );

                // Limpia el formulario.

                formulario.reset();

                // Conserva la mascota seleccionada cuando se abrió desde su perfil.
                
                if (
                    idMascotaUrl &&
                    selectorMascota
                ) {

                    selectorMascota.value =
                        String(idMascotaUrl);
                }

                estado.textContent =
                    "Reporte guardado en tu cuenta.";

                await cargarEmergencias();

            } catch (error) {

                console.error(
                    "Error guardando el reporte:",
                    error
                );

                estado.textContent =
                    error.message ||
                    "No se pudo guardar el reporte.";

            } finally {

                boton.disabled =
                    false;
            }
        }
    );

    // Carga inicialmente las mascotas para llenar el selector.

    cargarMascotas();

    // Carga las emergencias existentes del usuario.

    cargarEmergencias();

});