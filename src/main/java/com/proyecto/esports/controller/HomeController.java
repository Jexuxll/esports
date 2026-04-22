package com.proyecto.esports.controller;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

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

    private static final DateTimeFormatter HORA_FMT = DateTimeFormatter.ofPattern("HH:mm");

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
        model.addAttribute("todosTorneos", torneoService.listarTodos());

        String partidosJson = partidoService.listarTodos().stream()
                .filter(p -> p.getFechaPartido() != null)
                .map(p -> {
                    StringBuilder sb = new StringBuilder("{");
                    sb.append("\"year\":").append(p.getFechaPartido().getYear()).append(",");
                    sb.append("\"month\":").append(p.getFechaPartido().getMonthValue() - 1).append(",");
                    sb.append("\"day\":").append(p.getFechaPartido().getDayOfMonth()).append(",");
                    sb.append("\"hora\":\"").append(p.getFechaPartido().format(HORA_FMT)).append("\",");
                    sb.append("\"torneo\":\"").append(esc(p.getTorneo() != null ? p.getTorneo().getNombre() : "")).append("\",");
                    sb.append("\"ronda\":\"").append(esc(p.getRonda() != null ? p.getRonda() : "")).append("\",");
                    sb.append("\"localNombre\":\"").append(esc(p.getEquipoLocal() != null ? p.getEquipoLocal().getNombre() : "?")).append("\",");
                    sb.append("\"localTag\":\"").append(esc(p.getEquipoLocal() != null ? p.getEquipoLocal().getTag() : "?")).append("\",");
                    sb.append("\"localFoto\":").append(jsonStr(p.getEquipoLocal() != null ? p.getEquipoLocal().getFoto() : null)).append(",");
                    sb.append("\"visitanteNombre\":\"").append(esc(p.getEquipoVisitante() != null ? p.getEquipoVisitante().getNombre() : "?")).append("\",");
                    sb.append("\"visitanteTag\":\"").append(esc(p.getEquipoVisitante() != null ? p.getEquipoVisitante().getTag() : "?")).append("\",");
                    sb.append("\"visitanteFoto\":").append(jsonStr(p.getEquipoVisitante() != null ? p.getEquipoVisitante().getFoto() : null)).append(",");
                    sb.append("\"marcadorLocal\":").append(p.getMarcadorLocal() != null ? p.getMarcadorLocal() : "null").append(",");
                    sb.append("\"marcadorVisitante\":").append(p.getMarcadorVisitante() != null ? p.getMarcadorVisitante() : "null").append(",");
                    sb.append("\"ganador\":").append(jsonStr(p.getGanador() != null ? p.getGanador().getNombre() : null));
                    sb.append("}");
                    return sb.toString();
                })
                .collect(Collectors.joining(",", "[", "]"));

        model.addAttribute("partidosJson", partidosJson);
        return "index";
    }

    private static String esc(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private static String jsonStr(String s) {
        return s != null && !s.isEmpty() ? "\"" + esc(s) + "\"" : "null";
    }
}
