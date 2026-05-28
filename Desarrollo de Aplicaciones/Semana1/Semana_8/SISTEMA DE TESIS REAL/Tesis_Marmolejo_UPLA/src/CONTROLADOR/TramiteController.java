/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import conexion.Conexion;
import modelo.Tramite;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TramiteController {

    private Connection con;

    public TramiteController() {
        con = new Conexion().establecerConexion();
    }

    public boolean insertar(Tramite t) {
        try {
            String sql = "INSERT INTO tramite (codigo_estudiante, tipo_tramite, estado_actual) VALUES (?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, t.getCodigoEstudiante());
            ps.setString(2, t.getTipoTramite());
            ps.setString(3, t.getEstadoActual());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error insertar tramite: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Tramite t) {
        try {
            String sql = "UPDATE tramite SET codigo_estudiante=?, tipo_tramite=?, estado_actual=? WHERE id_tramite=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, t.getCodigoEstudiante());
            ps.setString(2, t.getTipoTramite());
            ps.setString(3, t.getEstadoActual());
            ps.setInt(4, t.getIdTramite());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error actualizar tramite: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int id) {
        try {
            PreparedStatement ps = con.prepareStatement("DELETE FROM tramite WHERE id_tramite=?");
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error eliminar tramite: " + e.getMessage());
            return false;
        }
    }

    public List<Tramite> listar() {
        List<Tramite> lista = new ArrayList<>();
        try {
            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM tramite ORDER BY id_tramite DESC");
            while (rs.next()) lista.add(mapear(rs));
            rs.close();
        } catch (SQLException e) {
            System.out.println("Error listar tramites: " + e.getMessage());
        }
        return lista;
    }

    public List<Tramite> listarPorEstudiante(String codigoEstudiante) {
        List<Tramite> lista = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM tramite WHERE codigo_estudiante=? ORDER BY fecha_inicio DESC");
            ps.setString(1, codigoEstudiante);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Error listarPorEstudiante: " + e.getMessage());
        }
        return lista;
    }

    public List<String> listarEstudiantesConNombre() {
        List<String> lista = new ArrayList<>();
        try {
            String sql = "SELECT u.codigo, u.nombres, u.apellidos FROM usuario u WHERE u.rol='Estudiante' AND u.estado='Activo' ORDER BY u.apellidos";
            ResultSet rs = con.createStatement().executeQuery(sql);
            while (rs.next()) {
                lista.add(rs.getString("codigo") + " - " + rs.getString("nombres") + " " + rs.getString("apellidos"));
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("Error listarEstudiantesConNombre: " + e.getMessage());
        }
        return lista;
    }

    public List<String> listarCodigosConNombre() {
        return listarEstudiantesConNombre();
    }

    private Tramite mapear(ResultSet rs) throws SQLException {
        Tramite t = new Tramite();
        t.setIdTramite(rs.getInt("id_tramite"));
        t.setCodigoEstudiante(rs.getString("codigo_estudiante"));
        t.setTipoTramite(rs.getString("tipo_tramite"));
        t.setEstadoActual(rs.getString("estado_actual"));
        t.setFechaInicio(rs.getTimestamp("fecha_inicio"));
        t.setFechaActualizacion(rs.getTimestamp("fecha_actualizacion"));
        return t;
    }
}