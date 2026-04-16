package com.proyecto.esports.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.proyecto.esports.model.Juego;
import com.proyecto.esports.repository.JuegoDAO;

@Service
public class JuegoService {
    private final JuegoDAO juegoDAO;

    public JuegoService(@Qualifier("JuegoDAOJdbc") JuegoDAO juegoDAO) {
        this.juegoDAO = juegoDAO;
    }

    public void guardar(Juego juego) { juegoDAO.guardar(juego); }

    public void actualizar(Juego juego) { juegoDAO.actualizar(juego); }

    public void eliminar(int id) { juegoDAO.eliminar(id); }

    public Juego obtenerPorId(int id) { return juegoDAO.obtenerPorId(id); }

    public List<Juego> listarTodos() { return juegoDAO.listarTodos(); }
}
