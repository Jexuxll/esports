/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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

@Repository
@Qualifier("EquipoDAOJdbc")
public class EquipoDAOJdbc implements EquipoDAO {
    
    private Connection getConnection(){
        return Conexion.getInstancia().getConnection();
    }
    
    @Override
    public void guardar(Equipo equipo) {
        String sql = "INSERT INTO equipos (nombre, tag_equipos, pais, foto) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, equipo.getNombre());
            stmt.setString(2, equipo.getTag());
            stmt.setString(3, equipo.getPais());
            stmt.setString(4, equipo.getFoto());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar el equipo " + equipo.getNombre(), e);
        }
    }

    @Override
    public void actualizar(Equipo equipo) {
        String sql = "UPDATE equipos SET nombre = ?, tag_equipos = ?, pais = ?, foto = ? WHERE id_equipo = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, equipo.getNombre());
            pstmt.setString(2, equipo.getTag());
            pstmt.setString(3, equipo.getPais());
            pstmt.setString(4, equipo.getFoto());
            pstmt.setInt(5, equipo.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar el equipo con id " + equipo.getId(), e);
        }
    }

    @Override
    public void eliminar(int id) {
        Connection connection = getConnection();
        boolean autoCommitOriginal = true;

        try {
            autoCommitOriginal = connection.getAutoCommit();
            connection.setAutoCommit(false);

            try (PreparedStatement deletePartidos = connection.prepareStatement(
                     "DELETE FROM partidos WHERE id_equipo_local = ? OR id_equipo_visitante = ?");
                 PreparedStatement deleteJugadores = connection.prepareStatement(
                     "DELETE FROM jugadores WHERE id_equipo = ?");
                 PreparedStatement deleteInscripciones = connection.prepareStatement(
                     "DELETE FROM equipos_torneos WHERE id_equipo = ?");
                 PreparedStatement deleteEquipo = connection.prepareStatement(
                     "DELETE FROM equipos WHERE id_equipo = ?")) {

                deletePartidos.setInt(1, id);
                deletePartidos.setInt(2, id);
                deletePartidos.executeUpdate();

                deleteJugadores.setInt(1, id);
                deleteJugadores.executeUpdate();

                deleteInscripciones.setInt(1, id);
                deleteInscripciones.executeUpdate();

                deleteEquipo.setInt(1, id);
                int filasEliminadas = deleteEquipo.executeUpdate();

                if (filasEliminadas == 0) {
                    throw new SQLException("No se encontro el equipo con id " + id + " para eliminar");
                }
            }

            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackError) {
                throw new RuntimeException("Error al revertir la transaccion de eliminacion del equipo " + id, rollbackError);
            }
            throw new RuntimeException("Error al eliminar el equipo " + id, e);
        } finally {
            try {
                connection.setAutoCommit(autoCommitOriginal);
            } catch (SQLException e) {
                throw new RuntimeException("No se pudo restaurar autoCommit tras eliminar equipo " + id, e);
            }
        }
    }

    @Override
    public Equipo obtenerPorId(int id) {
        String sql = "SELECT * FROM equipos WHERE id_equipo=?";
        Equipo equipo = null;
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    equipo = mapearEquipo(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return equipo;
    }   

    @Override
    public List<Equipo> listarTodos() {
        List<Equipo> equipos = new ArrayList<>();
        String sql = "SELECT * FROM equipos";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                equipos.add(mapearEquipo(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return equipos;
    }
    
    private Equipo mapearEquipo(ResultSet rs) throws SQLException {
        Equipo equipo = new Equipo();
        equipo.setId(rs.getInt("id_equipo"));
        equipo.setNombre(rs.getString("nombre"));
        equipo.setTag(rs.getString("tag_equipos"));
        equipo.setPais(rs.getString("pais"));
        equipo.setFoto(rs.getString("foto"));
        return equipo;
    }
}
