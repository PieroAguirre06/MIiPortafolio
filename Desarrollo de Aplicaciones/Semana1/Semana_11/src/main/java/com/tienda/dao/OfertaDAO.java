/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tienda.dao;

import com.tienda.model.OfertaBean;

public class OfertaDAO {
    
    public OfertaBean calcularOferta(double precioDocena, int cantidadDocenas) {
        OfertaBean oferta = new OfertaBean();
        
        oferta.setPrecioDocena(precioDocena);
        oferta.setCantidadDocenas(cantidadDocenas);
        
        // Calcular importe de compra
        double importeCompra = precioDocena * cantidadDocenas;
        oferta.setImporteCompra(importeCompra);
        
        // Calcular descuento según tabla
        double porcentajeDescuento;
        if (cantidadDocenas >= 10) {
            porcentajeDescuento = 0.20; // 20%
        } else {
            porcentajeDescuento = 0.10; // 10%
        }
        
        double descuento = importeCompra * porcentajeDescuento;
        oferta.setDescuento(descuento);
        
        // Calcular importe a pagar
        double importePagar = importeCompra - descuento;
        oferta.setImportePagar(importePagar);
        
        // Calcular lapiceros de obsequio
        int lapiceros = 0;
        if (importePagar >= 200) {
            lapiceros = cantidadDocenas * 2; // 2 por cada docena
        }
        oferta.setLapicerosObsequio(lapiceros);
        
        // Ahorro total = descuento + valor de lapiceros (S/ 0.50 cada uno)
        double valorLapiceros = lapiceros * 0.50;
        double ahorroTotal = descuento + valorLapiceros;
        oferta.setAhorroTotal(ahorroTotal);
        
        return oferta;
    }
}