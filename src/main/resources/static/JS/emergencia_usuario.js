document.addEventListener("DOMContentLoaded", function () {
    const apiBase = `${window.location.origin}/api`;
    const usuarioGuardado = localStorage.getItem("usuarioLogueado");
    const formulario = document.getElementById("formEmergenciaUsuario");
    const lista = document.getElementById("listaEmergencias");
    const estado = document.getElementById("estadoEmergencia");
    const boton = document.getElementById("btnEnviarEmergencia");
    const idMascota = new URLSearchParams(window.location.search).get("idMascota");

    if (!usuarioGuardado) {
        window.location.href = "/Front%20end/Modulo_1_gestion_usuario/iniciar_sesion.html";
        return;
    }

    let usuario;
    try {
        usuario = JSON.parse(usuarioGuardado);
    } catch (error) {
        localStorage.removeItem("usuarioLogueado");
        localStorage.removeItem("idUsuario");
        window.location.href = "/Front%20end/Modulo_1_gestion_usuario/iniciar_sesion.html";
        return;
    }

    const idUsuario = usuario.idUsuario || usuario.id_usuario || localStorage.getItem("idUsuario");
    if (!idUsuario) {
        estado.textContent = "No se pudo identificar tu cuenta.";
        return;
    }

    async function solicitar(url, opciones = {}) {
        const respuesta = await fetch(`${apiBase}${url}`, {
            ...opciones,
            headers: {
                ...(opciones.body ? { "Content-Type": "application/json" } : {}),
                ...opciones.headers
            }
        });
        if (!respuesta.ok) throw new Error(await respuesta.text() || "No se pudo completar la solicitud.");
        const texto = await respuesta.text();
        return texto ? JSON.parse(texto) : null;
    }

    function mostrarEmergencias(emergencias) {
        lista.replaceChildren();
        if (!emergencias.length) {
            lista.textContent = "Todavía no tienes reportes.";
            return;
        }

        emergencias.forEach(function (emergencia) {
            const articulo = document.createElement("article");
            articulo.className = "bg-white p-3 rounded";
            const titulo = document.createElement("h3");
            titulo.className = "h6";
            titulo.textContent = emergencia.tipo || "Emergencia";
            const descripcion = document.createElement("p");
            descripcion.className = "mb-1";
            descripcion.textContent = emergencia.descripcion || "Sin descripción";
            const detalle = document.createElement("small");
            const nombreMascota = emergencia.mascota?.nombre;
            detalle.textContent = [nombreMascota, emergencia.fecha]
                .filter(Boolean)
                .join(" · ");
            articulo.append(titulo, descripcion, detalle);
            lista.append(articulo);
        });
    }

    async function cargarEmergencias() {
        try {
            const emergencias = await solicitar(`/emergencias/usuario/${encodeURIComponent(idUsuario)}`);
            mostrarEmergencias(emergencias);
        } catch (error) {
            console.error("Error cargando reportes de emergencia:", error);
            estado.textContent = "No se pudieron cargar tus reportes.";
        }
    }

    if (idMascota) {
        const enlaceVolver = document.getElementById("volverEmergencia");
        enlaceVolver.href = `/Front%20end/Modulo_2_gestion_mascotas/perfil_mascota.html?id=${encodeURIComponent(idMascota)}`;
        solicitar(`/mascotas/${encodeURIComponent(idMascota)}`)
            .then(mascota => {
                document.getElementById("mascotaEmergencia").textContent = `Mascota: ${mascota.nombre}`;
            })
            .catch(error => {
                console.error("No se pudo cargar la mascota:", error);
                estado.textContent = "No se pudo verificar la mascota seleccionada.";
            });
    }

    formulario.addEventListener("submit", async function (event) {
        event.preventDefault();
        estado.textContent = "";
        boton.disabled = true;

        const descripcion = document.getElementById("descripcionEmergenciaUsuario").value.trim();
        const emergencia = {
            tipo: document.getElementById("tipoEmergenciaUsuario").value,
            descripcion,
            usuario: { idUsuario: Number(idUsuario) },
            mascota: idMascota ? { idMascota: Number(idMascota) } : null
        };

        try {
            await solicitar("/emergencias/guardar", {
                method: "POST",
                body: JSON.stringify(emergencia)
            });
            formulario.reset();
            estado.textContent = "Reporte guardado en tu cuenta.";
            await cargarEmergencias();
        } catch (error) {
            console.error("Error guardando el reporte:", error);
            estado.textContent = error.message || "No se pudo guardar el reporte.";
        } finally {
            boton.disabled = false;
        }
    });

    cargarEmergencias();
});