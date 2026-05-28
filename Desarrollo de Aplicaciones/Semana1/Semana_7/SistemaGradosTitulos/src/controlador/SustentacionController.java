/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import conexion.Conexion;
import modelo.Sustentacion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SustentacionController {

    private Connection con;

    public SustentacionController() {
        con = new Conexion().establecerConexion();
    }

    public boolean insertar(Sustentacion s) {
        try {
            String sql = "INSERT INTO sustentacion (id_proyecto, resolucion_expedito, " +
                         "fecha_hora_programada, modalidad_sustentacion, lugar_enlace, " +
                         "nota_final_numerica, nota_final_letras, condicion_acta, " +
                         "aprobacion_tipo, observaciones_acta) VALUES (?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, s.getIdProyecto());
            ps.setString(2, s.getResolucionExpedito());
            ps.setTimestamp(3, s.getFechaHoraProgramada());
            ps.setString(4, s.getModalidadSustentacion());
            ps.setString(5, s.getLugarEnlace());
            ps.setBigDecimal(6, s.getNotaFinalNumerica());
            ps.setString(7, s.getNotaFinalLetras());
            ps.setString(8, s.getCondicionActa());
            ps.setString(9, s.getAprobacionTipo());
            ps.setString(10, s.getObservacionesActa());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error insertar sustentacion: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Sustentacion s) {
        try {
            String sql = "UPDATE sustentacion SET id_proyecto=?, resolucion_expedito=?, " +
                         "fecha_hora_programada=?, modalidad_sustentacion=?, lugar_enlace=?, " +
                         "nota_final_numerica=?, nota_final_letras=?, condicion_acta=?, " +
                         "aprobacion_tipo=?, observaciones_acta=? WHERE id_sustentacion=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, s.getIdProyecto());
            ps.setString(2, s.getResolucionExpedito());
            ps.setTimestamp(3, s.getFechaHoraProgramada());
            ps.setString(4, s.getModalidadSustentacion());
            ps.setString(5, s.getLugarEnlace());
            ps.setBigDecimal(6, s.getNotaFinalNumerica());
            ps.setString(7, s.getNotaFinalLetras());
            ps.setString(8, s.getCondicionActa());
            ps.setString(9, s.getAprobacionTipo());
            ps.setString(10, s.getObservacionesActa());
            ps.setInt(11, s.getIdSustentacion());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error actualizar sustentacion: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int id) {
        try {
            PreparedStatement ps = con.prepareStatement("DELETE FROM sustentacion WHERE id_sustentacion=?");
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error eliminar sustentacion: " + e.getMessage());
            return false;
        }
    }

    public List<Sustentacion> listar() {
        List<Sustentacion> lista = new ArrayList<>();
        try {
            ResultSet rs = con.createStatement().executeQuery(
                "SELECT * FROM sustentacion ORDER BY id_sustentacion DESC");
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.out.println("Error listar sustentaciones: " + e.getMessage());
        }
        return lista;
    }

    public Sustentacion buscarPorId(int id) {
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM sustentacion WHERE id_sustentacion=?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.out.println("Error buscar sustentacion: " + e.getMessage());
        }
        return null;
    }

    private Sustentacion mapear(ResultSet rs) throws SQLException {
        Sustentacion s = new Sustentacion();
        s.setIdSustentacion(rs.getInt("id_sustentacion"));
        s.setIdProyecto(rs.getInt("id_proyecto"));
        s.setResolucionExpedito(rs.getString("resolucion_expedito"));
        s.setFechaHoraProgramada(rs.getTimestamp("fecha_hora_programada"));
        s.setModalidadSustentacion(rs.getString("modalidad_sustentacion"));
        s.setLugarEnlace(rs.getString("lugar_enlace"));
        s.setNotaFinalNumerica(rs.getBigDecimal("nota_final_numerica"));
        s.setNotaFinalLetras(rs.getString("nota_final_letras"));
        s.setCondicionActa(rs.getString("condicion_acta"));
        s.setAprobacionTipo(rs.getString("aprobacion_tipo"));
        s.setObservacionesActa(rs.getString("observaciones_acta"));
        return s;
    }
}