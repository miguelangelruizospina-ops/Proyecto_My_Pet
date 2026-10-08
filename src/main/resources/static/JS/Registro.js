// Inicializa el registro de usuario cuando la página termina de cargar.

document.addEventListener("DOMContentLoaded", function () {

    const formulario = document.getElementById("formRegistro");
    const mensajeRegistro = document.getElementById("mensajeRegistro");
    const botonRegistro = formulario?.querySelector("[type='submit']");

    // Verifica que exista el formulario de registro.

    if (!formulario) {
        console.error("No se encontró el formulario formRegistro.");
        return;
    }

    // Gestiona el envío del formulario para registrar un nuevo usuario.

    formulario.addEventListener("submit", async function (event) {

        event.preventDefault();

        // Obtiene y limpia los datos ingresados en el formulario.

        const nombre = document.getElementById("nombre").value.trim();
        const apellidos = document.getElementById("apellidos").value.trim();
        const usuario = document.getElementById("usuario").value.trim();
        const correo = document.getElementById("correo").value.trim();
        const contrasena = document.getElementById("contrasena").value;

        // Verifica que todos los campos obligatorios tengan información.

        if (!nombre || !apellidos || !usuario || !correo || !contrasena) {
            if (mensajeRegistro) mensajeRegistro.textContent = "Completa todos los campos.";
            return;
        }

        // Limpia el mensaje anterior y desactiva el botón durante el registro.

        if (mensajeRegistro) mensajeRegistro.textContent = "";
        if (botonRegistro) botonRegistro.disabled = true;

        // Construye los datos que serán enviados al backend.

        const datosUsuario = {
            nombre: nombre,
            apellidos: apellidos,
            usuario: usuario,
            correo: correo,
            contrasena: contrasena
        };

        try {

            // Envía los datos del nuevo usuario al backend.

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

            // Obtiene la información del usuario creado.

            const usuarioCreado = await respuesta.json();

            // Guarda la información del usuario en el almacenamiento local.

            localStorage.setItem(
                "usuarioLogueado",
                JSON.stringify(usuarioCreado)
            );
            localStorage.setItem(
                "idUsuario",
                usuarioCreado.idUsuario
            );

            // Informa que el registro se realizó correctamente.

            if (mensajeRegistro) mensajeRegistro.textContent = "Cuenta creada correctamente.";

            formulario.reset();

            // Redirige al usuario a la página principal de My Pet.

            window.location.href = "/Front%20end/Modulo_1_gestion_usuario/home.html";

        } catch (error) {

            // Muestra el error cuando ocurre un problema de conexión.

            console.error("Error de conexión:", error);

            if (mensajeRegistro) {
                mensajeRegistro.textContent = "No se pudo conectar con el servidor. Comprueba que My Pet esté iniciado.";
            }

        } finally {

            // Reactiva el botón después de finalizar el proceso de registro.

            if (botonRegistro) botonRegistro.disabled = false;
        }

    });

});