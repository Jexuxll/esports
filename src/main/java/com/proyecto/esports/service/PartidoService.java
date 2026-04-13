/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyecto.esports.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.proyecto.esports.model.Partido;
import com.proyecto.esports.repository.PartidoDAO;

@Service
public class PartidoService {
    private final PartidoDAO partidoDAO;

    @Autowired

    public PartidoService(@Qualifier("PartidoDAOJdbc") PartidoDAO partidoDAO) {
        this.partidoDAO = partidoDAO;
    }

    public void guardar(Partido partido) {
        partidoDAO.guardar(partido);
    }

    public void actualizar(Partido partido) {
        partidoDAO.actualizar(partido);
    }

    public void eliminar(int id) {
        partidoDAO.eliminar(id);
    }

    public Partido obtenerPorId(int id) {
        return partidoDAO.obtenerPorId(id);
    }

    public List<Partido> listarTodos() {
        return partidoDAO.listarTodos();
    }

    public List<Partido> listarPorTorneo(int idTorneo) {
        return partidoDAO.listarPorTorneo(idTorneo);
    }
}
