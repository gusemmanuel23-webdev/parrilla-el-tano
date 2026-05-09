document.addEventListener("DOMContentLoaded", function() {

    const inputFecha = document.getElementById('fechaReserva');



    if (inputFecha) {

        // 1. Sincronización con Zona Horaria Argentina (GMT-3)

        // Usamos un enfoque más robusto para asegurar que 'hoy' sea hoy en Argentina

        const ahora = new Date();

        const argentinaOffset = -3 * 60; // Offset en minutos

        const localTime = new Date(ahora.getTime() + (argentinaOffset + ahora.getTimezoneOffset()) * 60000);



        // Formatear para datetime-local (YYYY-MM-DDTHH:mm)

        const year = localTime.getFullYear();

        const month = String(localTime.getMonth() + 1).padStart(2, '0');

        const day = String(localTime.getDate()).padStart(2, '0');

        const hours = String(localTime.getHours()).padStart(2, '0');

        const minutes = String(localTime.getMinutes()).padStart(2, '0');



        const minDateTime = `${year}-${month}-${day}T${hours}:${minutes}`;



        // 2. Establecer el límite mínimo en el input

        inputFecha.min = minDateTime;



        // 3. Validación mejorada (Sin alerts intrusivos)

        inputFecha.addEventListener('change', function() {

            if (this.value < this.min) {

                // En lugar de alert, reseteamos y podemos marcar el borde en rojo

                this.value = this.min;

                this.classList.add('is-invalid'); // Clase de error si usas Bootstrap o CSS custom

                console.warn("Intento de reserva en fecha pasada bloqueado.");

            } else {

                this.classList.remove('is-invalid');

            }

        });

    }

});