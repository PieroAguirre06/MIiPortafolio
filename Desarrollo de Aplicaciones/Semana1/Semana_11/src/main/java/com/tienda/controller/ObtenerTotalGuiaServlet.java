/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tienda.controller;

import com.tienda.dao.VentaDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "ObtenerTotalGuiaServlet", urlPatterns = {"/obtenerTotalGuia"})
public class ObtenerTotalGuiaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private VentaDAO ventaDAO;

    @Override
    public void init() throws ServletException {
        ventaDAO = new VentaDAO();
        System.out.println("✅ ObtenerTotalGuiaServlet inicializado");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            String numGuiaStr = request.getParameter("numGuia");
            System.out.println("🔍 Buscando guía N°: " + numGuiaStr);

            if (numGuiaStr == null || numGuiaStr.trim().isEmpty()) {
                response.getWriter().write("{\"error\":\"Número de guía requerido\"}");
                return;
            }

            int numGuia = Integer.parseInt(numGuiaStr);

            // Verificar si existe la guía
            boolean existe = ventaDAO.existeGuia(numGuia);
            System.out.println("📋 ¿Existe guía? " + existe);

            if (!existe) {
                response.getWriter().write("{\"error\":\"La guía N° " + numGuia + " no existe\"}");
                return;
            }

            double total = ventaDAO.obtenerTotalGuia(numGuia);
            System.out.println("💰 Total de guía N° " + numGuia + ": " + total);

            response.getWriter().write("{\"total\":" + total + "}");

        } catch (NumberFormatException e) {
            System.err.println("❌ Error: Número de guía inválido");
            response.getWriter().write("{\"error\":\"Número de guía inválido\"}");
        } catch (Exception e) {
            System.err.println("❌ Error en ObtenerTotalGuiaServlet: " + e.getMessage());
            e.printStackTrace();
            response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}
