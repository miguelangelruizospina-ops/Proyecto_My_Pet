// =========================================================
// ME GUSTA DE PUBLICACIONES
// =========================================================


// =========================================================
// CARGAR ME GUSTA DE UNA PUBLICACIÓN
// =========================================================

async function cargarMeGustaPublicacion(idPublicacion) {

    try {

        // Consulta la cantidad de Me gusta de la publicación
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


        // Busca el botón correspondiente a la publicación
        const boton =
            document.querySelector(
                `[data-boton-me-gusta-publicacion="${idPublicacion}"]`
            );

        if (!boton) {
            return;
        }


        // Busca el contador dentro del botón
        const contador =
            boton.querySelector(
                "[data-me-gusta-publicacion]"
            );

        // Actualiza la cantidad
        if (contador) {
            contador.textContent =
                datosCantidad.cantidad;
        }


        // Consulta si el usuario actual
        // ya dio Me gusta
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


// =========================================================
// VERIFICAR ESTADO DEL ME GUSTA
// =========================================================

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


        // Actualiza el corazón según el estado
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


// =========================================================
// CAMBIAR ME GUSTA
// =========================================================

async function cambiarMeGustaPublicacion(
    idPublicacion
) {

    try {

        // Envía la solicitud para agregar
        // o quitar el Me gusta
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


        // Recibe:
        //
        // {
        //     meGusta: true/false,
        //     cantidad: número
        // }
        const datos =
            await respuesta.json();


        // Busca el botón de la publicación
        const boton =
            document.querySelector(
                `[data-boton-me-gusta-publicacion="${idPublicacion}"]`
            );

        if (!boton) {
            return;
        }


        // Actualiza el contador
        const contador =
            boton.querySelector(
                "[data-me-gusta-publicacion]"
            );

        if (contador) {
            contador.textContent =
                datos.cantidad;
        }


        // Actualiza el corazón
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


// =========================================================
// ACTUALIZAR ICONO DEL CORAZÓN
// =========================================================

function actualizarIconoMeGustaPublicacion(
    boton,
    activo
) {

    const icono =
        boton.querySelector("i");

    if (!icono) {
        return;
    }


    // Corazón vacío cuando NO hay Me gusta
    icono.classList.toggle(
        "bi-heart",
        !activo
    );


    // Corazón lleno cuando SÍ hay Me gusta
    icono.classList.toggle(
        "bi-heart-fill",
        activo
    );
}