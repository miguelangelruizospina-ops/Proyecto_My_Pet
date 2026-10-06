document.addEventListener("DOMContentLoaded", function () {

// ============================================================
// REFERENCIA AL BOTÓN DE INICIO DE SESIÓN
// ============================================================

const boton =
    document.getElementById("btnIniciarSesion");


// ============================================================
// OBTENER EL TOKEN CSRF DESDE LA COOKIE
// ============================================================

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


// ============================================================
// SOLICITAR EL TOKEN CSRF AL SERVIDOR
// ============================================================

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


// ============================================================
// INICIO DE SESIÓN
// ============================================================

boton.addEventListener(
    "click",
    async function (e) {

        // Evita que el formulario recargue la página.
        e.preventDefault();


        // ====================================================
        // OBTENER LOS DATOS DEL FORMULARIO
        // ====================================================

        const correo =
            document
                .getElementById("correo")
                .value
                .trim();

        const contrasena =
            document
                .getElementById("contrasena")
                .value;


        // ====================================================
        // VALIDAR LOS CAMPOS
        // ====================================================

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

            // =================================================
            // OBTENER EL TOKEN CSRF
            // =================================================

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


            // =================================================
            // ENVIAR LAS CREDENCIALES AL BACKEND
            // =================================================

            const respuesta =
                await fetch(
                    "/api/usuario/login",
                    {
                        method: "POST",

                        // Mantiene la cookie de sesión
                        // utilizada por Spring Security.
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


            // =================================================
            // LOGIN EXITOSO
            // =================================================

            if (respuesta.ok) {

                const usuario =
                    await respuesta.json();


                console.log(
                    "Usuario encontrado:",
                    usuario
                );


                // =================================================
                // GUARDAR INFORMACIÓN DEL USUARIO EN EL NAVEGADOR
                // =================================================

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


                // =================================================
                // IDENTIFICAR EL ROL DEL USUARIO
                // =================================================

                const rol =
                    usuario.rol
                        ? usuario.rol
                            .toString()
                            .trim()
                            .toUpperCase()
                        : "";


                // =================================================
                // REDIRECCIONAR SEGÚN EL ROL
                // =================================================

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

                // =================================================
                // LOGIN INCORRECTO
                // =================================================

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

            // ====================================================
            // ERROR DE CONEXIÓN O DEL SERVIDOR
            // ====================================================

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
