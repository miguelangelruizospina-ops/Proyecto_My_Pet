document.addEventListener("DOMContentLoaded", function () {
    const estadoConexion = document.getElementById("estadoConexion");
    const estadoReporte = document.getElementById("estadoReporte");
    const botonConexion = document.getElementById("btnProbarConexion");

    function obtenerUsuario() {
        try {
            return JSON.parse(localStorage.getItem("usuarioLogueado") || "null");
        } catch (error) {
            return null;
        }
    }

    botonConexion.addEventListener("click", async function () {
        botonConexion.disabled = true;
        estadoConexion.textContent = "Comprobando...";

        try {
            const usuario = obtenerUsuario();
            const idUsuario = usuario && (usuario.idUsuario || usuario.id_usuario || localStorage.getItem("idUsuario"));
            const ruta = idUsuario
                ? `/api/mascotas/usuario/${encodeURIComponent(idUsuario)}`
                : "/api/usuario/csrf";
            const respuesta = await fetch(ruta);

            if (!respuesta.ok) {
                throw new Error(`El servidor respondió ${respuesta.status}.`);
            }

            estadoConexion.textContent = idUsuario
                ? "Servidor, sesión y consulta de datos responden correctamente."
                : "El servidor responde. Inicia sesión para comprobar también la consulta de datos.";
        } catch (error) {
            console.error("Fallo comprobando la conexión:", error);
            estadoConexion.textContent = "No se pudo conectar. Comprueba que Spring Boot y MySQL estén iniciados.";
        } finally {
            botonConexion.disabled = false;
        }
    });

    document.getElementById("btnCopiarReporte").addEventListener("click", async function () {
        const usuario = obtenerUsuario();
        const reporte = [
            `Problema: ${document.getElementById("descripcionProblema").value.trim() || "Sin descripción"}`,
            `Página: ${window.location.href}`,
            `Fecha: ${new Date().toLocaleString("es")}`,
            `Navegador: ${navigator.userAgent}`,
            `Sesión: ${usuario ? "iniciada" : "no iniciada"}`,
            `Estado de conexión: ${estadoConexion.textContent}`
        ].join("\n");

        try {
            await navigator.clipboard.writeText(reporte);
            estadoReporte.textContent = "Reporte copiado.";
        } catch (error) {
            console.error("No se pudo copiar el reporte:", error);
            estadoReporte.textContent = "El navegador no permitió copiarlo. Selecciona y copia manualmente el texto de descripción.";
        }
    });
});