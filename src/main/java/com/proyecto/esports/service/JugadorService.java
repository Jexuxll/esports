/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyecto.esports.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.proyecto.esports.model.Jugador;
import com.proyecto.esports.repository.JugadorDAO;

@Service
public class JugadorService {
    private final JugadorDAO jugadorDAO;

    @Autowired
    
    public JugadorService(@Qualifier("JugadorDAOJdbc") JugadorDAO jugadorDAO) {
        this.jugadorDAO = jugadorDAO;
    }

    public void guardar(Jugador jugador) {
        jugadorDAO.guardar(jugador);
    }

    public void actualizar(Jugador jugador) {
        jugadorDAO.actualizar(jugador);
    }

    public void eliminar(int id) {
        jugadorDAO.eliminar(id);
    }

    public Jugador obtenerPorId(int id) {
        return jugadorDAO.obtenerPorId(id);
    }

    public List<Jugador> listarTodos() {
        return jugadorDAO.listarTodos();
    }

    public List<Jugador> listarPorEquipo(int idEquipo) {
        return jugadorDAO.listarPorEquipo(idEquipo);
    }
}
