package com.reservas.eltano.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "El nombre solo puede contener letras")
    private String clienteNombre;

    @NotNull(message = "La cantidad de personas es obligatoria")
    @Min(value = 1, message = "Mínimo 1 persona")
    @Max(value = 20, message = "Máximo 20 personas por reserva web")
    private Integer cantidadPersonas;

    @NotNull(message = "La fecha y hora son obligatorias")
    @Future(message = "La reserva no puede ser en el pasado")
    private LocalDateTime fechaHora;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^[0-9]{8,15}$", message = "El teléfono debe ser solo números (8 a 15 dígitos)")
    private String telefono;

    private String estado = "PENDIENTE";
}