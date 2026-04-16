package com.proyecto.esports.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.proyecto.esports.service.EquipoService;
import com.proyecto.esports.service.JugadorService;
import com.proyecto.esports.service.PartidoService;
import com.proyecto.esports.service.TorneoService;

@Controller
public class HomeController {

    private final EquipoService equipoService;
    private final JugadorService jugadorService;
    private final TorneoService torneoService;
    private final PartidoService partidoService;

    @Autowired
    public HomeController(EquipoService equipoService, JugadorService jugadorService,
                          TorneoService torneoService, PartidoService partidoService) {
        this.equipoService = equipoService;
        this.jugadorService = jugadorService;
        this.torneoService = torneoService;
        this.partidoService = partidoService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("totalEquipos", equipoService.listarTodos().size());
        model.addAttribute("totalJugadores", jugadorService.listarTodos().size());
        model.addAttribute("totalTorneos", torneoService.listarTodos().size());
        model.addAttribute("totalPartidos", partidoService.listarTodos().size());
        model.addAttribute("ultimosTorneos", torneoService.listarTodos()
                .stream()
                .limit(3)
                .toList());
        return "index";
    }
}
