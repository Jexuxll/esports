package com.proyecto.esports.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.proyecto.esports.model.EquipoTorneo;
import com.proyecto.esports.service.EquipoTorneoService;


@Controller
public class EquipoTorneoController {
    private final EquipoTorneoService equipoTorneoService;

    @Autowired
    public EquipoTorneoController(EquipoTorneoService equipoTorneoService) {
        this.equipoTorneoService = equipoTorneoService;
    }

    @GetMapping("/equipos-torneos")
    public String listarEquiposTorneos(Model model) {
        model.addAttribute("equiposTorneos", equipoTorneoService.listarTodos());
        return "indexEquipoTorneo"; 
    }

    @GetMapping("/equipos-torneos/nuevo")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("equipoTorneo", new EquipoTorneo());
        return "registro_equipo_torneo"; 
    }

    @PostMapping("/equipos-torneos/guardar")
    public String guardarEquipoTorneo(@ModelAttribute EquipoTorneo equipoTorneo) {
        equipoTorneoService.guardar(equipoTorneo);
        return "redirect:/equipos-torneos"; 
    }

    @GetMapping("/equipos-torneos/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable int id, Model model) {
        EquipoTorneo equipoTorneo = equipoTorneoService.obtenerPorId(id);
        model.addAttribute("equipoTorneo", equipoTorneo);
        return "registro_equipo_torneo"; 
    }

    @PostMapping("/equipos-torneos/actualizar")
    public String actualizarEquipoTorneo(@ModelAttribute EquipoTorneo equipoTorneo) {
        equipoTorneoService.actualizar(equipoTorneo);
        return "redirect:/equipos-torneos"; 
    }

    @GetMapping("/equipos-torneos/eliminar/{id}")
    public String eliminarEquipoTorneo(@PathVariable int id) throws IOException {
        equipoTorneoService.eliminar(id);
        return "redirect:/equipos-torneos"; 
    }

}