/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tienda.model;

import java.io.Serializable;

public class OfertaBean implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private double precioDocena;
    private int cantidadDocenas;
    private double importeCompra;
    private double descuento;
    private double importePagar;
    private int lapicerosObsequio;
    private double ahorroTotal;
    
    public OfertaBean() {}
    
    public double getPrecioDocena() {
        return precioDocena;
    }
    
    public void setPrecioDocena(double precioDocena) {
        this.precioDocena = precioDocena;
    }
    
    public int getCantidadDocenas() {
        return cantidadDocenas;
    }
    
    public void setCantidadDocenas(int cantidadDocenas) {
        this.cantidadDocenas = cantidadDocenas;
    }
    
    public double getImporteCompra() {
        return importeCompra;
    }
    
    public void setImporteCompra(double importeCompra) {
        this.importeCompra = importeCompra;
    }
    
    public double getDescuento() {
        return descuento;
    }
    
    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }
    
    public double getImportePagar() {
        return importePagar;
    }
    
    public void setImportePagar(double importePagar) {
        this.importePagar = importePagar;
    }
    
    public int getLapicerosObsequio() {
        return lapicerosObsequio;
    }
    
    public void setLapicerosObsequio(int lapicerosObsequio) {
        this.lapicerosObsequio = lapicerosObsequio;
    }
    
    public double getAhorroTotal() {
        return ahorroTotal;
    }
    
    public void setAhorroTotal(double ahorroTotal) {
        this.ahorroTotal = ahorroTotal;
    }
}