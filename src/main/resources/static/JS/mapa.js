// Inicializa el mapa cuando la página termina de cargar.

document.addEventListener("DOMContentLoaded", () => {

    // Configura el mapa y sus elementos principales.

    const mapa = L.map("mapa").setView([4.5709, -74.2973], 6);

    L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
        maxZoom: 19,
        attribution: "&copy; OpenStreetMap"
    }).addTo(mapa);

    // Obtiene los elementos utilizados para buscar lugares y gestionar la ubicación.

    const buscador = document.getElementById("buscadorMapa");
    const botonBuscar = document.getElementById("btnBuscar");
    const botonUbicacion = document.getElementById("btnMiUbicacion");
    const mensaje = document.getElementById("mensajeMapa");

    let marcadorUsuario = null;
    let ubicacionUsuario = null;

    // Solicita permiso para acceder a la ubicación y centra el mapa cuando es autorizada.

    function obtenerUbicacion() {

        if (!navigator.geolocation) {

            mensaje.textContent =
                "Tu navegador no permite obtener la ubicación.";

            return;
        }

        mensaje.textContent =
            "Solicitando permiso para acceder a tu ubicación...";

        navigator.geolocation.getCurrentPosition(

            (posicion) => {

                const latitud = posicion.coords.latitude;
                const longitud = posicion.coords.longitude;

                ubicacionUsuario = [latitud, longitud];

                mapa.setView(ubicacionUsuario, 16);

                if (marcadorUsuario) {
                    mapa.removeLayer(marcadorUsuario);
                }

                marcadorUsuario = L.marker(ubicacionUsuario)
                    .addTo(mapa)
                    .bindPopup("<strong>Tu ubicación</strong>")
                    .openPopup();

                mensaje.textContent =
                    "Ubicación encontrada. Puedes buscar lugares cercanos.";
            },

            (error) => {

                switch (error.code) {

                    case error.PERMISSION_DENIED:

                        mensaje.textContent =
                            "No permitiste el acceso a tu ubicación. Permite el acceso desde el navegador para utilizar esta función.";

                        break;

                    case error.POSITION_UNAVAILABLE:

                        mensaje.textContent =
                            "No fue posible obtener tu ubicación.";

                        break;

                    case error.TIMEOUT:

                        mensaje.textContent =
                            "La ubicación tardó demasiado. Intenta nuevamente.";

                        break;

                    default:

                        mensaje.textContent =
                            "Ocurrió un error al obtener tu ubicación.";
                }
            },

            {
                enableHighAccuracy: true,
                timeout: 15000,
                maximumAge: 0
            }
        );
    }

    // Regresa el mapa a la ubicación actual del usuario.

    botonUbicacion.addEventListener("click", () => {

        if (ubicacionUsuario) {

            mapa.setView(ubicacionUsuario, 16);

            if (marcadorUsuario) {
                marcadorUsuario.openPopup();
            }

        } else {

            obtenerUbicacion();

        }
    });

    // Busca lugares utilizando el servicio de OpenStreetMap.

    async function buscarLugar() {

        const texto = buscador.value.trim();

        if (texto === "") {

            mensaje.textContent =
                "Escribe un lugar para realizar la búsqueda.";

            return;
        }

        mensaje.textContent =
            "Buscando...";

        try {

            const respuesta = await fetch(
                `https://nominatim.openstreetmap.org/search?format=json&limit=5&q=${encodeURIComponent(texto)}`
            );

            if (!respuesta.ok) {
                throw new Error("Error en la búsqueda");
            }

            const resultados = await respuesta.json();

            if (resultados.length === 0) {

                mensaje.textContent =
                    "No se encontraron resultados.";

                return;
            }

            const resultado = resultados[0];

            const latitud = parseFloat(resultado.lat);
            const longitud = parseFloat(resultado.lon);

            mapa.setView([latitud, longitud], 16);

            L.marker([latitud, longitud])
                .addTo(mapa)
                .bindPopup(`<strong>${resultado.display_name}</strong>`)
                .openPopup();

            mensaje.textContent =
                "Resultado encontrado.";

        } catch (error) {

            console.error(error);

            mensaje.textContent =
                "No fue posible realizar la búsqueda.";
        }
    }

    // Ejecuta la búsqueda cuando se presiona el botón correspondiente.

    botonBuscar.addEventListener("click", buscarLugar);

    // Permite realizar la búsqueda presionando la tecla Enter.

    buscador.addEventListener("keypress", (evento) => {

        if (evento.key === "Enter") {
            buscarLugar();
        }
    });

    // Solicita el permiso de ubicación al ingresar al mapa.

    obtenerUbicacion();

});