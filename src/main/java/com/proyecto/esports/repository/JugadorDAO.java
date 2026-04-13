/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.proyecto.esports.repository;

import java.util.List;

import com.proyecto.esports.model.Jugador;


public interface JugadorDAO {
    void guardar(Jugador jugador);
    void actualizar(Jugador jugador);
    void eliminar(int id);
    Jugador obtenerPorId(int id);
    List<Jugador> listarTodos();
    List<Jugador> listarPorEquipo(int idEquipo);
}
