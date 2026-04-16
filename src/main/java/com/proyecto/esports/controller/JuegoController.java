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

import com.proyecto.esports.model.Juego;
import com.proyecto.esports.service.JuegoService;

@Controller
public class JuegoController {

    private final JuegoService juegoService;

    @Value("${app.upload.dir}")
    private String uploadDir;

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
    public String guardarJuego(@ModelAttribute Juego juego,
                               @RequestParam("fotoFile") MultipartFile fotoFile) throws IOException {
        if (fotoFile != null && !fotoFile.isEmpty()) {
            String nombre = System.currentTimeMillis() + "_" + fotoFile.getOriginalFilename();
            Path dir = Paths.get(uploadDir);
            Files.createDirectories(dir);
            Files.copy(fotoFile.getInputStream(), dir.resolve(nombre), StandardCopyOption.REPLACE_EXISTING);
            juego.setFoto(nombre);
        }
        juegoService.guardar(juego);
        return "redirect:/juegos";
    }

    @GetMapping("/juegos/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable int id, Model model) {
        model.addAttribute("juego", juegoService.obtenerPorId(id));
        return "registro_juego";
    }

    @PostMapping("/juegos/actualizar")
    public String actualizarJuego(@ModelAttribute Juego juego,
                                  @RequestParam("fotoFile") MultipartFile fotoFile) throws IOException {
        Juego juegoExistente = juegoService.obtenerPorId(juego.getId());
        if (fotoFile != null && !fotoFile.isEmpty()) {
            String nombre = System.currentTimeMillis() + "_" + fotoFile.getOriginalFilename();
            Path dir = Paths.get(uploadDir);
            Files.createDirectories(dir);
            Files.copy(fotoFile.getInputStream(), dir.resolve(nombre), StandardCopyOption.REPLACE_EXISTING);
            juego.setFoto(nombre);
        } else {
            juego.setFoto(juegoExistente.getFoto());
        }
        juegoService.actualizar(juego);
        return "redirect:/juegos";
    }

    @GetMapping("/juegos/eliminar/{id}")
    public String eliminarJuego(@PathVariable int id) throws IOException {
        Juego juego = juegoService.obtenerPorId(id);
        if (juego.getFoto() != null) {
            Path fotoPath = Paths.get(uploadDir).resolve(juego.getFoto());
            Files.deleteIfExists(fotoPath);
        }
        juegoService.eliminar(id);
        return "redirect:/juegos";
    }
}

