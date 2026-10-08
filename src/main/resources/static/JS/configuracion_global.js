// Configuración global de My Pet.

(function () {

    // Aplica la configuración general de la aplicación.
   
    function aplicarConfiguracionGlobal() {

        const body = document.body;

        if (!body) {
            return;
        }

        // Aplica el tema seleccionado por el usuario.
       
        let tema =
            localStorage.getItem("temaMyPet") || "claro";

        if (
            tema !== "claro" &&
            tema !== "oscuro"
        ) {
            tema = "claro";
        }

        body.classList.remove(
            "tema-claro",
            "tema-oscuro"
        );

        body.classList.add(
            tema === "oscuro"
                ? "tema-oscuro"
                : "tema-claro"
        );

        document.documentElement.setAttribute(
            "data-tema",
            tema
        );

        // Aplica el tamaño de letra seleccionado por el usuario.
       
        let tamanoLetra =
            localStorage.getItem("tamanoLetraMyPet")
            || "normal";

        const tamanosValidos = [
            "pequena",
            "normal",
            "grande",
            "muy-grande"
        ];

        if (!tamanosValidos.includes(tamanoLetra)) {
            tamanoLetra = "normal";
        }

        body.classList.remove(
            "letra-pequena",
            "letra-normal",
            "letra-grande",
            "letra-muy-grande"
        );

        body.classList.add(
            "letra-" + tamanoLetra
        );

        document.documentElement.setAttribute(
            "data-tamano-letra",
            tamanoLetra
        );

        // Aplica el color principal seleccionado por el usuario.
       
        let color =
            localStorage.getItem("colorMyPet")
            || "naranja";

        const coloresValidos = [
            "naranja",
            "azul",
            "verde",
            "morado"
        ];

        if (!coloresValidos.includes(color)) {
            color = "naranja";
        }

        body.classList.remove(
            "color-naranja-activo",
            "color-azul-activo",
            "color-verde-activo",
            "color-morado-activo"
        );

        body.classList.add(
            "color-" + color + "-activo"
        );

        document.documentElement.setAttribute(
            "data-color",
            color
        );

        // Aplica la configuración de alto contraste.
        
        const altoContraste =
            localStorage.getItem(
                "altoContrasteMyPet"
            ) === "true";

        body.classList.toggle(
            "alto-contraste",
            altoContraste
        );

        document.documentElement.setAttribute(
            "data-contraste",
            altoContraste
                ? "alto"
                : "normal"
        );

        // Verifica y configura el estado de las notificaciones.
       
        const notificaciones =
            localStorage.getItem(
                "notificacionesMyPet"
            );

        // Si nunca se ha configurado,
        // las notificaciones quedan activadas.
       
        if (notificaciones === null) {

            localStorage.setItem(
                "notificacionesMyPet",
                "true"
            );

        }

        // Marca que la configuración global de My Pet ya fue aplicada.
        
        body.classList.add(
            "configuracion-mypet-aplicada"
        );

    }

    // Ejecuta la configuración cuando termina de cargar la página.
    
    if (document.readyState === "loading") {

        document.addEventListener(
            "DOMContentLoaded",
            aplicarConfiguracionGlobal
        );

    } else {

        aplicarConfiguracionGlobal();

    }

})();