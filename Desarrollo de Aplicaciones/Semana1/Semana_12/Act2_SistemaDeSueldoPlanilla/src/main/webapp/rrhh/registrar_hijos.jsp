<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.planilla.model.Usuario" %>
<%
    Usuario usuario = (Usuario) session.getAttribute("usuario");
    if (usuario == null || !"RRHH".equals(usuario.getNomRol())) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Registrar Hijos</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: 'Inter', sans-serif; background: #e8f4f8; padding: 20px; }
        .container { max-width: 600px; margin: 0 auto; background: white; border-radius: 24px; padding: 30px; box-shadow: 0 20px 60px rgba(0,0,0,0.1); }
        h1 { color: #1F2937; margin-bottom: 20px; }
        .form-group { margin-bottom: 16px; }
        label { display: block; font-weight: 600; margin-bottom: 6px; color: #374151; }
        input { width: 100%; padding: 10px; border: 1.5px solid #D1D5DB; border-radius: 8px; font-size: 15px; }
        input:focus { outline: none; border-color: #2563EB; }
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
    <h1>👶 Registrar Número de Hijos</h1>
    
    <%
        String mensaje = (String) request.getAttribute("mensaje");
        String tipo = (String) request.getAttribute("tipo");
        if (mensaje != null && !mensaje.isEmpty()) {
    %>
        <div class="mensaje <%= "exito".equals(tipo) ? "exito" : "error" %>"><%= mensaje %></div>
    <% } %>
    
    <form action="${pageContext.request.contextPath}/rrhh" method="POST">
        <input type="hidden" name="action" value="guardarHijos">
        <div class="form-group">
            <label>Número de hijos</label>
            <input type="number" name="hijos" placeholder="Ej. 2" min="0" required>
        </div>
        <button type="submit" class="btn btn-primary">Guardar Hijos</button>
    </form>
    
    <div class="acciones">
        <a href="${pageContext.request.contextPath}/index.jsp" class="btn btn-secondary">← Volver al Panel</a>
    </div>
</div>
</body>
</html>