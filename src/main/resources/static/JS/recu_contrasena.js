// Inicializa el cambio de contraseña cuando la página termina de cargar.

document.addEventListener("DOMContentLoaded", function () {

    const correo =
        document.getElementById("correoRecuperacion");

    const nuevaContrasena =
        document.getElementById("nuevaContrasena");

    const contrasenaActual =
        document.getElementById("contrasenaActual");

    const confirmarContrasena =
        document.getElementById("confirmarContrasena");

    const captchaPregunta =
        document.getElementById("captchaPregunta");

    const captchaRespuesta =
        document.getElementById("captchaRespuesta");

    const boton =
        document.getElementById("btnCambiarContrasena");

    const mensaje =
        document.getElementById("mensajeRecuperacion");

    // Genera una pregunta de seguridad para validar la solicitud.

    const numero1 = Math.floor(Math.random() * 10) + 1;
    const numero2 = Math.floor(Math.random() * 10) + 1;

    const resultadoCaptcha = numero1 + numero2;

    captchaPregunta.textContent =
        "¿Cuánto es " + numero1 + " + " + numero2 + "?";

    // Gestiona el proceso para cambiar la contraseña.

    boton.addEventListener("click", async function () {

        mensaje.textContent = "";

        // Verifica que se haya ingresado el correo.

        if (correo.value.trim() === "") {

            mensaje.textContent =
                "Por favor, ingresa tu correo.";

            return;
        }

        // Verifica que se haya ingresado la contraseña actual.

        if (contrasenaActual.value === "") {
            mensaje.textContent = "Ingresa tu contraseña actual.";
            return;
        }

        // Verifica que se haya ingresado una nueva contraseña.

        if (nuevaContrasena.value.trim() === "") {

            mensaje.textContent =
                "Por favor, ingresa una nueva contraseña.";

            return;
        }

        // Verifica que se haya confirmado la nueva contraseña.

        if (confirmarContrasena.value.trim() === "") {

            mensaje.textContent =
                "Por favor, confirma la contraseña.";

            return;
        }

        // Comprueba que las contraseñas coincidan.

        if (
            nuevaContrasena.value !==
            confirmarContrasena.value
        ) {

            mensaje.textContent =
                "Las contraseñas no coinciden.";

            return;
        }

        // Verifica que se haya respondido la pregunta de seguridad.

        if (captchaRespuesta.value.trim() === "") {

            mensaje.textContent =
                "Por favor, responde la verificación de seguridad.";

            return;
        }

        // Comprueba que la respuesta de seguridad sea correcta.

        if (
            parseInt(captchaRespuesta.value) !==
            resultadoCaptcha
        ) {

            mensaje.textContent =
                "La respuesta de seguridad es incorrecta.";

            return;
        }

        try {

            // Envía al backend los datos necesarios para cambiar la contraseña.

            const respuesta = await fetch(
                "http://localhost:8082/api/usuario/cambiar-contrasena",
                {
                    method: "PUT",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({
                        correo: correo.value.trim(),
                        contrasenaActual: contrasenaActual.value,
                        nuevaContrasena: nuevaContrasena.value
                    })
                }
            );

            if (!respuesta.ok) {
                mensaje.textContent = await respuesta.text() || "No se pudo cambiar la contraseña.";
                return;
            }

            // Informa que el cambio de contraseña se realizó correctamente.

            mensaje.textContent = "La contraseña se cambió correctamente.";
            nuevaContrasena.value = "";
            confirmarContrasena.value = "";
            contrasenaActual.value = "";

        } catch (error) {

            console.error("Error cambiando la contraseña:", error);
            mensaje.textContent = "No se pudo conectar con el servidor.";
        }

    });

});