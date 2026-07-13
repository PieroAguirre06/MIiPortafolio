/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tienda.dao;

import com.tienda.model.UsuarioBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    
    // Autenticar usuario
    public UsuarioBean autenticar(String username, String clave) throws SQLException {
        String sql = "SELECT u.CodUsuario, u.Username, u.Clave, u.Nombres, u.Apellidos, u.Correo, "
                   + "u.Estado, u.CodRol, r.NomRol "
                   + "FROM USUARIO u "
                   + "INNER JOIN ROL r ON u.CodRol = r.CodRol "
                   + "WHERE u.Username = ? AND u.Estado = 1";
        
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, username);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String claveBD = rs.getString("Clave");
                    
                    // Comparación directa (para producción usar bcrypt)
                    if (!clave.equals(claveBD)) {
                        return null;
                    }
                    
                    UsuarioBean usuario = new UsuarioBean();
                    usuario.setCodUsuario(rs.getInt("CodUsuario"));
                    usuario.setUsername(rs.getString("Username"));
                    usuario.setClave(rs.getString("Clave"));
                    usuario.setNombres(rs.getString("Nombres"));
                    usuario.setApellidos(rs.getString("Apellidos"));
                    usuario.setCorreo(rs.getString("Correo"));
                    usuario.setEstado(rs.getInt("Estado"));
                    usuario.setCodRol(rs.getInt("CodRol"));
                    usuario.setNombreRol(rs.getString("NomRol"));
                    
                    return usuario;
                }
            }
        }
        return null;
    }
    
    // Listar todos los usuarios
    public List<UsuarioBean> listarUsuarios() throws SQLException {
        List<UsuarioBean> usuarios = new ArrayList<>();
        String sql = "SELECT u.CodUsuario, u.Username, u.Clave, u.Nombres, u.Apellidos, u.Correo, "
                   + "u.Estado, u.CodRol, r.NomRol "
                   + "FROM USUARIO u "
                   + "INNER JOIN ROL r ON u.CodRol = r.CodRol "
                   + "ORDER BY u.CodUsuario";
        
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                UsuarioBean usuario = new UsuarioBean();
                usuario.setCodUsuario(rs.getInt("CodUsuario"));
                usuario.setUsername(rs.getString("Username"));
                usuario.setClave(rs.getString("Clave"));
                usuario.setNombres(rs.getString("Nombres"));
                usuario.setApellidos(rs.getString("Apellidos"));
                usuario.setCorreo(rs.getString("Correo"));
                usuario.setEstado(rs.getInt("Estado"));
                usuario.setCodRol(rs.getInt("CodRol"));
                usuario.setNombreRol(rs.getString("NomRol"));
                usuarios.add(usuario);
            }
        }
        return usuarios;
    }
    
    // Buscar usuario por ID
    public UsuarioBean buscarUsuario(int codUsuario) throws SQLException {
        String sql = "SELECT u.CodUsuario, u.Username, u.Clave, u.Nombres, u.Apellidos, u.Correo, "
                   + "u.Estado, u.CodRol, r.NomRol "
                   + "FROM USUARIO u "
                   + "INNER JOIN ROL r ON u.CodRol = r.CodRol "
                   + "WHERE u.CodUsuario = ?";
        
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, codUsuario);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    UsuarioBean usuario = new UsuarioBean();
                    usuario.setCodUsuario(rs.getInt("CodUsuario"));
                    usuario.setUsername(rs.getString("Username"));
                    usuario.setClave(rs.getString("Clave"));
                    usuario.setNombres(rs.getString("Nombres"));
                    usuario.setApellidos(rs.getString("Apellidos"));
                    usuario.setCorreo(rs.getString("Correo"));
                    usuario.setEstado(rs.getInt("Estado"));
                    usuario.setCodRol(rs.getInt("CodRol"));
                    usuario.setNombreRol(rs.getString("NomRol"));
                    return usuario;
                }
            }
        }
        return null;
    }
    
    // Actualizar usuario (solo estado y rol)
    public boolean actualizarUsuario(int codUsuario, int estado, int codRol) throws SQLException {
        String sql = "UPDATE USUARIO SET Estado = ?, CodRol = ? WHERE CodUsuario = ?";
        
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, estado);
            ps.setInt(2, codRol);
            ps.setInt(3, codUsuario);
            
            return ps.executeUpdate() > 0;
        }
    }
    
    // Eliminar usuario (cambiar estado a 0)
    public boolean eliminarUsuario(int codUsuario) throws SQLException {
        String sql = "UPDATE USUARIO SET Estado = 0 WHERE CodUsuario = ?";
        
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, codUsuario);
            
            return ps.executeUpdate() > 0;
        }
    }
    
    // Existe usuario
    public boolean existeUsuario(String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM USUARIO WHERE Username = ?";
        
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, username);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}