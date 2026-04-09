/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.proyecto.esports.repository;

import com.proyecto.esports.model.Equipo;
import java.util.List;


public interface EquipoDAO {
    void guardar(Equipo equipo);
    void actualizar(Equipo equipo);
    void eliminar(int id);
    Equipo obtenerPorId(int id);
    List<Equipo> listarTodos();
}
