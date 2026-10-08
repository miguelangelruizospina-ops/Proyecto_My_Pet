// Gestiona los Me gusta de las publicaciones.

// Carga la cantidad y el estado del Me gusta de una publicación.

async function cargarMeGustaPublicacion(idPublicacion) {

    try {

        // Consulta la cantidad de Me gusta de la publicación.

        const respuestaCantidad = await fetch(
            `/api/me-gusta/publicaciones/${idPublicacion}/cantidad`
        );

        if (!respuestaCantidad.ok) {
            console.error(
                "No se pudo obtener la cantidad de Me gusta de la publicación."
            );
            return;
        }

        const datosCantidad =
            await respuestaCantidad.json();

        // Busca el botón correspondiente a la publicación.

        const boton =
            document.querySelector(
                `[data-boton-me-gusta-publicacion="${idPublicacion}"]`
            );

        if (!boton) {
            return;
        }

        // Busca el contador dentro del botón.

        const contador =
            boton.querySelector(
                "[data-me-gusta-publicacion]"
            );

        // Actualiza la cantidad de Me gusta.

        if (contador) {
            contador.textContent =
                datosCantidad.cantidad;
        }

        // Consulta si el usuario actual ya dio Me gusta.

        await actualizarEstadoMeGustaPublicacion(
            idPublicacion,
            boton
        );

    } catch (error) {

        console.error(
            "Error al cargar el Me gusta de la publicación:",
            error
        );
    }
}

// Consulta si el usuario actual dio Me gusta a la publicación.

async function actualizarEstadoMeGustaPublicacion(
    idPublicacion,
    boton
) {

    try {

        const respuesta = await fetch(
            `/api/me-gusta/publicaciones/${idPublicacion}/mi-me-gusta`
        );

        if (!respuesta.ok) {
            console.error(
                "No se pudo consultar el estado del Me gusta."
            );
            return;
        }

        const datos =
            await respuesta.json();

        // Actualiza el corazón según el estado del Me gusta.

        actualizarIconoMeGustaPublicacion(
            boton,
            datos.meGusta
        );

    } catch (error) {

        console.error(
            "Error al consultar el estado del Me gusta:",
            error
        );
    }
}

// Agrega o elimina el Me gusta de una publicación.

async function cambiarMeGustaPublicacion(
    idPublicacion
) {

    try {

        // Envía la solicitud para agregar o quitar el Me gusta.

        const respuesta = await fetch(
            `/api/me-gusta/publicaciones/${idPublicacion}`,
            {
                method: "POST"
            }
        );

        if (!respuesta.ok) {

            const mensaje =
                await respuesta.text();

            console.error(
                "No se pudo cambiar el Me gusta de la publicación:",
                mensaje
            );

            return;
        }

        // Obtiene el nuevo estado y la cantidad actualizada de Me gusta.

        const datos =
            await respuesta.json();

        // Busca el botón correspondiente a la publicación.

        const boton =
            document.querySelector(
                `[data-boton-me-gusta-publicacion="${idPublicacion}"]`
            );

        if (!boton) {
            return;
        }

        // Actualiza el contador de Me gusta.

        const contador =
            boton.querySelector(
                "[data-me-gusta-publicacion]"
            );

        if (contador) {
            contador.textContent =
                datos.cantidad;
        }

        // Actualiza el icono según el nuevo estado.

        actualizarIconoMeGustaPublicacion(
            boton,
            datos.meGusta
        );

    } catch (error) {

        console.error(
            "Error al cambiar el Me gusta de la publicación:",
            error
        );
    }
}

// Actualiza el icono del botón según el estado del Me gusta.

function actualizarIconoMeGustaPublicacion(
    boton,
    activo
) {

    const icono =
        boton.querySelector("i");

    if (!icono) {
        return;
    }

    // Muestra el corazón vacío cuando el Me gusta está inactivo.

    icono.classList.toggle(
        "bi-heart",
        !activo
    );

    // Muestra el corazón lleno cuando el Me gusta está activo.

    icono.classList.toggle(
        "bi-heart-fill",
        activo
    );
}