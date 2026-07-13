/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tienda.dao;

import com.tienda.model.ProductoBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {
    
    public List<ProductoBean> listarProductos() throws SQLException {
        List<ProductoBean> productos = new ArrayList<>();
        String sql = "SELECT a.CodArticulo, a.DescripcionArticulo, a.Presentacion, " +
                    "a.StockActual, a.StockMinimo, a.Descontinuado, " +
                    "l.NomLinea, p.NomProveedor " +
                    "FROM ARTICULO a " +
                    "INNER JOIN LINEA l ON a.CodLinea = l.CodLinea " +
                    "INNER JOIN PROVEEDOR p ON a.CodProveedor = p.CodProveedor " +
                    "WHERE a.Descontinuado = 0 " +
                    "ORDER BY a.DescripcionArticulo";
        
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                ProductoBean producto = new ProductoBean();
                producto.setCodArticulo(rs.getInt("CodArticulo"));
                producto.setDescripcion(rs.getString("DescripcionArticulo"));
                producto.setPresentacion(rs.getString("Presentacion"));
                producto.setStockActual(rs.getInt("StockActual"));
                producto.setStockMinimo(rs.getInt("StockMinimo"));
                producto.setDescontinuado(rs.getBoolean("Descontinuado"));
                producto.setLinea(rs.getString("NomLinea"));
                producto.setProveedor(rs.getString("NomProveedor"));
                productos.add(producto);
            }
        }
        return productos;
    }
    
    public ProductoBean buscarProducto(int codArticulo) throws SQLException {
        String sql = "SELECT a.CodArticulo, a.DescripcionArticulo, a.Presentacion, " +
                    "a.StockActual, a.StockMinimo, a.Descontinuado, " +
                    "l.NomLinea, p.NomProveedor, g.PrecioVenta " +
                    "FROM ARTICULO a " +
                    "INNER JOIN LINEA l ON a.CodLinea = l.CodLinea " +
                    "INNER JOIN PROVEEDOR p ON a.CodProveedor = p.CodProveedor " +
                    "LEFT JOIN GUIA_DETALLE g ON a.CodArticulo = g.CodArticulo " +
                    "WHERE a.CodArticulo = ?";
        
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, codArticulo);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ProductoBean producto = new ProductoBean();
                    producto.setCodArticulo(rs.getInt("CodArticulo"));
                    producto.setDescripcion(rs.getString("DescripcionArticulo"));
                    producto.setPresentacion(rs.getString("Presentacion"));
                    producto.setStockActual(rs.getInt("StockActual"));
                    producto.setStockMinimo(rs.getInt("StockMinimo"));
                    producto.setDescontinuado(rs.getBoolean("Descontinuado"));
                    producto.setLinea(rs.getString("NomLinea"));
                    producto.setProveedor(rs.getString("NomProveedor"));
                    producto.setPrecioVenta(rs.getDouble("PrecioVenta"));
                    return producto;
                }
            }
        }
        return null;
    }
    
    public boolean actualizarStock(int codArticulo, int cantidad) throws SQLException {
        String sql = "UPDATE ARTICULO SET StockActual = StockActual - ? WHERE CodArticulo = ? AND StockActual >= ?";
        
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, cantidad);
            ps.setInt(2, codArticulo);
            ps.setInt(3, cantidad);
            
            return ps.executeUpdate() > 0;
        }
    }
}