package com.proyecto.esports.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.proyecto.esports.model.EquipoTorneo;

@Repository
@Qualifier("EquipoTorneoDAOJdbc")
public class EquipoTorneoDAOJdbc implements EquipoTorneoDAO {

    private Connection getConnection() {
        return Conexion.getInstancia().getConnection();
    }

    @Override
    public void guardar(EquipoTorneo et) {
        String sql = "INSERT INTO equipos_torneos (id_equipo, id_torneo, fecha_inscripcion) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {

            stmt.setInt(1, et.getEquipo().getId());
            stmt.setInt(2, et.getTorneo().getId());
            stmt.setDate(3, java.sql.Date.valueOf(et.getFechaInscripcion()));

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actualizar(EquipoTorneo et) {
        String sql = "UPDATE equipos_torneos SET id_equipo=?, id_torneo=?, fecha_inscripcion=? WHERE id_equipo_torneo=?";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {

            stmt.setInt(1, et.getEquipo().getId());
            stmt.setInt(2, et.getTorneo().getId());
            stmt.setDate(3, java.sql.Date.valueOf(et.getFechaInscripcion()));
            stmt.setInt(4, et.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void eliminar(int id) {
        String sql = "DELETE FROM equipos_torneos WHERE id_equipo_torneo=?";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public EquipoTorneo obtenerPorId(int id) {
        String sql = "SELECT * FROM equipos_torneos WHERE id_equipo_torneo=?";
        EquipoTorneo et = null;

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                et = mapearEquipoTorneo(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return et;
    }

    @Override
    public List<EquipoTorneo> listarTodos() {
        List<EquipoTorneo> lista = new ArrayList<>();
        String sql = "SELECT * FROM equipos_torneos";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearEquipoTorneo(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
    
    private EquipoTorneo mapearEquipoTorneo(ResultSet rs) throws SQLException {
        EquipoTorneo et = new EquipoTorneo();
        et.setId(rs.getInt("id_equipo_torneo"));

        et.getEquipo().setId(rs.getInt("id_equipo"));
        et.getTorneo().setId(rs.getInt("id_torneo"));

        return et;
    }
}
