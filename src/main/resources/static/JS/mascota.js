/* ============================================================
   MY PET - MASCOTA.JS

   Maneja:
   - Mis mascotas
   - Agregar mascota
   - Perfil de mascota
   - Edición del perfil
   - Foto de mascota
   ============================================================ */

const API_BASE = `${window.location.origin}/api`;

let mascotaActual = null;
let perfilMascotaActual = null;
let idMascotaActual = null;
let fotoMascotaNueva = null;

function leerArchivoComoDataUrl(archivo) {
    return new Promise(function (resolve, reject) {
        const lector = new FileReader();
        lector.addEventListener("load", () => resolve(lector.result), { once: true });
        lector.addEventListener("error", () => reject(new Error("No se pudo leer la imagen.")), { once: true });
        lector.readAsDataURL(archivo);
    });
}

function obtenerRutaFoto(foto) {
    if (!foto) return "/Front%20end/fotos/perro.jpg";
    if (foto.startsWith("data:") || foto.startsWith("/") || /^https?:\/\//.test(foto)) {
        return foto;
    }
    return `../fotos/${encodeURIComponent(foto)}`;
}


/* ============================================================
   INICIO
   ============================================================ */

document.addEventListener("DOMContentLoaded", function () {

    const contenedorMascotas =
        document.getElementById("contenedorMascotas");

    const informacionMascota =
        document.getElementById("informacionMascota");

    if (contenedorMascotas) {
        iniciarMisMascotas();
    }

    if (informacionMascota) {
        iniciarPerfilMascota();
    }
});


/* ============================================================
   USUARIO LOGUEADO
   ============================================================ */

function obtenerUsuarioLogueado() {

    const usuarioGuardado =
        localStorage.getItem("usuarioLogueado");

    if (!usuarioGuardado) {
        return null;
    }

    try {

        return JSON.parse(usuarioGuardado);

    } catch (error) {

        console.error(
            "Error leyendo usuarioLogueado:",
            error
        );

        return null;
    }
}


function obtenerIdUsuario() {

    const usuario =
        obtenerUsuarioLogueado();

    if (!usuario) {
        return null;
    }

    return (
        usuario.idUsuario ||
        usuario.id_usuario ||
        localStorage.getItem("idUsuario")
    );
}


/* ============================================================
   MIS MASCOTAS
   ============================================================ */

function iniciarMisMascotas() {

    const idUsuario =
        obtenerIdUsuario();

    if (!idUsuario) {
        mostrarSinSesion();
        return;
    }

    configurarBotonAgregarMascota();

    configurarFormularioMascota();

    cargarMascotas();
}


/* ============================================================
   CARGAR MASCOTAS
   ============================================================ */

async function cargarMascotas() {

    const idUsuario =
        obtenerIdUsuario();

    const contenedor =
        document.getElementById("contenedorMascotas");

    if (!idUsuario || !contenedor) {
        return;
    }

    try {

        const respuesta =
            await fetch(
                `${API_BASE}/mascotas/usuario/${idUsuario}`
            );

        if (!respuesta.ok) {

            throw new Error(
                "No se pudieron cargar las mascotas."
            );
        }

        const mascotas =
            await respuesta.json();

        mostrarMascotas(mascotas);

    } catch (error) {

        console.error(
            "Error cargando mascotas:",
            error
        );

        contenedor.textContent =
            "No fue posible cargar las mascotas.";
    }
}


/* ============================================================
   MOSTRAR MASCOTAS
   ============================================================ */

function mostrarMascotas(mascotas) {

    const contenedor =
        document.getElementById(
            "contenedorMascotas"
        );

    if (!contenedor) {
        return;
    }

    const columnaAgregar =
        document.getElementById("columnaAgregarMascota");

    contenedor
        .querySelectorAll("[data-tarjeta-mascota]")
        .forEach(tarjeta => tarjeta.remove());

    contenedor
        .querySelectorAll("[data-mensaje-mascotas]")
        .forEach(mensaje => mensaje.remove());

    if (!mascotas.length) {
        const mensaje = document.createElement("p");
        mensaje.className = "col-12 text-center text-white";
        mensaje.dataset.mensajeMascotas = "true";
        mensaje.textContent = "Aún no tienes mascotas registradas.";
        contenedor.insertBefore(mensaje, columnaAgregar);
        return;
    }

    mascotas.forEach(function (mascota) {
        const columna = document.createElement("div");
        columna.className = "col-12 col-md-6 col-lg-4 d-flex flex-column align-items-center";
        columna.dataset.tarjetaMascota = "true";

        const enlace = document.createElement("a");
        enlace.className = "marco-foto-per-masco";
        enlace.href = `perfil_mascota.html?id=${encodeURIComponent(mascota.idMascota)}`;
        enlace.setAttribute("aria-label", `Ver perfil de ${mascota.nombre || "mascota"}`);

        const imagen = document.createElement("img");
        const rutaFoto = mascota.foto || "/Front%20end/fotos/perro.jpg";
        imagen.src = rutaFoto.startsWith("data:") ||
            rutaFoto.startsWith("/") ||
            rutaFoto.startsWith("http://") ||
            rutaFoto.startsWith("https://")
            ? rutaFoto
            : `../fotos/${encodeURIComponent(rutaFoto)}`;
        imagen.alt = mascota.nombre || "Foto de mascota";
        imagen.addEventListener("error", function () {
            imagen.src = "/Front%20end/fotos/perro.jpg";
        }, { once: true });

        const etiqueta = document.createElement("span");
        etiqueta.className = "overlay-ver-perfil";
        etiqueta.textContent = "Ver perfil";

        enlace.append(imagen, etiqueta);

        const nombre = document.createElement("h2");
        nombre.className = "nom-masco mt-3";
        nombre.textContent = mascota.nombre || "Mascota";

        const especie = document.createElement("p");
        especie.className = "text-white mb-1";
        especie.textContent = `Especie: ${mascota.especie || "No registrada"}`;

        const raza = document.createElement("p");
        raza.className = "text-white mb-2";
        raza.textContent = `Raza: ${mascota.raza || "No registrada"}`;

        const botonVerPerfil = document.createElement("a");
        botonVerPerfil.className = "btn-perfil-mascota mt-2";
        botonVerPerfil.href = enlace.href;
        botonVerPerfil.textContent = "Ver perfil";

        columna.append(enlace, nombre, especie, raza, botonVerPerfil);
        contenedor.insertBefore(columna, columnaAgregar);
    });
}


/* ============================================================
   BOTÓN AGREGAR MASCOTA
   ============================================================ */

function configurarBotonAgregarMascota() {

    const boton =
        document.getElementById(
            "btnAbrirAgregarMascota"
        );

    const ventana =
        document.getElementById(
            "ventanaAgregarMascota"
        );

    if (!boton || !ventana) {
        return;
    }

    boton.addEventListener(
        "click",
        function () {

            if (
                typeof bootstrap !== "undefined" &&
                bootstrap.Modal
            ) {

                const modal =
                    bootstrap.Modal.getOrCreateInstance(
                        ventana
                    );

                modal.show();

            } else {

                ventana.style.display =
                    "block";
            }
        }
    );
}


/* ============================================================
   FORMULARIO AGREGAR MASCOTA
   ============================================================ */

function configurarFormularioMascota() {

    const formulario =
        document.getElementById(
            "formAgregarMascota"
        );

    if (!formulario) {
        return;
    }

    formulario.addEventListener(
        "submit",
        async function (evento) {

            evento.preventDefault();

            await guardarMascota();
        }
    );

    const inputFoto = document.getElementById("fotoMascota");
    const previewFoto = document.getElementById("previewFotoMascota");
    inputFoto?.addEventListener("change", async function () {
        const archivo = inputFoto.files[0];
        if (!archivo) {
            previewFoto?.classList.add("d-none");
            return;
        }
        if (!["image/jpeg", "image/png", "image/webp", "image/gif"].includes(archivo.type) ||
            archivo.size > 5 * 1024 * 1024) {
            alert("Selecciona una imagen JPG, PNG, WebP o GIF de máximo 5 MB.");
            inputFoto.value = "";
            previewFoto?.classList.add("d-none");
            return;
        }
        if (previewFoto) {
            previewFoto.src = await leerArchivoComoDataUrl(archivo);
            previewFoto.classList.remove("d-none");
        }
    });
}


/* ============================================================
   GUARDAR MASCOTA
   ============================================================ */

async function guardarMascota() {

    const idUsuario =
        obtenerIdUsuario();

    if (!idUsuario) {

        alert(
            "No se encontró el usuario."
        );

        return;
    }


    const nombre =
        obtenerValor(
            "nombreMascotaNueva",
            "nombreMascota"
        );

    const especie =
        obtenerValor(
            "especieMascotaNueva",
            "especieMascota"
        );

    const raza =
        obtenerValor(
            "razaMascotaNueva",
            "razaMascota"
        );

    const fechaNacimiento =
        obtenerValor(
            "fechaNacimientoMascotaNueva",
            "fechaNacimientoMascota"
        );

    const archivoFoto = document.getElementById("fotoMascota")?.files?.[0];
    let foto = "";

    if (archivoFoto) {
        if (!["image/jpeg", "image/png", "image/webp", "image/gif"].includes(archivoFoto.type) ||
            archivoFoto.size > 5 * 1024 * 1024) {
            alert("Selecciona una imagen JPG, PNG, WebP o GIF de máximo 5 MB.");
            return;
        }
        foto = await leerArchivoComoDataUrl(archivoFoto);
    }


    const mascota = {

        nombre: nombre,

        especie: especie,

        raza: raza,

        fechaNacimiento: fechaNacimiento,

        foto: foto,

        usuario: {
            idUsuario: parseInt(idUsuario)
        }
    };


    try {

        const respuesta =
            await fetch(
                `${API_BASE}/mascotas/crear`,
                {
                    method: "POST",

                    headers: {
                        "Content-Type":
                            "application/json"
                    },

                    body:
                        JSON.stringify(mascota)
                }
            );


        if (!respuesta.ok) {

            const mensaje =
                await respuesta.text();

            throw new Error(
                mensaje ||
                "No fue posible guardar la mascota."
            );
        }


        const mascotaCreada =
            await respuesta.json();


        alert(
            "Mascota agregada correctamente."
        );


        const formulario =
            document.getElementById(
                "formAgregarMascota"
            );

        if (formulario) {
            formulario.reset();
        }

        document.getElementById("previewFotoMascota")?.classList.add("d-none");


        cerrarModalAgregarMascota();


        await cargarMascotas();


        window.location.href =
            "perfil_mascota.html?id=" +
            mascotaCreada.idMascota;


    } catch (error) {

        console.error(
            "Error guardando mascota:",
            error
        );

        alert(
            error.message ||
            "No fue posible guardar la mascota."
        );
    }
}


/* ============================================================
   CERRAR MODAL AGREGAR MASCOTA
   ============================================================ */

function cerrarModalAgregarMascota() {

    const ventana =
        document.getElementById(
            "ventanaAgregarMascota"
        );

    if (!ventana) {
        return;
    }


    if (
        typeof bootstrap !== "undefined" &&
        bootstrap.Modal
    ) {

        const modal =
            bootstrap.Modal.getInstance(
                ventana
            );

        if (modal) {
            modal.hide();
        }

    } else {

        ventana.style.display =
            "none";
    }
}


/* ============================================================
   PERFIL DE MASCOTA
   ============================================================ */

async function iniciarPerfilMascota() {

    const parametros =
        new URLSearchParams(
            window.location.search
        );

    idMascotaActual =
        parametros.get("id");


    if (!idMascotaActual) {

        alert(
            "No se encontró la mascota."
        );

        return;
    }

    const parametroMascota = `?idMascota=${encodeURIComponent(idMascotaActual)}`;
    const enlaceDocumentos = document.getElementById("enlaceDocumentos");
    const enlaceHistorial = document.getElementById("enlaceHistorial");
    const enlaceEmergencia = document.getElementById("enlaceEmergencia");
    const idUsuario = obtenerIdUsuario();

    if (enlaceDocumentos) {
        enlaceDocumentos.href += parametroMascota;
    }

    if (enlaceHistorial) {
        enlaceHistorial.href += parametroMascota;
    }

    if (enlaceEmergencia) {
        const parametrosEmergencia = new URLSearchParams({ idMascota: idMascotaActual });
        enlaceEmergencia.href += `?${parametrosEmergencia.toString()}`;
    }


    await cargarPerfilMascota();

    configurarPerfil();
}


/* ============================================================
   CARGAR MASCOTA
   ============================================================ */

async function cargarPerfilMascota() {

    const idUsuario =
        obtenerIdUsuario();

    if (!idUsuario) {

        alert(
            "No se encontró el usuario."
        );

        return;
    }


    try {

        const respuesta =
            await fetch(
                `${API_BASE}/mascotas/usuario/${idUsuario}`
            );


        if (!respuesta.ok) {

            throw new Error(
                "No fue posible consultar las mascotas."
            );
        }


        const mascotas =
            await respuesta.json();


        mascotaActual =
            mascotas.find(
                function (mascota) {

                    return String(
                        mascota.idMascota
                    ) === String(
                        idMascotaActual
                    );
                }
            );


        if (!mascotaActual) {

            throw new Error(
                "No se encontró la mascota."
            );
        }

        const respuestaPerfil = await fetch(
            `${API_BASE}/perfiles-mascotas/mascota/${idMascotaActual}`
        );

        if (respuestaPerfil.status === 404) {
            perfilMascotaActual = null;
        } else if (!respuestaPerfil.ok) {
            throw new Error("No se pudo cargar el perfil de la mascota.");
        } else {
            perfilMascotaActual = await respuestaPerfil.json();
        }


        mostrarDatosMascota();

    } catch (error) {

        console.error(
            "Error cargando perfil:",
            error
        );

        alert(error.message);
    }
}


/* ============================================================
   MOSTRAR DATOS DE MASCOTA
   ============================================================ */

function mostrarDatosMascota() {

    if (!mascotaActual) {
        return;
    }

    establecerTexto(
        "nombreMascotaTitulo",
        mascotaActual.nombre || "Mascota"
    );

    establecerTexto(
        "nombreMascota",
        "Nombre: " + (mascotaActual.nombre || "No registrado")
    );

    establecerTexto(
        "especieMascota",
        "Especie: " + (mascotaActual.especie || "No registrada")
    );

    establecerTexto(
        "razaMascota",
        "Raza: " + (mascotaActual.raza || "No registrada")
    );

    establecerTexto(
        "fechaNacimientoMascota",
        "Fecha de nacimiento: " +
        (
            mascotaActual.fechaNacimiento
                ? formatearFecha(mascotaActual.fechaNacimiento)
                : "No registrada"
        )
    );

    establecerTexto(
        "edadMascota",
        "Edad: " +
        (
            mascotaActual.fechaNacimiento
                ? calcularEdad(mascotaActual.fechaNacimiento)
                : "No registrada"
        )
    );

    establecerTexto(
        "caracteristicasInformacion",
        "Características: " +
        (perfilMascotaActual?.caracteristicas || "No registradas")
    );

    establecerTexto(
        "comportamientoInformacion",
        "Comportamiento: " +
        (perfilMascotaActual?.comportamiento || "No registrado")
    );

    establecerTexto(
        "gustosInformacion",
        "Gustos: " +
        (perfilMascotaActual?.gustos || "No registrados")
    );

    establecerTexto(
        "cuidadosInformacion",
        "Cuidados especiales: " +
        (perfilMascotaActual?.cuidadosEspeciales || "No registrados")
    );

    mostrarFotoMascota();
}

/* ============================================================
   MOSTRAR FOTO
   ============================================================ */

function mostrarFotoMascota() {

    const imagen =
        document.getElementById(
            "fotoMascota"
        );

    if (!imagen || !mascotaActual) {
        return;
    }


    imagen.src = obtenerRutaFoto(mascotaActual.foto);
}


/* ============================================================
   CONFIGURAR PERFIL
   ============================================================ */

function configurarPerfil() {

    const botonEditar =
        document.getElementById(
            "editarInformacion"
        );

    if (botonEditar) {

        botonEditar.addEventListener(
            "click",
            abrirEdicion
        );
    }


    const cancelar =
        document.getElementById(
            "cancelarEdicion"
        );

    if (cancelar) {

        cancelar.addEventListener(
            "click",
            cancelarEdicion
        );
    }


    const formulario =
        document.getElementById(
            "formEditarMascota"
        );

    if (formulario) {

        formulario.addEventListener(
            "submit",
            guardarEdicion
        );
    }


    const inputFoto =
        document.getElementById(
            "input-foto-mascota"
        );

    if (inputFoto) {

        inputFoto.addEventListener(
            "change",
            cambiarFoto
        );
    }
}


/* ============================================================
   ABRIR EDICIÓN
   ============================================================ */

function abrirEdicion() {

    if (!mascotaActual) {
        return;
    }


    establecerValor(
        "editarNombre",
        mascotaActual.nombre
    );


    establecerValor(
        "editarEspecie",
        mascotaActual.especie
    );


    establecerValor(
        "editarRaza",
        mascotaActual.raza
    );


    establecerValor(
        "editarFechaNacimiento",
        mascotaActual.fechaNacimiento
    );


    establecerValor(
        "editarCaracteristicas",
        perfilMascotaActual?.caracteristicas || ""
    );


    establecerValor(
        "editarComportamiento",
        perfilMascotaActual?.comportamiento || ""
    );


    establecerValor(
        "editarGustos",
        perfilMascotaActual?.gustos || ""
    );


    establecerValor(
        "editarCuidados",
        perfilMascotaActual?.cuidadosEspeciales || ""
    );


    const informacion =
        document.getElementById(
            "informacionMascota"
        );

    const formulario =
        document.getElementById(
            "formularioEdicion"
        );


    if (informacion) {
        informacion.style.display = "none";
    }


    if (formulario) {
        formulario.style.display = "block";
    }
}


/* ============================================================
   CANCELAR EDICIÓN
   ============================================================ */

function cancelarEdicion() {

    const informacion =
        document.getElementById(
            "informacionMascota"
        );

    const formulario =
        document.getElementById(
            "formularioEdicion"
        );


    if (informacion) {
        informacion.style.display = "block";
    }


    if (formulario) {
        formulario.style.display = "none";
    }
}


/* ============================================================
   GUARDAR EDICIÓN
   ============================================================ */

async function guardarEdicion(evento) {

    evento.preventDefault();

    if (!mascotaActual) {
        return;
    }

    const datosMascota = {
        nombre: obtenerValor("editarNombre"),
        especie: obtenerValor("editarEspecie"),
        raza: obtenerValor("editarRaza"),
        fechaNacimiento: obtenerValor("editarFechaNacimiento"),
        foto: mascotaActual.foto || "",
        usuario: { idUsuario: mascotaActual.usuario.idUsuario }
    };

    const datosPerfil = {
        caracteristicas: obtenerValor("editarCaracteristicas"),
        comportamiento: obtenerValor("editarComportamiento"),
        gustos: obtenerValor("editarGustos"),
        cuidadosEspeciales: obtenerValor("editarCuidados"),
        mascota: { idMascota: Number(idMascotaActual) }
    };

    try {
        const respuestaMascota = await fetch(
            `${API_BASE}/mascotas/actualizar/${idMascotaActual}`,
            {
                method: "PUT",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(datosMascota)
            }
        );

        if (!respuestaMascota.ok) {
            throw new Error(await respuestaMascota.text() || "No se pudo guardar la mascota.");
        }
        mascotaActual = await respuestaMascota.json();

        const perfilExiste = Boolean(perfilMascotaActual?.idPerfilMascota);
        const respuestaPerfil = await fetch(
            perfilExiste
                ? `${API_BASE}/perfiles-mascotas/actualizar/${perfilMascotaActual.idPerfilMascota}`
                : `${API_BASE}/perfiles-mascotas/guardar`,
            {
                method: perfilExiste ? "PUT" : "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(datosPerfil)
            }
        );

        if (!respuestaPerfil.ok) {
            throw new Error(await respuestaPerfil.text() || "No se pudo guardar el perfil de la mascota.");
        }
        perfilMascotaActual = await respuestaPerfil.json();

        mostrarDatosMascota();
        cancelarEdicion();
        alert("Información de la mascota y su perfil guardada correctamente.");
    } catch (error) {
        console.error("Error guardando los datos de la mascota:", error);
        alert(error.message || "No se pudo guardar la información.");
    }
}


/* ============================================================
   CAMBIAR FOTO
   ============================================================ */

async function cambiarFoto(evento) {
    const archivo = evento.target.files[0];
    if (!archivo || !mascotaActual) return;

    if (!archivo.type.startsWith("image/") || archivo.size > 5 * 1024 * 1024) {
        alert("Selecciona una imagen válida de máximo 5 MB.");
        evento.target.value = "";
        return;
    }

    try {
        fotoMascotaNueva = await leerArchivoComoDataUrl(archivo);
        const respuesta = await fetch(
            `${API_BASE}/mascotas/actualizar/${idMascotaActual}`,
            {
                method: "PUT",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({
                    nombre: mascotaActual.nombre,
                    especie: mascotaActual.especie,
                    raza: mascotaActual.raza,
                    fechaNacimiento: mascotaActual.fechaNacimiento,
                    foto: fotoMascotaNueva
                })
            }
        );
        if (!respuesta.ok) {
            throw new Error(await respuesta.text() || "No se pudo guardar la foto.");
        }
        mascotaActual = await respuesta.json();
        mostrarFotoMascota();
        fotoMascotaNueva = null;
    } catch (error) {
        console.error("Error guardando foto de mascota:", error);
        alert(error.message || "No se pudo guardar la foto.");
    }
}


/* ============================================================
   SIN SESIÓN
   ============================================================ */

function mostrarSinSesion() {

    const contenedor =
        document.getElementById(
            "contenedorMascotas"
        );


    if (!contenedor) {
        return;
    }


    contenedor.textContent =
        "Debes iniciar sesión para ver tus mascotas.";
}


/* ============================================================
   FUNCIONES AUXILIARES
   ============================================================ */

function obtenerValor(
    id,
    idAlternativo
) {

    let elemento =
        document.getElementById(
            id
        );


    if (
        !elemento &&
        idAlternativo
    ) {

        elemento =
            document.getElementById(
                idAlternativo
            );
    }


    if (!elemento) {
        return "";
    }


    return (
        elemento.value ||
        ""
    ).trim();
}


function establecerValor(
    id,
    valor
) {

    const elemento =
        document.getElementById(
            id
        );


    if (!elemento) {
        return;
    }


    elemento.value =
        valor || "";
}


function establecerTexto(
    id,
    valor
) {

    const elemento =
        document.getElementById(
            id
        );


    if (!elemento) {
        return;
    }


    elemento.textContent =
        valor ||
        "No registrado";
}


/* ============================================================
   EDAD
   ============================================================ */

function calcularEdad(
    fechaNacimiento
) {

    const nacimiento =
        new Date(
            fechaNacimiento
        );


    const hoy =
        new Date();


    let edad =
        hoy.getFullYear() -
        nacimiento.getFullYear();


    const diferenciaMes =
        hoy.getMonth() -
        nacimiento.getMonth();


    if (
        diferenciaMes < 0 ||
        (
            diferenciaMes === 0 &&
            hoy.getDate() <
            nacimiento.getDate()
        )
    ) {

        edad--;
    }


    if (edad < 0) {
        return "No válida";
    }


    if (edad === 1) {
        return "1 año";
    }


    return edad + " años";
}


/* ============================================================
   FORMATO DE FECHA
   ============================================================ */

function formatearFecha(
    fecha
) {

    if (!fecha) {
        return "";
    }


    const partes =
        fecha.split("-");


    if (partes.length !== 3) {
        return fecha;
    }


    return (
        partes[2] +
        "/" +
        partes[1] +
        "/" +
        partes[0]
    );
}