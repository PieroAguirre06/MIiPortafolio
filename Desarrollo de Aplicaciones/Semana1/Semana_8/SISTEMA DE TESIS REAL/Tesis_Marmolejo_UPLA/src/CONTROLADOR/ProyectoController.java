/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import conexion.Conexion;
import modelo.Proyecto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProyectoController {

    private Connection con;

    public ProyectoController() {
        con = new Conexion().establecerConexion();
    }

    public boolean insertar(Proyecto p) {
        try {
            String sql = "INSERT INTO proyecto (id_tramite, titulo, modalidad, enfoque, " +
                         "porcentaje_similitud, estado, url_repositorio) VALUES (?,?,?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, p.getIdTramite());
            ps.setString(2, p.getTitulo());
            ps.setString(3, p.getModalidad());
            ps.setString(4, p.getEnfoque());
            ps.setBigDecimal(5, p.getPorcentajeSimilitud());
            ps.setString(6, p.getEstado());
            ps.setString(7, p.getUrlRepositorio());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error insertar proyecto: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Proyecto p) {
        try {
            String sql = "UPDATE proyecto SET id_tramite=?, titulo=?, modalidad=?, enfoque=?, " +
                         "porcentaje_similitud=?, estado=?, url_repositorio=? WHERE id_proyecto=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, p.getIdTramite());
            ps.setString(2, p.getTitulo());
            ps.setString(3, p.getModalidad());
            ps.setString(4, p.getEnfoque());
            ps.setBigDecimal(5, p.getPorcentajeSimilitud());
            ps.setString(6, p.getEstado());
            ps.setString(7, p.getUrlRepositorio());
            ps.setInt(8, p.getIdProyecto());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error actualizar proyecto: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int id) {
        try {
            PreparedStatement ps = con.prepareStatement("DELETE FROM proyecto WHERE id_proyecto=?");
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error eliminar proyecto: " + e.getMessage());
            return false;
        }
    }

    public List<Proyecto> listar() {
        List<Proyecto> lista = new ArrayList<>();
        try {
            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM proyecto ORDER BY id_proyecto DESC");
            while (rs.next()) lista.add(mapear(rs));
            rs.close();
        } catch (SQLException e) {
            System.out.println("Error listar proyectos: " + e.getMessage());
        }
        return lista;
    }

    public Proyecto buscarPorId(int id) {
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM proyecto WHERE id_proyecto=?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Proyecto p = mapear(rs);
                rs.close();
                ps.close();
                return p;
            }
        } catch (SQLException e) {
            System.out.println("Error buscar proyecto: " + e.getMessage());
        }
        return null;
    }

    public List<String> listarProyectosCombo() {
        List<String> lista = new ArrayList<>();
        try {
            String sql = "SELECT p.id_proyecto, p.titulo, u.nombres, u.apellidos " +
                         "FROM proyecto p " +
                         "JOIN tramite t ON p.id_tramite = t.id_tramite " +
                         "JOIN usuario u ON t.codigo_estudiante = u.codigo " +
                         "ORDER BY p.id_proyecto DESC";
            ResultSet rs = con.createStatement().executeQuery(sql);
            while (rs.next()) {
                String titulo = rs.getString("titulo");
                if (titulo.length() > 40) titulo = titulo.substring(0, 40) + "...";
                lista.add(rs.getInt("id_proyecto") + " - " + titulo + " (" + rs.getString("nombres") + " " + rs.getString("apellidos") + ")");
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("Error listarProyectosCombo: " + e.getMessage());
        }
        return lista;
    }

    private Proyecto mapear(ResultSet rs) throws SQLException {
        Proyecto p = new Proyecto();
        p.setIdProyecto(rs.getInt("id_proyecto"));
        p.setIdTramite(rs.getInt("id_tramite"));
        p.setTitulo(rs.getString("titulo"));
        p.setModalidad(rs.getString("modalidad"));
        p.setEnfoque(rs.getString("enfoque"));
        p.setPorcentajeSimilitud(rs.getBigDecimal("porcentaje_similitud"));
        p.setEstado(rs.getString("estado"));
        p.setUrlRepositorio(rs.getString("url_repositorio"));
        p.setFechaRegistro(rs.getTimestamp("fecha_registro"));
        return p;
    }
}