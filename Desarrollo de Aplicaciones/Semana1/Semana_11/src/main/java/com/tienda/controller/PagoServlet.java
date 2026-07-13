/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tienda.controller;

import com.tienda.dao.VentaDAO;
import com.tienda.model.UsuarioBean;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "PagoServlet", urlPatterns = {"/pago"})
public class PagoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private VentaDAO ventaDAO;

    @Override
    public void init() throws ServletException {
        ventaDAO = new VentaDAO();
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
            int numGuia = Integer.parseInt(request.getParameter("numGuia"));
            String metodoPago = request.getParameter("metodoPago");
            double montoRecibido = Double.parseDouble(request.getParameter("montoRecibido"));

            // Verificar si la guía existe
            if (!ventaDAO.existeGuia(numGuia)) {
                session.setAttribute("mensaje", "❌ La guía N° " + numGuia + " no existe");
                session.setAttribute("tipoMensaje", "error");
                response.sendRedirect("dashboard_cajero.jsp");
                return;
            }

            double total = ventaDAO.obtenerTotalGuia(numGuia);

            if (total == 0.0) {
                session.setAttribute("mensaje", "❌ La guía N° " + numGuia + " no tiene productos");
                session.setAttribute("tipoMensaje", "error");
                response.sendRedirect("dashboard_cajero.jsp");
                return;
            }

            if (montoRecibido < total) {
                session.setAttribute("mensaje", "❌ Monto insuficiente");
                session.setAttribute("tipoMensaje", "error");
                response.sendRedirect("dashboard_cajero.jsp");
                return;
            }

            double vuelto = montoRecibido - total;

            // ========== GUARDAR PAGO EN SESIÓN ==========
            // Obtener lista de pagos de la sesión
            List<String> pagos = (List<String>) session.getAttribute("pagosCaja");
            if (pagos == null) {
                pagos = new ArrayList<>();
            }

            // Agregar el nuevo pago
            String pagoStr = "Guía #" + numGuia + " - S/ " + String.format("%.2f", total);
            pagos.add(pagoStr);
            session.setAttribute("pagosCaja", pagos);

            // Actualizar total de la caja
            Double totalCaja = (Double) session.getAttribute("totalCaja");
            if (totalCaja == null) {
                totalCaja = 0.0;
            }
            totalCaja += total;
            session.setAttribute("totalCaja", totalCaja);

            // ========== MENSAJE DE ÉXITO ==========
            session.setAttribute("mensaje", "✅ Pago registrado! Guía N° " + numGuia +
                              " - Total: S/ " + String.format("%.2f", total) +
                              " - Vuelto: S/ " + String.format("%.2f", vuelto));
            session.setAttribute("tipoMensaje", "success");

        } catch (Exception e) {
            session.setAttribute("mensaje", "❌ Error: " + e.getMessage());
            session.setAttribute("tipoMensaje", "error");
        }

        response.sendRedirect("dashboard_cajero.jsp");
    }
}