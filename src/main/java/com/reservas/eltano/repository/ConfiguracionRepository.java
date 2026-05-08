package com.reservas.eltano.repository;

import com.reservas.eltano.model.Configuracion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ConfiguracionRepository extends JpaRepository<Configuracion, String> {
    // Buscamos por la clave "SISTEMA_ABIERTO"
    Optional<Configuracion> findByClave(String clave);
}