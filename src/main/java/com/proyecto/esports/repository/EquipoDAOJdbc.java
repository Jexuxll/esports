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
        String sql = "INSERT INTO equipos (nombre, tag, pais, foto) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, equipo.getNombre());
            stmt.setString(2, equipo.getTag());
            stmt.setString(3, equipo.getPais());
            stmt.setString(4, equipo.getFoto());
            stmt.executeUpdate();
            System.out.println("✅ Equipo guardado correctamente.");
        } catch (SQLException e) {
            System.err.println("❌ Error al guardar el equipo.");
            e.printStackTrace();
        }
    }

    @Override
    public void actualizar(Equipo equipo) {
        String sql = "UPDATE equipos SET nombre = ?, tag = ?, pais = ?, foto = ? WHERE id_equipo = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, equipo.getNombre());
            pstmt.setString(2, equipo.getTag());
            pstmt.setString(3, equipo.getPais());
            pstmt.setString(4, equipo.getFoto());
            pstmt.setInt(5, equipo.getId());
            pstmt.executeUpdate();
            System.out.println("✅ Equipo actualizado correctamente.");
        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar el equipo.");
            e.printStackTrace();
        }
    }

    @Override
    public void eliminar(int id) {
        String sql = "DELETE FROM equipos WHERE id_equipo = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("✅ Equipo eliminado correctamente.");
        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar el equipo.");
            e.printStackTrace();
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
            System.err.println("❌ Error al obtener el equipo por ID.");
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
            System.out.println("✅ Listado de equipos recuperado correctamente.");
        } catch (SQLException e) {
            System.err.println("❌ Error al listar los equipos.");
            e.printStackTrace();
        }
        return equipos;
    }
    
    private Equipo mapearEquipo(ResultSet rs) throws SQLException {
        Equipo equipo = new Equipo();
        equipo.setId(rs.getInt("id_equipo"));
        equipo.setNombre(rs.getString("nombre"));
        equipo.setTag(rs.getString("tag"));
        equipo.setPais(rs.getString("pais"));
        equipo.setFoto(rs.getString("foto"));
        return equipo;
    }
}
