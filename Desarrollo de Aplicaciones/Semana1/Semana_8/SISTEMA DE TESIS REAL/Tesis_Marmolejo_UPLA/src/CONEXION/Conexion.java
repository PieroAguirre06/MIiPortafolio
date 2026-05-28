/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private Connection conectar = null;
    private String usuario = "root";
    private String contrasena = "admin";  // CAMBIA A TU CONTRASEÑA DE MYSQL
    private String bd = "bd_ibercap_upla";
    private String ip = "localhost";
    private String puerto = "3306";
    private String cadena = "jdbc:mysql://" + ip + ":" + puerto + "/" + bd
                          + "?useSSL=false&serverTimezone=America/Lima&allowPublicKeyRetrieval=true";

    public Connection establecerConexion() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conectar = DriverManager.getConnection(cadena, usuario, contrasena);
            if (conectar != null) {
                System.out.println("✅ Conexión exitosa a la BD: " + bd);
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Error driver: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error conexión: " + e.getMessage());
        }
        return conectar;
    }
    
    public void cerrarConexion() {
        try {
            if (conectar != null && !conectar.isClosed()) {
                conectar.close();
                System.out.println("✅ Conexión cerrada");
            }
        } catch (SQLException e) {
            System.out.println("Error al cerrar conexión: " + e.getMessage());
        }
    }
}
