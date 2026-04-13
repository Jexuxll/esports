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
import com.proyecto.esports.service.PartidoService;
import com.proyecto.esports.service.TorneoService;

@Controller
public class TorneoController {
    private final TorneoService torneoService;
    private final EquipoTorneoService equipoTorneoService;
    private final EquipoService equipoService;
    private final PartidoService partidoService;

    @Autowired
    public TorneoController(TorneoService torneoService, EquipoTorneoService equipoTorneoService, EquipoService equipoService, PartidoService partidoService) {
        this.torneoService = torneoService;
        this.equipoTorneoService = equipoTorneoService;
        this.equipoService = equipoService;
        this.partidoService = partidoService;
    }

    @GetMapping("/torneos")
    public String listarTorneos(Model model) {
        model.addAttribute("torneos", torneoService.listarTodos());
        return "indexTorneo";
    }

    @GetMapping("/torneos/nuevo")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("torneo", new Torneo());
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
        return "redirect:/torneos";
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
        model.addAttribute("torneo", torneoService.obtenerPorId(id));
        model.addAttribute("equiposInscritos", equipoTorneoService.listarPorTorneo(id));
        model.addAttribute("partidosTorneo", partidoService.listarPorTorneo(id));
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
