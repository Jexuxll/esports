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
import java.util.List;
import java.util.Locale;
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
import com.proyecto.esports.model.Jugador;
import com.proyecto.esports.service.EquipoService;
import com.proyecto.esports.service.JuegoService;
import com.proyecto.esports.service.JugadorService;


@Controller
public class JugadorController {
    private final JugadorService jugadorService;
    private final EquipoService equipoService;
    private final JuegoService juegoService;

    @Autowired
    public JugadorController(JugadorService jugadorService, EquipoService equipoService, JuegoService juegoService) {
        this.jugadorService = jugadorService;
        this.equipoService = equipoService;
        this.juegoService = juegoService;
    }

    @ModelAttribute("nacionalidades")
    public List<String> cargarNacionalidades() {
        Locale localeEs = new Locale("es", "ES");
        return Arrays.stream(Locale.getISOCountries())
                .map(codigo -> new Locale("", codigo).getDisplayCountry(localeEs))
                .filter(nombre -> nombre != null && !nombre.isBlank())
                .distinct()
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());
    }
    
    @GetMapping("/jugadores")
    public String listarJugadores(Model model) {
        model.addAttribute("jugadores", jugadorService.listarTodos());
        return "indexJugador"; 
    }

    @GetMapping("/jugadores/nuevo")
    public String mostrarFormularioRegistro(@RequestParam(name = "equipoId", required = false) Integer equipoId,
                                            Model model) {
        Jugador jugador = new Jugador();
        Equipo equipoSeleccionado = null;

        if (equipoId != null) {
            equipoSeleccionado = equipoService.obtenerPorId(equipoId);
            if (equipoSeleccionado != null) {
                jugador.setEquipo(equipoSeleccionado);
            }
        }

        model.addAttribute("jugador", jugador);
        model.addAttribute("equipoSeleccionado", equipoSeleccionado);
        if (equipoSeleccionado == null) {
            model.addAttribute("equipos", equipoService.listarTodos());
        }
        model.addAttribute("juegos", juegoService.listarTodos());
        return "registro_jugador";
    }

    @Value("${app.upload.dir}")
    private String uploadDir;

    @PostMapping("/jugadores/guardar")
    public String guardarJugador(@ModelAttribute Jugador jugador,
                                @RequestParam("fotoFile") MultipartFile fotoFile) throws IOException {
        if (fotoFile != null && !fotoFile.isEmpty()) {
            String nombre = System.currentTimeMillis() + "_" + fotoFile.getOriginalFilename();

            Path dir = Paths.get(uploadDir);
            Files.createDirectories(dir);

            Files.copy(fotoFile.getInputStream(), dir.resolve(nombre), StandardCopyOption.REPLACE_EXISTING);
            jugador.setFoto(nombre);
        }
    
        jugadorService.guardar(jugador);
        return jugador.getEquipo() != null
            ? "redirect:/equipos/" + jugador.getEquipo().getId()
            : "redirect:/jugadores";
    }
    
    @GetMapping("/jugadores/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable int id, Model model) {
        Jugador jugador = jugadorService.obtenerPorId(id);
        model.addAttribute("jugador", jugador);
        model.addAttribute("equipos", equipoService.listarTodos());
        model.addAttribute("juegos", juegoService.listarTodos());
        return "registro_jugador";
    }

    @PostMapping("/jugadores/actualizar")
    public String actualizarJugador(@ModelAttribute Jugador jugador,
                                   @RequestParam("fotoFile") MultipartFile fotoFile) throws IOException {
        
        Jugador jugadorExistente = jugadorService.obtenerPorId(jugador.getId());

        if (fotoFile != null && !fotoFile.isEmpty()) {
            String nombre = System.currentTimeMillis() + "_" + fotoFile.getOriginalFilename();

            Path dir = Paths.get(uploadDir);
            Files.createDirectories(dir);

            Files.copy(fotoFile.getInputStream(), dir.resolve(nombre), StandardCopyOption.REPLACE_EXISTING);
            jugador.setFoto(nombre);

        } else {
            jugador.setFoto(jugadorExistente.getFoto());
        }

        jugadorService.actualizar(jugador);
        return "redirect:/jugadores/" + jugador.getId();
    }

    @GetMapping("/jugadores/eliminar/{id}")
    public String eliminarJugador(@PathVariable int id) throws IOException {

        Jugador jugador = jugadorService.obtenerPorId(id);
        
        if(jugador.getFoto() != null){
            Path fotoAntigua = Paths.get(uploadDir).resolve(jugador.getFoto());
            Files.deleteIfExists(fotoAntigua);
        }
        jugadorService.eliminar(id);
        return "redirect:/jugadores"; 
    }

    @GetMapping("/jugadores/{id}")
    public String verDetalleJugador(@PathVariable int id, Model model) {
        model.addAttribute("jugador", jugadorService.obtenerPorId(id));
        return "detalle_jugador"; 
    }
}
