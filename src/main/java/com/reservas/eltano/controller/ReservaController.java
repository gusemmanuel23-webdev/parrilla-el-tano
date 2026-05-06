package com.reservas.eltano.controller;

import com.reservas.eltano.model.Reserva;
import com.reservas.eltano.service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // Importación agregada para prolijidad
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @PostMapping("/reservar")
    public String procesarReserva(@ModelAttribute Reserva reserva) {
        reservaService.guardarReserva(reserva);
        return "redirect:/success.html";
    }

    // UNIFICACIÓN: Un solo método para la tabla y el contador
    @GetMapping("/admin/reservas")
    public String listarReservas(Model model) {
        List<Reserva> lista = reservaService.obtenerTodas();
        Integer totalComensales = reservaService.obtenerTotalComensales();

        model.addAttribute("reservas", lista);
        model.addAttribute("total", totalComensales != null ? totalComensales : 0);
        return "admin";
    }

    @GetMapping("/admin/estado/{id}/{nuevoEstado}")
    public String actualizarEstado(@PathVariable Long id, @PathVariable String nuevoEstado) {
        reservaService.cambiarEstado(id, nuevoEstado);
        return "redirect:/admin/reservas";
    }

    @GetMapping("/admin/eliminar/{id}")
    public String eliminarReserva(@PathVariable Long id) {
        reservaService.eliminarReserva(id);
        return "redirect:/admin/reservas";
    }
}