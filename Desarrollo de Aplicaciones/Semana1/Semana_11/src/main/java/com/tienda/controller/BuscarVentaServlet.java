/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tienda.controller;

import com.tienda.dao.VentaDAO;
import com.tienda.model.VentaBean;
import com.tienda.model.UsuarioBean;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "BuscarVentaServlet", urlPatterns = {"/buscarVenta"})
public class BuscarVentaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private VentaDAO ventaDAO;
    
    @Override
    public void init() throws ServletException {
        ventaDAO = new VentaDAO();
        System.out.println("✅ BuscarVentaServlet inicializado");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        UsuarioBean usuario = (UsuarioBean) session.getAttribute("usuario");
        
        if (usuario == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        try {
            String numGuiaStr = request.getParameter("numGuia");
            
            if (numGuiaStr == null || numGuiaStr.trim().isEmpty()) {
                session.setAttribute("mensaje", "❌ Ingrese un número de guía");
                session.setAttribute("tipoMensaje", "error");
                response.sendRedirect("dashboard.jsp");
                return;
            }
            
            int numGuia = Integer.parseInt(numGuiaStr);
            
            // Buscar la venta
            VentaBean venta = ventaDAO.obtenerVentaPorGuia(numGuia);
            
            if (venta != null) {
                // Guardar en sesión para mostrar en una página o redirigir
                session.setAttribute("ventaEncontrada", venta);
                session.setAttribute("mensaje", "✅ Venta encontrada: Guía N° " + numGuia);
                session.setAttribute("tipoMensaje", "success");
                
                // Redirigir a una página de detalle (puedes crear detalleVenta.jsp)
                request.setAttribute("venta", venta);
                request.getRequestDispatcher("detalleVenta.jsp").forward(request, response);
                
            } else {
                session.setAttribute("mensaje", "❌ No se encontró la guía N° " + numGuia);
                session.setAttribute("tipoMensaje", "error");
                response.sendRedirect("dashboard.jsp");
            }
            
        } catch (NumberFormatException e) {
            session.setAttribute("mensaje", "❌ Número de guía inválido");
            session.setAttribute("tipoMensaje", "error");
            response.sendRedirect("dashboard.jsp");
        } catch (Exception e) {
            System.err.println("❌ Error en BuscarVentaServlet: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("mensaje", "❌ Error: " + e.getMessage());
            session.setAttribute("tipoMensaje", "error");
            response.sendRedirect("dashboard.jsp");
        }
    }
}