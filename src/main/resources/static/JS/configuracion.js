
// =====================================================
// CONFIGURACIÓN GLOBAL - MY PET
// =====================================================

document.addEventListener("DOMContentLoaded", function () {

    // =====================================================
    // CONTROLES
    // =====================================================

    const tema = document.getElementById("tema");

    const tamanoLetra =
        document.getElementById("tamanoLetra");

    const altoContraste =
        document.getElementById("altoContraste");

    const notificaciones =
        document.getElementById("notificaciones");

    const btnEliminarCuenta =
        document.getElementById("btnEliminarCuenta");

    const botonesColor =
        document.querySelectorAll(".color-opcion");


    // =====================================================
    // CARGAR CONFIGURACIÓN GUARDADA
    // =====================================================

    cargarTema();
    cargarTamanoLetra();
    cargarColor();
    cargarContraste();
    cargarNotificaciones();


    // =====================================================
    // TEMA
    // =====================================================

    if (tema) {

        tema.addEventListener("change", function () {

            const valor = tema.value;

            localStorage.setItem(
                "temaMyPet",
                valor
            );

            aplicarTema(valor);

        });

    }


    function cargarTema() {

        const valor =
            localStorage.getItem("temaMyPet") || "claro";

        if (tema) {
            tema.value = valor;
        }

        aplicarTema(valor);

    }


    function aplicarTema(valor) {

        document.body.classList.remove(
            "tema-claro",
            "tema-oscuro"
        );

        document.body.classList.add(
            valor === "oscuro"
                ? "tema-oscuro"
                : "tema-claro"
        );

        document.documentElement.setAttribute(
            "data-tema",
            valor
        );

    }


    // =====================================================
    // TAMAÑO DE LETRA
    // =====================================================

    if (tamanoLetra) {

        tamanoLetra.addEventListener(
            "change",
            function () {

                const valor =
                    tamanoLetra.value;

                localStorage.setItem(
                    "tamanoLetraMyPet",
                    valor
                );

                aplicarTamanoLetra(valor);

            }
        );

    }


    function cargarTamanoLetra() {

        const valor =
            localStorage.getItem(
                "tamanoLetraMyPet"
            ) || "normal";

        if (tamanoLetra) {
            tamanoLetra.value = valor;
        }

        aplicarTamanoLetra(valor);

    }


    function aplicarTamanoLetra(valor) {

        document.body.classList.remove(
            "letra-pequena",
            "letra-normal",
            "letra-grande",
            "letra-muy-grande"
        );

        document.body.classList.add(
            "letra-" + valor
        );

        document.documentElement.setAttribute(
            "data-tamano-letra",
            valor
        );

    }


    // =====================================================
    // COLOR DE INTERFAZ
    // =====================================================

    botonesColor.forEach(function (boton) {

        boton.addEventListener(
            "click",
            function () {

                let color = null;


                if (
                    boton.classList.contains(
                        "color-naranja"
                    )
                ) {
                    color = "naranja";
                }


                if (
                    boton.classList.contains(
                        "color-azul"
                    )
                ) {
                    color = "azul";
                }


                if (
                    boton.classList.contains(
                        "color-verde"
                    )
                ) {
                    color = "verde";
                }


                if (
                    boton.classList.contains(
                        "color-morado"
                    )
                ) {
                    color = "morado";
                }


                if (!color) {
                    return;
                }


                localStorage.setItem(
                    "colorMyPet",
                    color
                );

                aplicarColor(color);

            }
        );

    });


    function cargarColor() {

        const color =
            localStorage.getItem(
                "colorMyPet"
            ) || "naranja";

        aplicarColor(color);

    }


    function aplicarColor(color) {

        // =================================================
        // VALIDAR COLOR
        // =================================================

        const coloresValidos = [
            "naranja",
            "azul",
            "verde",
            "morado"
        ];


        if (
            !coloresValidos.includes(color)
        ) {
            color = "naranja";
        }


        // =================================================
        // CLASE DE COLOR EN BODY
        // =================================================

        document.body.classList.remove(
            "color-naranja-activo",
            "color-azul-activo",
            "color-verde-activo",
            "color-morado-activo"
        );

        document.body.classList.add(
            "color-" + color + "-activo"
        );


        // =================================================
        // IDENTIFICACIÓN DEL COLOR ACTIVO
        // =================================================

        document.documentElement.setAttribute(
            "data-color",
            color
        );


        // =================================================
        // MARCAR BOTÓN SELECCIONADO
        // =================================================

        botonesColor.forEach(function (boton) {

            boton.classList.remove(
                "seleccionado"
            );

        });


        const botonActivo =
            document.querySelector(
                ".color-" + color
            );


        if (botonActivo) {

            botonActivo.classList.add(
                "seleccionado"
            );

        }

    }


    // =====================================================
    // ALTO CONTRASTE
    // =====================================================

    if (altoContraste) {

        altoContraste.addEventListener(
            "change",
            function () {

                const activado =
                    altoContraste.checked;

                localStorage.setItem(
                    "altoContrasteMyPet",
                    activado
                );

                aplicarContraste(
                    activado
                );

            }
        );

    }


    function cargarContraste() {

        const guardado =
            localStorage.getItem(
                "altoContrasteMyPet"
            );


        const activado =
            guardado === "true";


        if (altoContraste) {

            altoContraste.checked =
                activado;

        }


        aplicarContraste(
            activado
        );

    }


    function aplicarContraste(activado) {

        document.body.classList.toggle(
            "alto-contraste",
            activado
        );

        document.documentElement.setAttribute(
            "data-contraste",
            activado
                ? "alto"
                : "normal"
        );

    }


    // =====================================================
    // NOTIFICACIONES
    // =====================================================

    if (notificaciones) {

        notificaciones.addEventListener(
            "change",
            function () {

                const activadas =
                    notificaciones.checked;

                localStorage.setItem(
                    "notificacionesMyPet",
                    activadas
                );

            }
        );

    }


    function cargarNotificaciones() {

        const guardado =
            localStorage.getItem(
                "notificacionesMyPet"
            );


        if (!notificaciones) {
            return;
        }


        if (guardado === null) {

            notificaciones.checked = true;

            localStorage.setItem(
                "notificacionesMyPet",
                "true"
            );

        } else {

            notificaciones.checked =
                guardado === "true";

        }

    }


    // =====================================================
    // ELIMINAR CUENTA
    // =====================================================

    if (btnEliminarCuenta) {

        btnEliminarCuenta.addEventListener(
            "click",
            function () {

                const confirmar =
                    confirm(
                        "¿Está seguro de que desea eliminar su cuenta? Esta acción no se puede deshacer."
                    );


                if (!confirmar) {
                    return;
                }


                /*
                 * Por ahora solamente se cierra la sesión local.
                 *
                 * La eliminación física de la cuenta se conectará
                 * posteriormente con el backend.
                 */

                localStorage.removeItem(
                    "usuarioLogueado"
                );

                localStorage.removeItem(
                    "idUsuario"
                );


                alert(
                    "Sesión cerrada."
                );


                window.location.href =
                    "iniciar_sesion.html";

            }
        );

    }

});

