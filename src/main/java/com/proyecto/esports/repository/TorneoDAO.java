/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.proyecto.esports.repository;

import java.util.List;

import com.proyecto.esports.model.Torneo;


public interface TorneoDAO {
    void guardar(Torneo torneo);
    void actualizar(Torneo torneo);
    void eliminar(int id);
    Torneo obtenerPorId(int id);
    List<Torneo> listarTodos();
}
