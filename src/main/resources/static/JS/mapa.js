// Inicializa el mapa cuando la página termina de cargar.

document.addEventListener("DOMContentLoaded", () => {

    // Configura el mapa y sus elementos principales.

    const mapa = L.map("mapa").setView([6.2442, -75.5812], 13);

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
    let marcadoresBusqueda = [];
    let solicitudActual = null;

    // Muestra los mensajes informativos del mapa.

    function mostrarMensaje(texto) {
        mensaje.textContent = texto;
    }

    // Elimina los marcadores de búsquedas anteriores.

    function limpiarMarcadores() {
        marcadoresBusqueda.forEach((marcador) => mapa.removeLayer(marcador));
        marcadoresBusqueda = [];
    }

    // Solicita permiso para acceder a la ubicación del usuario.

    function obtenerUbicacion() {

        if (!navigator.geolocation) {
            mostrarMensaje("Tu navegador no permite obtener la ubicación. Puedes buscar por ciudad.");
            return;
        }

        mostrarMensaje("Obteniendo tu ubicación...");

        navigator.geolocation.getCurrentPosition(
            (posicion) => {

                ubicacionUsuario = [
                    posicion.coords.latitude,
                    posicion.coords.longitude
                ];

                mapa.setView(ubicacionUsuario, 15);

                if (marcadorUsuario) {
                    mapa.removeLayer(marcadorUsuario);
                }

                marcadorUsuario = L.marker(ubicacionUsuario)
                    .addTo(mapa)
                    .bindPopup("<strong>Tu ubicación</strong>");

                marcadorUsuario.openPopup();

                mostrarMensaje("Ubicación encontrada. Busca veterinarias, clínicas, parques o tiendas cercanas.");
            },
            (error) => {

                const mensajes = {
                    1: "No permitiste acceder a tu ubicación. Puedes buscar por ciudad.",
                    2: "No fue posible obtener tu ubicación. Puedes buscar por ciudad.",
                    3: "La ubicación tardó demasiado. Puedes buscar por ciudad."
                };

                mostrarMensaje(mensajes[error.code] || "No fue posible obtener tu ubicación.");
            },
            {
                enableHighAccuracy: true,
                timeout: 15000,
                maximumAge: 60000
            }
        );
    }

    // Centra el mapa en la ubicación actual del usuario.

    botonUbicacion.addEventListener("click", () => {

        if (ubicacionUsuario) {
            mapa.setView(ubicacionUsuario, 16);
            marcadorUsuario.openPopup();
        } else {
            obtenerUbicacion();
        }
    });

    // Determina las categorías de lugares que se pueden consultar.

    function obtenerCategorias(texto) {

        const busqueda = texto.toLowerCase();

        if (/veterinar|veterinari|veterinaria/.test(busqueda)) {
            return [
                'node["amenity"="veterinary"]',
                'way["amenity"="veterinary"]',
                'relation["amenity"="veterinary"]'
            ];
        }

        if (/cl[ií]nica|hospital veterinario/.test(busqueda)) {
            return [
                'node["amenity"="veterinary"]',
                'way["amenity"="veterinary"]',
                'relation["amenity"="veterinary"]',
                'node["amenity"="animal_hospital"]',
                'way["amenity"="animal_hospital"]'
            ];
        }

        if (/tienda|accesorios|comida para mascotas|pet shop/.test(busqueda)) {
            return [
                'node["shop"="pet"]',
                'way["shop"="pet"]',
                'relation["shop"="pet"]'
            ];
        }

        if (/parque/.test(busqueda)) {
            return [
                'node["leisure"="park"]',
                'way["leisure"="park"]',
                'relation["leisure"="park"]',
                'node["leisure"="dog_park"]',
                'way["leisure"="dog_park"]'
            ];
        }

        if (/peluquer[ií]a|peluquer[ií]a canina|est[eé]tica canina/.test(busqueda)) {
            return [
                'node["shop"="pet_grooming"]',
                'way["shop"="pet_grooming"]',
                'node["shop"="pet"]'
            ];
        }

        return [
            'node["amenity"="veterinary"]',
            'way["amenity"="veterinary"]',
            'relation["amenity"="veterinary"]',
            'node["shop"="pet"]',
            'way["shop"="pet"]',
            'relation["shop"="pet"]',
            'node["leisure"="dog_park"]',
            'way["leisure"="dog_park"]'
        ];
    }

    // Calcula la distancia aproximada entre dos coordenadas geográficas.

    function calcularDistancia(lat1, lon1, lat2, lon2) {

        const radioTierra = 6371;
        const radianes = Math.PI / 180;

        const diferenciaLatitud = (lat2 - lat1) * radianes;
        const diferenciaLongitud = (lon2 - lon1) * radianes;

        const a =
            Math.sin(diferenciaLatitud / 2) ** 2 +
            Math.cos(lat1 * radianes) *
            Math.cos(lat2 * radianes) *
            Math.sin(diferenciaLongitud / 2) ** 2;

        return radioTierra * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }

    // Busca establecimientos registrados cerca de las coordenadas indicadas.

    async function buscarCercanos(texto, centro) {

        const categorias = obtenerCategorias(texto);
        const radio = 10000;

        const consultas = categorias.map((categoria) =>
            `${categoria}(around:${radio},${centro[0]},${centro[1]});`
        ).join("\n");

        const consulta = `
            [out:json][timeout:25];
            (
                ${consultas}
            );
            out center tags;
        `;

        const respuesta = await fetch(
            "https://overpass-api.de/api/interpreter",
            {
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8"
                },
                body: "data=" + encodeURIComponent(consulta),
                signal: solicitudActual.signal
            }
        );

        if (!respuesta.ok) {
            throw new Error("El servicio de lugares cercanos no respondió.");
        }

        const datos = await respuesta.json();

        return datos.elements.map((lugar) => {

            const latitud = lugar.lat ?? lugar.center?.lat;
            const longitud = lugar.lon ?? lugar.center?.lon;

            return {
                latitud: Number(latitud),
                longitud: Number(longitud),
                nombre: lugar.tags?.name || "Establecimiento sin nombre",
                tipo: lugar.tags?.amenity || lugar.tags?.shop || lugar.tags?.leisure || "Lugar",
                direccion: [
                    lugar.tags?.["addr:street"],
                    lugar.tags?.["addr:housenumber"],
                    lugar.tags?.["addr:city"]
                ].filter(Boolean).join(" "),
                distancia: calcularDistancia(
                    centro[0],
                    centro[1],
                    Number(latitud),
                    Number(longitud)
                )
            };
        }).filter((lugar) =>
            Number.isFinite(lugar.latitud) &&
            Number.isFinite(lugar.longitud)
        );
    }

    // Busca una ciudad o lugar cuando no se dispone de coordenadas cercanas.

    async function buscarCiudad(texto) {

        const parametros = new URLSearchParams({
            format: "jsonv2",
            q: texto,
            countrycodes: "co",
            limit: "1"
        });

        const respuesta = await fetch(
            "https://nominatim.openstreetmap.org/search?" + parametros.toString()
        );

        if (!respuesta.ok) {
            throw new Error("No fue posible localizar la ciudad.");
        }

        const resultados = await respuesta.json();

        if (!resultados.length) {
            return null;
        }

        return [
            Number(resultados[0].lat),
            Number(resultados[0].lon)
        ];
    }

    // Evita interpretar como HTML los nombres de los establecimientos.

    function escaparHTML(texto) {

        return String(texto).replace(/[&<>"']/g, (caracter) => ({
            "&": "&amp;",
            "<": "&lt;",
            ">": "&gt;",
            '"': "&quot;",
            "'": "&#39;"
        })[caracter]);
    }

    // Ejecuta la búsqueda de lugares cercanos o por ciudad.

    async function buscarLugar() {

        const texto = buscador.value.trim();

        if (!texto) {
            mostrarMensaje("Escribe qué lugar deseas buscar.");
            buscador.focus();
            return;
        }

        if (solicitudActual) {
            solicitudActual.abort();
        }

        solicitudActual = new AbortController();

        const controlador = solicitudActual;

        botonBuscar.disabled = true;
        mostrarMensaje("Buscando establecimientos registrados en OpenStreetMap...");

        try {

            let centro;

            const consultaCiudad = /\b(medell[ií]n|bogot[aá]|cali|barranquilla|cartagena|bucaramanga|pereira|manizales|armenia|itag[uü][ií]|colombia)\b/i.test(texto);

            if (consultaCiudad) {
                centro = await buscarCiudad(texto);

                if (!centro) {
                    mostrarMensaje("No se encontró la ciudad o ubicación indicada.");
                    return;
                }
            } else if (ubicacionUsuario) {
                centro = ubicacionUsuario;
            } else {
                centro = await buscarCiudad("Medellín, Colombia");

                if (!centro) {
                    mostrarMensaje("No fue posible determinar una ubicación para la búsqueda.");
                    return;
                }
            }

            if (controlador !== solicitudActual) {
                return;
            }

            const resultados = await buscarCercanos(texto, centro);

            if (controlador !== solicitudActual) {
                return;
            }

            limpiarMarcadores();

            if (resultados.length === 0) {
                mostrarMensaje("No se encontraron establecimientos registrados en un radio de 10 km. Prueba otra categoría o ciudad.");
                mapa.setView(centro, 13);
                return;
            }

            resultados.sort((a, b) => a.distancia - b.distancia);

            resultados.forEach((lugar) => {

                const marcador = L.marker([
                    lugar.latitud,
                    lugar.longitud
                ]).addTo(mapa);

                const contenido = `
                    <strong>${escaparHTML(lugar.nombre)}</strong><br>
                    Tipo: ${escaparHTML(lugar.tipo)}<br>
                    ${lugar.direccion ? "Dirección: " + escaparHTML(lugar.direccion) + "<br>" : ""}
                    Distancia aproximada: ${lugar.distancia.toFixed(2)} km
                `;

                marcador.bindPopup(contenido);
                marcadoresBusqueda.push(marcador);
            });

            const grupo = L.featureGroup(marcadoresBusqueda);

            mapa.fitBounds(grupo.getBounds(), {
                padding: [30, 30],
                maxZoom: 16
            });

            mostrarMensaje(
                `Se encontraron ${resultados.length} lugares registrados en un radio de 10 km. Los resultados están ordenados por distancia.`
            );

        } catch (error) {

            if (error.name === "AbortError") {
                return;
            }

            console.error("Error en la búsqueda de My Pet:", error);

            mostrarMensaje(
                "No fue posible consultar los lugares. El servicio puede estar ocupado; intenta nuevamente."
            );

        } finally {

            if (controlador === solicitudActual) {
                botonBuscar.disabled = false;
                solicitudActual = null;
            }
        }
    }

    // Conecta los controles de búsqueda.

    botonBuscar.addEventListener("click", buscarLugar);

    buscador.addEventListener("keydown", (evento) => {
        if (evento.key === "Enter") {
            evento.preventDefault();
            buscarLugar();
        }
    });

    // Obtiene la ubicación al ingresar al módulo.

    obtenerUbicacion();

});
