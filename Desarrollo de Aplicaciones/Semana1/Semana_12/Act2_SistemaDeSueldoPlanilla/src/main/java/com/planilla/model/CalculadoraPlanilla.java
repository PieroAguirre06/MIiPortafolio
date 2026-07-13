/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.planilla.model;

public class CalculadoraPlanilla {
    private static final double TARIFA_A = 45.0;
    private static final double TARIFA_B = 37.5;
    private static final double BONIF_HASTA_3 = 40.5;
    private static final double BONIF_MAS_3 = 35.0;
    private static final double DESC_ALTO = 0.135;
    private static final double DESC_BAJO = 0.10;
    private static final double LIMITE_DESC = 3500.0;

    public static Empleado calcular(Empleado emp) {
        double tarifa = emp.getCategoria().equalsIgnoreCase("A") ? TARIFA_A : TARIFA_B;
        double basico = tarifa * emp.getHorasTrabajadas();
        emp.setSueldoBasico(basico);

        int hijos = emp.getNumeroHijos();
        double bonif = (hijos <= 3) ? hijos * BONIF_HASTA_3 : hijos * BONIF_MAS_3;
        double bruto = basico + bonif;
        emp.setSueldoBruto(bruto);

        double descuento = (bruto >= LIMITE_DESC) ? bruto * DESC_ALTO : bruto * DESC_BAJO;
        emp.setDescuento(descuento);
        emp.setSueldoNeto(bruto - descuento);

        return emp;
    }
}