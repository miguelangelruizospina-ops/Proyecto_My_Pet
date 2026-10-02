(function () {
    const fetchOriginal = window.fetch.bind(window);
    let tokenRequest = null;

    function leerCookie(nombre) {
        const prefijo = `${nombre}=`;
        const cookie = document.cookie
            .split(";")
            .map(valor => valor.trim())
            .find(valor => valor.startsWith(prefijo));
        return cookie ? decodeURIComponent(cookie.slice(prefijo.length)) : null;
    }

    async function obtenerTokenCsrf() {
        const tokenExistente = leerCookie("XSRF-TOKEN");
        if (tokenExistente) return tokenExistente;

        if (!tokenRequest) {
            tokenRequest = fetchOriginal("/api/usuario/csrf", {
                credentials: "same-origin"
            }).finally(() => {
                tokenRequest = null;
            });
        }

        const respuesta = await tokenRequest;
        if (!respuesta.ok) throw new Error("No se pudo iniciar la sesión segura.");
        return leerCookie("XSRF-TOKEN");
    }

    window.fetch = async function (input, init = {}) {
        const requestUrl = input instanceof Request ? input.url : input;
        const url = new URL(requestUrl, window.location.href);
        if (url.origin !== window.location.origin || !url.pathname.startsWith("/api/")) {
            return fetchOriginal(input, init);
        }

        const method = (init.method || (input instanceof Request ? input.method : "GET")).toUpperCase();
        const headers = new Headers(input instanceof Request ? input.headers : undefined);
        new Headers(init.headers || {}).forEach((valor, nombre) => headers.set(nombre, valor));
        const opciones = {
            ...init,
            headers,
            credentials: init.credentials || "same-origin"
        };

        if (!["GET", "HEAD", "OPTIONS", "TRACE"].includes(method)) {
            const token = await obtenerTokenCsrf();
            headers.set("X-XSRF-TOKEN", token);
        }

        return fetchOriginal(input, opciones);
    };
})();