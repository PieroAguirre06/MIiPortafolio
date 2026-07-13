/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tienda.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
    private static final String URL = "jdbc:mysql://localhost:3306/QhatuPERU?useSSL=false&serverTimezone=America/Lima&allowPublicKeyRetrieval=true";
    private static final String USUARIO = "root";
    private static final String CLAVE = "admin";  
    
    // Usar ThreadLocal para mantener una conexión por hilo
    private static ThreadLocal<Connection> conexionThread = new ThreadLocal<>();
    
    private ConexionDB() {}
    
    public static Connection getConexion() throws SQLException {
        Connection conn = conexionThread.get();
        if (conn == null || conn.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection(URL, USUARIO, CLAVE);
                conn.setAutoCommit(true);
                conexionThread.set(conn);
                System.out.println("✅ Conexión a MySQL exitosa!");
            } catch (ClassNotFoundException e) {
                throw new SQLException("Driver MySQL no encontrado", e);
            }
        }
        return conn;
    }
    
    public static void cerrarConexion() {
        try {
            Connection conn = conexionThread.get();
            if (conn != null && !conn.isClosed()) {
                conn.close();
                conexionThread.remove();
                System.out.println("🔒 Conexión cerrada");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}