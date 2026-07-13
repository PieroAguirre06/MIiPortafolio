/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tienda.dao;

import com.tienda.model.VentaBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VentaDAO {
    
    // Obtener siguiente número de guía
    public int obtenerSiguienteNumGuia() throws SQLException {
        String sql = "SELECT COALESCE(MAX(NumGuia), 0) FROM GUIA_ENVIO";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1) + 1;
            }
            return 1;
        }
    }
    
    // Registrar venta
    public boolean registrarVenta(VentaBean venta) throws SQLException {
        int nuevoNumGuia = obtenerSiguienteNumGuia();
        venta.setNumGuia(nuevoNumGuia);
        
        String sqlGuia = "INSERT INTO GUIA_ENVIO (NumGuia, CodTienda, FechaSalida, CodTransportista) VALUES (?, ?, NOW(), ?)";
        String sqlDetalle = "INSERT INTO GUIA_DETALLE (NumGuia, CodArticulo, PrecioVenta, CantidadEnviada) VALUES (?, ?, ?, ?)";
        String sqlUpdateStock = "UPDATE ARTICULO SET StockActual = StockActual - ? WHERE CodArticulo = ? AND StockActual >= ?";
        
        Connection conn = null;
        try {
            conn = ConexionDB.getConexion();
            conn.setAutoCommit(false);
            
            try (PreparedStatement psGuia = conn.prepareStatement(sqlGuia)) {
                psGuia.setInt(1, nuevoNumGuia);
                psGuia.setInt(2, venta.getCodTienda());
                psGuia.setInt(3, venta.getCodTransportista());
                psGuia.executeUpdate();
            }
            
            try (PreparedStatement psDetalle = conn.prepareStatement(sqlDetalle)) {
                psDetalle.setInt(1, nuevoNumGuia);
                psDetalle.setInt(2, venta.getCodArticulo());
                psDetalle.setDouble(3, venta.getPrecioVenta());
                psDetalle.setInt(4, venta.getCantidad());
                psDetalle.executeUpdate();
            }
            
            try (PreparedStatement psStock = conn.prepareStatement(sqlUpdateStock)) {
                psStock.setInt(1, venta.getCantidad());
                psStock.setInt(2, venta.getCodArticulo());
                psStock.setInt(3, venta.getCantidad());
                int filasStock = psStock.executeUpdate();
                if (filasStock == 0) {
                    throw new SQLException("Stock insuficiente");
                }
            }
            
            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) conn.close();
        }
    }
    
    // Listar todas las ventas
    public List<VentaBean> listarVentas() throws SQLException {
        List<VentaBean> ventas = new ArrayList<>();
        String sql = "SELECT g.NumGuia, g.CodTienda, g.FechaSalida, g.CodTransportista, " +
                    "t.Direccion as NombreTienda, tr.NomTransportista as Transportista, " +
                    "gd.CodArticulo, a.DescripcionArticulo as Articulo, " +
                    "gd.PrecioVenta, gd.CantidadEnviada " +
                    "FROM GUIA_ENVIO g " +
                    "INNER JOIN TIENDA t ON g.CodTienda = t.CodTienda " +
                    "INNER JOIN TRANSPORTISTA tr ON g.CodTransportista = tr.CodTransportista " +
                    "INNER JOIN GUIA_DETALLE gd ON g.NumGuia = gd.NumGuia " +
                    "INNER JOIN ARTICULO a ON gd.CodArticulo = a.CodArticulo " +
                    "ORDER BY g.FechaSalida DESC";
        
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                VentaBean venta = new VentaBean();
                venta.setNumGuia(rs.getInt("NumGuia"));
                venta.setCodTienda(rs.getInt("CodTienda"));
                venta.setNombreTienda(rs.getString("NombreTienda"));
                venta.setFechaSalida(rs.getTimestamp("FechaSalida"));
                venta.setTransportista(rs.getString("Transportista"));
                venta.setCodArticulo(rs.getInt("CodArticulo"));
                venta.setArticulo(rs.getString("Articulo"));
                venta.setPrecioVenta(rs.getDouble("PrecioVenta"));
                venta.setCantidad(rs.getInt("CantidadEnviada"));
                ventas.add(venta);
            }
        }
        return ventas;
    }
    
    // Obtener total de una guía
    public double obtenerTotalGuia(int numGuia) throws SQLException {
        String sql = "SELECT SUM(PrecioVenta * CantidadEnviada) as Total FROM GUIA_DETALLE WHERE NumGuia = ?";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, numGuia);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("Total");
                }
            }
        }
        return 0.0;
    }
    
    // Verificar si existe una guía
    public boolean existeGuia(int numGuia) throws SQLException {
        String sql = "SELECT COUNT(*) FROM GUIA_ENVIO WHERE NumGuia = ?";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, numGuia);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
    
    // ========== NUEVO MÉTODO ==========
    /**
     * Obtiene una venta completa por número de guía
     */
    public VentaBean obtenerVentaPorGuia(int numGuia) throws SQLException {
        String sql = "SELECT g.NumGuia, g.CodTienda, g.FechaSalida, g.CodTransportista, " +
                    "t.Direccion as NombreTienda, " +
                    "tr.NomTransportista as Transportista, " +
                    "gd.CodArticulo, a.DescripcionArticulo as Articulo, " +
                    "gd.PrecioVenta, gd.CantidadEnviada " +
                    "FROM GUIA_ENVIO g " +
                    "INNER JOIN TIENDA t ON g.CodTienda = t.CodTienda " +
                    "INNER JOIN TRANSPORTISTA tr ON g.CodTransportista = tr.CodTransportista " +
                    "INNER JOIN GUIA_DETALLE gd ON g.NumGuia = gd.NumGuia " +
                    "INNER JOIN ARTICULO a ON gd.CodArticulo = a.CodArticulo " +
                    "WHERE g.NumGuia = ?";
        
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, numGuia);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    VentaBean venta = new VentaBean();
                    venta.setNumGuia(rs.getInt("NumGuia"));
                    venta.setCodTienda(rs.getInt("CodTienda"));
                    venta.setNombreTienda(rs.getString("NombreTienda"));
                    venta.setFechaSalida(rs.getTimestamp("FechaSalida"));
                    venta.setTransportista(rs.getString("Transportista"));
                    venta.setCodArticulo(rs.getInt("CodArticulo"));
                    venta.setArticulo(rs.getString("Articulo"));
                    venta.setPrecioVenta(rs.getDouble("PrecioVenta"));
                    venta.setCantidad(rs.getInt("CantidadEnviada"));
                    return venta;
                }
            }
        }
        return null;
    }
}