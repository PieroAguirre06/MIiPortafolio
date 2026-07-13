/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.planilla.dao;

import com.planilla.model.Rol;
import com.planilla.model.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    
    // ===== AUTENTICACIÓN =====
    public Usuario autenticar(String username, String clave) {
        String sql = "SELECT u.*, r.NomRol FROM USUARIO u JOIN ROL r ON u.CodRol = r.CodRol WHERE u.Username = ? AND u.Clave = ? AND u.Estado = 1";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, username);
            pst.setString(2, clave);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                Usuario u = new Usuario();
                u.setCodUsuario(rs.getInt("CodUsuario"));
                u.setUsername(rs.getString("Username"));
                u.setClave(rs.getString("Clave"));
                u.setNombres(rs.getString("Nombres"));
                u.setApellidos(rs.getString("Apellidos"));
                u.setCorreo(rs.getString("Correo"));
                u.setEstado(rs.getBoolean("Estado"));
                u.setCodRol(rs.getInt("CodRol"));
                u.setNomRol(rs.getString("NomRol"));
                return u;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // ===== MÉTODOS ADMIN =====
    public List<Usuario> listarEmpleados() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT u.*, r.NomRol FROM USUARIO u JOIN ROL r ON u.CodRol = r.CodRol WHERE u.CodRol = 2 AND u.Estado = 1";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                Usuario u = new Usuario();
                u.setCodUsuario(rs.getInt("CodUsuario"));
                u.setUsername(rs.getString("Username"));
                u.setClave(rs.getString("Clave"));
                u.setNombres(rs.getString("Nombres"));
                u.setApellidos(rs.getString("Apellidos"));
                u.setCorreo(rs.getString("Correo"));
                u.setEstado(rs.getBoolean("Estado"));
                u.setCodRol(rs.getInt("CodRol"));
                u.setNomRol(rs.getString("NomRol"));
                lista.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
    
    public boolean registrarEmpleado(String username, String clave, String nombres, String apellidos, String correo) {
        String sql = "INSERT INTO USUARIO (Username, Clave, Nombres, Apellidos, Correo, Estado, CodRol) VALUES (?, ?, ?, ?, ?, 1, 2)";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, username);
            pst.setString(2, clave);
            pst.setString(3, nombres);
            pst.setString(4, apellidos);
            pst.setString(5, correo);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean actualizarEmpleado(int codUsuario, String nombres, String apellidos, String correo) {
        String sql = "UPDATE USUARIO SET Nombres = ?, Apellidos = ?, Correo = ? WHERE CodUsuario = ? AND CodRol = 2";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, nombres);
            pst.setString(2, apellidos);
            pst.setString(3, correo);
            pst.setInt(4, codUsuario);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean eliminarEmpleado(int codUsuario) {
        String sql = "UPDATE USUARIO SET Estado = 0 WHERE CodUsuario = ? AND CodRol = 2";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, codUsuario);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean registrarUsuarioSistema(String username, String clave, String nombres, String apellidos, String correo, int codRol) {
        String sql = "INSERT INTO USUARIO (Username, Clave, Nombres, Apellidos, Correo, Estado, CodRol) VALUES (?, ?, ?, ?, ?, 1, ?)";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, username);
            pst.setString(2, clave);
            pst.setString(3, nombres);
            pst.setString(4, apellidos);
            pst.setString(5, correo);
            pst.setInt(6, codRol);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public int contarEmpleados() {
        String sql = "SELECT COUNT(*) FROM USUARIO WHERE CodRol = 2 AND Estado = 1";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public List<Rol> listarRoles() {
        List<Rol> roles = new ArrayList<>();
        String sql = "SELECT CodRol, NomRol FROM ROL";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                Rol r = new Rol();
                r.setCodRol(rs.getInt("CodRol"));
                r.setNomRol(rs.getString("NomRol"));
                roles.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roles;
    }
}