/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tienda.model;

import java.io.Serializable;
import java.util.Date;

public class VentaBean implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int numGuia;
    private int codTienda;
    private String nombreTienda;
    private Date fechaSalida;
    private int codTransportista;
    private String transportista;
    private int codArticulo;
    private String articulo;
    private double precioVenta;
    private int cantidad;
    private double total;
    
    public VentaBean() {}
    
    // Getters y Setters
    public int getNumGuia() {
        return numGuia;
    }
    
    public void setNumGuia(int numGuia) {
        this.numGuia = numGuia;
    }
    
    public int getCodTienda() {
        return codTienda;
    }
    
    public void setCodTienda(int codTienda) {
        this.codTienda = codTienda;
    }
    
    public String getNombreTienda() {
        return nombreTienda;
    }
    
    public void setNombreTienda(String nombreTienda) {
        this.nombreTienda = nombreTienda;
    }
    
    public Date getFechaSalida() {
        return fechaSalida;
    }
    
    public void setFechaSalida(Date fechaSalida) {
        this.fechaSalida = fechaSalida;
    }
    
    public int getCodTransportista() {
        return codTransportista;
    }
    
    public void setCodTransportista(int codTransportista) {
        this.codTransportista = codTransportista;
    }
    
    public String getTransportista() {
        return transportista;
    }
    
    public void setTransportista(String transportista) {
        this.transportista = transportista;
    }
    
    public int getCodArticulo() {
        return codArticulo;
    }
    
    public void setCodArticulo(int codArticulo) {
        this.codArticulo = codArticulo;
    }
    
    public String getArticulo() {
        return articulo;
    }
    
    public void setArticulo(String articulo) {
        this.articulo = articulo;
    }
    
    public double getPrecioVenta() {
        return precioVenta;
    }
    
    public void setPrecioVenta(double precioVenta) {
        this.precioVenta = precioVenta;
    }
    
    public int getCantidad() {
        return cantidad;
    }
    
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    
    public double getTotal() {
        return precioVenta * cantidad;
    }
}