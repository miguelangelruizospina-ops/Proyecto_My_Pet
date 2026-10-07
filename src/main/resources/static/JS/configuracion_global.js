
// =====================================================
// CONFIGURACIÓN GLOBAL - MY PET
// Aplica las preferencias guardadas en toda la aplicación
// =====================================================

(function () {

    // =================================================
    // APLICAR CONFIGURACIÓN
    // =================================================

    function aplicarConfiguracionGlobal() {

        const body = document.body;

        if (!body) {
            return;
        }


        // =================================================
        // TEMA
        // =================================================

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


        // =================================================
        // TAMAÑO DE LETRA
        // =================================================

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


        // =================================================
        // COLOR PRINCIPAL
        // =================================================

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


        // =================================================
        // ALTO CONTRASTE
        // =================================================

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


        // =================================================
        // NOTIFICACIONES
        // =================================================

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


        // =================================================
        // MARCAR CONFIGURACIÓN APLICADA
        // =================================================

        body.classList.add(
            "configuracion-mypet-aplicada"
        );

    }


    // =====================================================
    // EJECUTAR AL CARGAR LA PÁGINA
    // =====================================================

    if (document.readyState === "loading") {

        document.addEventListener(
            "DOMContentLoaded",
            aplicarConfiguracionGlobal
        );

    } else {

        aplicarConfiguracionGlobal();

    }

})();
