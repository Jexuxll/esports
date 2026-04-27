package com.proyecto.esports.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.proyecto.esports.model.Juego;

@Repository
@Qualifier("JuegoDAOJdbc")
public class JuegoDAOJdbc implements JuegoDAO {

    private Connection getConnection() {
        return Conexion.getInstancia().getConnection();
    }

    @Override
    public void guardar(Juego juego) {
        String sql = "INSERT INTO juegos (nombre) VALUES (?)";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, juego.getNombre());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actualizar(Juego juego) {
        String sql = "UPDATE juegos SET nombre = ? WHERE id_juego = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, juego.getNombre());
            stmt.setInt(2, juego.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void eliminar(int id) {
        String sql = "DELETE FROM juegos WHERE id_juego = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Juego obtenerPorId(int id) {
        String sql = "SELECT * FROM juegos WHERE id_juego = ?";
        Juego juego = null;
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    juego = mapear(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return juego;
    }

    @Override
    public List<Juego> listarTodos() {
        List<Juego> juegos = new ArrayList<>();
        String sql = "SELECT * FROM juegos ORDER BY nombre";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                juegos.add(mapear(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return juegos;
    }

    private Juego mapear(ResultSet rs) throws SQLException {
        return new Juego(rs.getInt("id_juego"), rs.getString("nombre"));
    }
}
