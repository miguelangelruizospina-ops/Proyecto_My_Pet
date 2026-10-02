document.addEventListener("DOMContentLoaded", function () {

    const boton = document.getElementById("btnIniciarSesion");

    boton.addEventListener("click", async function (e) {

        // Evita que el formulario recargue la página
        e.preventDefault();

        const correo =
            document.getElementById("correo").value.trim();

        const contrasena =
            document.getElementById("contrasena").value;


        // Validar campos
        if (correo === "" || contrasena === "") {

            alert("Por favor, ingresa el correo y la contraseña.");

            return;
        }


        try {

            const respuesta = await fetch(
                "http://localhost:8082/api/usuario/login",
                {
                    method: "POST",

                    headers: {
                        "Content-Type": "application/json"
                    },

                    body: JSON.stringify({
                        correo: correo,
                        contrasena: contrasena
                    })
                }
            );


            if (respuesta.ok) {

                const usuario = await respuesta.json();

                console.log(
                    "Usuario encontrado:",
                    usuario
                );


                // Guardar usuario completo
                localStorage.setItem(
                    "usuarioLogueado",
                    JSON.stringify(usuario)
                );


                // Guardar también el ID del usuario
                localStorage.setItem(
                    "idUsuario",
                    usuario.idUsuario
                );


                console.log(
                    "ID del usuario guardado:",
                    usuario.idUsuario
                );


                alert("Inicio de sesión exitoso");


                // Verificar el rol
                const rol = usuario.rol
                    ? usuario.rol.toString().trim().toUpperCase()
                    : "";


                if (rol === "ADMINISTRADOR") {

                    window.location.href = "/Front%20end/Modulo_1_gestion_usuario/admin.html";

                } else {

                    window.location.href = "/Front%20end/Modulo_1_gestion_usuario/home.html";

                }


            } else {

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

            console.error(
                "Error de conexión:",
                error
            );

            alert(
                "No se pudo conectar con el servidor."
            );

        }

    });

});