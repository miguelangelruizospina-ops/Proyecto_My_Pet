const API_BASE = `${window.location.origin}/api`;

const formularioChatbot = document.getElementById("formChatbot");
const mensajeChatbot = document.getElementById("mensajeChatbot");
const conversacionChatbot = document.getElementById("conversacionChatbot");
const btnEnviarChatbot = document.getElementById("btnEnviarChatbot");
const btnEliminarConversacion = document.getElementById("btnEliminarConversacion");
const indicadorChatbot = document.getElementById("indicadorChatbot");

let historialChatbot = [];


/* ==========================================
   OBTENER ID DEL USUARIO
   ========================================== */

function obtenerIdUsuario() {

    const usuarioGuardado = localStorage.getItem("usuarioLogueado");

    if (usuarioGuardado) {

        try {

            const usuario = JSON.parse(usuarioGuardado);

            if (usuario.idUsuario) {
                return usuario.idUsuario;
            }

            if (usuario.id_usuario) {
                return usuario.id_usuario;
            }

        } catch (error) {

            console.error(
                "No se pudo leer usuarioLogueado:",
                error
            );

        }
    }

    const idUsuario = localStorage.getItem("idUsuario");

    if (idUsuario) {
        return Number(idUsuario);
    }

    return null;
}


/* ==========================================
   CREAR MENSAJE EN LA CONVERSACIÓN
   ========================================== */

function agregarMensaje(tipo, texto) {

    const contenedorMensaje = document.createElement("div");
    const contenidoMensaje = document.createElement("div");

    contenedorMensaje.classList.add(tipo);
    contenidoMensaje.classList.add("mensaje-contenido");

    contenidoMensaje.textContent = texto;

    contenedorMensaje.appendChild(contenidoMensaje);
    conversacionChatbot.appendChild(contenedorMensaje);

    conversacionChatbot.scrollTop =
        conversacionChatbot.scrollHeight;
}


/* ==========================================
   MOSTRAR MENSAJE INICIAL
   ========================================== */

function mostrarMensajeInicial() {

    conversacionChatbot.replaceChildren();

    agregarMensaje(
        "mensaje-chatbot",
        "Hola. Soy el asistente virtual de My Pet. Puedo orientarte sobre alimentación, cuidados, higiene, comportamiento, vacunación y prevención."
    );
}


/* ==========================================
   CARGAR HISTORIAL
   ========================================== */

async function cargarHistorial() {

    const idUsuario = obtenerIdUsuario();

    if (!idUsuario) {

        console.warn(
            "No se encontró el usuario conectado."
        );

        mostrarMensajeInicial();

        return;
    }

    try {

        const respuesta = await fetch(
            `${API_BASE}/chatbot/historial/${idUsuario}`
        );

        if (!respuesta.ok) {

            throw new Error(
                "No se pudo cargar el historial."
            );

        }

        historialChatbot = await respuesta.json();

        conversacionChatbot.replaceChildren();

        if (historialChatbot.length === 0) {

            mostrarMensajeInicial();

            return;
        }

        historialChatbot.forEach(registro => {

            agregarMensaje(
                "mensaje-usuario",
                registro.mensaje
            );

            agregarMensaje(
                "mensaje-chatbot",
                registro.respuesta
            );

        });

    } catch (error) {

        console.error(
            "Error al cargar historial:",
            error
        );

        mostrarMensajeInicial();
    }
}


/* ==========================================
   ENVIAR MENSAJE A GEMINI
   ========================================== */

async function enviarMensaje() {

    const mensaje = mensajeChatbot.value.trim();

    if (!mensaje) {
        return;
    }

    const idUsuario = obtenerIdUsuario();

    if (!idUsuario) {

        alert(
            "No se encontró el usuario conectado."
        );

        return;
    }

    agregarMensaje(
        "mensaje-usuario",
        mensaje
    );

    mensajeChatbot.value = "";

    btnEnviarChatbot.disabled = true;

    indicadorChatbot.style.display = "block";

    try {

        const parametros = new URLSearchParams();

        parametros.append(
            "idUsuario",
            idUsuario
        );

        parametros.append(
            "mensaje",
            mensaje
        );

        const respuesta = await fetch(
            `${API_BASE}/chatbot/mensaje?${parametros.toString()}`,
            {
                method: "POST"
            }
        );

        if (!respuesta.ok) {

            const textoError = await respuesta.text();

            throw new Error(
                textoError ||
                "No se pudo obtener la respuesta del chatbot."
            );
        }

        const datos = await respuesta.json();

        agregarMensaje(
            "mensaje-chatbot",
            datos.respuesta
        );

        historialChatbot.push(datos);

    } catch (error) {

        console.error(
            "Error al enviar mensaje:",
            error
        );

        agregarMensaje(
            "mensaje-chatbot",
            "No pude procesar tu mensaje en este momento. Intenta nuevamente."
        );

    } finally {

        indicadorChatbot.style.display = "none";

        btnEnviarChatbot.disabled = false;

        mensajeChatbot.focus();
    }
}


/* ==========================================
   ELIMINAR UN MENSAJE DEL HISTORIAL
   ========================================== */

async function eliminarMensaje(idChatbot) {

    const respuesta = await fetch(
        `${API_BASE}/chatbot/eliminar/${idChatbot}`,
        {
            method: "DELETE"
        }
    );

    if (!respuesta.ok) {

        throw new Error(
            "No se pudo eliminar el mensaje."
        );
    }
}


/* ==========================================
   LIMPIAR TODA LA CONVERSACIÓN
   ========================================== */

async function limpiarConversacion() {

    if (historialChatbot.length === 0) {

        mostrarMensajeInicial();

        return;
    }

    const confirmar = confirm(
        "¿Quieres eliminar toda la conversación?"
    );

    if (!confirmar) {
        return;
    }

    btnEliminarConversacion.disabled = true;

    try {

        for (const registro of historialChatbot) {

            if (registro.idChatbot) {

                await eliminarMensaje(
                    registro.idChatbot
                );
            }
        }

        historialChatbot = [];

        mostrarMensajeInicial();

    } catch (error) {

        console.error(
            "Error al limpiar conversación:",
            error
        );

        alert(
            "No fue posible eliminar toda la conversación."
        );

        await cargarHistorial();

    } finally {

        btnEliminarConversacion.disabled = false;
    }
}


/* ==========================================
   EVENTO DEL FORMULARIO
   ========================================== */

if (formularioChatbot) {

    formularioChatbot.addEventListener(
        "submit",
        function (evento) {

            evento.preventDefault();

            enviarMensaje();
        }
    );
}


/* ==========================================
   BOTÓN LIMPIAR CONVERSACIÓN
   ========================================== */

if (btnEliminarConversacion) {

    btnEliminarConversacion.addEventListener(
        "click",
        function () {

            limpiarConversacion();
        }
    );
}


/* ==========================================
   INICIAR CHATBOT
   ========================================== */

document.addEventListener(
    "DOMContentLoaded",
    function () {

        cargarHistorial();

        if (mensajeChatbot) {
            mensajeChatbot.focus();
        }

    }
);