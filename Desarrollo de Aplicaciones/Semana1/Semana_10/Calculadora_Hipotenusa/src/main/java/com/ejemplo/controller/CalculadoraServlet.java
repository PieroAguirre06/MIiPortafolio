/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ejemplo.controller;

import com.ejemplo.model.Triangulo;
import com.ejemplo.service.CalculoService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CalculadoraServlet extends HttpServlet {

    private final CalculoService service = new CalculoService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        String aStr = req.getParameter("catetoA");
        String bStr = req.getParameter("catetoB");

        if (aStr == null || aStr.trim().isEmpty() || bStr == null || bStr.trim().isEmpty()) {
            req.setAttribute("error", "Por favor, ingrese ambos catetos.");
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
            return;
        }

        try {
            double a = Double.parseDouble(aStr);
            double b = Double.parseDouble(bStr);

            if (a <= 0 || b <= 0) {
                req.setAttribute("error", "Los catetos deben ser números positivos.");
                req.getRequestDispatcher("/index.jsp").forward(req, resp);
                return;
            }

            Triangulo triangulo = service.calcularTriangulo(a, b);
            req.setAttribute("triangulo", triangulo);

        } catch (NumberFormatException e) {
            req.setAttribute("error", "Debe ingresar números válidos.");
        }

        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }
}