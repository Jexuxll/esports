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
        String sql = "INSERT INTO juegos (nombre, foto) VALUES (?, ?)";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, juego.getNombre());
            stmt.setString(2, juego.getFoto());
            stmt.executeUpdate();
            System.out.println("✅ Juego guardado correctamente.");
        } catch (SQLException e) {
            System.err.println("❌ Error al guardar el juego.");
            e.printStackTrace();
        }
    }

    @Override
    public void actualizar(Juego juego) {
        String sql = "UPDATE juegos SET nombre = ?, foto = ? WHERE id_juego = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, juego.getNombre());
            stmt.setString(2, juego.getFoto());
            stmt.setInt(3, juego.getId());
            stmt.executeUpdate();
            System.out.println("✅ Juego actualizado correctamente.");
        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar el juego.");
            e.printStackTrace();
        }
    }

    @Override
    public void eliminar(int id) {
        String sql = "DELETE FROM juegos WHERE id_juego = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("✅ Juego eliminado correctamente.");
        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar el juego.");
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
            System.err.println("❌ Error al obtener el juego por ID.");
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
            System.err.println("❌ Error al listar los juegos.");
            e.printStackTrace();
        }
        return juegos;
    }

    private Juego mapear(ResultSet rs) throws SQLException {
        return new Juego(rs.getInt("id_juego"), rs.getString("nombre"), rs.getString("foto"));
    }
}
