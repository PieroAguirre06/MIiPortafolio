/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tienda.controller;

import com.tienda.dao.IngresoDAO;
import com.tienda.model.UsuarioBean;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "IngresoServlet", urlPatterns = {"/ingreso"})
public class IngresoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private IngresoDAO ingresoDAO;

    @Override
    public void init() {
        ingresoDAO = new IngresoDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        UsuarioBean usuario = (UsuarioBean) session.getAttribute("usuario");

        if (usuario == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            int codArticulo = Integer.parseInt(request.getParameter("codArticulo"));
            int cantidad = Integer.parseInt(request.getParameter("cantidad"));
            double precioCompra = Double.parseDouble(request.getParameter("precioCompra"));

            // 🔴 AQUÍ SE LLAMA AL MÉTODO: registrarIngreso
            boolean registrado = ingresoDAO.registrarIngreso(codArticulo, cantidad, precioCompra);

            if (registrado) {
                session.setAttribute("mensaje", "✅ Ingreso registrado exitosamente!");
                session.setAttribute("tipoMensaje", "success");
            } else {
                session.setAttribute("mensaje", "❌ Error al registrar el ingreso");
                session.setAttribute("tipoMensaje", "error");
            }

        } catch (Exception e) {
            session.setAttribute("mensaje", "❌ Error: " + e.getMessage());
            session.setAttribute("tipoMensaje", "error");
        }

        response.sendRedirect("dashboard_almacenero.jsp");
    }
}