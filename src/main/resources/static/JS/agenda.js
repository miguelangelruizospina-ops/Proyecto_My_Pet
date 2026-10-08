 // Configuración y elementos principales de la agenda.

document.addEventListener("DOMContentLoaded", function () {

    const apiBase = `${window.location.origin}/api`;
    const usuarioGuardado = localStorage.getItem("usuarioLogueado");
    const eventosGenerales = document.getElementById("eventosAgenda");
    const eventosMascota = document.getElementById("eventosMascota");
    const estadoAgenda = document.getElementById("estadoAgenda");
    const mascotasAgenda = document.getElementById("mascotasAgenda");
    const estadoMascotas = document.getElementById("estadoMascotasAgenda");
    const formulario = document.getElementById("formNuevoEvento");
    const dialogo = document.getElementById("dialogo_nuevo_evento");
    const selectorMascota = document.getElementById("mascotaEvento");
    const esAgendaPersonalizada = Boolean(eventosMascota);
    let mascotas = [];

    // Verifica que exista una sesión de usuario antes de cargar la agenda.

    if (!usuarioGuardado) {
        window.location.href = "../Modulo_1_gestion_usuario/iniciar_sesion.html";
        return;
    }

    let usuario;
    try {
        usuario = JSON.parse(usuarioGuardado);
    } catch (error) {

        // Elimina los datos de sesión inválidos y redirige al inicio de sesión.

        localStorage.removeItem("usuarioLogueado");
        localStorage.removeItem("idUsuario");
        window.location.href = "../Modulo_1_gestion_usuario/iniciar_sesion.html";
        return;
    }

    // Obtiene el identificador del usuario desde la sesión almacenada.
    
    const idUsuario = usuario.idUsuario || usuario.id_usuario || localStorage.getItem("idUsuario");
    if (!idUsuario) {
        if (estadoAgenda) estadoAgenda.textContent = "No se pudo identificar al usuario.";
        return;
    }

    // Obtiene la fecha actual en formato local.
    
    function fechaLocal(fecha) {
        const ahora = new Date();
        const anio = ahora.getFullYear();
        const mes = String(ahora.getMonth() + 1).padStart(2, "0");
        const dia = String(ahora.getDate()).padStart(2, "0");
        return fecha || `${anio}-${mes}-${dia}`;
    }

    // Gestiona las solicitudes a la API y procesa las respuestas.
    
    async function solicitar(url, opciones = {}) {
        const respuesta = await fetch(`${apiBase}${url}`, {
            ...opciones,
            headers: {
                ...(opciones.body ? { "Content-Type": "application/json" } : {}),
                ...opciones.headers
            }
        });

        if (!respuesta.ok) {
            const mensaje = await respuesta.text();
            throw new Error(mensaje || "No se pudo completar la solicitud.");
        }

        const contenido = await respuesta.text();
        return contenido ? JSON.parse(contenido) : null;
    }

    // Muestra el mensaje correspondiente cuando ocurre un error en la agenda.
    
    function mostrarError(error) {
        console.error("Error en agenda:", error);
        if (estadoAgenda) estadoAgenda.textContent = "No se pudo cargar o guardar la agenda.";
    }

    // Crea una tarjeta para mostrar la información de un evento.
    
    function crearTarjeta(evento, incluirMascota) {
        const tarjeta = document.createElement("article");
        tarjeta.className = "tarjeta_agenda p-3 text-start";

        const titulo = document.createElement("h4");
        titulo.className = "h6 titulo_seccion";
        titulo.textContent = evento.tipoEvento || "Evento";
        tarjeta.append(titulo);

        const fecha = document.createElement("p");
        fecha.className = "mb-1";
        fecha.textContent = evento.fecha
            ? new Date(evento.fecha).toLocaleString("es-CO")
            : "Fecha no disponible";
        tarjeta.append(fecha);

        if (incluirMascota && evento.mascota) {
            const mascota = document.createElement("p");
            mascota.className = "mb-1";
            mascota.textContent = `Mascota: ${evento.mascota.nombre || "Sin nombre"}`;
            tarjeta.append(mascota);
        }

        if (evento.descripcion) {
            const descripcion = document.createElement("p");
            descripcion.className = "mb-2";
            descripcion.textContent = evento.descripcion;
            tarjeta.append(descripcion);
        }

        const botonEliminar = document.createElement("button");
        botonEliminar.type = "button";
        botonEliminar.className = "btn btn-outline-danger btn-sm";
        botonEliminar.textContent = "Eliminar";
        botonEliminar.addEventListener("click", async function () {
            if (!window.confirm("¿Eliminar este evento?")) return;
            try {
                await solicitar(`/eventos/eliminar/${evento.idEvento}`, { method: "DELETE" });
                await cargarEventos();
            } catch (error) {
                mostrarError(error);
            }
        });
        tarjeta.append(botonEliminar);
        return tarjeta;
    }

    // Carga y muestra los eventos de la agenda general o personalizada.
    
    async function cargarEventos() {
        const fechaSeleccionada = document.getElementById("calendario_principal")?.value;
        let eventos;

        if (esAgendaPersonalizada) {
            const idMascota = new URLSearchParams(window.location.search).get("mascota");
            eventos = await solicitar(`/eventos/mascota/${encodeURIComponent(idMascota)}`);
            eventosMascota.replaceChildren();
            if (!eventos.length) {
                eventosMascota.textContent = "Esta mascota todavía no tiene eventos.";
                return;
            }
            eventos.sort((a, b) => new Date(a.fecha) - new Date(b.fecha));
            eventos.forEach(evento => eventosMascota.append(crearTarjeta(evento, false)));
            if (estadoAgenda) estadoAgenda.textContent = "";
            return;
        }

        const grupos = await Promise.all(mascotas.map(mascota =>
            solicitar(`/eventos/mascota/${mascota.idMascota}`)
        ));
        eventos = grupos.flat().filter(evento =>
            !fechaSeleccionada || (evento.fecha && evento.fecha.slice(0, 10) === fechaSeleccionada)
        );
        eventos.sort((a, b) => new Date(a.fecha) - new Date(b.fecha));
        eventosGenerales.replaceChildren();

        if (!eventos.length) {
            eventosGenerales.textContent = "No hay eventos para esta fecha.";
        } else {
            eventos.forEach(evento => eventosGenerales.append(crearTarjeta(evento, true)));
        }
        if (estadoAgenda) estadoAgenda.textContent = "";
    }

    // Muestra las mascotas disponibles para consultar o asociar eventos.
    
    function mostrarMascotas() {
        if (mascotasAgenda) {
            mascotasAgenda.replaceChildren();
            if (!mascotas.length) {
                mascotasAgenda.textContent = "Aún no tienes mascotas registradas.";
            }
            mascotas.forEach(mascota => {
                const enlace = document.createElement("a");
                enlace.className = "btn_mascota_card";
                enlace.href = `agenda_personalizada.html?mascota=${encodeURIComponent(mascota.idMascota)}`;
                enlace.textContent = `Ver agenda de ${mascota.nombre || "mascota"}`;
                mascotasAgenda.append(enlace);
            });
        }

        if (selectorMascota) {
            selectorMascota.replaceChildren();
            mascotas.forEach(mascota => {
                const opcion = document.createElement("option");
                opcion.value = mascota.idMascota;
                opcion.textContent = mascota.nombre || "Mascota";
                selectorMascota.append(opcion);
            });
        }

        if (estadoMascotas) estadoMascotas.textContent = "";
    }

    // Carga la información inicial del usuario, sus mascotas y sus eventos.
    
    async function iniciar() {
        try {
            mascotas = await solicitar(`/mascotas/usuario/${encodeURIComponent(idUsuario)}`);
            mostrarMascotas();

            const selectorFecha = document.getElementById("calendario_principal");
            if (selectorFecha) {
                selectorFecha.value = fechaLocal(selectorFecha.value);
                selectorFecha.addEventListener("change", cargarEventos);
            }

            if (esAgendaPersonalizada) {
                const idMascota = new URLSearchParams(window.location.search).get("mascota");
                const mascota = mascotas.find(item => String(item.idMascota) === String(idMascota));
                if (!mascota) {
                    estadoAgenda.textContent = "No se encontró esa mascota en tu cuenta.";
                    return;
                }
                document.getElementById("tituloAgendaMascota").textContent = `Agenda de ${mascota.nombre}`;
                selectorMascota.value = mascota.idMascota;
            }

            await cargarEventos();
        } catch (error) {
            mostrarError(error);
            if (estadoMascotas) estadoMascotas.textContent = "No se pudieron cargar tus mascotas.";
        }
    }

    // Abre el formulario para registrar un nuevo evento.
    
    document.getElementById("btnNuevoEvento")?.addEventListener("click", function () {
        if (!mascotas.length) {
            if (estadoAgenda) estadoAgenda.textContent = "Registra una mascota antes de crear un evento.";
            return;
        }
        const fecha = document.getElementById("fechaEvento");
        if (fecha && !fecha.value) fecha.value = fechaLocal();
        dialogo.showModal();
    });

    // Cierra el formulario de registro de eventos.
    
    document.getElementById("cerrarDialogoEvento")?.addEventListener("click", function () {
        dialogo.close();
    });

    // Registra un nuevo evento asociado a una mascota.
    
    formulario?.addEventListener("submit", async function (event) {
        event.preventDefault();
        const fecha = document.getElementById("fechaEvento").value;
        const hora = document.getElementById("horaEvento").value;

        try {
            await solicitar("/eventos/guardar", {
                method: "POST",
                body: JSON.stringify({
                    tipoEvento: document.getElementById("tipoEvento").value.trim(),
                    fecha: `${fecha}T${hora}:00`,
                    descripcion: document.getElementById("descripcionEvento").value.trim(),
                    mascota: { idMascota: Number(selectorMascota.value) }
                })
            });
            formulario.reset();
            dialogo.close();
            await cargarEventos();
        } catch (error) {
            mostrarError(error);
        }
    });

    iniciar();
});