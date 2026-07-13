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

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UsuarioDAO usuarioDAO;
    
    @Override
    public void init() {
        usuarioDAO = new UsuarioDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Redirigir al login si ya está logueado
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("usuario") != null) {
            response.sendRedirect("dashboard");
        } else {
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String clave = request.getParameter("clave");
        
        try {
            if (username == null || username.trim().isEmpty() || 
                clave == null || clave.trim().isEmpty()) {
                
                request.setAttribute("error", "Por favor ingrese usuario y contraseña");
                request.getRequestDispatcher("login.jsp").forward(request, response);
                return;
            }
            
            UsuarioBean usuario = usuarioDAO.autenticar(username, clave);
            
            if (usuario != null) {
                // Login exitoso
                HttpSession session = request.getSession();
                session.setAttribute("usuario", usuario);
                session.setAttribute("nombreUsuario", usuario.getNombreCompleto());
                session.setAttribute("rolUsuario", usuario.getNombreRol());
                session.setMaxInactiveInterval(3600); // 1 hora
                
                response.sendRedirect("dashboard.jsp");
            } else {
                request.setAttribute("error", "Usuario o contraseña incorrectos");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            request.setAttribute("error", "Error al iniciar sesión: " + e.getMessage());
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
