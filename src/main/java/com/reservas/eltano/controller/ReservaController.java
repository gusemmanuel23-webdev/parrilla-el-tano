package com.reservas.eltano.controller;

import com.reservas.eltano.model.Reserva;
import com.reservas.eltano.service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    // --- RUTAS PÚBLICAS (Archivos en /static) ---

    // Al entrar a la raíz, redirigimos al index.html que está en static
    @GetMapping("/")
    public String raiz() {
        return "redirect:/index.html";
    }

    @PostMapping("/reservar")
    public String procesarReserva(@ModelAttribute Reserva reserva) {
        reservaService.guardarReserva(reserva);
        // Redirige al archivo estático success.html
        return "redirect:/success.html";
    }

    // --- RUTAS DE ADMINISTRACIÓN (Archivos en /templates) ---

    @GetMapping("/admin")
    public String listarReservas(Model model) {
        List<Reserva> lista = reservaService.obtenerTodas();
        Integer totalComensales = reservaService.obtenerTotalComensales();

        model.addAttribute("reservas", lista);
        model.addAttribute("total", totalComensales != null ? totalComensales : 0);
        return "admin"; // Busca admin.html en /templates
    }

    @GetMapping("/admin/estado/{id}/{nuevoEstado}")
    public String actualizarEstado(@PathVariable Long id, @PathVariable String nuevoEstado) {
        reservaService.cambiarEstado(id, nuevoEstado);
        return "redirect:/admin";
    }

    @GetMapping("/admin/eliminar/{id}")
    public String eliminarReserva(@PathVariable Long id) {
        reservaService.eliminarReserva(id);
        return "redirect:/admin";
    }
}