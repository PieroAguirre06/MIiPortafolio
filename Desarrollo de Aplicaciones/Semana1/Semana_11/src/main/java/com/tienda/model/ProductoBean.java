/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tienda.model;

import java.io.Serializable;

public class ProductoBean implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int codArticulo;
    private String descripcion;
    private String presentacion;
    private double precioVenta;
    private int stockActual;
    private int stockMinimo;
    private String linea;
    private String proveedor;
    private boolean descontinuado;
    
    public ProductoBean() {}
    
    // Getters y Setters
    public int getCodArticulo() {
        return codArticulo;
    }
    
    public void setCodArticulo(int codArticulo) {
        this.codArticulo = codArticulo;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getPresentacion() {
        return presentacion;
    }
    
    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }
    
    public double getPrecioVenta() {
        return precioVenta;
    }
    
    public void setPrecioVenta(double precioVenta) {
        this.precioVenta = precioVenta;
    }
    
    public int getStockActual() {
        return stockActual;
    }
    
    public void setStockActual(int stockActual) {
        this.stockActual = stockActual;
    }
    
    public int getStockMinimo() {
        return stockMinimo;
    }
    
    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }
    
    public String getLinea() {
        return linea;
    }
    
    public void setLinea(String linea) {
        this.linea = linea;
    }
    
    public String getProveedor() {
        return proveedor;
    }
    
    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }
    
    public boolean isDescontinuado() {
        return descontinuado;
    }
    
    public void setDescontinuado(boolean descontinuado) {
        this.descontinuado = descontinuado;
    }
    
    public boolean isStockBajo() {
        return stockActual <= stockMinimo;
    }
}