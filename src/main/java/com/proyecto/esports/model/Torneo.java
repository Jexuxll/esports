/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyecto.esports.model;

import java.time.LocalDate;
import java.util.List;


public class Torneo {
    private int id;
    private String nombre;
    private String juego;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String estado;
    private String foto;
    private String tag;
    
    private List<EquipoTorneo> equiposInscritos;
    private List<Partido> partidos;

    public Torneo(int id, String nombre, String juego, LocalDate fechaInicio, LocalDate fechaFin, String estado, String foto) {
        this.id = id;
        this.nombre = nombre;
        this.juego = juego;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
        this.foto = foto;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Torneo() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getJuego() {
        return juego;
    }

    public void setJuego(String juego) {
        this.juego = juego;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<EquipoTorneo> getEquiposInscritos() {
        return equiposInscritos;
    }

    public void setEquiposInscritos(List<EquipoTorneo> equiposInscritos) {
        this.equiposInscritos = equiposInscritos;
    }

    public List<Partido> getPartidos() {
        return partidos;
    }

    public void setPartidos(List<Partido> partidos) {
        this.partidos = partidos;
    }
}
