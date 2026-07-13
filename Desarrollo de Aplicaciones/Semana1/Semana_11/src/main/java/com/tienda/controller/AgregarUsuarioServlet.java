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
import java.sql.Connection;
import java.sql.PreparedStatement;
import com.tienda.dao.ConexionDB;

@WebServlet(name = "AgregarUsuarioServlet", urlPatterns = {"/agregarUsuario"})
public class AgregarUsuarioServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UsuarioDAO usuarioDAO;
    
    @Override
    public void init() {
        usuarioDAO = new UsuarioDAO();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        UsuarioBean usuario = (UsuarioBean) session.getAttribute("usuario");
        
        if (usuario == null || !usuario.getNombreRol().contains("Administrador")) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        try {
            String username = request.getParameter("username");
            String clave = request.getParameter("clave");
            String nombres = request.getParameter("nombres");
            String apellidos = request.getParameter("apellidos");
            String correo = request.getParameter("correo");
            int codRol = Integer.parseInt(request.getParameter("codRol"));
            int estado = Integer.parseInt(request.getParameter("estado"));
            
            // Verificar si el usuario ya existe
            if (usuarioDAO.existeUsuario(username)) {
                session.setAttribute("mensaje", "❌ El usuario '" + username + "' ya existe");
                session.setAttribute("tipoMensaje", "error");
                response.sendRedirect("admin?accion=usuarios");
                return;
            }
            
            // Insertar usuario
            String sql = "INSERT INTO USUARIO (Username, Clave, Nombres, Apellidos, Correo, Estado, CodRol) VALUES (?, ?, ?, ?, ?, ?, ?)";
            
            try (Connection conn = ConexionDB.getConexion();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                
                ps.setString(1, username);
                ps.setString(2, clave);
                ps.setString(3, nombres);
                ps.setString(4, apellidos);
                ps.setString(5, correo);
                ps.setInt(6, estado);
                ps.setInt(7, codRol);
                
                int insertado = ps.executeUpdate();
                
                if (insertado > 0) {
                    session.setAttribute("mensaje", "✅ Usuario '" + username + "' agregado exitosamente!");
                    session.setAttribute("tipoMensaje", "success");
                } else {
                    session.setAttribute("mensaje", "❌ Error al agregar usuario");
                    session.setAttribute("tipoMensaje", "error");
                }
            }
            
        } catch (Exception e) {
            session.setAttribute("mensaje", "❌ Error: " + e.getMessage());
            session.setAttribute("tipoMensaje", "error");
        }
        
        response.sendRedirect("admin?accion=usuarios");
    }
}