package com.reservas.eltano.controller;

import com.reservas.eltano.model.Reserva;
import com.reservas.eltano.model.Configuracion;
import com.reservas.eltano.repository.ConfiguracionRepository;
import com.reservas.eltano.service.ReservaService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private ConfiguracionRepository configRepo;

    @GetMapping("/")
    public String raiz(Model model) {
        boolean sistemaAbierto = configRepo.findByClave("SISTEMA_ABIERTO")
                .map(c -> Boolean.parseBoolean(c.getValor()))
                .orElse(true);
        model.addAttribute("sistemaAbierto", sistemaAbierto);
        model.addAttribute("reserva", new Reserva());
        return "index";
    }

    @PostMapping("/reservar")
    public String procesarReserva(@Valid @ModelAttribute Reserva reserva,
                                  BindingResult bindingResult,
                                  Model model,
                                  RedirectAttributes redirectAttributes, HttpServletResponse response) {

        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        response.setDateHeader("Expires", 0); // Proxies.
        // 1. Verificar si el sistema está abierto
        boolean sistemaAbierto = configRepo.findByClave("SISTEMA_ABIERTO")
                .map(c -> Boolean.parseBoolean(c.getValor()))
                .orElse(true);

        if (!sistemaAbierto) return "redirect:/?error=sistema_cerrado";

        // 2. Verificar errores de validación (Nombre corto, fecha pasada, etc.)
        if (bindingResult.hasErrors()) {
            model.addAttribute("sistemaAbierto", sistemaAbierto);
            // Retornamos la misma vista del formulario para mostrar los errores
            return "index";
        }

        // 3. Guardar y enviar mensaje de éxito
        reservaService.guardarReserva(reserva);

        return "redirect:/reserva-exitosa";
    }

    @GetMapping("/reserva-exitosa")
    public String reservaExitosa() {
        return "success";
    }

    @GetMapping("/admin")
    public String listarReservas(
            @RequestParam(name = "fecha", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            Model model) {



        List<Reserva> lista;
        int totalComensales;

        // Estado del sistema
        boolean sistemaAbierto = configRepo.findByClave("SISTEMA_ABIERTO")
                .map(c -> Boolean.parseBoolean(c.getValor()))
                .orElse(true);

        if (fecha != null) {
            lista = reservaService.obtenerPorFecha(fecha);
            totalComensales = reservaService.obtenerTotalComensalesPorFecha(fecha);
            model.addAttribute("titulo", "Reservas: " + fecha);
        } else {
            lista = reservaService.obtenerTodas();
            totalComensales = lista.stream()
                    .filter(r -> !"CANCELADA".equals(r.getEstado()))
                    .mapToInt(Reserva::getCantidadPersonas)
                    .sum();
            model.addAttribute("titulo", "Todas las Reservas");
        }

        model.addAttribute("reservas", lista);
        model.addAttribute("total", totalComensales);
        model.addAttribute("sistemaAbierto", sistemaAbierto);
        model.addAttribute("fechaFiltro", fecha);

        return "admin";
    }

    @PostMapping("/admin/toggle-sistema")
    public String toggleSistema() {
        Configuracion config = configRepo.findByClave("SISTEMA_ABIERTO")
                .orElseGet(() -> {
                    Configuracion c = new Configuracion();
                    c.setClave("SISTEMA_ABIERTO");
                    c.setValor("true");
                    return c;
                });

        boolean estadoActual = Boolean.parseBoolean(config.getValor());
        config.setValor(String.valueOf(!estadoActual));
        configRepo.save(config);
        return "redirect:/admin";
    }

    @PostMapping("/admin/eliminar/{id}")
    public String eliminarReserva(@PathVariable Long id) {
        reservaService.eliminarReserva(id);
        return "redirect:/admin";
    }

    @PostMapping("/admin/confirmar/{id}")
    public String confirmarReserva(@PathVariable Long id) {
        // Buscamos la reserva
        Reserva reserva = reservaService.obtenerPorId(id);
        if (reserva != null) {
            reserva.setEstado("CONFIRMADA");
            reservaService.guardarReserva(reserva); // Guardamos el cambio
        }
        return "redirect:/admin";
    }

    @PostMapping("/admin/cancelar/{id}")
    public String cancelarReserva(@PathVariable Long id) {
        Reserva reserva = reservaService.obtenerPorId(id);
        if (reserva != null) {
            reserva.setEstado("CANCELADA");
            reservaService.guardarReserva(reserva);
        }
        return "redirect:/admin";
    }
}