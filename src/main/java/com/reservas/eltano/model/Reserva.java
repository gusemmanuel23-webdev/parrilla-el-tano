package com.reservas.eltano.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data // Si no usas Lombok, recordá generar Getters y Setters manualmente
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String clienteNombre;
    private Integer cantidadPersonas;
    private LocalDateTime fechaHora;
    private String telefono;
    @Column(columnDefinition = "varchar(255) default 'PENDIENTE'")
    private String estado = "PENDIENTE";
}