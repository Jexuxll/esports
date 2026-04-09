/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyecto.esports.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.proyecto.esports.model.Torneo;
import com.proyecto.esports.repository.TorneoDAO;

@Service
public class TorneoService {
    private final TorneoDAO torneoDAO;

    public TorneoService(@Qualifier("torneoDAOJdbc") TorneoDAO torneoDAO) {
        this.torneoDAO = torneoDAO;
    }

    public void guardar(Torneo torneo) {
        torneoDAO.guardar(torneo);
    }

    public void actualizar(Torneo torneo) {
        torneoDAO.actualizar(torneo);
    }

    public void eliminar(int id) {
        torneoDAO.eliminar(id);
    }

    public Torneo obtenerPorId(int id) {
        return torneoDAO.obtenerPorId(id);
    }

    public java.util.List<Torneo> listarTodos() {
        return torneoDAO.listarTodos();
    }
}
