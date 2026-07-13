<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.planilla.model.Usuario, java.util.List" %>
<%
    Usuario usuario = (Usuario) session.getAttribute("usuario");
    if (usuario == null || !"Administrador".equals(usuario.getNomRol())) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    List<Usuario> empleados = (List<Usuario>) request.getAttribute("empleados");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Editar Empleados</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: 'Inter', sans-serif; background: #e8f4f8; padding: 20px; }
        .container { max-width: 900px; margin: 0 auto; background: white; border-radius: 24px; padding: 30px; box-shadow: 0 20px 60px rgba(0,0,0,0.1); }
        h1 { color: #1F2937; margin-bottom: 20px; }
        table { width: 100%; border-collapse: collapse; margin-top: 16px; }
        th, td { padding: 10px 12px; text-align: left; border-bottom: 1px solid #E5E7EB; }
        th { background: #F3F4F6; font-weight: 600; color: #374151; }
        .btn { padding: 6px 14px; border: none; border-radius: 6px; font-weight: 600; cursor: pointer; text-decoration: none; display: inline-block; font-size: 13px; }
        .btn-primary { background: #2563EB; color: white; }
        .btn-primary:hover { background: #1D4ED8; }
        .btn-secondary { background: #e5e7eb; color: #374151; }
        .btn-secondary:hover { background: #d1d5db; }
        .mensaje { padding: 12px; border-radius: 8px; margin-bottom: 16px; }
        .mensaje.exito { background: #D1FAE5; color: #065F46; }
        .mensaje.error { background: #FEE2E2; color: #991B1B; }
        .acciones { display: flex; gap: 12px; margin-top: 20px; flex-wrap: wrap; }
        .form-inline { display: inline; }
        .form-inline input { padding: 6px 10px; border: 1px solid #D1D5DB; border-radius: 4px; font-size: 13px; }
        .form-inline .btn { margin-left: 4px; }
    </style>
</head>
<body>
<div class="container">
    <h1>✏️ Editar Empleados</h1>
    
    <%
        String mensaje = (String) request.getAttribute("mensaje");
        String tipo = (String) request.getAttribute("tipo");
        if (mensaje != null && !mensaje.isEmpty()) {
    %>
        <div class="mensaje <%= "exito".equals(tipo) ? "exito" : "error" %>"><%= mensaje %></div>
    <% } %>
    
    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Usuario</th>
                <th>Nombres</th>
                <th>Apellidos</th>
                <th>Correo</th>
                <th>Acción</th>
            </tr>
        </thead>
        <tbody>
            <% if (empleados != null && !empleados.isEmpty()) {
                for (Usuario emp : empleados) {
            %>
            <tr>
                <form action="${pageContext.request.contextPath}/admin" method="POST" class="form-inline">
                    <input type="hidden" name="action" value="actualizarEmpleado">
                    <input type="hidden" name="codUsuario" value="<%= emp.getCodUsuario() %>">
                    <td><%= emp.getCodUsuario() %></td>
                    <td><%= emp.getUsername() %></td>
                    <td><input type="text" name="nombres" value="<%= emp.getNombres() %>" required></td>
                    <td><input type="text" name="apellidos" value="<%= emp.getApellidos() %>" required></td>
                    <td><input type="email" name="correo" value="<%= emp.getCorreo() %>" required></td>
                    <td><button type="submit" class="btn btn-primary">Actualizar</button></td>
                </form>
            </tr>
            <%      }
                } else { %>
            <tr><td colspan="6">No hay empleados registrados.</td></tr>
            <% } %>
        </tbody>
    </table>
    
    <div class="acciones">
        <a href="${pageContext.request.contextPath}/index.jsp" class="btn btn-secondary">← Volver al Panel</a>
    </div>
</div>
</body>
</html>