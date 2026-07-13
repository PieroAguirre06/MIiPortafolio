<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.planilla.model.Usuario, com.planilla.model.Empleado" %>
<%
    Usuario usuario = (Usuario) session.getAttribute("usuario");
    if (usuario == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    String rol = usuario.getNomRol();
    boolean esAdmin = "Administrador".equals(rol);
    boolean esRRHH = "RRHH".equals(rol);
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Panel - QhatuPERU</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800&display=swap" rel="stylesheet">
    <style>
        /* ===== TODOS LOS ESTILOS (igual que antes) ===== */
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body {
            font-family: 'Inter', sans-serif;
            background: #e8f4f8;
            min-height: 100vh;
            padding: 20px;
        }
        .container {
            max-width: 1000px;
            margin: 0 auto;
            background: white;
            border-radius: 24px;
            padding: 30px;
            box-shadow: 0 20px 60px rgba(0,0,0,0.1);
        }
        .header-university {
            text-align: center;
            border-bottom: 2px solid #e5e7eb;
            padding-bottom: 16px;
            margin-bottom: 16px;
        }
        .logos { display: flex; justify-content: center; gap: 20px; margin-bottom: 10px; }
        .logos img { height: 50px; width: auto; }
        .header-university h2 { font-size: 14px; color: #6B7280; font-weight: 600; letter-spacing: 1px; text-transform: uppercase; }
        .header-university .student-name { font-size: 16px; color: #1F2937; font-weight: 600; margin: 4px 0; }
        .header-university .student-role { font-size: 12px; color: #9CA3AF; font-weight: 500; }
        .user-bar {
            display: flex;
            justify-content: space-between;
            align-items: center;
            background: #F3F4F6;
            padding: 10px 16px;
            border-radius: 12px;
            margin-bottom: 20px;
            flex-wrap: wrap;
        }
        .user-info { font-size: 14px; }
        .btn-logout {
            background: #EF4444;
            color: white;
            padding: 6px 14px;
            border-radius: 8px;
            text-decoration: none;
            font-weight: 600;
            font-size: 13px;
            transition: 0.2s;
        }
        .btn-logout:hover { background: #DC2626; }
        .role-menu {
            display: flex;
            gap: 12px;
            margin-bottom: 20px;
            flex-wrap: wrap;
            padding: 12px 16px;
            background: #F9FAFB;
            border-radius: 12px;
            border: 1px solid #E5E7EB;
        }
        .role-menu .btn-role {
            padding: 10px 18px;
            background: #e5e7eb;
            border: none;
            border-radius: 8px;
            font-weight: 600;
            color: #374151;
            cursor: pointer;
            transition: 0.2s;
            text-decoration: none;
            display: inline-block;
            font-size: 14px;
        }
        .role-menu .btn-role:hover { background: #d1d5db; }
        .role-menu .btn-role.primary { background: #2563EB; color: white; }
        .role-menu .btn-role.primary:hover { background: #1D4ED8; }
        .role-menu .btn-role.success { background: #059669; color: white; }
        .role-menu .btn-role.success:hover { background: #047857; }
        .role-menu .btn-role.warning { background: #D97706; color: white; }
        .role-menu .btn-role.warning:hover { background: #B45309; }
        .role-menu .btn-role.danger { background: #DC2626; color: white; }
        .role-menu .btn-role.danger:hover { background: #B91C1C; }
        .role-menu .menu-label {
            font-weight: 700;
            color: #374151;
            display: flex;
            align-items: center;
            margin-right: 8px;
            font-size: 14px;
        }
        .main-title { text-align: center; margin-bottom: 16px; }
        .main-title h1 { font-size: 28px; font-weight: 800; color: #1F2937; letter-spacing: -0.5px; }
        .main-title .subtitle { font-size: 16px; color: #6B7280; margin-top: 8px; }
        .input-section {
            background: #F9FAFB;
            border-radius: 16px;
            padding: 24px;
            margin: 16px 0;
        }
        .section-label { font-size: 14px; font-weight: 700; color: #374151; text-transform: uppercase; letter-spacing: 0.5px; display: block; margin-bottom: 16px; }
        .form-group { margin-bottom: 16px; }
        .form-group label { display: block; font-size: 14px; font-weight: 600; color: #374151; margin-bottom: 6px; }
        .form-group select, .form-group input {
            width: 100%;
            padding: 10px 14px;
            border: 1.5px solid #D1D5DB;
            border-radius: 10px;
            font-size: 15px;
            background: white;
            transition: 0.2s;
        }
        .form-group select:focus, .form-group input:focus { outline: none; border-color: #2563EB; box-shadow: 0 0 0 3px rgba(37,99,235,0.1); }
        .btn-calculate {
            width: 100%;
            padding: 14px;
            background: #2563EB;
            color: white;
            border: none;
            border-radius: 12px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: 0.2s;
            margin-top: 8px;
        }
        .btn-calculate:hover { background: #1D4ED8; transform: translateY(-1px); box-shadow: 0 4px 12px rgba(37,99,235,0.3); }
        .results-section { margin-top: 24px; border-top: 2px solid #E5E7EB; padding-top: 24px; }
        .results-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 12px; }
        .result-card { background: #F9FAFB; border-radius: 12px; padding: 16px; text-align: center; }
        .result-card .result-label { font-size: 12px; color: #6B7280; font-weight: 500; text-transform: uppercase; letter-spacing: 0.3px; }
        .result-card .result-value { font-size: 22px; font-weight: 700; color: #1F2937; margin-top: 4px; }
        .result-value.positive { color: #059669; }
        .result-value.negative { color: #DC2626; }
        .result-card.highlight { background: #EFF6FF; border: 1.5px solid #BFDBFE; }
        .result-card.highlight .result-value { color: #1D4ED8; }
        .error-message { background: #FEE2E2; color: #991B1B; padding: 12px 16px; border-radius: 10px; font-size: 14px; font-weight: 500; margin-top: 16px; }
        .footer { text-align: center; margin-top: 24px; padding-top: 16px; border-top: 1px solid #E5E7EB; font-size: 12px; color: #9CA3AF; }
        @media (max-width: 600px) { .results-grid { grid-template-columns: 1fr; } }
    </style>
</head>
<body>
<div class="container">
    <!-- Encabezado -->
    <div class="header-university">
        <div class="logos">
            <img src="${pageContext.request.contextPath}/imagenes/logo-upla.png" alt="UPLA">
            <img src="${pageContext.request.contextPath}/imagenes/logo-facultad.png" alt="Facultad">
        </div>
        <h2>UNIVERSIDAD PERUANA LOS ANDES</h2>
        <div class="student-name">AGUIRRE OSORES PIERO</div>
        <div class="student-role">Estudiante · PROGRAMACIÓN · Actividad 02</div>
        <div style="font-size: 12px; color: #6B7280; margin-top: 2px;">FACULTAD DE INGENIERÍA · Huancayo</div>
    </div>

    <!-- Barra de usuario -->
    <div class="user-bar">
        <span class="user-info">👋 Bienvenido, <strong><%= usuario.getNombres() %></strong> (<%= rol %>)</span>
        <a href="logout" class="btn-logout">Cerrar sesión</a>
    </div>

    <!-- ===== MENÚ SEGÚN ROL ===== -->
    <div class="role-menu">
        <span class="menu-label">📋 Acciones:</span>
<% if (esAdmin) { %>
    <a href="${pageContext.request.contextPath}/admin?action=registrarEmpleado" class="btn-role primary">📋 Registrar Empleados</a>
    <a href="${pageContext.request.contextPath}/admin?action=editarEmpleado" class="btn-role primary">✏️ Editar Empleados</a>
    <a href="${pageContext.request.contextPath}/admin?action=eliminarEmpleado" class="btn-role danger">🗑️ Eliminar Empleados</a>
    <a href="${pageContext.request.contextPath}/admin?action=registrarUsuario" class="btn-role success">👥 Registrar Usuarios</a>
    <a href="${pageContext.request.contextPath}/admin?action=verCalculos" class="btn-role warning">📊 Ver Cálculos</a>
    <a href="${pageContext.request.contextPath}/admin?action=reportes" class="btn-role warning">📈 Generar Reportes</a>
<% } else if (esRRHH) { %>
    <a href="${pageContext.request.contextPath}/rrhh?action=registrarHoras" class="btn-role primary">📋 Registrar Horas</a>
    <a href="${pageContext.request.contextPath}/rrhh?action=registrarCategoria" class="btn-role primary">📝 Registrar Categoría</a>
    <a href="${pageContext.request.contextPath}/rrhh?action=registrarHijos" class="btn-role success">👶 Registrar Hijos</a>
    <a href="${pageContext.request.contextPath}/rrhh?action=verHistorial" class="btn-role warning">📊 Ver Historial</a>
<% } else { %>
    <span>Sin acciones definidas para este rol.</span>
<% } %>
    </div>

    <!-- ===== CALCULADORA ===== -->
    <div class="main-title">
        <h1>Calculadora de Planilla</h1>
        <p class="subtitle">Calcula sueldo básico, bruto, descuentos y neto</p>
    </div>

    <form action="${pageContext.request.contextPath}/calcular" method="POST" class="input-section">
        <span class="section-label">📊 DATOS DE ENTRADA</span>
        <div class="form-group">
            <label for="categoria">Categoría</label>
            <select id="categoria" name="categoria" required>
                <option value="">Seleccionar</option>
                <option value="A" ${param.categoria == 'A' ? 'selected' : ''}>Categoría A — S/ 45.00/hora</option>
                <option value="B" ${param.categoria == 'B' ? 'selected' : ''}>Categoría B — S/ 37.50/hora</option>
            </select>
        </div>
        <div class="form-group">
            <label for="horas">Horas trabajadas</label>
            <input type="number" id="horas" name="horas" placeholder="Ej. 40" value="${param.horas}" min="0" required>
        </div>
        <div class="form-group">
            <label for="hijos">Número de hijos</label>
            <input type="number" id="hijos" name="hijos" placeholder="Ej. 2" value="${param.hijos}" min="0" required>
        </div>
        <button type="submit" class="btn-calculate">Calcular sueldo</button>
    </form>

    <div class="error-message" id="errorMessage" style="display:${not empty error ? 'block' : 'none'}">
        ${error}
    </div>

    <%
        Empleado emp = (Empleado) request.getAttribute("empleado");
        Boolean resultado = (Boolean) request.getAttribute("resultado");
        if (resultado != null && resultado && emp != null) {
    %>
    <div class="results-section">
        <span class="section-label">💰 RESULTADOS</span>
        <div class="results-grid">
            <div class="result-card">
                <div class="result-label">Sueldo Básico</div>
                <div class="result-value">S/ <%= String.format("%,.2f", emp.getSueldoBasico()) %></div>
            </div>
            <div class="result-card">
                <div class="result-label">Sueldo Bruto</div>
                <div class="result-value">S/ <%= String.format("%,.2f", emp.getSueldoBruto()) %></div>
            </div>
            <div class="result-card">
                <div class="result-label">Descuento</div>
                <div class="result-value negative">-S/ <%= String.format("%,.2f", emp.getDescuento()) %></div>
            </div>
            <div class="result-card highlight">
                <div class="result-label">Sueldo Neto</div>
                <div class="result-value positive">S/ <%= String.format("%,.2f", emp.getSueldoNeto()) %></div>
            </div>
        </div>
    </div>
    <% } %>

    <div class="footer">Actividad 02 · Programación · Universidad Peruana Los Andes</div>
</div>
<script>
    setTimeout(() => {
        const errorMsg = document.getElementById('errorMessage');
        if (errorMsg) errorMsg.style.display = 'none';
    }, 3000);
</script>
</body>
</html>