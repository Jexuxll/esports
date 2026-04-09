/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyecto.esports.model;

import java.time.LocalDateTime;


public class Partido {
    private int id;
    private Torneo torneo;
    private Equipo equipoLocal;
    private Equipo equipoVisitante;
    
    private LocalDateTime fechaPartido;
    private String ronda;
    private Integer marcadorLocal;
    private Integer marcadorVisitante;
    
    private Equipo ganador;

    public Partido() {
    }

    public Partido(int id, Torneo torneo, Equipo equipoLocal, Equipo equipoVisitante, LocalDateTime fechaPartido, String ronda, Integer marcadorLocal, Integer marcadorVisitante, Equipo ganador) {
        this.id = id;
        this.torneo = torneo;
        this.equipoLocal = equipoLocal;
        this.equipoVisitante = equipoVisitante;
        this.fechaPartido = fechaPartido;
        this.ronda = ronda;
        this.marcadorLocal = marcadorLocal;
        this.marcadorVisitante = marcadorVisitante;
        this.ganador = ganador;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Torneo getTorneo() {
        return torneo;
    }

    public void setTorneo(Torneo torneo) {
        this.torneo = torneo;
    }

    public Equipo getEquipoLocal() {
        return equipoLocal;
    }

    public void setEquipoLocal(Equipo equipoLocal) {
        this.equipoLocal = equipoLocal;
    }

    public Equipo getEquipoVisitante() {
        return equipoVisitante;
    }

    public void setEquipoVisitante(Equipo equipoVisitante) {
        this.equipoVisitante = equipoVisitante;
    }

    public LocalDateTime getFechaPartido() {
        return fechaPartido;
    }

    public void setFechaPartido(LocalDateTime fechaPartido) {
        this.fechaPartido = fechaPartido;
    }

    public String getRonda() {
        return ronda;
    }

    public void setRonda(String ronda) {
        this.ronda = ronda;
    }

    public Integer getMarcadorLocal() {
        return marcadorLocal;
    }

    public void setMarcadorLocal(Integer marcadorLocal) {
        this.marcadorLocal = marcadorLocal;
    }

    public Integer getMarcadorVisitante() {
        return marcadorVisitante;
    }

    public void setMarcadorVisitante(Integer marcadorVisitante) {
        this.marcadorVisitante = marcadorVisitante;
    }

    public Equipo getGanador() {
        return ganador;
    }

    public void setGanador(Equipo ganador) {
        this.ganador = ganador;
    }
}
