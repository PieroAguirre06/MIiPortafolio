/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.planilla.controller;

import com.planilla.model.CalculadoraPlanilla;
import com.planilla.model.Empleado;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/calcular")
public class PlanillaServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String categoria = req.getParameter("categoria");
        String horasStr = req.getParameter("horas");
        String hijosStr = req.getParameter("hijos");

        if (categoria == null || horasStr == null || hijosStr == null ||
                horasStr.isEmpty() || hijosStr.isEmpty()) {
            req.setAttribute("error", "Todos los campos son obligatorios");
            req.getRequestDispatcher("index.jsp").forward(req, resp);
            return;
        }

        try {
            int horas = Integer.parseInt(horasStr);
            int hijos = Integer.parseInt(hijosStr);
            Empleado emp = new Empleado(categoria, horas, hijos);
            emp = CalculadoraPlanilla.calcular(emp);
            req.setAttribute("empleado", emp);
            req.setAttribute("resultado", true);
            
            // ===== GUARDAR EN HISTORIAL (SESIÓN) =====
            HttpSession session = req.getSession();
            List<String> historial = (List<String>) session.getAttribute("historialCalculos");
            if (historial == null) {
                historial = new ArrayList<>();
            }
            String registro = String.format("Cat: %s, Horas: %d, Hijos: %d → Básico: S/%.2f, Bruto: S/%.2f, Neto: S/%.2f",
                    categoria, horas, hijos, emp.getSueldoBasico(), emp.getSueldoBruto(), emp.getSueldoNeto());
            historial.add(registro);
            session.setAttribute("historialCalculos", historial);
            // ===========================================
            
        } catch (NumberFormatException e) {
            req.setAttribute("error", "Horas e hijos deben ser números válidos");
        }
        req.getRequestDispatcher("index.jsp").forward(req, resp);
    }
}