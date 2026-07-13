<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.planilla.model.Usuario, java.util.List" %>
<%
    Usuario usuario = (Usuario) session.getAttribute("usuario");
    if (usuario == null || !"Administrador".equals(usuario.getNomRol())) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    List<String> historial = (List<String>) request.getAttribute("historial");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Ver Cálculos</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: 'Inter', sans-serif; background: #e8f4f8; padding: 20px; }
        .container { max-width: 900px; margin: 0 auto; background: white; border-radius: 24px; padding: 30px; box-shadow: 0 20px 60px rgba(0,0,0,0.1); }
        h1 { color: #1F2937; margin-bottom: 20px; }
        .card { background: #F9FAFB; border-radius: 12px; padding: 16px; margin-bottom: 10px; border-left: 4px solid #2563EB; }
        .card .detalle { font-weight: 500; margin-top: 4px; }
        .empty { color: #6B7280; text-align: center; padding: 40px; }
        .btn-secondary { background: #e5e7eb; color: #374151; text-decoration: none; display: inline-block; padding: 10px 24px; border-radius: 8px; font-weight: 600; }
        .btn-secondary:hover { background: #d1d5db; }
        .acciones { margin-top: 20px; }
    </style>
</head>
<body>
<div class="container">
    <h1>📊 Historial de Cálculos</h1>
    
    <% if (historial != null && !historial.isEmpty()) {
        for (String registro : historial) {
    %>
        <div class="card">
            <div class="detalle"><%= registro %></div>
        </div>
    <%      }
        } else { %>
        <div class="empty">No hay cálculos registrados aún.</div>
    <% } %>
    
    <div class="acciones">
        <a href="${pageContext.request.contextPath}/index.jsp" class="btn-secondary">← Volver al Panel</a>
    </div>
</div>
</body>
</html>