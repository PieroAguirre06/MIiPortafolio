/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tienda.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IngresoDAO {

    // Método para obtener el siguiente número de orden
    public int obtenerSiguienteNumOrden() throws SQLException {
        String sql = "SELECT COALESCE(MAX(NumOrden), 0) FROM ORDEN_COMPRA";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1) + 1;
            }
            return 1;
        }
    }

    // 🔴 ESTE ES EL MÉTODO QUE DEBES TENER: registrarIngreso(int, int, double)
    public boolean registrarIngreso(int codArticulo, int cantidad, double precioCompra) throws SQLException {
        int numOrden = obtenerSiguienteNumOrden();

        String sqlOrden = "INSERT INTO ORDEN_COMPRA (NumOrden, FechaOrden, FechaIngreso) VALUES (?, NOW(), NOW())";
        String sqlDetalle = "INSERT INTO ORDEN_DETALLE (NumOrden, CodArticulo, PrecioCompra, CantidadSolicitada, CantidadRecibida, Estado) VALUES (?, ?, ?, ?, ?, 'Completo')";
        String sqlUpdateStock = "UPDATE ARTICULO SET StockActual = StockActual + ? WHERE CodArticulo = ?";

        try (Connection conn = ConexionDB.getConexion()) {
            conn.setAutoCommit(false);

            // Insertar orden
            try (PreparedStatement psOrden = conn.prepareStatement(sqlOrden)) {
                psOrden.setInt(1, numOrden);
                psOrden.executeUpdate();
            }

            // Insertar detalle
            try (PreparedStatement psDetalle = conn.prepareStatement(sqlDetalle)) {
                psDetalle.setInt(1, numOrden);
                psDetalle.setInt(2, codArticulo);
                psDetalle.setDouble(3, precioCompra);
                psDetalle.setInt(4, cantidad);
                psDetalle.setInt(5, cantidad);
                psDetalle.executeUpdate();
            }

            // Actualizar stock
            try (PreparedStatement psStock = conn.prepareStatement(sqlUpdateStock)) {
                psStock.setInt(1, cantidad);
                psStock.setInt(2, codArticulo);
                psStock.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            throw e;
        }
    }
}