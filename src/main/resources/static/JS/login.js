// Inicializa las funciones de inicio de sesión cuando la página termina de cargar.

document.addEventListener("DOMContentLoaded", function () {

    // Obtiene el botón utilizado para iniciar sesión.

    const boton =
        document.getElementById("btnIniciarSesion");

    // Obtiene el token CSRF almacenado en la cookie del navegador.

    function obtenerTokenCsrf() {

        const nombreCookie = "XSRF-TOKEN";

        const cookies =
            document.cookie.split(";");

        for (const cookie of cookies) {

            const parte = cookie.trim();

            if (parte.startsWith(nombreCookie + "=")) {

                return decodeURIComponent(
                    parte.substring(
                        nombreCookie.length + 1
                    )
                );
            }
        }

        return null;
    }


    // Solicita al servidor un nuevo token CSRF cuando no existe uno disponible.

    async function obtenerCsrf() {

        const respuesta =
            await fetch(
                "/api/usuario/csrf",
                {
                    method: "GET",

                    // Permite conservar las cookies de sesión.
                    credentials: "include"
                }
            );

        if (!respuesta.ok) {

            throw new Error(
                "No se pudo obtener el token de seguridad."
            );
        }

        return obtenerTokenCsrf();
    }

    // Gestiona el proceso de inicio de sesión del usuario.

    boton.addEventListener(
        "click",
        async function (e) {

            // Evita que el formulario recargue la página.
            e.preventDefault();

            // Obtiene los datos ingresados en el formulario.

            const correo =
                document
                    .getElementById("correo")
                    .value
                    .trim();

            const contrasena =
                document
                    .getElementById("contrasena")
                    .value;

            // Verifica que el correo y la contraseña hayan sido ingresados.

            if (
                correo === "" ||
                contrasena === ""
            ) {

                alert(
                    "Por favor, ingresa el correo y la contraseña."
                );

                return;
            }

            try {

                // Obtiene el token CSRF necesario para realizar el inicio de sesión.

                let tokenCsrf =
                    obtenerTokenCsrf();

                if (!tokenCsrf) {

                    tokenCsrf =
                        await obtenerCsrf();
                }

                if (!tokenCsrf) {

                    throw new Error(
                        "No se pudo obtener el token CSRF."
                    );
                }


                // Envía las credenciales del usuario al backend.

                const respuesta =
                    await fetch(
                        "/api/usuario/login",
                        {
                            method: "POST",

                            // Mantiene la cookie de sesión utilizada por Spring Security.
                            credentials: "include",

                            headers: {
                                "Content-Type":
                                    "application/json",

                                "X-XSRF-TOKEN":
                                    tokenCsrf
                            },

                            body: JSON.stringify({

                                correo:
                                    correo,

                                contrasena:
                                    contrasena
                            })
                        }
                    );


                // Procesa la respuesta cuando el inicio de sesión es exitoso.

                if (respuesta.ok) {

                    const usuario =
                        await respuesta.json();


                    console.log(
                        "Usuario encontrado:",
                        usuario
                    );


                    // Guarda la información del usuario en el almacenamiento local.

                    localStorage.setItem(
                        "usuarioLogueado",
                        JSON.stringify(usuario)
                    );

                    localStorage.setItem(
                        "idUsuario",
                        usuario.idUsuario
                    );


                    console.log(
                        "ID del usuario guardado:",
                        usuario.idUsuario
                    );


                    alert(
                        "Inicio de sesión exitoso"
                    );


                    // Obtiene y normaliza el rol del usuario.

                    const rol =
                        usuario.rol
                            ? usuario.rol
                                .toString()
                                .trim()
                                .toUpperCase()
                            : "";


                    // Redirige al usuario según el rol registrado.

                    if (
                        rol === "ADMINISTRADOR"
                    ) {

                        window.location.href =
                            "/Front%20end/Modulo_1_gestion_usuario/admin.html";

                    } else {

                        window.location.href =
                            "/Front%20end/Modulo_1_gestion_usuario/home.html";
                    }


                } else {

                    // Obtiene el mensaje enviado por el servidor cuando las credenciales son incorrectas.

                    const mensaje =
                        await respuesta.text();

                    alert(
                        "Correo o contraseña incorrectos."
                    );

                    console.error(
                        "Error:",
                        mensaje
                    );
                }


            } catch (error) {

                // Muestra el error cuando ocurre un problema de conexión o del servidor.

                console.error(
                    "Error de conexión:",
                    error
                );

                alert(
                    error.message ||
                    "No se pudo conectar con el servidor."
                );
            }
        }
    );

});