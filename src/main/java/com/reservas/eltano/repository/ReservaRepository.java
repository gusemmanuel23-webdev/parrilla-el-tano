package com.reservas.eltano.repository;

import com.reservas.eltano.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    // Buscar reservas: desde el inicio del día (inclusive) hasta el inicio del siguiente (exclusive)
    @Query("SELECT r FROM Reserva r WHERE r.fechaHora >= :inicio AND r.fechaHora < :fin ORDER BY r.fechaHora ASC")
    List<Reserva> findReservasDelDia(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);

    // Sumar personas con la misma lógica de rango seguro
    @Query("SELECT SUM(r.cantidadPersonas) FROM Reserva r WHERE r.fechaHora >= :inicio AND r.fechaHora < :fin AND r.estado != 'CANCELADA'")
    Integer sumarTotalPersonasPorFecha(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);

    // Mantenemos el orden general por si se necesita
    List<Reserva> findAllByOrderByFechaHoraAsc();
}