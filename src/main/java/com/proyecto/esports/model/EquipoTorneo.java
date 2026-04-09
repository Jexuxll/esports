/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyecto.esports.model;

import java.time.LocalDate;


public class EquipoTorneo {
    private int id;
    private Equipo equipo;
    private Torneo torneo;
    private LocalDate fechaInscripcion;

    public EquipoTorneo() {
    }

    public EquipoTorneo(int id, Equipo equipo, Torneo torneo, LocalDate fechaInscripcion) {
        this.id = id;
        this.equipo = new Equipo();
        this.torneo = new Torneo();
        this.fechaInscripcion = fechaInscripcion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    public Torneo getTorneo() {
        return torneo;
    }

    public void setTorneo(Torneo torneo) {
        this.torneo = torneo;
    }

    public LocalDate getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(LocalDate fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }
    
}
