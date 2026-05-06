package com.reservas.eltano.repository;

import com.reservas.eltano.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findAllByOrderByFechaHoraAsc();

    @Query("SELECT SUM(r.cantidadPersonas) FROM Reserva r WHERE r.estado != 'CANCELADA'")
    Integer sumarTotalPersonas();
}