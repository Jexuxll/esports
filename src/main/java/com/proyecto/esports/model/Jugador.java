/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyecto.esports.model;


public class Jugador {
    private int id;
    private String nickname;
    private String nombre;
    private String apellido;
    private String rol;
    private String juego;
    private String foto;
    private int edad;
    private String nacionalidad;
    
    private Equipo equipo;

    public Jugador() {
    }

    public Jugador(int id, String nickname, String nombre, String apellido, String rol, String juego, String foto, int edad, String nacionalidad, Equipo equipo) {
        this.id = id;
        this.nickname = nickname;
        this.nombre = nombre;
        this.apellido = apellido;
        this.rol = rol;
        this.juego = juego;
        this.foto = foto;
        this.edad = edad;
        this.nacionalidad = nacionalidad;
        this.equipo = equipo;
    }

    public String getJuego() {
        return juego;
    }

    public void setJuego(String juego) {
        this.juego = juego;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }
    
    
}
