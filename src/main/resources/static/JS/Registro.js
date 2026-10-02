document.addEventListener("DOMContentLoaded", function () {

    const formulario = document.getElementById("formRegistro");
    const mensajeRegistro = document.getElementById("mensajeRegistro");
    const botonRegistro = formulario?.querySelector("[type='submit']");

    if (!formulario) {
        console.error("No se encontró el formulario formRegistro.");
        return;
    }

    formulario.addEventListener("submit", async function (event) {

        event.preventDefault();

        const nombre = document.getElementById("nombre").value.trim();
        const apellidos = document.getElementById("apellidos").value.trim();
        const usuario = document.getElementById("usuario").value.trim();
        const correo = document.getElementById("correo").value.trim();
        const contrasena = document.getElementById("contrasena").value;

        if (!nombre || !apellidos || !usuario || !correo || !contrasena) {
            if (mensajeRegistro) mensajeRegistro.textContent = "Completa todos los campos.";
            return;
        }

        if (mensajeRegistro) mensajeRegistro.textContent = "";
        if (botonRegistro) botonRegistro.disabled = true;

        const datosUsuario = {
            nombre: nombre,
            apellidos: apellidos,
            usuario: usuario,
            correo: correo,
            contrasena: contrasena
        };

        try {

            const respuesta = await fetch(
                "/api/usuario/registro",
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify(datosUsuario)
                }
            );

            if (!respuesta.ok) {
                const mensaje = await respuesta.text();

                console.error("Error del servidor:", mensaje);

                if (mensajeRegistro) {
                    mensajeRegistro.textContent = mensaje || "No se pudo registrar el usuario.";
                }
                return;
            }

            const usuarioCreado = await respuesta.json();

            localStorage.setItem(
                "usuarioLogueado",
                JSON.stringify(usuarioCreado)
            );
            localStorage.setItem(
                "idUsuario",
                usuarioCreado.idUsuario
            );

            if (mensajeRegistro) mensajeRegistro.textContent = "Cuenta creada correctamente.";

            formulario.reset();

            window.location.href = "/Front%20end/Modulo_1_gestion_usuario/home.html";

        } catch (error) {

            console.error("Error de conexión:", error);

            if (mensajeRegistro) {
                mensajeRegistro.textContent = "No se pudo conectar con el servidor. Comprueba que My Pet esté iniciado.";
            }
        } finally {
            if (botonRegistro) botonRegistro.disabled = false;
        }

    });

});
