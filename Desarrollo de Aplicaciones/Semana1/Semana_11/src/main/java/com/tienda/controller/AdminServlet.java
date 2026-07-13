/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tienda.controller;

import com.tienda.dao.ProductoDAO;
import com.tienda.dao.UsuarioDAO;
import com.tienda.dao.VentaDAO;
import com.tienda.model.ProductoBean;
import com.tienda.model.UsuarioBean;
import com.tienda.model.VentaBean;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "AdminServlet", urlPatterns = {"/admin"})
public class AdminServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        UsuarioBean usuario = (UsuarioBean) session.getAttribute("usuario");
        
        if (usuario == null || !usuario.getNombreRol().contains("Administrador")) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        String accion = request.getParameter("accion");
        String destino = "dashboard_admin.jsp";
        
        try {
            if ("usuarios".equals(accion)) {
                UsuarioDAO usuarioDAO = new UsuarioDAO();
                List<UsuarioBean> usuarios = usuarioDAO.listarUsuarios();
                request.setAttribute("usuarios", usuarios);
                destino = "admin_usuarios.jsp";
                
            } else if ("inventario".equals(accion)) {
                ProductoDAO productoDAO = new ProductoDAO();
                List<ProductoBean> productos = productoDAO.listarProductos();
                request.setAttribute("productos", productos);
                destino = "admin_inventario.jsp";
                
            } else if ("reportes".equals(accion)) {
                VentaDAO ventaDAO = new VentaDAO();
                List<VentaBean> ventas = ventaDAO.listarVentas();
                request.setAttribute("ventas", ventas);
                destino = "admin_reportes.jsp";
            }
        } catch (Exception e) {
            session.setAttribute("mensaje", "❌ Error al cargar datos: " + e.getMessage());
            session.setAttribute("tipoMensaje", "error");
            e.printStackTrace();
        }
        
        request.getRequestDispatcher(destino).forward(request, response);
    }
}