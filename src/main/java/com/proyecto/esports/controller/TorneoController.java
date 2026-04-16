/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyecto.esports.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.proyecto.esports.model.Equipo;
import com.proyecto.esports.model.EquipoTorneo;
import com.proyecto.esports.model.Torneo;
import com.proyecto.esports.service.EquipoService;
import com.proyecto.esports.service.EquipoTorneoService;
import com.proyecto.esports.service.JuegoService;
import com.proyecto.esports.service.PartidoService;
import com.proyecto.esports.service.TorneoService;

@Controller
public class TorneoController {
    private final TorneoService torneoService;
    private final EquipoTorneoService equipoTorneoService;
    private final EquipoService equipoService;
    private final PartidoService partidoService;
    private final JuegoService juegoService;

    @Autowired
    public TorneoController(TorneoService torneoService, EquipoTorneoService equipoTorneoService, EquipoService equipoService, PartidoService partidoService, JuegoService juegoService) {
        this.torneoService = torneoService;
        this.equipoTorneoService = equipoTorneoService;
        this.equipoService = equipoService;
        this.partidoService = partidoService;
        this.juegoService = juegoService;
    }

    @GetMapping("/torneos")
    public String listarTorneos(Model model) {
        model.addAttribute("torneos", torneoService.listarTodos());
        return "indexTorneo";
    }

    @GetMapping("/torneos/nuevo")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("torneo", new Torneo());
        model.addAttribute("juegos", juegoService.listarTodos());
        return "registro_torneo";
    }
    
    @Value("${app.upload.dir}")
    private String uploadDir;
    
    @PostMapping("/torneos/guardar")
    public String guardarTorneo(@ModelAttribute Torneo torneo,
                                @RequestParam("fotoFile") MultipartFile fotoFile) throws IOException {
        if (fotoFile != null && !fotoFile.isEmpty()) {
            String nombre = System.currentTimeMillis() + "_" + fotoFile.getOriginalFilename();

            Path dir = Paths.get(uploadDir);
            Files.createDirectories(dir);
            
            Files.copy(fotoFile.getInputStream(), dir.resolve(nombre), StandardCopyOption.REPLACE_EXISTING);
            torneo.setFoto(nombre);

        }

        torneoService.guardar(torneo);
        return "redirect:/torneos";
    }

    @GetMapping("/torneos/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable int id, Model model) {
        Torneo torneo = torneoService.obtenerPorId(id);
        model.addAttribute("torneo", torneo);
        model.addAttribute("juegos", juegoService.listarTodos());
        return "registro_torneo";
    }

    @PostMapping("/torneos/actualizar")
    public String actualizarTorneo(@ModelAttribute Torneo torneo,
                                  @RequestParam("fotoFile") MultipartFile fotoFile) throws IOException {

        Torneo torneoExistente = torneoService.obtenerPorId(torneo.getId());
        
        if (torneo.getFechaInicio() == null) {
            torneo.setFechaInicio(torneoExistente.getFechaInicio());
        }
        if (torneo.getFechaFin() == null) {
            torneo.setFechaFin(torneoExistente.getFechaFin());
        }

        if (fotoFile != null && !fotoFile.isEmpty()) {
            String nombre = System.currentTimeMillis() + "_" + fotoFile.getOriginalFilename();

            Path dir = Paths.get(uploadDir);
            Files.createDirectories(dir);
            
            Files.copy(fotoFile.getInputStream(), dir.resolve(nombre), StandardCopyOption.REPLACE_EXISTING);
            torneo.setFoto(nombre);

        } else {
            torneo.setFoto(torneoExistente.getFoto());
        }

        torneoService.actualizar(torneo);
        return "redirect:/torneos/" + torneo.getId();
    }

    @GetMapping("/torneos/eliminar/{id}")
    public String eliminarTorneo(@PathVariable int id) throws IOException {

        Torneo torneo = torneoService.obtenerPorId(id);
        if (torneo.getFoto() != null) {
            Path fotoPath = Paths.get(uploadDir).resolve(torneo.getFoto());
            Files.deleteIfExists(fotoPath);
        }
        
        torneoService.eliminar(id);
        return "redirect:/torneos";
    }

    @GetMapping("/torneos/{id}")
    public String verDetalleTorneo(@PathVariable int id, Model model) {
        List<com.proyecto.esports.model.Partido> partidos = partidoService.listarPorTorneo(id);

        Map<String, List<com.proyecto.esports.model.Partido>> porRonda = partidos.stream()
            .collect(Collectors.groupingBy(
                p -> p.getRonda() != null ? p.getRonda() : "Sin ronda",
                LinkedHashMap::new,
                Collectors.toList()
            ));

        List<String> ordenRondas = Arrays.asList(
            "fase de grupos", "grupos",
            "ronda de 32", "ronda 32",
            "ronda de 16", "octavos de final", "octavos",
            "cuartos de final", "cuartos",
            "semifinal", "semis", "semifinales",
            "final", "gran final"
        );
        List<String> rondasOrdenadas = new ArrayList<>(porRonda.keySet());
        rondasOrdenadas.sort((a, b) -> {
            int ia = ordenRondas.indexOf(a.toLowerCase());
            int ib = ordenRondas.indexOf(b.toLowerCase());
            if (ia == -1) ia = Integer.MAX_VALUE;
            if (ib == -1) ib = Integer.MAX_VALUE;
            return Integer.compare(ia, ib);
        });
        Map<String, List<com.proyecto.esports.model.Partido>> partidosPorRonda = new LinkedHashMap<>();
        for (String ronda : rondasOrdenadas) {
            partidosPorRonda.put(ronda, porRonda.get(ronda));
        }

        model.addAttribute("torneo", torneoService.obtenerPorId(id));
        model.addAttribute("equiposInscritos", equipoTorneoService.listarPorTorneo(id));
        model.addAttribute("partidosTorneo", partidos);
        model.addAttribute("partidosPorRonda", partidosPorRonda);
        return "detalle_torneo";
    }

    @GetMapping("/torneos/{id}/inscribir")
    public String inscribirEquipo(@PathVariable int id, Model model) {
        model.addAttribute("torneo", torneoService.obtenerPorId(id));
        model.addAttribute("equipos", equipoService.listarTodos());
        return "inscribir_equipo_torneo";
    }

    @PostMapping("/torneos/{id}/inscribir")
    public String procesarInscripcion(@PathVariable int id, @RequestParam int equipoId) {
        if (!equipoTorneoService.existeInscripcion(id, equipoId)) {
                EquipoTorneo et = new EquipoTorneo();
                et.setTorneo(torneoService.obtenerPorId(id));

                Equipo equipo = new Equipo();
                equipo.setId(equipoId);
                et.setEquipo(equipo);

                et.setFechaInscripcion(java.time.LocalDate.now());
                equipoTorneoService.guardar(et);
        }
        return "redirect:/torneos/" + id;
    }
}
