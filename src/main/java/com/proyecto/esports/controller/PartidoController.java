/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyecto.esports.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.proyecto.esports.model.Equipo;
import com.proyecto.esports.model.Partido;
import com.proyecto.esports.service.EquipoService;
import com.proyecto.esports.service.EquipoTorneoService;
import com.proyecto.esports.service.PartidoService;
import com.proyecto.esports.service.TorneoService;

@Controller
public class PartidoController {
    private final PartidoService partidoService;
    private final TorneoService torneoService;
    private final EquipoService equipoService;
    private final EquipoTorneoService equipoTorneoService;


    @Autowired
    public PartidoController(PartidoService partidoService, TorneoService torneoService, EquipoService equipoService, EquipoTorneoService equipoTorneoService) {
        this.partidoService = partidoService;
        this.torneoService = torneoService;
        this.equipoService = equipoService;
        this.equipoTorneoService = equipoTorneoService;
    }

    @GetMapping("/partidos")
    public String listarPartidos(Model model) {
        List<Partido> partidos = partidoService.listarTodos();
        Map<String, List<Partido>> partidosPorTorneo = partidos.stream()
            .collect(Collectors.groupingBy(
                p -> p.getTorneo() != null ? p.getTorneo().getNombre() : "Sin torneo",
                LinkedHashMap::new,
                Collectors.toList()
            ));
        model.addAttribute("partidos", partidos);
        model.addAttribute("partidosPorTorneo", partidosPorTorneo);
        return "indexPartido";
    }

    @GetMapping("/partidos/nuevo")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("partido", new Partido());
        model.addAttribute("torneos", torneoService.listarTodos());
        model.addAttribute("equipos", equipoService.listarTodos());
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
        model.addAttribute("torneos", torneoService.listarTodos());
        model.addAttribute("equipos", equipoService.listarTodos());
        return "editar_partido";
    }

    @PostMapping("/partidos/actualizar")
    public String actualizarPartido(@ModelAttribute Partido partido,
                                    @RequestParam(value = "ganadorId", required = false) Integer ganadorId) {
        if (partido.getFechaPartido() == null) {
            Partido existing = partidoService.obtenerPorId(partido.getId());
            partido.setFechaPartido(existing.getFechaPartido());
        }

        Integer localId = (partido.getEquipoLocal() != null) ? partido.getEquipoLocal().getId() : null;
        Integer visitanteId = (partido.getEquipoVisitante() != null) ? partido.getEquipoVisitante().getId() : null;

        if (ganadorId != null) {
            boolean ganadorValido = (localId != null && ganadorId.equals(localId))
                    || (visitanteId != null && ganadorId.equals(visitanteId));
            if (!ganadorValido){

            }
            Equipo ganador = new Equipo();
            ganador.setId(ganadorId);
            partido.setGanador(ganador);
        } else {
            partido.setGanador(null);
        }
        partidoService.actualizar(partido);
        return partido.getTorneo() != null
            ? "redirect:/torneos/" + partido.getTorneo().getId()
            : "redirect:/partidos";
    }

    @GetMapping("/partidos/eliminar/{id}")
    public String eliminarPartido(@PathVariable int id) {
        partidoService.eliminar(id);
        return "redirect:/partidos";
    }

    @GetMapping("/torneos/{idTorneo}/partidos/nuevo")
    public String nuevoPartidoDesdeTorneo(@PathVariable int idTorneo, Model model) {
        Partido partido = new Partido();
        partido.setTorneo(torneoService.obtenerPorId(idTorneo));

        model.addAttribute("partido", partido);
        model.addAttribute("torneo", torneoService.obtenerPorId(idTorneo));
        model.addAttribute("equiposInscritos", equipoTorneoService.listarPorTorneo(idTorneo));
        return "registro_partido";
    }

    @PostMapping("/torneos/{idTorneo}/partidos/guardar")
    public String guardarPartidoDesdeTorneo(@PathVariable int idTorneo, @ModelAttribute Partido partido, @RequestParam("equipoLocalId") int equipoLocalId, @RequestParam("equipoVisitanteId") int equipoVisitanteId) {
        partido.setTorneo(torneoService.obtenerPorId(idTorneo));

        Equipo local = new Equipo();
        local.setId(equipoLocalId);
        partido.setEquipoLocal(local);

        Equipo visitante = new Equipo();
        visitante.setId(equipoVisitanteId);
        partido.setEquipoVisitante(visitante);

        partidoService.guardar(partido);
        return "redirect:/torneos/" + idTorneo;
    }

}
