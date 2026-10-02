document.addEventListener("DOMContentLoaded", function () {

    console.log("USUARIO.JS CARGADO");

    // ==========================================
    // SESIÓN
    // ==========================================

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


    // ==========================================
    // ID DEL USUARIO
    // ==========================================

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


    // ==========================================
    // ELEMENTOS DEL HTML
    // ==========================================

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


    // ==========================================
    // BOTONES
    // ==========================================

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


    // ==========================================
    // VARIABLES DEL PERFIL
    // ==========================================

    let perfil = null;
    let perfilExiste = false;
    let fotoPerfilNueva = null;

    const fotoPerfilPredeterminada =
        "/Front%20end/fotos/ana.jpg";


    // ==========================================
    // ACTUALIZAR BOTONES
    // ==========================================

    function actualizarBotonesPerfil() {

        if (editarInformacion) {
            editarInformacion.hidden = !perfilExiste;
        }

        if (agregarInformacion) {
            agregarInformacion.hidden = perfilExiste;
        }
    }


    // ==========================================
    // ABRIR FORMULARIO
    // ==========================================

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


        // Cambiar título

        if (tituloFormularioPerfil) {

            tituloFormularioPerfil.textContent =
                esNuevoPerfil
                    ? "Agregar información del perfil"
                    : "Editar información del perfil";
        }


        // Ocultar información

        informacionUsuario.style.display = "none";


        // Mostrar formulario

        formularioEdicion.style.display = "block";


        // Ocultar botones

        accionesUsuario.style.display = "none";


        console.log("FORMULARIO DE EDICIÓN ABIERTO");
    }


    // ==========================================
    // BOTÓN EDITAR INFORMACIÓN
    // ==========================================

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


    // ==========================================
    // BOTÓN AGREGAR INFORMACIÓN
    // ==========================================

    if (agregarInformacion) {

        agregarInformacion.addEventListener(
            "click",
            function (event) {

                event.preventDefault();

                abrirFormularioEdicion(true);
            }
        );
    }


    // ==========================================
    // BOTÓN CANCELAR
    // ==========================================

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


    // ==========================================
    // OBTENER RUTA DE FOTO
    // ==========================================

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


    // ==========================================
    // CARGAR PERFIL
    // ==========================================

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


            // ==========================================
            // NO EXISTE PERFIL
            // ==========================================

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


            // ==========================================
            // ERROR DEL SERVIDOR
            // ==========================================

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


            // ==========================================
            // PERFIL EXISTE
            // ==========================================

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


            // ==========================================
            // NOMBRE SUPERIOR
            // ==========================================

            document.getElementById(
                "usu-cen-h1"
            ).textContent =
                perfil.nombreUsuario ||
                usuario.nombre ||
                "Tu usuario";


            // ==========================================
            // INFORMACIÓN DEL USUARIO
            // ==========================================

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


            // ==========================================
            // INFORMACIÓN DEL PERFIL
            // ==========================================

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


            // ==========================================
            // CARGAR DATOS EN EL FORMULARIO
            // ==========================================

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


            // ==========================================
            // FOTO
            // ==========================================

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


    // ==========================================
    // GUARDAR CAMBIOS
    // ==========================================

    if (formulario) {

        formulario.addEventListener(
            "submit",
            async function (event) {

                event.preventDefault();


                // ==========================================
                // DATOS DEL FORMULARIO
                // ==========================================

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


                    // ==========================================
                    // ACTUALIZAR USUARIO
                    // ==========================================

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


                    // ==========================================
                    // DATOS DEL PERFIL
                    // ==========================================

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


                    // ==========================================
                    // GUARDAR PERFIL
                    // ==========================================

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


                    // ==========================================
                    // ACTUALIZAR LOCAL STORAGE
                    // ==========================================

                    localStorage.setItem(
                        "usuarioLogueado",
                        JSON.stringify(usuario)
                    );

                    localStorage.setItem(
                        "idUsuario",
                        idUsuario
                    );


                    // ==========================================
                    // CERRAR FORMULARIO
                    // ==========================================

                    formularioEdicion.style.display =
                        "none";

                    informacionUsuario.style.display =
                        "block";

                    accionesUsuario.style.display =
                        "flex";


                    alert(
                        "Información actualizada correctamente."
                    );


                    // ==========================================
                    // RECARGAR DATOS
                    // ==========================================

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


    // ==========================================
    // CERRAR SESIÓN
    // ==========================================

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


    // ==========================================
    // CAMBIAR FOTO
    // ==========================================

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


    // ==========================================
    // CARGAR PERFIL AL ABRIR LA PÁGINA
    // ==========================================

    cargarPerfil();

});