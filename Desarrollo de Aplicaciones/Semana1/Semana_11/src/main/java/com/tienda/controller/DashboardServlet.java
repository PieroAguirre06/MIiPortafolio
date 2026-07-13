/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tienda.controller;

import com.tienda.model.UsuarioBean;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "DashboardServlet", urlPatterns = {"/dashboard"})
public class DashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        UsuarioBean usuario = (UsuarioBean) session.getAttribute("usuario");
        
        if (usuario == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        // Redirigir según el rol
        String rol = usuario.getNombreRol();
        String destino = "dashboard.jsp";
        
        if (rol != null) {
            if (rol.contains("Administrador")) {
                destino = "dashboard_admin.jsp";
            } else if (rol.contains("Vendedor")) {
                destino = "dashboard_vendedor.jsp";
            } else if (rol.contains("Almacenero")) {
                destino = "dashboard_almacenero.jsp";
            } else if (rol.contains("Cajero")) {
                destino = "dashboard_cajero.jsp";
            } else {
                destino = "dashboard.jsp";
            }
        }
        
        request.getRequestDispatcher(destino).forward(request, response);
    }
}