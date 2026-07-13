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

@WebServlet(name = "VentaServlet", urlPatterns = {"/venta"})
public class VentaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private VentaDAO ventaDAO;
    
    @Override
    public void init() throws ServletException {
        ventaDAO = new VentaDAO();
        System.out.println("✅ VentaServlet inicializado");
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        UsuarioBean usuario = (UsuarioBean) session.getAttribute("usuario");
        
        if (usuario == null) {
            System.out.println("❌ Sesión no válida");
            response.sendRedirect("login.jsp");
            return;
        }
        
        System.out.println("🔐 Usuario autenticado: " + usuario.getUsername());
        
        try {
            // Obtener parámetros
            String codArticuloStr = request.getParameter("codArticulo");
            String cantidadStr = request.getParameter("cantidad");
            String precioVentaStr = request.getParameter("precioVenta");
            String codTiendaStr = request.getParameter("codTienda");
            String codTransportistaStr = request.getParameter("codTransportista");
            
            System.out.println("📦 Datos recibidos: codArticulo=" + codArticuloStr + 
                             ", cantidad=" + cantidadStr + 
                             ", precioVenta=" + precioVentaStr +
                             ", codTienda=" + codTiendaStr +
                             ", codTransportista=" + codTransportistaStr);
            
            // Validar que no sean nulos
            if (codArticuloStr == null || codArticuloStr.trim().isEmpty() ||
                cantidadStr == null || cantidadStr.trim().isEmpty() ||
                precioVentaStr == null || precioVentaStr.trim().isEmpty() ||
                codTiendaStr == null || codTiendaStr.trim().isEmpty() ||
                codTransportistaStr == null || codTransportistaStr.trim().isEmpty()) {
                
                session.setAttribute("mensaje", "❌ Todos los campos son obligatorios");
                session.setAttribute("tipoMensaje", "error");
                response.sendRedirect("dashboard_vendedor.jsp");
                return;
            }
            
            // Convertir parámetros
            int codArticulo = Integer.parseInt(codArticuloStr);
            int cantidad = Integer.parseInt(cantidadStr);
            double precioVenta = Double.parseDouble(precioVentaStr);
            int codTienda = Integer.parseInt(codTiendaStr);
            int codTransportista = Integer.parseInt(codTransportistaStr);
            
            // Validar datos
            if (cantidad <= 0) {
                session.setAttribute("mensaje", "❌ La cantidad debe ser mayor a 0");
                session.setAttribute("tipoMensaje", "error");
                response.sendRedirect("dashboard_vendedor.jsp");
                return;
            }
            
            if (precioVenta < 0) {
                session.setAttribute("mensaje", "❌ El precio no puede ser negativo");
                session.setAttribute("tipoMensaje", "error");
                response.sendRedirect("dashboard_vendedor.jsp");
                return;
            }
            
            // Crear objeto Venta (el número de guía se genera automáticamente)
            VentaBean venta = new VentaBean();
            venta.setCodArticulo(codArticulo);
            venta.setCantidad(cantidad);
            venta.setPrecioVenta(precioVenta);
            venta.setCodTienda(codTienda);
            venta.setCodTransportista(codTransportista);
            
            // Registrar venta
            boolean registrado = ventaDAO.registrarVenta(venta);
            
            if (registrado) {
                session.setAttribute("mensaje", "✅ Venta registrada exitosamente! Guía N° " + venta.getNumGuia());
                session.setAttribute("tipoMensaje", "success");
                System.out.println("✅ Venta registrada. Guía N° " + venta.getNumGuia());
            } else {
                session.setAttribute("mensaje", "❌ Error al registrar la venta");
                session.setAttribute("tipoMensaje", "error");
                System.out.println("❌ Fallo al registrar venta");
            }
            
        } catch (NumberFormatException e) {
            System.err.println("❌ Error de formato numérico: " + e.getMessage());
            session.setAttribute("mensaje", "❌ Datos inválidos. Verifica que los números sean correctos.");
            session.setAttribute("tipoMensaje", "error");
        } catch (Exception e) {
            System.err.println("❌ Error en VentaServlet: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("mensaje", "❌ Error: " + e.getMessage());
            session.setAttribute("tipoMensaje", "error");
        }
        
        response.sendRedirect("dashboard_vendedor.jsp");
    }
}