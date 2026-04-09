/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyecto.esports.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.proyecto.esports.model.Equipo;
import com.proyecto.esports.repository.EquipoDAO;

@Service
public class EquipoService {
    private final EquipoDAO equipoDAO;

    @Autowired

    public EquipoService(@Qualifier("EquipoDAOJdbc") EquipoDAO equipoDAO) {
        this.equipoDAO = equipoDAO;
    }

    public void guardar(Equipo equipo) {
        equipoDAO.guardar(equipo);
    }

    public void actualizar(Equipo equipo) {
        equipoDAO.actualizar(equipo);
    }

    public void eliminar(int id) {
        equipoDAO.eliminar(id);
    }

    public Equipo obtenerPorId(int id) {
        return equipoDAO.obtenerPorId(id);
    }

    public List<Equipo> listarTodos() {
        return equipoDAO.listarTodos();
    }
}
