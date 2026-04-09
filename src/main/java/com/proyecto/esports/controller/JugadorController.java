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

import com.proyecto.esports.model.Jugador;
import com.proyecto.esports.service.EquipoService;
import com.proyecto.esports.service.JugadorService;


@Controller
public class JugadorController {
    private final JugadorService jugadorService;
    private final EquipoService equipoService;

    @Autowired
    public JugadorController(JugadorService jugadorService, EquipoService equipoService) {
        this.jugadorService = jugadorService;
        this.equipoService = equipoService;
    }

    @GetMapping("/jugadores")
    public String listarJugadores(Model model) {
        model.addAttribute("jugadores", jugadorService.listarTodos());
        return "indexJugador"; 
    }

    @GetMapping("/jugadores/nuevo")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("jugador", new Jugador());
        model.addAttribute("equipos", equipoService.listarTodos());
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
        return "redirect:/jugadores"; 
    }
    
    @GetMapping("/jugadores/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable int id, Model model) {
        Jugador jugador = jugadorService.obtenerPorId(id);
        model.addAttribute("jugador", jugador);
        model.addAttribute("equipos", equipoService.listarTodos());
        return "registro_jugador";
    }

    @PostMapping("/jugadores/actualizar")
    public String actualizarJugador(@ModelAttribute Jugador jugador,
                                   @RequestParam("fotoFile") MultipartFile fotoFile) throws IOException {
        if (fotoFile != null && !fotoFile.isEmpty()) {
            String nombre = System.currentTimeMillis() + "_" + fotoFile.getOriginalFilename();

            Path dir = Paths.get(uploadDir);
            Files.createDirectories(dir);

            Files.copy(fotoFile.getInputStream(), dir.resolve(nombre), StandardCopyOption.REPLACE_EXISTING);
            jugador.setFoto(nombre);
        }

        jugadorService.actualizar(jugador);
        return "redirect:/jugadores"; 
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
}
