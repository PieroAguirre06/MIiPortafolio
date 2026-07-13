<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.planilla.model.Usuario" %>
<%
    Usuario usuario = (Usuario) session.getAttribute("usuario");
    if (usuario == null || !"Administrador".equals(usuario.getNomRol())) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    Integer totalEmpleados = (Integer) request.getAttribute("totalEmpleados");
    if (totalEmpleados == null) totalEmpleados = 0;
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Reportes</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: 'Inter', sans-serif; background: #e8f4f8; padding: 20px; }
        .container { max-width: 800px; margin: 0 auto; background: white; border-radius: 24px; padding: 30px; box-shadow: 0 20px 60px rgba(0,0,0,0.1); }
        h1 { color: #1F2937; margin-bottom: 20px; }
        .stats { display: grid; grid-template-columns: 1fr 1fr 1fr; gap: 20px; margin: 20px 0; }
        .stat-card { background: #F3F4F6; border-radius: 12px; padding: 20px; text-align: center; }
        .stat-card .numero { font-size: 32px; font-weight: 700; color: #2563EB; }
        .stat-card .label { font-size: 14px; color: #6B7280; margin-top: 4px; }
        .btn-secondary { background: #e5e7eb; color: #374151; text-decoration: none; display: inline-block; padding: 10px 24px; border-radius: 8px; font-weight: 600; }
        .btn-secondary:hover { background: #d1d5db; }
        .acciones { margin-top: 20px; }
    </style>
</head>
<body>
<div class="container">
    <h1>📈 Reportes del Sistema</h1>
    
    <div class="stats">
        <div class="stat-card">
            <div class="numero"><%= totalEmpleados %></div>
            <div class="label">Empleados Activos</div>
        </div>
        <div class="stat-card">
            <div class="numero">2</div>
            <div class="label">Roles Disponibles</div>
        </div>
        <div class="stat-card">
            <div class="numero">1</div>
            <div class="label">Administradores</div>
        </div>
    </div>
    
    <div style="background: #F9FAFB; border-radius: 12px; padding: 16px; margin-top: 16px;">
        <p><strong>Resumen:</strong></p>
        <p>• El sistema cuenta con <strong><%= totalEmpleados %></strong> empleados registrados.</p>
        <p>• Los roles disponibles son: Administrador y RRHH.</p>
        <p>• Cada cálculo de sueldo queda registrado en el historial.</p>
    </div>
    
    <div class="acciones">
        <a href="${pageContext.request.contextPath}/index.jsp" class="btn-secondary">← Volver al Panel</a>
    </div>
</div>
</body>
</html>