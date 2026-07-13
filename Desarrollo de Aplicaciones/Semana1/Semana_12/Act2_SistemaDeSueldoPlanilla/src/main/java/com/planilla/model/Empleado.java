/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.planilla.model;

public class Empleado {
    private String categoria;
    private int horasTrabajadas;
    private int numeroHijos;
    private double sueldoBasico;
    private double sueldoBruto;
    private double descuento;
    private double sueldoNeto;

    public Empleado() {}
    public Empleado(String categoria, int horasTrabajadas, int numeroHijos) {
        this.categoria = categoria;
        this.horasTrabajadas = horasTrabajadas;
        this.numeroHijos = numeroHijos;
    }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public int getHorasTrabajadas() { return horasTrabajadas; }
    public void setHorasTrabajadas(int horasTrabajadas) { this.horasTrabajadas = horasTrabajadas; }
    public int getNumeroHijos() { return numeroHijos; }
    public void setNumeroHijos(int numeroHijos) { this.numeroHijos = numeroHijos; }
    public double getSueldoBasico() { return sueldoBasico; }
    public void setSueldoBasico(double sueldoBasico) { this.sueldoBasico = sueldoBasico; }
    public double getSueldoBruto() { return sueldoBruto; }
    public void setSueldoBruto(double sueldoBruto) { this.sueldoBruto = sueldoBruto; }
    public double getDescuento() { return descuento; }
    public void setDescuento(double descuento) { this.descuento = descuento; }
    public double getSueldoNeto() { return sueldoNeto; }
    public void setSueldoNeto(double sueldoNeto) { this.sueldoNeto = sueldoNeto; }
}