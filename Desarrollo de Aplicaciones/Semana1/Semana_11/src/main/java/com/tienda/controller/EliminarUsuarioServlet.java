/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tienda.controller;

import com.tienda.dao.UsuarioDAO;
import com.tienda.model.UsuarioBean;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "EliminarUsuarioServlet", urlPatterns = {"/eliminarUsuario"})
public class EliminarUsuarioServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UsuarioDAO usuarioDAO;
    
    @Override
    public void init() {
        usuarioDAO = new UsuarioDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        UsuarioBean usuario = (UsuarioBean) session.getAttribute("usuario");
        
        if (usuario == null || !usuario.getNombreRol().contains("Administrador")) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        try {
            int codUsuario = Integer.parseInt(request.getParameter("cod"));
            
            boolean eliminado = usuarioDAO.eliminarUsuario(codUsuario);
            
            if (eliminado) {
                session.setAttribute("mensaje", "✅ Usuario eliminado exitosamente!");
                session.setAttribute("tipoMensaje", "success");
            } else {
                session.setAttribute("mensaje", "❌ Error al eliminar usuario");
                session.setAttribute("tipoMensaje", "error");
            }
            
        } catch (Exception e) {
            session.setAttribute("mensaje", "❌ Error: " + e.getMessage());
            session.setAttribute("tipoMensaje", "error");
        }
        
        response.sendRedirect("admin?accion=usuarios");
    }
}