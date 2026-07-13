/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ejemplo.service;

import com.ejemplo.model.Triangulo;

public class CalculoService {

    public double calcularHipotenusa(double a, double b) {
        return Math.sqrt(a * a + b * b);
    }

    public Triangulo calcularTriangulo(double a, double b) {
        Triangulo t = new Triangulo(a, b);
        t.setHipotenusa(calcularHipotenusa(a, b));
        return t;
    }
}