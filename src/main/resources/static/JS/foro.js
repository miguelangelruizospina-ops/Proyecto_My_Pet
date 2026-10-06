// ==========================================
// FORO - MI PET
// Publicaciones, comentarios, respuestas
// Me gusta, editar y eliminar
// ==========================================


// ==========================================
// INICIALIZACIÓN
// ==========================================

document.addEventListener("DOMContentLoaded", () => {

    cargarPublicaciones();

    const formulario =
        document.getElementById(
            "formulario-nueva-publicacion"
        );

    if (formulario) {

        formulario.addEventListener(
            "submit",
            async (evento) => {

                evento.preventDefault();

                await crearPublicacion();
            }
        );
    }
});


// ==========================================
// PUBLICACIONES
// ==========================================

async function cargarPublicaciones() {

    const contenedor =
        document.querySelector(".contenedor_foro");

    if (!contenedor) {
        return;
    }

    try {

        const respuesta =
            await fetch(
                "/api/publicaciones-foro/listar"
            );

        if (!respuesta.ok) {

            throw new Error(
                "No se pudieron cargar las publicaciones."
            );
        }

        const publicaciones =
            await respuesta.json();

        contenedor.innerHTML = "";

        if (
            !Array.isArray(publicaciones) ||
            publicaciones.length === 0
        ) {

            mostrarForoVacio(contenedor);

            return;
        }

        publicaciones.forEach(
            (publicacion) => {

                const tarjeta =
                    crearTarjetaPublicacion(
                        publicacion
                    );

                contenedor.appendChild(
                    tarjeta
                );
            }
        );

        publicaciones.forEach(
            (publicacion) => {

                // ME GUSTA DE LA PUBLICACIÓN
                cargarMeGustaPublicacion(
                    publicacion.idPublicacion
                );

                // COMENTARIOS
                cargarComentarios(
                    publicacion.idPublicacion
                );
            }
        );

    } catch (error) {

        console.error(
            "Error al cargar publicaciones:",
            error
        );

        contenedor.innerHTML = `
            <div class="alert alert-danger">
                No se pudieron cargar las publicaciones.
            </div>
        `;
    }
}


// ==========================================
// TARJETA DE PUBLICACIÓN
// ==========================================

function crearTarjetaPublicacion(
    publicacion
) {

    const tarjeta =
        document.createElement("div");

    tarjeta.className =
        "card tarjeta_post shadow-lg mb-4";

    tarjeta.dataset.idPublicacion =
        publicacion.idPublicacion;

    const usuario =
        obtenerNombreUsuario(
            publicacion.usuario
        );

    const fecha =
        formatearFecha(
            publicacion.fecha
        );

    tarjeta.innerHTML = `

        <div class="card-body p-4">

            <div class="d-flex align-items-center mb-3">

                <div class="avatar_usuario me-3">

                    <i
                        class="bi bi-person-fill fs-3 text-secondary">
                    </i>

                </div>

                <div>

                    <h6 class="m-0 fw-bold text-dark">
                        ${escaparHTML(usuario)}
                    </h6>

                    <small class="text-muted">
                        ${fecha}
                    </small>

                </div>

            </div>


            <div
                id="contenido-publicacion-${publicacion.idPublicacion}">

                <h5 class="fw-bold text-dark mb-2">
                    ${escaparHTML(publicacion.titulo)}
                </h5>

                <p class="card-text text-dark fs-5">
                    ${escaparHTML(publicacion.contenido)}
                </p>

            </div>


            <div class="d-flex gap-2 mb-3">

                <button
                    type="button"
                    class="btn_editar_hover"
                    onclick="editarPublicacion(${publicacion.idPublicacion})">

                    <i class="bi bi-pencil-fill me-1"></i>
                    Editar

                </button>

                <button
                    type="button"
                    class="btn_eliminar_hover"
                    onclick="eliminarPublicacion(${publicacion.idPublicacion})">

                    <i class="bi bi-trash-fill me-1"></i>
                    Eliminar

                </button>

            </div>


            <!-- ME GUSTA DE LA PUBLICACIÓN -->

            <div class="d-flex align-items-center gap-3 mb-3">

                <button
                    type="button"
                    class="btn_accion_icono"
                    data-boton-me-gusta-publicacion="${publicacion.idPublicacion}"
                    onclick="cambiarMeGustaPublicacion(${publicacion.idPublicacion})"
                    title="Me gusta">

                    <i
                        class="bi bi-heart me-1 text-danger">
                    </i>

                    <span
                        data-me-gusta-publicacion>
                        0
                    </span>

                </button>

            </div>


            <hr class="my-3 text-muted opacity-25">


            <div
                class="d-flex align-items-center gap-3">

                <button
                    type="button"
                    class="btn_accion_icono"
                    onclick="mostrarFormularioComentario(${publicacion.idPublicacion})"
                    title="Comentar">

                    <i
                        class="bi bi-chat-dots-fill me-1 text-primary">
                    </i>

                    <span>
                        Comentar
                    </span>

                </button>

            </div>


            <div
                id="comentarios-${publicacion.idPublicacion}"
                class="mt-4">
            </div>

        </div>
    `;

    return tarjeta;
}


// ==========================================
// EDITAR PUBLICACIÓN
// ==========================================

async function editarPublicacion(
    idPublicacion
) {

    const contenedor =
        document.getElementById(
            `contenido-publicacion-${idPublicacion}`
        );

    if (!contenedor) {
        return;
    }

    const tituloActual =
        contenedor.querySelector("h5")?.textContent || "";

    const contenidoActual =
        contenedor.querySelector("p")?.textContent || "";

    contenedor.innerHTML = `

        <div class="mb-3">

            <label class="form-label fw-bold">
                Título
            </label>

            <input
                type="text"
                id="editar-titulo-publicacion-${idPublicacion}"
                class="form-control"
                maxlength="255"
                value="${escaparAtributo(tituloActual)}">

        </div>

        <div class="mb-3">

            <label class="form-label fw-bold">
                Contenido
            </label>

            <textarea
                id="editar-contenido-publicacion-${idPublicacion}"
                class="form-control"
                rows="4"
                maxlength="5000">${escaparHTML(contenidoActual)}</textarea>

        </div>

        <div class="d-flex justify-content-end gap-2 mb-3">

            <button
                type="button"
                class="btn-foro-formulario btn-foro-cancelar"
                onclick="cargarPublicaciones()">

                Cancelar

            </button>

            <button
                type="button"
                class="btn-foro-formulario btn-foro-confirmar"
                onclick="guardarEdicionPublicacion(${idPublicacion})">

                Guardar

            </button>

        </div>
    `;
}


// ==========================================
// GUARDAR EDICIÓN DE PUBLICACIÓN
// ==========================================

async function guardarEdicionPublicacion(
    idPublicacion
) {

    const campoTitulo =
        document.getElementById(
            `editar-titulo-publicacion-${idPublicacion}`
        );

    const campoContenido =
        document.getElementById(
            `editar-contenido-publicacion-${idPublicacion}`
        );

    if (!campoTitulo || !campoContenido) {
        return;
    }

    const titulo =
        campoTitulo.value.trim();

    const contenido =
        campoContenido.value.trim();

    if (!titulo) {

        alert(
            "El título no puede estar vacío."
        );

        campoTitulo.focus();

        return;
    }

    if (!contenido) {

        alert(
            "El contenido no puede estar vacío."
        );

        campoContenido.focus();

        return;
    }

    try {

        const respuesta =
            await fetch(
                `/api/publicaciones-foro/${idPublicacion}`,
                {

                    method: "PUT",

                    headers: {
                        "Content-Type": "application/json"
                    },

                    body: JSON.stringify({
                        titulo: titulo,
                        contenido: contenido
                    })
                }
            );

        if (!respuesta.ok) {

            const mensaje =
                await respuesta.text();

            alert(
                mensaje ||
                "No se pudo actualizar la publicación."
            );

            return;
        }

        await cargarPublicaciones();

    } catch (error) {

        console.error(
            "Error al actualizar publicación:",
            error
        );

        alert(
            "Ocurrió un error al actualizar la publicación."
        );
    }
}


// ==========================================
// ELIMINAR PUBLICACIÓN
// ==========================================

async function eliminarPublicacion(
    idPublicacion
) {

    const confirmar =
        confirm(
            "¿Estás seguro de que deseas eliminar esta publicación?"
        );

    if (!confirmar) {
        return;
    }

    try {

        const respuesta =
            await fetch(
                `/api/publicaciones-foro/${idPublicacion}`,
                {
                    method: "DELETE"
                }
            );

        if (!respuesta.ok) {

            const mensaje =
                await respuesta.text();

            alert(
                mensaje ||
                "No se pudo eliminar la publicación."
            );

            return;
        }

        await cargarPublicaciones();

    } catch (error) {

        console.error(
            "Error al eliminar publicación:",
            error
        );

        alert(
            "Ocurrió un error al eliminar la publicación."
        );
    }
}


// ==========================================
// FORO VACÍO
// ==========================================

function mostrarForoVacio(
    contenedor
) {

    contenedor.innerHTML = `

        <div
            class="card tarjeta_post shadow-lg mb-4">

            <div class="card-body p-4 text-center">

                <div
                    class="espacio_vacio_publicacion my-3">

                    <p class="m-0 fw-bold">
                        No hay publicaciones en este momento.
                    </p>

                    <small class="text-muted">
                        ¡Usa el botón "+" para crear una nueva!
                    </small>

                </div>

            </div>

        </div>
    `;
}


// ==========================================
// CREAR PUBLICACIÓN
// ==========================================

async function crearPublicacion() {

    const campoTitulo =
        document.getElementById(
            "titulo-publicacion"
        );

    const campoContenido =
        document.getElementById(
            "contenido-publicacion"
        );

    if (
        !campoTitulo ||
        !campoContenido
    ) {
        return;
    }

    const titulo =
        campoTitulo.value.trim();

    const contenido =
        campoContenido.value.trim();

    if (!titulo) {

        alert(
            "Debes escribir un título."
        );

        campoTitulo.focus();

        return;
    }

    if (!contenido) {

        alert(
            "Debes escribir el contenido de la publicación."
        );

        campoContenido.focus();

        return;
    }

    try {

        const respuesta =
            await fetch(
                "/api/publicaciones-foro/guardar",
                {

                    method: "POST",

                    headers: {
                        "Content-Type": "application/json"
                    },

                    body: JSON.stringify({
                        titulo: titulo,
                        contenido: contenido
                    })
                }
            );

        if (!respuesta.ok) {

            const mensaje =
                await respuesta.text();

            alert(
                mensaje ||
                "No se pudo crear la publicación."
            );

            return;
        }

        const formulario =
            document.getElementById(
                "formulario-nueva-publicacion"
            );

        if (formulario) {
            formulario.reset();
        }

        const dialogo =
            document.getElementById(
                "dialogo_nuevo_post"
            );

        if (dialogo) {
            dialogo.close();
        }

        await cargarPublicaciones();

    } catch (error) {

        console.error(
            "Error al crear publicación:",
            error
        );

        alert(
            "Ocurrió un error al crear la publicación."
        );
    }
}


// ==========================================
// COMENTARIOS
// ==========================================

async function cargarComentarios(
    idPublicacion
) {

    const contenedor =
        document.getElementById(
            `comentarios-${idPublicacion}`
        );

    if (!contenedor) {
        return;
    }

    try {

        const respuesta =
            await fetch(
                `/api/comentarios-foro/publicacion/${idPublicacion}`
            );

        if (!respuesta.ok) {

            console.error(
                "Error al cargar comentarios:",
                respuesta.status
            );

            return;
        }

        const comentarios =
            await respuesta.json();

        contenedor.innerHTML = `

            <div class="mb-3">

                <h6 class="fw-bold text-dark">
                    Comentarios
                </h6>

                <div
                    id="formulario-comentario-${idPublicacion}">
                </div>

            </div>

        `;

        if (
            !Array.isArray(comentarios) ||
            comentarios.length === 0
        ) {

            contenedor.innerHTML += `

                <small class="text-muted">
                    Sé el primero en comentar.
                </small>

            `;

            return;
        }

        comentarios
            .filter(
                comentario =>
                    comentario.comentarioPadre === null ||
                    comentario.comentarioPadre === undefined
            )
            .forEach(
                comentario => {

                    contenedor.appendChild(
                        crearElementoComentario(
                            comentario
                        )
                    );
                }
            );

    } catch (error) {

        console.error(
            "Error al cargar comentarios:",
            error
        );
    }
}


// ==========================================
// ELEMENTO DE COMENTARIO
// ==========================================

function crearElementoComentario(
    comentario
) {

    const elemento =
        document.createElement("div");

    elemento.className =
        "border rounded-3 p-3 mb-3";

    const usuario =
        obtenerNombreUsuario(
            comentario.usuario
        );

    elemento.innerHTML = `

        <div
            class="d-flex align-items-center mb-2">

            <i
                class="bi bi-person-circle fs-4 me-2 text-secondary">
            </i>

            <div>

                <strong>
                    ${escaparHTML(usuario)}
                </strong>

                <small
                    class="text-muted d-block">

                    ${formatearFecha(
                        comentario.fecha
                    )}

                </small>

            </div>

        </div>


        <div
            id="contenido-comentario-${comentario.idComentario}">

            <p class="mb-2 text-dark">

                ${escaparHTML(
                    comentario.contenido
                )}

            </p>

        </div>


        <div class="d-flex gap-2 mb-2">

            <button
                type="button"
                class="btn_editar_hover"
                onclick="editarComentario(${comentario.idComentario})">

                <i class="bi bi-pencil-fill me-1"></i>
                Editar

            </button>

            <button
                type="button"
                class="btn_eliminar_hover"
                onclick="eliminarComentario(${comentario.idComentario}, ${obtenerIdPublicacion(comentario)})">

                <i class="bi bi-trash-fill me-1"></i>
                Eliminar

            </button>

        </div>


        <div class="d-flex gap-3">

            <button
                type="button"
                class="btn_accion_icono"
                data-boton-me-gusta-comentario="${comentario.idComentario}"
                onclick="cambiarMeGustaComentario(${comentario.idComentario})"
                title="Me gusta">

                <i
                    class="bi bi-heart me-1 text-danger">
                </i>

                <span
                    data-me-gusta-comentario>
                    0
                </span>

            </button>


            <button
                type="button"
                class="btn_accion_icono"
                onclick="mostrarFormularioRespuesta(${comentario.idComentario})">

                <i
                    class="bi bi-reply me-1">
                </i>

                Responder

            </button>

        </div>


        <div
            id="respuesta-formulario-${comentario.idComentario}"
            class="mt-3">
        </div>


        <div
            id="respuestas-${comentario.idComentario}"
            class="ms-4 mt-3">
        </div>

    `;

    cargarMeGustaComentario(
        comentario.idComentario
    );

    cargarRespuestas(
        comentario.idComentario
    );

    return elemento;
}


// ==========================================
// EDITAR COMENTARIO
// ==========================================

function editarComentario(
    idComentario
) {

    const contenedor =
        document.getElementById(
            `contenido-comentario-${idComentario}`
        );

    if (!contenedor) {
        return;
    }

    const contenidoActual =
        contenedor.querySelector("p")?.textContent || "";

    contenedor.innerHTML = `

        <textarea
            id="editar-comentario-${idComentario}"
            class="form-control mb-2"
            rows="3"
            maxlength="1000">${escaparHTML(contenidoActual)}</textarea>

        <div class="d-flex justify-content-end gap-2 mb-3">

            <button
                type="button"
                class="btn-foro-formulario btn-foro-cancelar"
                onclick="restaurarComentario(${idComentario})">

                Cancelar

            </button>

            <button
                type="button"
                class="btn-foro-formulario btn-foro-confirmar"
                onclick="guardarEdicionComentario(${idComentario})">

                Guardar

            </button>

        </div>
    `;

    const textarea =
        document.getElementById(
            `editar-comentario-${idComentario}`
        );

    if (textarea) {
        textarea.focus();
    }
}


// ==========================================
// GUARDAR EDICIÓN DE COMENTARIO
// ==========================================

async function guardarEdicionComentario(
    idComentario
) {

    const textarea =
        document.getElementById(
            `editar-comentario-${idComentario}`
        );

    if (!textarea) {
        return;
    }

    const contenido =
        textarea.value.trim();

    if (!contenido) {

        alert(
            "El comentario no puede estar vacío."
        );

        textarea.focus();

        return;
    }

    try {

        const respuesta =
            await fetch(
                `/api/comentarios-foro/${idComentario}`,
                {

                    method: "PUT",

                    headers: {
                        "Content-Type": "application/json"
                    },

                    body: JSON.stringify({
                        contenido: contenido
                    })
                }
            );

        if (!respuesta.ok) {

            const mensaje =
                await respuesta.text();

            alert(
                mensaje ||
                "No se pudo actualizar el comentario."
            );

            return;
        }

        await recargarComentarioDesdePublicacion(
            idComentario
        );

    } catch (error) {

        console.error(
            "Error al actualizar comentario:",
            error
        );

        alert(
            "Ocurrió un error al actualizar el comentario."
        );
    }
}


// ==========================================
// CANCELAR EDICIÓN DE COMENTARIO
// ==========================================

async function restaurarComentario(
    idComentario
) {

    await recargarComentarioDesdePublicacion(
        idComentario
    );
}


// ==========================================
// ELIMINAR COMENTARIO
// ==========================================

async function eliminarComentario(
    idComentario,
    idPublicacion
) {

    const confirmar =
        confirm(
            "¿Estás seguro de que deseas eliminar este comentario?"
        );

    if (!confirmar) {
        return;
    }

    try {

        const respuesta =
            await fetch(
                `/api/comentarios-foro/${idComentario}`,
                {
                    method: "DELETE"
                }
            );

        if (!respuesta.ok) {

            const mensaje =
                await respuesta.text();

            alert(
                mensaje ||
                "No se pudo eliminar el comentario."
            );

            return;
        }

        if (idPublicacion) {

            await cargarComentarios(
                idPublicacion
            );

        } else {

            location.reload();
        }

    } catch (error) {

        console.error(
            "Error al eliminar comentario:",
            error
        );

        alert(
            "Ocurrió un error al eliminar el comentario."
        );
    }
}


// ==========================================
// FORMULARIO DE COMENTARIO
// ==========================================

function mostrarFormularioComentario(
    idPublicacion
) {

    const contenedor =
        document.getElementById(
            `formulario-comentario-${idPublicacion}`
        );

    if (!contenedor) {
        return;
    }

    contenedor.innerHTML = `

        <div class="mb-3">

            <textarea
                id="texto-comentario-${idPublicacion}"
                class="form-control"
                rows="3"
                maxlength="1000"
                placeholder="Escribe un comentario...">
            </textarea>

            <div class="d-flex justify-content-end gap-2 mt-2">

                <button
                    type="button"
                    class="btn-foro-formulario btn-foro-cancelar"
                    onclick="cancelarFormularioComentario(${idPublicacion})">

                    Cancelar

                </button>

                <button
                    type="button"
                    class="btn-foro-formulario btn-foro-confirmar"
                    onclick="crearComentario(${idPublicacion})">

                    Comentar

                </button>

            </div>

        </div>

    `;

    const textarea =
        document.getElementById(
            `texto-comentario-${idPublicacion}`
        );

    if (textarea) {
        textarea.focus();
    }
}


// ==========================================
// CANCELAR COMENTARIO
// ==========================================

function cancelarFormularioComentario(
    idPublicacion
) {

    const contenedor =
        document.getElementById(
            `formulario-comentario-${idPublicacion}`
        );

    if (contenedor) {
        contenedor.innerHTML = "";
    }
}


// ==========================================
// CREAR COMENTARIO
// ==========================================

async function crearComentario(
    idPublicacion
) {

    const textarea =
        document.getElementById(
            `texto-comentario-${idPublicacion}`
        );

    if (!textarea) {
        return;
    }

    const contenido =
        textarea.value.trim();

    if (!contenido) {

        alert(
            "Escribe un comentario antes de enviarlo."
        );

        textarea.focus();

        return;
    }

    try {

        const respuesta =
            await fetch(
                `/api/comentarios-foro/publicacion/${idPublicacion}`,
                {

                    method: "POST",

                    headers: {
                        "Content-Type": "application/json"
                    },

                    body: JSON.stringify({
                        contenido: contenido
                    })
                }
            );

        if (!respuesta.ok) {

            const mensaje =
                await respuesta.text();

            alert(
                mensaje ||
                "No se pudo crear el comentario."
            );

            return;
        }

        await cargarComentarios(
            idPublicacion
        );

    } catch (error) {

        console.error(
            "Error al crear comentario:",
            error
        );

        alert(
            "Ocurrió un error al crear el comentario."
        );
    }
}


// ==========================================
// RESPUESTAS
// ==========================================

async function cargarRespuestas(
    idComentario
) {

    const contenedor =
        document.getElementById(
            `respuestas-${idComentario}`
        );

    if (!contenedor) {
        return;
    }

    try {

        const respuesta =
            await fetch(
                `/api/comentarios-foro/${idComentario}/respuestas`
            );

        if (!respuesta.ok) {

            console.error(
                "Error al cargar respuestas:",
                respuesta.status
            );

            return;
        }

        const respuestas =
            await respuesta.json();

        contenedor.innerHTML = "";

        if (
            !Array.isArray(respuestas) ||
            respuestas.length === 0
        ) {
            return;
        }

        respuestas.forEach(
            respuesta => {

                contenedor.appendChild(
                    crearRespuesta(
                        respuesta
                    )
                );
            }
        );

    } catch (error) {

        console.error(
            "Error al cargar respuestas:",
            error
        );
    }
}


// ==========================================
// CREAR ELEMENTO DE RESPUESTA
// ==========================================

function crearRespuesta(
    respuesta
) {

    const elemento =
        document.createElement("div");

    elemento.className =
        "border-start ps-3 mb-3";

    const usuario =
        obtenerNombreUsuario(
            respuesta.usuario
        );

    elemento.innerHTML = `

        <div
            class="d-flex align-items-center mb-2">

            <i
                class="bi bi-person-circle fs-5 me-2 text-secondary">
            </i>

            <div>

                <strong>
                    ${escaparHTML(usuario)}
                </strong>

                <small
                    class="text-muted d-block">

                    ${formatearFecha(
                        respuesta.fecha
                    )}

                </small>

            </div>

        </div>


        <div
            id="contenido-respuesta-${respuesta.idComentario}">

            <p class="mb-2 text-dark">

                ${escaparHTML(
                    respuesta.contenido
                )}

            </p>

        </div>


        <div class="d-flex gap-2 mb-2">

            <button
                type="button"
                class="btn_editar_hover"
                onclick="editarRespuesta(${respuesta.idComentario})">

                <i class="bi bi-pencil-fill me-1"></i>
                Editar

            </button>

            <button
                type="button"
                class="btn_eliminar_hover"
                onclick="eliminarRespuesta(${respuesta.idComentario}, ${obtenerIdComentarioPadre(respuesta)})">

                <i class="bi bi-trash-fill me-1"></i>
                Eliminar

            </button>

        </div>


        <button
            type="button"
            class="btn_accion_icono"
            data-boton-me-gusta-comentario="${respuesta.idComentario}"
            onclick="cambiarMeGustaComentario(${respuesta.idComentario})"
            title="Me gusta">

            <i
                class="bi bi-heart me-1 text-danger">
            </i>

            <span
                data-me-gusta-comentario>
                0
            </span>

        </button>

    `;

    cargarMeGustaComentario(
        respuesta.idComentario
    );

    return elemento;
}


// ==========================================
// EDITAR RESPUESTA
// ==========================================

function editarRespuesta(
    idComentario
) {

    const contenedor =
        document.getElementById(
            `contenido-respuesta-${idComentario}`
        );

    if (!contenedor) {
        return;
    }

    const contenidoActual =
        contenedor.querySelector("p")?.textContent || "";

    contenedor.innerHTML = `

        <textarea
            id="editar-respuesta-${idComentario}"
            class="form-control mb-2"
            rows="3"
            maxlength="1000">${escaparHTML(contenidoActual)}</textarea>

        <div class="d-flex justify-content-end gap-2 mb-3">

            <button
                type="button"
                class="btn-foro-formulario btn-foro-cancelar"
                onclick="restaurarRespuesta(${idComentario})">

                Cancelar

            </button>

            <button
                type="button"
                class="btn-foro-formulario btn-foro-confirmar"
                onclick="guardarEdicionRespuesta(${idComentario})">

                Guardar

            </button>

        </div>
    `;

    const textarea =
        document.getElementById(
            `editar-respuesta-${idComentario}`
        );

    if (textarea) {
        textarea.focus();
    }
}


// ==========================================
// GUARDAR EDICIÓN DE RESPUESTA
// ==========================================

async function guardarEdicionRespuesta(
    idComentario
) {

    const textarea =
        document.getElementById(
            `editar-respuesta-${idComentario}`
        );

    if (!textarea) {
        return;
    }

    const contenido =
        textarea.value.trim();

    if (!contenido) {

        alert(
            "La respuesta no puede estar vacía."
        );

        textarea.focus();

        return;
    }

    try {

        const respuesta =
            await fetch(
                `/api/comentarios-foro/${idComentario}`,
                {

                    method: "PUT",

                    headers: {
                        "Content-Type": "application/json"
                    },

                    body: JSON.stringify({
                        contenido: contenido
                    })
                }
            );

        if (!respuesta.ok) {

            const mensaje =
                await respuesta.text();

            alert(
                mensaje ||
                "No se pudo actualizar la respuesta."
            );

            return;
        }

        location.reload();

    } catch (error) {

        console.error(
            "Error al actualizar respuesta:",
            error
        );

        alert(
            "Ocurrió un error al actualizar la respuesta."
        );
    }
}


// ==========================================
// CANCELAR EDICIÓN DE RESPUESTA
// ==========================================

async function restaurarRespuesta(
    idComentario
) {

    location.reload();
}


// ==========================================
// ELIMINAR RESPUESTA
// ==========================================

async function eliminarRespuesta(
    idComentario,
    idComentarioPadre
) {

    const confirmar =
        confirm(
            "¿Estás seguro de que deseas eliminar esta respuesta?"
        );

    if (!confirmar) {
        return;
    }

    try {

        const respuesta =
            await fetch(
                `/api/comentarios-foro/${idComentario}`,
                {
                    method: "DELETE"
                }
            );

        if (!respuesta.ok) {

            const mensaje =
                await respuesta.text();

            alert(
                mensaje ||
                "No se pudo eliminar la respuesta."
            );

            return;
        }

        if (idComentarioPadre) {

            await cargarRespuestas(
                idComentarioPadre
            );

        } else {

            location.reload();
        }

    } catch (error) {

        console.error(
            "Error al eliminar respuesta:",
            error
        );

        alert(
            "Ocurrió un error al eliminar la respuesta."
        );
    }
}


// ==========================================
// FORMULARIO DE RESPUESTA
// ==========================================

function mostrarFormularioRespuesta(
    idComentario
) {

    const contenedor =
        document.getElementById(
            `respuesta-formulario-${idComentario}`
        );

    if (!contenedor) {
        return;
    }

    contenedor.innerHTML = `

        <div class="mb-3">

            <textarea
                id="texto-respuesta-${idComentario}"
                class="form-control"
                rows="2"
                maxlength="1000"
                placeholder="Escribe una respuesta...">
            </textarea>

            <div class="d-flex justify-content-end gap-2 mt-2">

                <button
                    type="button"
                    class="btn-foro-formulario btn-foro-cancelar"
                    onclick="cancelarFormularioRespuesta(${idComentario})">

                    Cancelar

                </button>

                <button
                    type="button"
                    class="btn-foro-formulario btn-foro-confirmar"
                    onclick="crearRespuesta(${idComentario})">

                    Responder

                </button>

            </div>

        </div>

    `;

    const textarea =
        document.getElementById(
            `texto-respuesta-${idComentario}`
        );

    if (textarea) {
        textarea.focus();
    }
}


// ==========================================
// CANCELAR RESPUESTA
// ==========================================

function cancelarFormularioRespuesta(
    idComentario
) {

    const contenedor =
        document.getElementById(
            `respuesta-formulario-${idComentario}`
        );

    if (contenedor) {
        contenedor.innerHTML = "";
    }
}


// ==========================================
// CREAR RESPUESTA
// ==========================================

async function crearRespuesta(
    idComentario
) {

    const textarea =
        document.getElementById(
            `texto-respuesta-${idComentario}`
        );

    if (!textarea) {
        return;
    }

    const contenido =
        textarea.value.trim();

    if (!contenido) {

        alert(
            "Escribe una respuesta antes de enviarla."
        );

        textarea.focus();

        return;
    }

    try {

        const respuesta =
            await fetch(
                `/api/comentarios-foro/${idComentario}/responder`,
                {

                    method: "POST",

                    headers: {
                        "Content-Type": "application/json"
                    },

                    body: JSON.stringify({
                        contenido: contenido
                    })
                }
            );

        if (!respuesta.ok) {

            const mensaje =
                await respuesta.text();

            alert(
                mensaje ||
                "No se pudo crear la respuesta."
            );

            return;
        }

        await cargarRespuestas(
            idComentario
        );

        cancelarFormularioRespuesta(
            idComentario
        );

    } catch (error) {

        console.error(
            "Error al crear respuesta:",
            error
        );

        alert(
            "Ocurrió un error al crear la respuesta."
        );
    }
}


// ==========================================
// ME GUSTA DE COMENTARIOS
// ==========================================

async function cargarMeGustaComentario(
    idComentario
) {

    try {

        const respuesta =
            await fetch(
                `/api/me-gusta/comentarios/${idComentario}/cantidad`
            );

        if (!respuesta.ok) {
            return;
        }

        const datos =
            await respuesta.json();

        const boton =
            document.querySelector(
                `[data-boton-me-gusta-comentario="${idComentario}"]`
            );

        if (!boton) {
            return;
        }

        const contador =
            boton.querySelector(
                "[data-me-gusta-comentario]"
            );

        if (contador) {

            contador.textContent =
                datos.cantidad;
        }

        await actualizarEstadoMeGustaComentario(
            idComentario,
            boton
        );

    } catch (error) {

        console.error(
            "Error al cargar Me gusta:",
            error
        );
    }
}


// ==========================================
// ESTADO DEL ME GUSTA
// ==========================================

async function actualizarEstadoMeGustaComentario(
    idComentario,
    boton
) {

    try {

        const respuesta =
            await fetch(
                `/api/me-gusta/comentarios/${idComentario}/mi-me-gusta`
            );

        if (!respuesta.ok) {
            return;
        }

        const datos =
            await respuesta.json();

        actualizarIconoMeGusta(
            boton,
            datos.meGusta
        );

    } catch (error) {

        console.error(
            "Error al consultar Me gusta:",
            error
        );
    }
}


// ==========================================
// CAMBIAR ME GUSTA
// ==========================================

async function cambiarMeGustaComentario(
    idComentario
) {

    try {

        const respuesta =
            await fetch(
                `/api/me-gusta/comentarios/${idComentario}`,
                {
                    method: "POST"
                }
            );

        if (!respuesta.ok) {

            const mensaje =
                await respuesta.text();

            alert(
                mensaje ||
                "No se pudo cambiar el Me gusta."
            );

            return;
        }

        const datos =
            await respuesta.json();

        const boton =
            document.querySelector(
                `[data-boton-me-gusta-comentario="${idComentario}"]`
            );

        if (!boton) {
            return;
        }

        const contador =
            boton.querySelector(
                "[data-me-gusta-comentario]"
            );

        if (contador) {

            contador.textContent =
                datos.cantidad;
        }

        actualizarIconoMeGusta(
            boton,
            datos.meGusta
        );

    } catch (error) {

        console.error(
            "Error al cambiar Me gusta:",
            error
        );

        alert(
            "Ocurrió un error al procesar el Me gusta."
        );
    }
}


// ==========================================
// ICONO ME GUSTA DE COMENTARIOS
// ==========================================

function actualizarIconoMeGusta(
    boton,
    activo
) {

    const icono =
        boton.querySelector("i");

    if (!icono) {
        return;
    }

    icono.classList.toggle(
        "bi-heart",
        !activo
    );

    icono.classList.toggle(
        "bi-heart-fill",
        activo
    );
}


// ==========================================
// RECARGAR COMENTARIO
// ==========================================

async function recargarComentarioDesdePublicacion(
    idComentario
) {

    const respuesta =
        await fetch(
            `/api/comentarios-foro/${idComentario}/respuestas`
        );

    if (respuesta.ok) {
        location.reload();
        return;
    }

    location.reload();
}


// ==========================================
// UTILIDADES
// ==========================================

function obtenerNombreUsuario(
    usuario
) {

    if (!usuario) {
        return "Usuario";
    }

    return (
        usuario.nombreUsuario ||
        usuario.nombre ||
        usuario.nombreCompleto ||
        usuario.usuario ||
        usuario.username ||
        "Usuario"
    );
}


function formatearFecha(
    fecha
) {

    if (!fecha) {
        return "";
    }

    const fechaObjeto =
        new Date(fecha);

    if (
        isNaN(
            fechaObjeto.getTime()
        )
    ) {

        return fecha;
    }

    return fechaObjeto.toLocaleString(
        "es-CO",
        {
            dateStyle: "short",
            timeStyle: "short"
        }
    );
}


function escaparHTML(
    texto
) {

    if (
        texto === null ||
        texto === undefined
    ) {

        return "";
    }

    const elemento =
        document.createElement("div");

    elemento.textContent =
        texto;

    return elemento.innerHTML;
}


function escaparAtributo(
    texto
) {

    return escaparHTML(texto)
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}


// ==========================================
// OBTENER ID DE PUBLICACIÓN
// ==========================================

function obtenerIdPublicacion(
    comentario
) {

    if (
        comentario &&
        comentario.publicacion &&
        comentario.publicacion.idPublicacion
    ) {

        return comentario.publicacion.idPublicacion;
    }

    return "null";
}


// ==========================================
// OBTENER ID DEL COMENTARIO PADRE
// ==========================================

function obtenerIdComentarioPadre(
    comentario
) {

    if (
        comentario &&
        comentario.comentarioPadre &&
        comentario.comentarioPadre.idComentario
    ) {

        return comentario.comentarioPadre.idComentario;
    }

    return "null";
}