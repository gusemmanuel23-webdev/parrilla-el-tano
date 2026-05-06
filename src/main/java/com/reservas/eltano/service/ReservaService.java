package com.reservas.eltano.service;

import com.reservas.eltano.model.Reserva;
import com.reservas.eltano.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    public void guardarReserva(Reserva reserva) {
        reservaRepository.save(reserva);
    }

    public List<Reserva> obtenerTodas() {
        return reservaRepository.findAllByOrderByFechaHoraAsc();
    }

    public void cambiarEstado(Long id, String nuevoEstado) {
        Reserva reserva = reservaRepository.findById(id).orElseThrow();
        reserva.setEstado(nuevoEstado);
        reservaRepository.save(reserva);
    }

    public void eliminarReserva(Long id) {
        reservaRepository.deleteById(id);
    }

    public Integer obtenerTotalComensales() {
        return reservaRepository.sumarTotalPersonas();
    }
}