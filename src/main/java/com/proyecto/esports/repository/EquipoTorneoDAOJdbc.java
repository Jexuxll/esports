package com.proyecto.esports.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.proyecto.esports.model.Equipo;
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

    @Override
    public List<EquipoTorneo> listarPorTorneo(int idTorneo) {
        List<EquipoTorneo> lista = new ArrayList<>();
        String sql = "SELECT et.id_equipo_torneo, et.id_torneo, et.fecha_inscripcion, " +
                     "e.id_equipo, e.nombre AS equipo_nombre, e.tag AS equipo_tag, " +
                     "e.foto AS equipo_foto, e.pais AS equipo_pais " +
                     "FROM equipos_torneos et " +
                     "JOIN equipos e ON et.id_equipo = e.id_equipo " +
                     "WHERE et.id_torneo = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, idTorneo);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                EquipoTorneo et = new EquipoTorneo();
                et.setId(rs.getInt("id_equipo_torneo"));
                if (rs.getDate("fecha_inscripcion") != null) {
                    et.setFechaInscripcion(rs.getDate("fecha_inscripcion").toLocalDate());
                }
                Equipo equipo = new Equipo();
                equipo.setId(rs.getInt("id_equipo"));
                equipo.setNombre(rs.getString("equipo_nombre"));
                equipo.setTag(rs.getString("equipo_tag"));
                equipo.setFoto(rs.getString("equipo_foto"));
                equipo.setPais(rs.getString("equipo_pais"));
                et.setEquipo(equipo);
                lista.add(et);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public boolean existeInscripcion(int idTorneo, int idEquipo) {
        String sql = "SELECT COUNT(*) FROM equipos_torneos WHERE id_torneo=? AND id_equipo=?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, idTorneo);
            stmt.setInt(2, idEquipo);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
