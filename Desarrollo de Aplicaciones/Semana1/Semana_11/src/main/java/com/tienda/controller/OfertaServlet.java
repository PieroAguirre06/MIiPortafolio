/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tienda.controller;

import com.tienda.dao.OfertaDAO;
import com.tienda.model.OfertaBean;
import java.io.IOException;

// IMPORTANTE: Usar jakarta.* para Tomcat 11
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "OfertaServlet", urlPatterns = {"/oferta"})
public class OfertaServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private OfertaDAO ofertaDAO;

    @Override
    public void init() throws ServletException {
        ofertaDAO = new OfertaDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Obtener parámetros del formulario
            String precioStr = request.getParameter("precioDocena");
            String cantidadStr = request.getParameter("cantidadDocenas");

            // Validar que no sean nulos o vacíos
            if (precioStr == null || precioStr.trim().isEmpty() || 
                cantidadStr == null || cantidadStr.trim().isEmpty()) {
                request.setAttribute("error", "Por favor complete todos los campos");
                request.getRequestDispatcher("index.jsp").forward(request, response);
                return;
            }

            double precioDocena = Double.parseDouble(precioStr);
            int cantidadDocenas = Integer.parseInt(cantidadStr);

            // Validar datos
            if (precioDocena < 0 || cantidadDocenas < 0) {
                request.setAttribute("error", "Los valores deben ser positivos");
                request.getRequestDispatcher("index.jsp").forward(request, response);
                return;
            }

            // Calcular oferta
            OfertaBean oferta = ofertaDAO.calcularOferta(precioDocena, cantidadDocenas);

            // Guardar en sesión para mostrar en resultado
            request.getSession().setAttribute("oferta", oferta);

            // Redirigir a la página de resultados
            response.sendRedirect("resultado.jsp");

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Por favor ingrese números válidos");
            request.getRequestDispatcher("index.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Error al procesar la solicitud: " + e.getMessage());
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("index.jsp");
    }

    @Override
    public String getServletInfo() {
        return "Servlet que calcula ofertas de la tienda";
    }
}