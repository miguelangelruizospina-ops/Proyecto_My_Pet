// Gestiona la información, edición, fotografía y sesión del usuario.

// Inicializa la gestión del usuario cuando la página termina de cargar.

document.addEventListener("DOMContentLoaded", function () {

    console.log("USUARIO.JS CARGADO");

    // Obtiene la información del usuario almacenada en la sesión local.

    const usuarioGuardado = localStorage.getItem("usuarioLogueado");

    if (!usuarioGuardado) {
        alert("No hay una sesión iniciada.");
        window.location.href = "iniciar_sesion.html";
        return;
    }

    let usuario;

    try {
        usuario = JSON.parse(usuarioGuardado);
    } catch (error) {

        console.error("Error leyendo la sesión:", error);

        localStorage.removeItem("usuarioLogueado");
        localStorage.removeItem("idUsuario");

        window.location.href = "iniciar_sesion.html";
        return;
    }

    // Obtiene el ID del usuario que inició sesión.

    const idUsuario =
        usuario.idUsuario ||
        usuario.id_usuario ||
        localStorage.getItem("idUsuario");

    console.log("Usuario de la sesión:", usuario);
    console.log("ID del usuario:", idUsuario);

    if (!idUsuario) {

        console.error("No se encontró el ID del usuario.");

        alert("No se pudo identificar al usuario.");

        return;
    }

    // Obtiene los elementos principales de la interfaz del usuario.

    const informacionUsuario =
        document.getElementById("informacionUsuario");

    const formularioEdicion =
        document.getElementById("formularioEdicion");

    const accionesUsuario =
        document.getElementById("accionesUsuario");

    const formulario =
        document.getElementById("formEditarPerfil");

    const cerrarSesion =
        document.getElementById("cerrarSesion");

    const inputFoto =
        document.getElementById("input-foto-perfil");

    const fotoPerfil =
        document.getElementById("fotoPerfil");

    // Obtiene los botones y controles utilizados para editar el perfil.

    const editarInformacion =
        document.getElementById("editarInformacion");

    const agregarInformacion =
        document.getElementById("agregarInformacion");

    const tituloFormularioPerfil =
        document.getElementById("tituloFormularioPerfil");

    const cancelarEdicion =
        document.getElementById("cancelarEdicion");

    console.log(
        "Botón editar encontrado:",
        editarInformacion
    );

    console.log(
        "Formulario encontrado:",
        formularioEdicion
    );

    // Define las variables utilizadas para controlar el perfil y su fotografía.

    let perfil = null;
    let perfilExiste = false;
    let fotoPerfilNueva = null;

    const fotoPerfilPredeterminada =
        "/Front%20end/fotos/ana.jpg";

    // Actualiza la visibilidad de los botones según exista información del perfil.

    function actualizarBotonesPerfil() {

        if (editarInformacion) {
            editarInformacion.hidden = !perfilExiste;
        }

        if (agregarInformacion) {
            agregarInformacion.hidden = perfilExiste;
        }
    }

    // Abre el formulario para crear o editar la información del perfil.

    function abrirFormularioEdicion(esNuevoPerfil = false) {

        console.log("SE HIZO CLIC EN EDITAR");

        if (!informacionUsuario ||
            !formularioEdicion ||
            !accionesUsuario) {

            console.error(
                "No se encontraron los elementos necesarios."
            );

            return;
        }

        // Actualiza el título según se cree o edite el perfil.

        if (tituloFormularioPerfil) {

            tituloFormularioPerfil.textContent =
                esNuevoPerfil
                    ? "Agregar información del perfil"
                    : "Editar información del perfil";
        }

        // Oculta la información actual del usuario.

        informacionUsuario.style.display = "none";

        // Muestra el formulario de edición.

        formularioEdicion.style.display = "block";

        // Oculta los botones de acciones del perfil.

        accionesUsuario.style.display = "none";

        console.log("FORMULARIO DE EDICIÓN ABIERTO");
    }

    // Configura el botón para editar la información existente del perfil.

    if (editarInformacion) {

        editarInformacion.addEventListener(
            "click",
            function (event) {

                event.preventDefault();

                abrirFormularioEdicion(false);
            }
        );

    } else {

        console.error(
            "NO SE ENCONTRÓ #editarInformacion"
        );
    }

    // Configura el botón para agregar información cuando no existe un perfil.

    if (agregarInformacion) {

        agregarInformacion.addEventListener(
            "click",
            function (event) {

                event.preventDefault();

                abrirFormularioEdicion(true);
            }
        );
    }

    // Configura el botón para cancelar la edición del perfil.

    if (cancelarEdicion) {

        cancelarEdicion.addEventListener(
            "click",
            function (event) {

                event.preventDefault();

                formularioEdicion.style.display = "none";

                informacionUsuario.style.display = "block";

                accionesUsuario.style.display = "flex";
            }
        );
    }

    // Obtiene la ruta correspondiente a la fotografía del perfil.

    function obtenerRutaFotoPerfil(rutaFoto) {

        if (
            !rutaFoto ||
            !rutaFoto.trim() ||
            rutaFoto === "foto.jpg"
        ) {
            return fotoPerfilPredeterminada;
        }

        if (
            rutaFoto.startsWith("data:") ||
            rutaFoto.startsWith("/") ||
            rutaFoto.startsWith("http://") ||
            rutaFoto.startsWith("https://")
        ) {
            return rutaFoto;
        }

        return `../fotos/${encodeURIComponent(rutaFoto)}`;
    }

    // Carga la información del perfil desde el backend.

    async function cargarPerfil() {

        try {

            console.log(
                "Consultando perfil del usuario:",
                idUsuario
            );

            const respuesta =
                await fetch(`/api/perfiles/${idUsuario}`);

            console.log(
                "Respuesta del servidor:",
                respuesta.status
            );

            // Crea una estructura vacía cuando el usuario todavía no tiene perfil.

            if (respuesta.status === 404) {

                perfilExiste = false;

                perfil = {

                    nombreUsuario:
                        usuario.nombre || "",

                    fotoPerfil: "",

                    biografia: "",

                    telefono: "",

                    ciudad: "",

                    genero: "",

                    fechaNacimiento: ""
                };
            }

            // Muestra el error cuando el servidor no puede entregar el perfil.

            else if (!respuesta.ok) {

                const mensaje =
                    await respuesta.text();

                console.error(
                    "Error obteniendo el perfil:",
                    respuesta.status,
                    mensaje
                );

                alert(
                    "No se pudo cargar el perfil del usuario."
                );

                return;
            }

            // Obtiene el perfil existente desde el backend.

            else {

                perfilExiste = true;

                perfil =
                    await respuesta.json();
            }

            actualizarBotonesPerfil();

            console.log(
                "Perfil obtenido:",
                perfil
            );

            // Muestra el nombre del usuario en el encabezado.

            document.getElementById(
                "usu-cen-h1"
            ).textContent =
                perfil.nombreUsuario ||
                usuario.nombre ||
                "Tu usuario";

            // Muestra la información general de la cuenta.

            document.getElementById(
                "nombreUsuario"
            ).textContent =
                "Nombre: " +
                (
                    perfil.nombreUsuario ||
                    usuario.nombre ||
                    "-"
                );

            document.getElementById(
                "apellidosUsuario"
            ).textContent =
                "Apellidos: " +
                (
                    usuario.apellidos ||
                    "-"
                );

            document.getElementById(
                "correoUsuario"
            ).textContent =
                "Correo: " +
                (
                    usuario.correo ||
                    "-"
                );

            document.getElementById(
                "usuarioUsuario"
            ).textContent =
                "Usuario: " +
                (
                    usuario.usuario ||
                    "-"
                );

            document.getElementById(
                "rolUsuario"
            ).textContent =
                "Rol: " +
                (
                    usuario.rol ||
                    "-"
                );

            document.getElementById(
                "fechaUsuario"
            ).textContent =
                "Fecha de creación: " +
                (
                    usuario.fechaCreacion ||
                    "-"
                );

            // Muestra la información adicional registrada en el perfil.

            document.getElementById(
                "biografiaUsuario"
            ).textContent =
                "Biografía: " +
                (
                    perfil.biografia ||
                    "-"
                );

            document.getElementById(
                "telefonoUsuario"
            ).textContent =
                "Teléfono: " +
                (
                    perfil.telefono ||
                    "-"
                );

            document.getElementById(
                "ciudadUsuario"
            ).textContent =
                "Ciudad: " +
                (
                    perfil.ciudad ||
                    "-"
                );

            document.getElementById(
                "generoUsuario"
            ).textContent =
                "Género: " +
                (
                    perfil.genero ||
                    "-"
                );

            document.getElementById(
                "fechaNacimientoUsuario"
            ).textContent =
                "Fecha de nacimiento: " +
                (
                    perfil.fechaNacimiento ||
                    "-"
                );

            // Carga los datos actuales del usuario y del perfil en el formulario.

            document.getElementById(
                "editarNombre"
            ).value =
                perfil.nombreUsuario ||
                usuario.nombre ||
                "";

            document.getElementById(
                "editarApellidos"
            ).value =
                usuario.apellidos ||
                "";

            document.getElementById(
                "editarUsuario"
            ).value =
                usuario.usuario ||
                "";

            document.getElementById(
                "editarCorreo"
            ).value =
                usuario.correo ||
                "";

            document.getElementById(
                "editarBiografia"
            ).value =
                perfil.biografia ||
                "";

            document.getElementById(
                "editarTelefono"
            ).value =
                perfil.telefono ||
                "";

            document.getElementById(
                "editarCiudad"
            ).value =
                perfil.ciudad ||
                "";

            document.getElementById(
                "editarGenero"
            ).value =
                perfil.genero ||
                "";

            document.getElementById(
                "editarFechaNacimiento"
            ).value =
                perfil.fechaNacimiento ||
                "";

            // Muestra la fotografía actual del perfil.

            if (fotoPerfil) {

                fotoPerfil.src =
                    obtenerRutaFotoPerfil(
                        perfil.fotoPerfil
                    );
            }

            console.log(
                "Perfil cargado correctamente."
            );

        } catch (error) {

            console.error(
                "Error cargando perfil:",
                error
            );

            alert(
                "No se pudo conectar con el servidor."
            );
        }
    }

    // Gestiona el envío del formulario para guardar los cambios del perfil.

    if (formulario) {

        formulario.addEventListener(
            "submit",
            async function (event) {

                event.preventDefault();

                // Obtiene y limpia los datos ingresados en el formulario.

                const nuevoNombre =
                    document.getElementById(
                        "editarNombre"
                    ).value.trim();

                const nuevosApellidos =
                    document.getElementById(
                        "editarApellidos"
                    ).value.trim();

                const nuevoUsuario =
                    document.getElementById(
                        "editarUsuario"
                    ).value.trim();

                const nuevoCorreo =
                    document.getElementById(
                        "editarCorreo"
                    ).value.trim();

                const nuevaBiografia =
                    document.getElementById(
                        "editarBiografia"
                    ).value.trim();

                const nuevoTelefono =
                    document.getElementById(
                        "editarTelefono"
                    ).value.trim();

                const nuevaCiudad =
                    document.getElementById(
                        "editarCiudad"
                    ).value.trim();

                const nuevoGenero =
                    document.getElementById(
                        "editarGenero"
                    ).value.trim();

                const nuevaFechaNacimiento =
                    document.getElementById(
                        "editarFechaNacimiento"
                    ).value;

                try {

                    if (!perfil) {

                        throw new Error(
                            "No se pudo cargar el perfil."
                        );
                    }

                    // Actualiza los datos principales del usuario.

                    const respuestaUsuario =
                        await fetch(
                            `/api/usuario/actualizar/${idUsuario}`,
                            {
                                method: "PUT",

                                headers: {
                                    "Content-Type":
                                        "application/json"
                                },

                                body: JSON.stringify({

                                    nombre:
                                        nuevoNombre,

                                    apellidos:
                                        nuevosApellidos,

                                    usuario:
                                        nuevoUsuario,

                                    correo:
                                        nuevoCorreo,

                                    contrasena:
                                        usuario.contrasena,

                                    rol:
                                        usuario.rol,

                                    fechaCreacion:
                                        usuario.fechaCreacion
                                })
                            }
                        );

                    if (!respuestaUsuario.ok) {

                        const mensaje =
                            await respuestaUsuario.text();

                        throw new Error(
                            mensaje ||
                            "No se pudo actualizar el usuario."
                        );
                    }

                    usuario =
                        await respuestaUsuario.json();

                    // Construye los datos actualizados del perfil.

                    const datosPerfil = {

                        idUsuario:
                            Number(idUsuario),

                        usuario: {
                            idUsuario:
                                Number(idUsuario)
                        },

                        nombreUsuario:
                            nuevoNombre,

                        fotoPerfil:
                            fotoPerfilNueva ||
                            obtenerRutaFotoPerfil(
                                perfil.fotoPerfil
                            ),

                        biografia:
                            nuevaBiografia,

                        telefono:
                            nuevoTelefono,

                        ciudad:
                            nuevaCiudad,

                        genero:
                            nuevoGenero,

                        fechaNacimiento:
                            nuevaFechaNacimiento ||
                            null
                    };

                    // Guarda o actualiza la información del perfil en el backend.

                    const respuestaPerfil =
                        await fetch(
                            perfilExiste
                                ? `/api/perfiles/actualizar/${idUsuario}`
                                : "/api/perfiles/crear",
                            {
                                method:
                                    perfilExiste
                                        ? "PUT"
                                        : "POST",

                                headers: {
                                    "Content-Type":
                                        "application/json"
                                },

                                body:
                                    JSON.stringify(
                                        datosPerfil
                                    )
                            }
                        );

                    if (!respuestaPerfil.ok) {

                        const mensaje =
                            await respuestaPerfil.text();

                        throw new Error(
                            mensaje ||
                            "No se pudo actualizar el perfil."
                        );
                    }

                    perfil =
                        await respuestaPerfil.json();

                    perfilExiste = true;

                    fotoPerfilNueva = null;

                    actualizarBotonesPerfil();

                    // Actualiza la información almacenada en el navegador.

                    localStorage.setItem(
                        "usuarioLogueado",
                        JSON.stringify(usuario)
                    );

                    localStorage.setItem(
                        "idUsuario",
                        idUsuario
                    );

                    // Cierra el formulario y vuelve a mostrar la información del perfil.

                    formularioEdicion.style.display =
                        "none";

                    informacionUsuario.style.display =
                        "block";

                    accionesUsuario.style.display =
                        "flex";

                    alert(
                        "Información actualizada correctamente."
                    );

                    // Recarga los datos actualizados del perfil.

                    await cargarPerfil();

                } catch (error) {

                    console.error(
                        "Error actualizando:",
                        error
                    );

                    alert(
                        error.message ||
                        "No se pudo actualizar la información."
                    );
                }
            }
        );
    }

    // Gestiona el cierre de sesión del usuario.

    if (cerrarSesion) {

        cerrarSesion.addEventListener(
            "click",
            async function (event) {

                event.preventDefault();

                try {

                    await fetch(
                        "http://localhost:8082/api/usuario/logout",
                        {
                            method: "POST"
                        }
                    );

                } catch (error) {

                    console.error(
                        "No se pudo cerrar la sesión:",
                        error
                    );
                }

                localStorage.removeItem(
                    "usuarioLogueado"
                );

                localStorage.removeItem(
                    "idUsuario"
                );

                window.location.href =
                    "iniciar_sesion.html";
            }
        );
    }

    // Gestiona la selección y validación de una nueva fotografía de perfil.

    if (inputFoto && fotoPerfil) {

        inputFoto.addEventListener(
            "change",
            function () {

                const archivo =
                    inputFoto.files[0];

                if (!archivo) {
                    return;
                }

                if (
                    ![
                        "image/jpeg",
                        "image/png",
                        "image/webp",
                        "image/gif"
                    ].includes(archivo.type)
                ) {

                    alert(
                        "Selecciona una imagen JPG, PNG, WebP o GIF."
                    );

                    inputFoto.value = "";

                    return;
                }

                if (archivo.size > 2 * 1024 * 1024) {

                    alert(
                        "La imagen no puede superar 2 MB."
                    );

                    inputFoto.value = "";

                    return;
                }

                const lector =
                    new FileReader();

                // Muestra la nueva fotografía y la conserva para guardarla en el perfil.

                lector.onload =
                    function (event) {

                        fotoPerfil.src =
                            event.target.result;

                        fotoPerfilNueva =
                            event.target.result;
                    };

                lector.readAsDataURL(
                    archivo
                );
            }
        );
    }

    // Carga el perfil automáticamente al abrir la página.

    cargarPerfil();

});