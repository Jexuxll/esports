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
import java.sql.SQLIntegrityConstraintViolationException;
import java.text.Normalizer;
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
import com.proyecto.esports.model.Partido;
import com.proyecto.esports.service.EquipoService;
import com.proyecto.esports.service.JugadorService;
import com.proyecto.esports.service.PartidoService;


@Controller
public class EquipoController {
    private final EquipoService equipoService;
    private final JugadorService jugadorService;
    private final PartidoService partidoService;
    
    @Autowired
    public EquipoController(EquipoService equipoService, JugadorService jugadorService, PartidoService partidoService) {
        this.equipoService = equipoService;
        this.jugadorService = jugadorService;
        this.partidoService = partidoService;
    }

    @ModelAttribute("paises")
    public List<String> cargarPaises() {
        Locale localeEs = new Locale.Builder().setLanguage("es").setRegion("ES").build();
        return Arrays.stream(Locale.getISOCountries())
            .map(codigo -> new Locale.Builder().setRegion(codigo).build().getDisplayCountry(localeEs))
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
                                @RequestParam("logoFile") MultipartFile logoFile,
                                Model model) throws IOException {
        String nombreArchivoSubido = null;

        if (logoFile != null && !logoFile.isEmpty()) {
            String nombre = System.currentTimeMillis() + "_" + logoFile.getOriginalFilename();

            Path dir = Paths.get(uploadDir);
            Files.createDirectories(dir);

            Files.copy(logoFile.getInputStream(), dir.resolve(nombre), StandardCopyOption.REPLACE_EXISTING);
            equipo.setFoto(nombre);
            nombreArchivoSubido = nombre;
        }

        try {
            equipoService.guardar(equipo);
        } catch (RuntimeException e) {
            borrarArchivoSubidoSiExiste(nombreArchivoSubido);
            return devolverFormularioEquipoConError(model, equipo, mensajeErrorEquipo(e));
        }

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
                                  @RequestParam("logoFile") MultipartFile logoFile,
                                  Model model) throws IOException {
        Equipo equipoExistente = equipoService.obtenerPorId(equipo.getId());
        String nombreArchivoSubido = null;

        if (logoFile != null && !logoFile.isEmpty()) {

            String nombre = System.currentTimeMillis() + "_" + logoFile.getOriginalFilename();

            Path dir = Paths.get(uploadDir);
            Files.createDirectories(dir);

            Files.copy(logoFile.getInputStream(), dir.resolve(nombre), StandardCopyOption.REPLACE_EXISTING);
            equipo.setFoto(nombre);
            nombreArchivoSubido = nombre;
            
        } else {
            equipo.setFoto(equipoExistente.getFoto());
        }

        try {
            equipoService.actualizar(equipo);
        } catch (RuntimeException e) {
            borrarArchivoSubidoSiExiste(nombreArchivoSubido);
            if (equipoExistente != null) {
                equipo.setFoto(equipoExistente.getFoto());
            }
            return devolverFormularioEquipoConError(model, equipo, mensajeErrorEquipo(e));
        }

        if (nombreArchivoSubido != null && equipoExistente.getFoto() != null) {
            Path fotoAntigua = Paths.get(uploadDir).resolve(equipoExistente.getFoto());
            Files.deleteIfExists(fotoAntigua);
        }

        return "redirect:/equipos/" + equipo.getId();
    }

    @GetMapping("/equipos/eliminar/{id}")
    public String eliminarEquipo(@PathVariable int id) throws IOException {

        Equipo equipo = equipoService.obtenerPorId(id);

        equipoService.eliminar(id);

        if (equipo != null && equipo.getFoto() != null) {
            Path fotoPath = Paths.get(uploadDir).resolve(equipo.getFoto());
            Files.deleteIfExists(fotoPath);
        }

        return "redirect:/equipos";
    }

    @GetMapping("/equipos/{id}")
    public String verDetalleEquipo(@PathVariable int id, Model model) {
        Equipo equipo = equipoService.obtenerPorId(id);
        model.addAttribute("equipo", equipo);
        model.addAttribute("banderaPais", banderaDesdePais(equipo != null ? equipo.getPais() : null));
        model.addAttribute("codigoPais", codigoIsoPais(equipo != null ? equipo.getPais() : null));
        model.addAttribute("paisMostrar", nombrePaisMostrar(equipo != null ? equipo.getPais() : null));

        List<com.proyecto.esports.model.Jugador> jugadores = jugadorService.listarPorEquipo(id);
        Map<String, List<com.proyecto.esports.model.Jugador>> jugadoresPorJuego = jugadores.stream()
            .collect(Collectors.groupingBy(
                j -> j.getJuego() != null && !j.getJuego().isBlank() ? j.getJuego() : "Sin juego",
                LinkedHashMap::new,
                Collectors.toList()
            ));
        model.addAttribute("jugadores", jugadores);
        model.addAttribute("jugadoresPorJuego", jugadoresPorJuego);

        List<Partido> partidosEquipo = partidoService.listarTodos().stream()
            .filter(p -> (p.getEquipoLocal() != null && p.getEquipoLocal().getId() == id)
                      || (p.getEquipoVisitante() != null && p.getEquipoVisitante().getId() == id))
            .sorted(Comparator.comparing(Partido::getFechaPartido, Comparator.nullsLast(Comparator.reverseOrder())))
            .collect(Collectors.toList());
        Map<String, List<Partido>> partidosPorTorneo = partidosEquipo.stream()
            .collect(Collectors.groupingBy(
                p -> p.getTorneo() != null && p.getTorneo().getNombre() != null && !p.getTorneo().getNombre().isBlank()
                    ? p.getTorneo().getNombre()
                    : "Sin torneo",
                LinkedHashMap::new,
                Collectors.toList()
            ));
        model.addAttribute("partidosEquipo", partidosEquipo);
        model.addAttribute("partidosPorTorneo", partidosPorTorneo);

        return "detalle_equipo";
    }

    private String banderaDesdePais(String nombrePais) {
        Locale localePais = resolverLocalePais(nombrePais);
        return localePais != null ? codigoABandera(localePais.getCountry()) : "🌍";
    }

    private String codigoIsoPais(String nombrePais) {
        Locale localePais = resolverLocalePais(nombrePais);
        return localePais != null ? localePais.getCountry().toLowerCase(Locale.ROOT) : null;
    }

    private String nombrePaisMostrar(String nombrePais) {
        if (nombrePais == null || nombrePais.isBlank()) {
            return "País no especificado";
        }

        Locale localePais = resolverLocalePais(nombrePais);
        if (localePais == null) {
            return nombrePais;
        }

        Locale localeEs = new Locale.Builder().setLanguage("es").setRegion("ES").build();
        return localePais.getDisplayCountry(localeEs);
    }

    private String devolverFormularioEquipoConError(Model model, Equipo equipo, String mensajeError) {
        model.addAttribute("equipo", equipo);
        model.addAttribute("errorMessage", mensajeError);
        return "registro_equipo";
    }

    private String mensajeErrorEquipo(RuntimeException e) {
        Throwable causa = e;
        while (causa != null) {
            if (causa instanceof SQLIntegrityConstraintViolationException) {
                String mensaje = causa.getMessage();
                if (mensaje != null && mensaje.contains("tag_equipos")) {
                    return "Ya existe un equipo con ese tag. Usa otro distinto.";
                }
            }
            causa = causa.getCause();
        }
        return "No se pudo guardar el equipo. Revisa los datos e intentalo de nuevo.";
    }

    private void borrarArchivoSubidoSiExiste(String nombreArchivo) throws IOException {
        if (nombreArchivo == null || nombreArchivo.isBlank()) {
            return;
        }

        Path archivo = Paths.get(uploadDir).resolve(nombreArchivo);
        Files.deleteIfExists(archivo);
    }

    private Locale resolverLocalePais(String nombrePais) {
        if (nombrePais == null || nombrePais.isBlank()) {
            return null;
        }

        String posibleCodigo = nombrePais.trim().toUpperCase(Locale.ROOT);
        if (posibleCodigo.matches("^[A-Z]{2}$")) {
            return new Locale.Builder().setRegion(posibleCodigo).build();
        }

        Locale localeEs = new Locale.Builder().setLanguage("es").setRegion("ES").build();
        Locale localeEn = Locale.ENGLISH;
        String paisBuscado = normalizar(nombrePais);

        Locale mejorCoincidencia = null;
        for (String codigo : Locale.getISOCountries()) {
            Locale localePais = new Locale.Builder().setRegion(codigo).build();
            String paisEnEspanol = localePais.getDisplayCountry(localeEs);
            String paisEnIngles = localePais.getDisplayCountry(localeEn);
            String nombreNativo = localePais.getDisplayCountry();

            String normEs = normalizar(paisEnEspanol);
            String normEn = normalizar(paisEnIngles);
            String normNative = normalizar(nombreNativo);

            if (normEs.equals(paisBuscado) || normEn.equals(paisBuscado) || normNative.equals(paisBuscado)) {
                return localePais;
            }

            if ((normEs.contains(paisBuscado) || paisBuscado.contains(normEs)
                    || normEn.contains(paisBuscado) || paisBuscado.contains(normEn)
                    || normNative.contains(paisBuscado) || paisBuscado.contains(normNative))
                && mejorCoincidencia == null) {
                mejorCoincidencia = localePais;
            }
        }

        return mejorCoincidencia;
    }

    private String normalizar(String texto) {
        String sinAcentos = Normalizer.normalize(texto, Normalizer.Form.NFD)
            .replaceAll("\\p{M}", "");
        return sinAcentos.toLowerCase(Locale.ROOT).trim();
    }

    private String codigoABandera(String codigoPais) {
        if (codigoPais == null || codigoPais.length() != 2) {
            return "🌍";
        }

        char primera = Character.toUpperCase(codigoPais.charAt(0));
        char segunda = Character.toUpperCase(codigoPais.charAt(1));
        if (primera < 'A' || primera > 'Z' || segunda < 'A' || segunda > 'Z') {
            return "🌍";
        }

        int base = 0x1F1E6;
        return new String(Character.toChars(base + (primera - 'A')))
            + new String(Character.toChars(base + (segunda - 'A')));
    }
}
