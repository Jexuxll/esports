package com.proyecto.esports.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.proyecto.esports.model.EquipoTorneo;
import com.proyecto.esports.repository.EquipoTorneoDAO;

@Service
public class EquipoTorneoService {
    private final EquipoTorneoDAO equipoTorneoDAO;

    @Autowired
    public EquipoTorneoService(@Qualifier("equipoTorneoDAOJdbc") EquipoTorneoDAO equipoTorneoDAO) {
        this.equipoTorneoDAO = equipoTorneoDAO;
    }

    public void guardar(EquipoTorneo equipoTorneo) {
        equipoTorneoDAO.guardar(equipoTorneo);
        
    }

    public void actualizar(EquipoTorneo equipoTorneo) {
        equipoTorneoDAO.actualizar(equipoTorneo);
    }

    public void eliminar(int id) {
        equipoTorneoDAO.eliminar(id);
    }

    public EquipoTorneo obtenerPorId(int id) {
        return equipoTorneoDAO.obtenerPorId(id);
    }

    public java.util.List<EquipoTorneo> listarTodos() {
        return equipoTorneoDAO.listarTodos();
    }

    public java.util.List<EquipoTorneo> listarPorTorneo(int idTorneo) {
        return equipoTorneoDAO.listarPorTorneo(idTorneo);
    }

    public boolean existeInscripcion(int idTorneo, int idEquipo) {
        return equipoTorneoDAO.existeInscripcion(idTorneo, idEquipo);
    }
}
