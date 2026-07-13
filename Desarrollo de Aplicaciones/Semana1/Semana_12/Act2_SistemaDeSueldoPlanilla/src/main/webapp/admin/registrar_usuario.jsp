<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.planilla.model.Usuario, com.planilla.model.Rol, java.util.List" %>
<%
    Usuario usuario = (Usuario) session.getAttribute("usuario");
    if (usuario == null || !"Administrador".equals(usuario.getNomRol())) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    List<Rol> roles = (List<Rol>) request.getAttribute("roles");
    if (roles == null) {
        roles = new com.planilla.dao.UsuarioDAO().listarRoles();
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Registrar Usuario</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: 'Inter', sans-serif; background: #e8f4f8; padding: 20px; }
        .container { max-width: 600px; margin: 0 auto; background: white; border-radius: 24px; padding: 30px; box-shadow: 0 20px 60px rgba(0,0,0,0.1); }
        h1 { color: #1F2937; margin-bottom: 20px; }
        .form-group { margin-bottom: 16px; }
        label { display: block; font-weight: 600; margin-bottom: 6px; color: #374151; }
        input, select { width: 100%; padding: 10px; border: 1.5px solid #D1D5DB; border-radius: 8px; font-size: 15px; }
        input:focus, select:focus { outline: none; border-color: #2563EB; }
        .btn { padding: 10px 24px; border: none; border-radius: 8px; font-weight: 600; cursor: pointer; }
        .btn-primary { background: #2563EB; color: white; }
        .btn-primary:hover { background: #1D4ED8; }
        .btn-secondary { background: #e5e7eb; color: #374151; text-decoration: none; display: inline-block; }
        .btn-secondary:hover { background: #d1d5db; }
        .mensaje { padding: 12px; border-radius: 8px; margin-bottom: 16px; }
        .mensaje.exito { background: #D1FAE5; color: #065F46; }
        .mensaje.error { background: #FEE2E2; color: #991B1B; }
        .acciones { display: flex; gap: 12px; margin-top: 20px; flex-wrap: wrap; }
    </style>
</head>
<body>
<div class="container">
    <h1>👥 Registrar Usuario del Sistema</h1>
    
    <%
        String mensaje = (String) request.getAttribute("mensaje");
        String tipo = (String) request.getAttribute("tipo");
        if (mensaje != null && !mensaje.isEmpty()) {
    %>
        <div class="mensaje <%= "exito".equals(tipo) ? "exito" : "error" %>"><%= mensaje %></div>
    <% } %>
    
    <form action="${pageContext.request.contextPath}/admin" method="POST">
        <input type="hidden" name="action" value="guardarUsuario">
        <div class="form-group">
            <label>Usuario</label>
            <input type="text" name="username" required>
        </div>
        <div class="form-group">
            <label>Contraseña</label>
            <input type="password" name="clave" required>
        </div>
        <div class="form-group">
            <label>Nombres</label>
            <input type="text" name="nombres" required>
        </div>
        <div class="form-group">
            <label>Apellidos</label>
            <input type="text" name="apellidos" required>
        </div>
        <div class="form-group">
            <label>Correo</label>
            <input type="email" name="correo" required>
        </div>
        <div class="form-group">
            <label>Rol</label>
            <select name="codRol" required>
                <option value="">Seleccionar</option>
                <% for (Rol r : roles) { %>
                    <option value="<%= r.getCodRol() %>"><%= r.getNomRol() %></option>
                <% } %>
            </select>
        </div>
        <button type="submit" class="btn btn-primary">Registrar Usuario</button>
    </form>
    
    <div class="acciones">
        <a href="${pageContext.request.contextPath}/index.jsp" class="btn btn-secondary">← Volver al Panel</a>
    </div>
</div>
</body>
</html>