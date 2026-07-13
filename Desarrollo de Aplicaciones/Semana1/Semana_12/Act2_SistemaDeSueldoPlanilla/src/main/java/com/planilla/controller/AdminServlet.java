/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.planilla.controller;

import com.planilla.dao.UsuarioDAO;
import com.planilla.model.Rol;
import com.planilla.model.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        HttpSession session = req.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        // Verificar que sea administrador
        if (usuario == null || !"Administrador".equals(usuario.getNomRol())) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        if (action == null) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        UsuarioDAO dao = new UsuarioDAO();

        switch (action) {
            case "registrarEmpleado":
                req.getRequestDispatcher("/admin/gestion_empleados.jsp").forward(req, resp);
                break;

            case "editarEmpleado":
                List<Usuario> empleados = dao.listarEmpleados();
                req.setAttribute("empleados", empleados);
                req.getRequestDispatcher("/admin/editar_empleado.jsp").forward(req, resp);
                break;

            case "eliminarEmpleado":
                List<Usuario> empleadosEliminar = dao.listarEmpleados();
                req.setAttribute("empleados", empleadosEliminar);
                req.getRequestDispatcher("/admin/eliminar_empleado.jsp").forward(req, resp);
                break;

            case "registrarUsuario":
                List<Rol> roles = dao.listarRoles();
                req.setAttribute("roles", roles);
                req.getRequestDispatcher("/admin/registrar_usuario.jsp").forward(req, resp);
                break;

            case "verCalculos":
                List<String> historial = (List<String>) session.getAttribute("historialCalculos");
                req.setAttribute("historial", historial);
                req.getRequestDispatcher("/admin/ver_calculos.jsp").forward(req, resp);
                break;

            case "reportes":
                int totalEmpleados = dao.contarEmpleados();
                req.setAttribute("totalEmpleados", totalEmpleados);
                req.getRequestDispatcher("/admin/reportes.jsp").forward(req, resp);
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

        if (usuario == null || !"Administrador".equals(usuario.getNomRol())) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        UsuarioDAO dao = new UsuarioDAO();
        String mensaje = "";
        String tipo = "error";

        switch (action) {
            case "guardarEmpleado":
                String username = req.getParameter("username");
                String clave = req.getParameter("clave");
                String nombres = req.getParameter("nombres");
                String apellidos = req.getParameter("apellidos");
                String correo = req.getParameter("correo");

                if (dao.registrarEmpleado(username, clave, nombres, apellidos, correo)) {
                    mensaje = "✅ Empleado registrado exitosamente";
                    tipo = "exito";
                } else {
                    mensaje = "❌ Error: El usuario ya existe o datos inválidos.";
                }
                req.setAttribute("mensaje", mensaje);
                req.setAttribute("tipo", tipo);
                req.getRequestDispatcher("/admin/gestion_empleados.jsp").forward(req, resp);
                break;

            case "actualizarEmpleado":
                int codUsuario = Integer.parseInt(req.getParameter("codUsuario"));
                String nuevosNombres = req.getParameter("nombres");
                String nuevosApellidos = req.getParameter("apellidos");
                String nuevoCorreo = req.getParameter("correo");

                if (dao.actualizarEmpleado(codUsuario, nuevosNombres, nuevosApellidos, nuevoCorreo)) {
                    mensaje = "✅ Empleado actualizado exitosamente";
                    tipo = "exito";
                } else {
                    mensaje = "❌ Error al actualizar empleado";
                }
                req.setAttribute("mensaje", mensaje);
                req.setAttribute("tipo", tipo);
                req.setAttribute("empleados", dao.listarEmpleados());
                req.getRequestDispatcher("/admin/editar_empleado.jsp").forward(req, resp);
                break;

            case "eliminarEmpleado":
                int codEliminar = Integer.parseInt(req.getParameter("codUsuario"));
                if (dao.eliminarEmpleado(codEliminar)) {
                    mensaje = "✅ Empleado eliminado exitosamente";
                    tipo = "exito";
                } else {
                    mensaje = "❌ Error al eliminar empleado";
                }
                req.setAttribute("mensaje", mensaje);
                req.setAttribute("tipo", tipo);
                req.setAttribute("empleados", dao.listarEmpleados());
                req.getRequestDispatcher("/admin/eliminar_empleado.jsp").forward(req, resp);
                break;

            case "guardarUsuario":
                String user = req.getParameter("username");
                String pass = req.getParameter("clave");
                String nom = req.getParameter("nombres");
                String ape = req.getParameter("apellidos");
                String email = req.getParameter("correo");
                int codRol = Integer.parseInt(req.getParameter("codRol"));

                if (dao.registrarUsuarioSistema(user, pass, nom, ape, email, codRol)) {
                    mensaje = "✅ Usuario registrado exitosamente";
                    tipo = "exito";
                } else {
                    mensaje = "❌ Error: El usuario ya existe o datos inválidos.";
                }
                req.setAttribute("mensaje", mensaje);
                req.setAttribute("tipo", tipo);
                req.setAttribute("roles", dao.listarRoles());
                req.getRequestDispatcher("/admin/registrar_usuario.jsp").forward(req, resp);
                break;

            default:
                resp.sendRedirect(req.getContextPath() + "/index.jsp");
                break;
        }
    }
}