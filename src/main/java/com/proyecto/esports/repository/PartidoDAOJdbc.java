/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyecto.esports.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.proyecto.esports.model.Equipo;
import com.proyecto.esports.model.Partido;
import com.proyecto.esports.model.Torneo;

@Repository
@Qualifier("PartidoDAOJdbc")
public class PartidoDAOJdbc implements PartidoDAO {

    private Connection getConnection(){
        return Conexion.getInstancia().getConnection();
    }

    @Override
    public void guardar(Partido partido) {
        String sql = "INSERT INTO partidos (id_torneo, id_equipo_local, id_equipo_visitante, fecha_partido, ronda, marcador_local, marcador_visitante, ganador_id_equipo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {

            stmt.setInt(1, partido.getTorneo().getId());
            stmt.setInt(2, partido.getEquipoLocal().getId());
            stmt.setInt(3, partido.getEquipoVisitante().getId());
            stmt.setTimestamp(4, Timestamp.valueOf(partido.getFechaPartido()));
            stmt.setString(5, partido.getRonda());
            stmt.setObject(6, partido.getMarcadorLocal());
            stmt.setObject(7, partido.getMarcadorVisitante());
            stmt.setObject(8, partido.getGanador() != null ? partido.getGanador().getId() : null);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actualizar(Partido partido) {
        String sql = "UPDATE partidos SET id_torneo=?, id_equipo_local=?, id_equipo_visitante=?, fecha_partido=?, ronda=?, marcador_local=?, marcador_visitante=?, ganador_id_equipo=? WHERE id_partido=?";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {

            stmt.setInt(1, partido.getTorneo().getId());
            stmt.setInt(2, partido.getEquipoLocal().getId());
            stmt.setInt(3, partido.getEquipoVisitante().getId());
            stmt.setTimestamp(4, Timestamp.valueOf(partido.getFechaPartido()));
            stmt.setString(5, partido.getRonda());
            stmt.setObject(6, partido.getMarcadorLocal());
            stmt.setObject(7, partido.getMarcadorVisitante());
            stmt.setObject(8, partido.getGanador() != null ? partido.getGanador().getId() : null);
            stmt.setInt(9, partido.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void eliminar(int id) {
        String sql = "DELETE FROM partidos WHERE id_partido=?";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Partido obtenerPorId(int id) {
        String sql = "SELECT * FROM partidos WHERE id_partido=?";
        Partido partido = null;

        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                partido = mapearPartido(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return partido;
    }

    @Override
    public List<Partido> listarTodos() {
        List<Partido> lista = new ArrayList<>();
        String sql = "SELECT * FROM partidos";

        try (PreparedStatement stmt = getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearPartido(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
    
    private Partido mapearPartido(ResultSet rs) throws SQLException {
        Partido partido = new Partido();

        partido.setId(rs.getInt("id_partido"));

        // Torneo
        Torneo torneo = new Torneo();
        torneo.setId(rs.getInt("id_torneo"));
        partido.setTorneo(torneo);

        // Equipos
        Equipo local = new Equipo();
        local.setId(rs.getInt("id_equipo_local"));
        partido.setEquipoLocal(local);

        Equipo visitante = new Equipo();
        visitante.setId(rs.getInt("id_equipo_visitante"));
        partido.setEquipoVisitante(visitante);

        // Fecha
        Timestamp ts = rs.getTimestamp("fecha_partido");
        partido.setFechaPartido(ts != null ? ts.toLocalDateTime() : LocalDateTime.now());

        partido.setRonda(rs.getString("ronda"));
        partido.setMarcadorLocal((Integer) rs.getObject("marcador_local"));
        partido.setMarcadorVisitante((Integer) rs.getObject("marcador_visitante"));

        // Ganador
        Integer ganadorId = (Integer) rs.getObject("ganador_id_equipo");
        if (ganadorId != null) {
            Equipo ganador = new Equipo();
            ganador.setId(ganadorId);
            partido.setGanador(ganador);
        }

        return partido;
    }
}
