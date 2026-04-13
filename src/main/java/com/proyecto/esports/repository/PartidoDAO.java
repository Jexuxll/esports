/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.proyecto.esports.repository;

import java.util.List;

import com.proyecto.esports.model.Partido;


public interface PartidoDAO {
    void guardar(Partido partido);
    void actualizar(Partido partido);
    void eliminar(int id);
    Partido obtenerPorId(int id);
    List<Partido> listarTodos();
    List<Partido> listarPorTorneo(int idTorneo);
}
