package com.proyecto.esports.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.proyecto.esports.model.Juego;
import com.proyecto.esports.service.JuegoService;

@Controller
public class JuegoController {

    private final JuegoService juegoService;

    @Autowired
    public JuegoController(JuegoService juegoService) {
        this.juegoService = juegoService;
    }

    @GetMapping("/juegos")
    public String listarJuegos(Model model) {
        model.addAttribute("juegos", juegoService.listarTodos());
        return "indexJuego";
    }

    @GetMapping("/juegos/nuevo")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("juego", new Juego());
        return "registro_juego";
    }

    @PostMapping("/juegos/guardar")
    public String guardarJuego(@ModelAttribute Juego juego) {
        juegoService.guardar(juego);
        return "redirect:/juegos";
    }

    @GetMapping("/juegos/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable int id, Model model) {
        model.addAttribute("juego", juegoService.obtenerPorId(id));
        return "registro_juego";
    }

    @PostMapping("/juegos/actualizar")
    public String actualizarJuego(@ModelAttribute Juego juego) {
        juegoService.actualizar(juego);
        return "redirect:/juegos";
    }

    @GetMapping("/juegos/eliminar/{id}")
    public String eliminarJuego(@PathVariable int id) {
        juegoService.eliminar(id);
        return "redirect:/juegos";
    }
}

