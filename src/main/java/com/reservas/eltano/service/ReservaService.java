package com.reservas.eltano.service;

import com.reservas.eltano.model.Reserva;
import com.reservas.eltano.model.Configuracion;
import com.reservas.eltano.repository.ReservaRepository;
import com.reservas.eltano.repository.ConfiguracionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private ConfiguracionRepository configuracionRepository;

    /**
     * Verifica si el restaurante está aceptando reservas.
     * Busca la clave 'SISTEMA_ABIERTO' en la tabla de configuración.
     */
    public boolean estaElSistemaAbierto() {
        return configuracionRepository.findByClave("SISTEMA_ABIERTO")
                .map(c -> c.getValor().equals("true"))
                .orElse(true); // Por defecto abierto si no existe el registro
    }

    public void guardarReserva(Reserva reserva) {
        if (!estaElSistemaAbierto()) {
            throw new RuntimeException("Lo sentimos, el sistema de reservas está cerrado por el momento.");
        }
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

    public Reserva obtenerPorId(Long id) {
        // findById devuelve un Optional, usamos orElse(null)
        // para que si no la encuentra, devuelva null y no rompa el programa.
        return reservaRepository.findById(id).orElse(null);
    }

    public void eliminarReserva(Long id) {
        reservaRepository.deleteById(id);
    }

    // --- MÉTODOS CON EL FIX DE PRECISIÓN ---

    public List<Reserva> obtenerPorFecha(LocalDate fecha) {
        LocalDateTime inicio = fecha.atStartOfDay();
        LocalDateTime fin = inicio.plusDays(1); // El día siguiente a las 00:00:00
        return reservaRepository.findReservasDelDia(inicio, fin);
    }

    public Integer obtenerTotalComensalesPorFecha(LocalDate fecha) {
        LocalDateTime inicio = fecha.atStartOfDay();
        LocalDateTime fin = inicio.plusDays(1);
        Integer total = reservaRepository.sumarTotalPersonasPorFecha(inicio, fin);
        return (total != null) ? total : 0;
    }

    /**
     * Permite al administrador abrir o cerrar el sistema.
     */
    public void setEstadoSistema(boolean abierto) {
        Configuracion config = configuracionRepository.findByClave("SISTEMA_ABIERTO")
                .orElse(new Configuracion());
        config.setClave("SISTEMA_ABIERTO");
        config.setValor(String.valueOf(abierto));
        configuracionRepository.save(config); // O configuracionRepository.save(config) dependiendo de tu setup
    }
}