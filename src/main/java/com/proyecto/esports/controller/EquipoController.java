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
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
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
import com.proyecto.esports.service.EquipoService;
import com.proyecto.esports.service.JugadorService;


@Controller
public class EquipoController {
    private final EquipoService equipoService;
    private final JugadorService jugadorService;
    
    @Autowired
    public EquipoController(EquipoService equipoService, JugadorService jugadorService) {
        this.equipoService = equipoService;
        this.jugadorService = jugadorService;
    }

    @ModelAttribute("paises")
    public List<String> cargarPaises() {
        Locale localeEs = new Locale("es", "ES");
        return Arrays.stream(Locale.getISOCountries())
                .map(codigo -> new Locale("", codigo).getDisplayCountry(localeEs))
                .filter(nombre -> nombre != null && !nombre.isBlank())
                .distinct()
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());
    }


    @GetMapping("/equipos")
    public String listarEquipos(Model model) {
        model.addAttribute("equipos", equipoService.listarTodos());
        return "indexEquipo"; 
    }

    @GetMapping("/equipos/nuevo")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("equipo", new Equipo());
        return "registro_equipo"; 
    }

    @Value("${app.upload.dir}")
    private String uploadDir;

    @PostMapping("/equipos/guardar")
    public String guardarEquipo(@ModelAttribute Equipo equipo,
                                @RequestParam("logoFile") MultipartFile logoFile) throws IOException {
        if (logoFile != null && !logoFile.isEmpty()) {
            String nombre = System.currentTimeMillis() + "_" + logoFile.getOriginalFilename();

            Path dir = Paths.get(uploadDir);
            Files.createDirectories(dir);

            Files.copy(logoFile.getInputStream(), dir.resolve(nombre), StandardCopyOption.REPLACE_EXISTING);
            equipo.setFoto(nombre);
        }

        equipoService.guardar(equipo);
        return "redirect:/equipos"; 
    }

    @GetMapping("/equipos/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable int id, Model model) {
        Equipo equipo = equipoService.obtenerPorId(id);
        model.addAttribute("equipo", equipo);
        return "registro_equipo"; 
    }

    @PostMapping("/equipos/actualizar")
    public String actualizarEquipo(@ModelAttribute Equipo equipo,
                                  @RequestParam("logoFile") MultipartFile logoFile) throws IOException {
        Equipo equipoExistente = equipoService.obtenerPorId(equipo.getId());

        if (logoFile != null && !logoFile.isEmpty()) {

            if(equipoExistente.getFoto() != null){
                Path fotoAntigua = Paths.get(uploadDir).resolve(equipoExistente.getFoto());
                Files.deleteIfExists(fotoAntigua);
            }

            String nombre = System.currentTimeMillis() + "_" + logoFile.getOriginalFilename();

            Path dir = Paths.get(uploadDir);
            Files.createDirectories(dir);

            Files.copy(logoFile.getInputStream(), dir.resolve(nombre), StandardCopyOption.REPLACE_EXISTING);
            equipo.setFoto(nombre);
            
        } else {
            equipo.setFoto(equipoExistente.getFoto());
        }

        equipoService.actualizar(equipo);
        return "redirect:/equipos/" + equipo.getId();
    }

    @GetMapping("/equipos/eliminar/{id}")
    public String eliminarEquipo(@PathVariable int id) throws IOException {

        Equipo equipo = equipoService.obtenerPorId(id);

        if (equipo != null && equipo.getFoto() != null) {

                Path fotoPath = Paths.get(uploadDir).resolve(equipo.getFoto());
                Files.deleteIfExists(fotoPath);
        }
        equipoService.eliminar(id);
        return "redirect:/equipos";
    }

    @GetMapping("/equipos/{id}")
    public String verDetalleEquipo(@PathVariable int id, Model model) {
        model.addAttribute("equipo", equipoService.obtenerPorId(id));
        List<com.proyecto.esports.model.Jugador> jugadores = jugadorService.listarPorEquipo(id);
        Map<String, List<com.proyecto.esports.model.Jugador>> jugadoresPorJuego = jugadores.stream()
            .collect(Collectors.groupingBy(
                j -> j.getJuego() != null && !j.getJuego().isBlank() ? j.getJuego() : "Sin juego",
                LinkedHashMap::new,
                Collectors.toList()
            ));
        model.addAttribute("jugadores", jugadores);
        model.addAttribute("jugadoresPorJuego", jugadoresPorJuego);
        return "detalle_equipo";
    }
}
