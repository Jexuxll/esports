package com.proyecto.esports.repository;

import java.util.List;

import com.proyecto.esports.model.Juego;

public interface JuegoDAO {
    void guardar(Juego juego);
    void actualizar(Juego juego);
    void eliminar(int id);
    Juego obtenerPorId(int id);
    List<Juego> listarTodos();
}
