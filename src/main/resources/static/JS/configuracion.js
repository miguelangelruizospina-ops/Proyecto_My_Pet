// Inicializa la configuración de My Pet cuando el contenido de la página ha terminado de cargar.

document.addEventListener("DOMContentLoaded", function () {

    // Controles principales de la configuración.

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

    // Carga la configuración guardada del usuario.

    cargarTema();
    cargarTamanoLetra();
    cargarColor();
    cargarContraste();
    cargarNotificaciones();

    // Gestiona el cambio del tema de la aplicación.
    
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

    // Carga el tema almacenado y lo aplica a la página.
    
    function cargarTema() {

        const valor =
            localStorage.getItem("temaMyPet") || "claro";

        if (tema) {
            tema.value = valor;
        }

        aplicarTema(valor);

    }

    // Aplica el tema seleccionado a la interfaz.
   
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

    // Gestiona el cambio del tamaño de letra.

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

    // Carga el tamaño de letra almacenado y lo aplica a la página.
    
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

    // Aplica el tamaño de letra seleccionado a la interfaz.
    
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

    // Gestiona la selección del color principal de la interfaz.
    
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

    // Carga el color almacenado y lo aplica a la interfaz.
    
    function cargarColor() {

        const color =
            localStorage.getItem(
                "colorMyPet"
            ) || "naranja";

        aplicarColor(color);

    }

    // Valida y aplica el color seleccionado a la interfaz.
    
    function aplicarColor(color) {

        // Valida que el color seleccionado pertenezca a las opciones disponibles.
        
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

        // Aplica la clase correspondiente al color seleccionado.
        
        document.body.classList.remove(
            "color-naranja-activo",
            "color-azul-activo",
            "color-verde-activo",
            "color-morado-activo"
        );

        document.body.classList.add(
            "color-" + color + "-activo"
        );

        // Identifica el color activo en la configuración de la página.
        
        document.documentElement.setAttribute(
            "data-color",
            color
        );

        // Actualiza el botón que corresponde al color seleccionado.
       
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

    // Gestiona el cambio de la configuración de alto contraste.
    
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

    // Carga la configuración de contraste almacenada y la aplica.
   
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

    // Aplica o desactiva el modo de alto contraste.
    
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

    // Gestiona el cambio de la configuración de notificaciones.
    
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

    // Carga el estado de las notificaciones almacenado por el usuario.
    
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

    // Gestiona la eliminación de la cuenta y el cierre de la sesión local.
    
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
                 * La eliminación física de la cuenta se conectará
                 * posteriormente con el backend.
                 * La eliminación definitiva queda pendiente durante el periodo de pruebas para evitar pérdida de datos.
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