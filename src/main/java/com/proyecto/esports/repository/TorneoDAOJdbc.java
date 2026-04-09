/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyecto.esports.repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.proyecto.esports.model.Torneo;

@Repository
@Qualifier("TorneoDAOJdbc")
public class TorneoDAOJdbc implements TorneoDAO{

    private Connection getConnection() {
        return Conexion.getInstancia().getConnection();
    }

    @Override
    public void guardar(Torneo torneo) {
        String sql = "INSERT INTO torneos (nombre, juego, fecha_inicio, fecha_fin, estado, imagen) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {

            stmt.setString(1, torneo.getNombre());
            stmt.setString(2, torneo.getJuego());
            stmt.setDate(3, torneo.getFechaInicio() != null ? Date.valueOf(torneo.getFechaInicio()) : null);
            stmt.setDate(4, torneo.getFechaFin() != null ? Date.valueOf(torneo.getFechaFin()) : null);
            stmt.setString(5, torneo.getEstado());
            stmt.setString(6, torneo.getFoto());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actualizar(Torneo torneo) {
        String sql = "UPDATE torneos SET nombre=?, juego=?, fecha_inicio=?, fecha_fin=?, estado=?, imagen=? WHERE id_torneo=?";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {

            stmt.setString(1, torneo.getNombre());
            stmt.setString(2, torneo.getJuego());
            stmt.setDate(3, torneo.getFechaInicio() != null ? Date.valueOf(torneo.getFechaInicio()) : null);
            stmt.setDate(4, torneo.getFechaFin() != null ? Date.valueOf(torneo.getFechaFin()) : null);
            stmt.setString(5, torneo.getEstado());
            stmt.setString(6, torneo.getFoto());
            stmt.setInt(7, torneo.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void eliminar(int id) {
        String sql = "DELETE FROM torneos WHERE id_torneo=?";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Torneo obtenerPorId(int id) {
        String sql = "SELECT * FROM torneos WHERE id_torneo=?";
        Torneo torneo = null;

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                torneo = mapearTorneo(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return torneo;
    }

    @Override
    public List<Torneo> listarTodos() {
        List<Torneo> lista = new ArrayList<>();
        String sql = "SELECT * FROM torneos";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearTorneo(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
    
    private Torneo mapearTorneo(ResultSet rs) throws SQLException {
        Torneo torneo = new Torneo();

        torneo.setId(rs.getInt("id_torneo"));
        torneo.setNombre(rs.getString("nombre"));
        torneo.setJuego(rs.getString("juego"));

        Date inicio = rs.getDate("fecha_inicio");
        torneo.setFechaInicio(inicio != null ? inicio.toLocalDate() : null);

        Date fin = rs.getDate("fecha_fin");
        torneo.setFechaFin(fin != null ? fin.toLocalDate() : null);

        torneo.setEstado(rs.getString("estado"));
        torneo.setFoto(rs.getString("imagen"));

        return torneo;
    }
}
