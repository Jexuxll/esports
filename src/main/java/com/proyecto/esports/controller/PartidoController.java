/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyecto.esports.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.proyecto.esports.model.Partido;
import com.proyecto.esports.service.PartidoService;

public class PartidoController {
    private final PartidoService partidoService;

    @Autowired
    public PartidoController(PartidoService partidoService) {
        this.partidoService = partidoService;
    }

    @GetMapping("/partidos")
    public String listarPartidos(Model model) {
        model.addAttribute("partidos", partidoService.listarTodos());
        return "indexPartido";
    }

    @GetMapping("/partidos/nuevo")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("partido", new Partido());
        return "registro_partido";
    }

    @PostMapping("/partidos/guardar")
    public String guardarPartido(@ModelAttribute Partido partido) {
        partidoService.guardar(partido);
        return "redirect:/partidos";
    }

    @GetMapping("/partidos/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable int id, Model model) {
        Partido partido = partidoService.obtenerPorId(id);
        model.addAttribute("partido", partido);
        return "editar_partido";
    }

    @PostMapping("/partidos/actualizar")
    public String actualizarPartido(@ModelAttribute Partido partido) {
        partidoService.actualizar(partido);
        return "redirect:/partidos";
    }

    @GetMapping("/partidos/eliminar/{id}")
    public String eliminarPartido(@PathVariable int id) {
        partidoService.eliminar(id);
        return "redirect:/partidos";
    }
}
