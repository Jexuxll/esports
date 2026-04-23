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
import com.proyecto.esports.model.Jugador;


@Repository
@Qualifier("JugadorDAOJdbc")
public class JugadorDAOJdbc implements JugadorDAO {

    private Connection getConnection(){
        return Conexion.getInstancia().getConnection();
    }
    
    @Override
    public void guardar(Jugador jugador) {
        String sql = "INSERT INTO jugadores (nickname, nombre, apellido, rol, juego, id_equipo, foto, edad, nacionalidad) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {

            stmt.setString(1, jugador.getNickname());
            stmt.setString(2, jugador.getNombre());
            stmt.setString(3, jugador.getApellido());
            stmt.setString(4, jugador.getRol());
            stmt.setString(5, jugador.getJuego());
            stmt.setInt(6, jugador.getEquipo().getId());
            stmt.setString(7, jugador.getFoto());
            stmt.setInt(8, jugador.getEdad());
            stmt.setString(9, jugador.getNacionalidad());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }    
    }

    @Override
    public void actualizar(Jugador jugador) {
        String sql = "UPDATE jugadores SET nickname = ?, nombre = ?, apellido = ?, rol = ?, juego = ?, id_equipo = ?, foto = ?, edad = ?, nacionalidad = ? WHERE id_jugador = ?";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {

            stmt.setString(1, jugador.getNickname());
            stmt.setString(2, jugador.getNombre());
            stmt.setString(3, jugador.getApellido());
            stmt.setString(4, jugador.getRol());
            stmt.setString(5, jugador.getJuego());
            stmt.setInt(6, jugador.getEquipo().getId());
            stmt.setString(7, jugador.getFoto());
            stmt.setInt(8, jugador.getEdad());
            stmt.setString(9, jugador.getNacionalidad());
            stmt.setInt(10, jugador.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void eliminar(int id) {
        String sql = "DELETE FROM jugadores WHERE id_jugador=?";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }    
    }

    @Override
    public Jugador obtenerPorId(int id) {
        String sql = "SELECT j.*, e.nombre AS equipo_nombre, e.tag_equipos AS equipo_tag " +
                     "FROM jugadores j LEFT JOIN equipos e ON j.id_equipo = e.id_equipo " +
                     "WHERE j.id_jugador=?";
        Jugador jugador = null;

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                jugador = new Jugador();
                jugador.setId(rs.getInt("id_jugador"));
                jugador.setNickname(rs.getString("nickname"));
                jugador.setNombre(rs.getString("nombre"));
                jugador.setApellido(rs.getString("apellido"));
                jugador.setRol(rs.getString("rol"));
                jugador.setJuego(rs.getString("juego"));
                jugador.setFoto(rs.getString("foto"));
                jugador.setEdad(rs.getInt("edad"));
                jugador.setNacionalidad(rs.getString("nacionalidad"));
                
                Equipo equipo = new Equipo();
                equipo.setId(rs.getInt("id_equipo"));
                equipo.setNombre(rs.getString("equipo_nombre"));
                equipo.setTag(rs.getString("equipo_tag"));
                jugador.setEquipo(equipo);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return jugador;    
    }

    @Override
    public List<Jugador> listarTodos() {
        List<Jugador> lista = new ArrayList<>();
        String sql = "SELECT j.*, e.nombre AS equipo_nombre, e.tag_equipos AS equipo_tag " +
                     "FROM jugadores j LEFT JOIN equipos e ON j.id_equipo = e.id_equipo";

        try (PreparedStatement pstmt = getConnection().prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Jugador jugador = new Jugador();
                jugador.setId(rs.getInt("id_jugador"));
                jugador.setNickname(rs.getString("nickname"));
                jugador.setNombre(rs.getString("nombre"));
                jugador.setApellido(rs.getString("apellido"));
                jugador.setRol(rs.getString("rol"));
                jugador.setJuego(rs.getString("juego"));
                jugador.setFoto(rs.getString("foto"));
                jugador.setEdad(rs.getInt("edad"));
                jugador.setNacionalidad(rs.getString("nacionalidad"));

                Equipo equipo = new Equipo();
                equipo.setId(rs.getInt("id_equipo"));
                equipo.setNombre(rs.getString("equipo_nombre"));
                equipo.setTag(rs.getString("equipo_tag"));
                jugador.setEquipo(equipo);

                lista.add(jugador);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public List<Jugador> listarPorEquipo(int idEquipo) {
        List<Jugador> lista = new ArrayList<>();
        String sql = "SELECT j.*, e.nombre AS equipo_nombre, e.tag_equipos AS equipo_tag " +
                     "FROM jugadores j LEFT JOIN equipos e ON j.id_equipo = e.id_equipo " +
                     "WHERE j.id_equipo = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, idEquipo);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Jugador jugador = new Jugador();
                jugador.setId(rs.getInt("id_jugador"));
                jugador.setNickname(rs.getString("nickname"));
                jugador.setNombre(rs.getString("nombre"));
                jugador.setApellido(rs.getString("apellido"));
                jugador.setRol(rs.getString("rol"));
                jugador.setJuego(rs.getString("juego"));
                jugador.setFoto(rs.getString("foto"));
                jugador.setEdad(rs.getInt("edad"));
                jugador.setNacionalidad(rs.getString("nacionalidad"));
                Equipo equipo = new Equipo();
                equipo.setId(rs.getInt("id_equipo"));
                equipo.setNombre(rs.getString("equipo_nombre"));
                equipo.setTag(rs.getString("equipo_tag"));
                jugador.setEquipo(equipo);
                lista.add(jugador);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

}
