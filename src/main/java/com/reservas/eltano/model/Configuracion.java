package com.reservas.eltano.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Configuracion {
    @Id
    private String clave; // Usaremos "SISTEMA_ABIERTO" como clave
    private String valor; // "true" o "false"
}