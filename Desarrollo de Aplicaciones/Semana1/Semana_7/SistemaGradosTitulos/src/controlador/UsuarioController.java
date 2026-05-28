/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import conexion.Conexion;
import modelo.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioController {

    private Connection con;

    public UsuarioController() {
        con = new Conexion().establecerConexion();
    }

    // ── MÉTODO PARA HASHEAR CONTRASEÑAS ──
    private String hashPassword(String password) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            System.out.println("Error al hashear: " + e.getMessage());
            return password;
        }
    }

    // ── LOGIN CORREGIDO ──
    public boolean login(String codigo, String password) {
        try {
            // Verificar conexión
            if (con == null || con.isClosed()) {
                con = new Conexion().establecerConexion();
            }
            
            String sql = "SELECT codigo FROM usuario WHERE codigo = ? AND password_hash = ? AND estado = 'Activo'";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, codigo);
            ps.setString(2, hashPassword(password));  // Hashear la contraseña ingresada
            ResultSet rs = ps.executeQuery();
            boolean existe = rs.next();
            
            rs.close();
            ps.close();
            
            if (existe) {
                System.out.println("✅ Login exitoso para: " + codigo);
            } else {
                System.out.println("❌ Login fallido para: " + codigo);
            }
            return existe;
        } catch (SQLException e) {
            System.out.println("Error login: " + e.getMessage());
            return false;
        }
    }

    public String obtenerRol(String codigo) {
        try {
            PreparedStatement ps = con.prepareStatement("SELECT rol FROM usuario WHERE codigo = ?");
            ps.setString(1, codigo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String rol = rs.getString("rol");
                rs.close();
                ps.close();
                return rol;
            }
        } catch (SQLException e) {
            System.out.println("Error obtenerRol: " + e.getMessage());
        }
        return null;
    }

    // ── CRUD ──
    public boolean insertar(Usuario u) {
        try {
            String sql = "INSERT INTO usuario (codigo, dni, nombres, apellidos, " +
                         "email_institucional, password_hash, rol, codigo_orcid, estado) " +
                         "VALUES (?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, u.getCodigo());
            ps.setString(2, u.getDni());
            ps.setString(3, u.getNombres());
            ps.setString(4, u.getApellidos());
            ps.setString(5, u.getEmailInstitucional());
            ps.setString(6, hashPassword(u.getPasswordHash())); // Hashear
            ps.setString(7, u.getRol());
            ps.setString(8, u.getCodigoOrcid());
            ps.setString(9, u.getEstado());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error insertar usuario: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Usuario u) {
        try {
            String sql = "UPDATE usuario SET dni=?, nombres=?, apellidos=?, " +
                         "email_institucional=?, rol=?, codigo_orcid=?, estado=? " +
                         "WHERE codigo=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, u.getDni());
            ps.setString(2, u.getNombres());
            ps.setString(3, u.getApellidos());
            ps.setString(4, u.getEmailInstitucional());
            ps.setString(5, u.getRol());
            ps.setString(6, u.getCodigoOrcid());
            ps.setString(7, u.getEstado());
            ps.setString(8, u.getCodigo());
            
            // Si se cambió la contraseña, actualizarla hasheada
            if (u.getPasswordHash() != null && !u.getPasswordHash().isEmpty()) {
                String sqlPass = "UPDATE usuario SET password_hash=? WHERE codigo=?";
                PreparedStatement psPass = con.prepareStatement(sqlPass);
                psPass.setString(1, hashPassword(u.getPasswordHash()));
                psPass.setString(2, u.getCodigo());
                psPass.executeUpdate();
                psPass.close();
            }
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error actualizar usuario: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(String codigo) {
        try {
            PreparedStatement ps = con.prepareStatement("DELETE FROM usuario WHERE codigo=?");
            ps.setString(1, codigo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error eliminar usuario: " + e.getMessage());
            return false;
        }
    }

    public List<Usuario> listar() {
        List<Usuario> lista = new ArrayList<>();
        try {
            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM usuario ORDER BY apellidos");
            while (rs.next()) lista.add(mapear(rs));
            rs.close();
        } catch (SQLException e) {
            System.out.println("Error listar: " + e.getMessage());
        }
        return lista;
    }

    public List<Usuario> listarPorRol(String rol) {
        List<Usuario> lista = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM usuario WHERE rol=? AND estado='Activo'");
            ps.setString(1, rol);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Error listarPorRol: " + e.getMessage());
        }
        return lista;
    }

    public Usuario buscarPorCodigo(String codigo) {
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM usuario WHERE codigo=?");
            ps.setString(1, codigo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Usuario u = mapear(rs);
                rs.close();
                ps.close();
                return u;
            }
        } catch (SQLException e) {
            System.out.println("Error buscar: " + e.getMessage());
        }
        return null;
    }

    public List<String> listarEstudiantesConNombre() {
        List<String> lista = new ArrayList<>();
        try {
            String sql = "SELECT codigo, nombres, apellidos FROM usuario WHERE rol='Estudiante' AND estado='Activo' ORDER BY apellidos";
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
    
    // Método para verificar si existe un código
    public boolean codigoExiste(String codigo) {
        try {
            PreparedStatement ps = con.prepareStatement("SELECT codigo FROM usuario WHERE codigo = ?");
            ps.setString(1, codigo);
            ResultSet rs = ps.executeQuery();
            boolean existe = rs.next();
            rs.close();
            ps.close();
            return existe;
        } catch (SQLException e) {
            return false;
        }
    }
    
    // Método para verificar si existe un DNI
    public boolean dniExiste(String dni) {
        try {
            PreparedStatement ps = con.prepareStatement("SELECT dni FROM usuario WHERE dni = ?");
            ps.setString(1, dni);
            ResultSet rs = ps.executeQuery();
            boolean existe = rs.next();
            rs.close();
            ps.close();
            return existe;
        } catch (SQLException e) {
            return false;
        }
    }

    private Usuario mapear(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setCodigo(rs.getString("codigo"));
        u.setDni(rs.getString("dni"));
        u.setNombres(rs.getString("nombres"));
        u.setApellidos(rs.getString("apellidos"));
        u.setEmailInstitucional(rs.getString("email_institucional"));
        u.setPasswordHash(rs.getString("password_hash"));
        u.setRol(rs.getString("rol"));
        u.setCodigoOrcid(rs.getString("codigo_orcid"));
        u.setEstado(rs.getString("estado"));
        return u;
    }
}