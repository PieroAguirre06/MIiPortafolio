/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.planilla.controller;

import com.planilla.model.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/rrhh")
public class RrhhServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        HttpSession session = req.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        // Verificar que sea RRHH
        if (usuario == null || !"RRHH".equals(usuario.getNomRol())) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        if (action == null) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        switch (action) {
            case "registrarHoras":
                req.getRequestDispatcher("/rrhh/registrar_horas.jsp").forward(req, resp);
                break;
            case "registrarCategoria":
                req.getRequestDispatcher("/rrhh/registrar_categoria.jsp").forward(req, resp);
                break;
            case "registrarHijos":
                req.getRequestDispatcher("/rrhh/registrar_hijos.jsp").forward(req, resp);
                break;
            case "verHistorial":
                // Obtener historial de cálculos de la sesión
                List<String> historial = (List<String>) session.getAttribute("historialCalculos");
                if (historial == null) {
                    historial = new ArrayList<>();
                }
                req.setAttribute("historial", historial);
                req.getRequestDispatcher("/rrhh/ver_historial_rrhh.jsp").forward(req, resp);
                break;
            default:
                resp.sendRedirect(req.getContextPath() + "/index.jsp");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        HttpSession session = req.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null || !"RRHH".equals(usuario.getNomRol())) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        String mensaje = "";
        String tipo = "error";

        switch (action) {
            case "guardarHoras":
                String horasStr = req.getParameter("horas");
                // Simular guardado en sesión
                session.setAttribute("horasRegistradas", horasStr);
                mensaje = "✅ Horas registradas: " + horasStr;
                tipo = "exito";
                req.setAttribute("mensaje", mensaje);
                req.setAttribute("tipo", tipo);
                req.getRequestDispatcher("/rrhh/registrar_horas.jsp").forward(req, resp);
                break;

            case "guardarCategoria":
                String categoria = req.getParameter("categoria");
                session.setAttribute("categoriaRegistrada", categoria);
                mensaje = "✅ Categoría registrada: " + categoria;
                tipo = "exito";
                req.setAttribute("mensaje", mensaje);
                req.setAttribute("tipo", tipo);
                req.getRequestDispatcher("/rrhh/registrar_categoria.jsp").forward(req, resp);
                break;

            case "guardarHijos":
                String hijosStr = req.getParameter("hijos");
                session.setAttribute("hijosRegistrados", hijosStr);
                mensaje = "✅ Número de hijos registrado: " + hijosStr;
                tipo = "exito";
                req.setAttribute("mensaje", mensaje);
                req.setAttribute("tipo", tipo);
                req.getRequestDispatcher("/rrhh/registrar_hijos.jsp").forward(req, resp);
                break;

            default:
                resp.sendRedirect(req.getContextPath() + "/index.jsp");
                break;
        }
    }
}