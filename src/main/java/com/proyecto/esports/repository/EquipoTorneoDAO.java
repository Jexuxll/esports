package com.proyecto.esports.repository;

import java.util.List;

import com.proyecto.esports.model.EquipoTorneo;

public interface EquipoTorneoDAO {
    void guardar(EquipoTorneo et);
    void actualizar(EquipoTorneo et);
    void eliminar(int id);
    EquipoTorneo obtenerPorId(int id);
    List<EquipoTorneo> listarTodos();
}
