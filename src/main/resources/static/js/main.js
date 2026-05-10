document.addEventListener("DOMContentLoaded", function() {

    // --- Lógica de Reservas (Index) ---
    const inputFecha = document.getElementById('fechaReserva');
    if (inputFecha) {
        initFechaValidador(inputFecha);
    }

    // --- Lógica de Éxito (Success) ---
    const successCard = document.querySelector('.success-card');
    if (successCard) {
        initSuccessEffects();
    }

});

/**
 * Validador de fechas (Tu lógica original)
 */
function initFechaValidador(inputFecha) {
    const ahora = new Date();
    const argentinaOffset = -3 * 60;
    const localTime = new Date(ahora.getTime() + (argentinaOffset + ahora.getTimezoneOffset()) * 60000);

    const year = localTime.getFullYear();
    const month = String(localTime.getMonth() + 1).padStart(2, '0');
    const day = String(localTime.getDate()).padStart(2, '0');
    const hours = String(localTime.getHours()).padStart(2, '0');
    const minutes = String(localTime.getMinutes()).padStart(2, '0');

    const minDateTime = `${year}-${month}-${day}T${hours}:${minutes}`;
    inputFecha.min = minDateTime;

    inputFecha.addEventListener('change', function() {
        if (this.value < this.min) {
            this.value = this.min;
            this.classList.add('is-invalid');
        } else {
            this.classList.remove('is-invalid');
        }
    });
}

/**
 * Funciones adicionales para la página de éxito
 */
function initSuccessEffects() {
    console.log("¡Reserva confirmada con éxito!");
    // Aquí podrías añadir un confetti.js o un log de analítica
}

/**
 * Prevención de acceso a páginas protegidas mediante el botón "Atrás"
 * después de cerrar sesión.
 */
window.addEventListener('pageshow', function (event) {
    // event.persisted es true si la página se carga desde la caché del navegador
    // performance.getEntriesByType("navigation")[0].type detecta si es un "back_forward"
    const historyTraversal = event.persisted ||
        (typeof window.performance != "undefined" &&
            window.performance.getEntriesByType("navigation")[0].type === "back_forward");

    if (historyTraversal) {
        // Forzamos la recarga de la página.
        // Al recargar, Spring Security verificará la sesión y redirigirá al login si no existe.
        window.location.reload();
    }
});