/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyecto.esports.model;

import java.util.List;


public class Equipo {
    private int id;
    private String nombre;
    private String pais;
    private String tag;
    private String foto;
    
    private List<Jugador> jugadores;
    private List<EquipoTorneo> inscripciones;

    public Equipo() {
    }

    public Equipo(int id, String nombre, String pais, String tag, String foto) {
        this.id = id;
        this.nombre = nombre;
        this.pais = pais;
        this.tag = tag;
        this.foto = foto;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
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

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public List<Jugador> getJugadores() {
        return jugadores;
    }

    public void setJugadores(List<Jugador> jugadores) {
        this.jugadores = jugadores;
    }

    public List<EquipoTorneo> getInscripciones() {
        return inscripciones;
    }

    public void setInscripciones(List<EquipoTorneo> inscripciones) {
        this.inscripciones = inscripciones;
    }
    
    
}
