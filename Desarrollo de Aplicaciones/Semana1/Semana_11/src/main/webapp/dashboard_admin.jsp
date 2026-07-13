<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.tienda.model.UsuarioBean" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin - QhatuPERU</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: #e8f4f8;
            min-height: 100vh;
        }
        .navbar {
            background: #1a3a4a;
            padding: 15px 30px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            color: white;
            flex-wrap: wrap;
        }
        .navbar .logo { font-size: 24px; font-weight: 700; }
        .navbar .logo span { color: #2ecc71; }
        .navbar .user-info { display: flex; align-items: center; gap: 15px; flex-wrap: wrap; }
        .navbar .role-badge {
            background: #2ecc71;
            padding: 5px 15px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: 600;
        }
        .btn-logout {
            background: #e74c3c;
            color: white;
            border: none;
            padding: 8px 20px;
            border-radius: 8px;
            cursor: pointer;
            font-weight: 600;
            text-decoration: none;
        }
        .btn-logout:hover { background: #c0392b; }
        
        .container { max-width: 1200px; margin: 30px auto; padding: 0 20px; }
        .dashboard-title { color: #1a3a4a; margin-bottom: 25px; }
        
        .grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
            gap: 25px;
        }
        .card {
            background: white;
            border-radius: 12px;
            padding: 25px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.08);
            transition: all 0.3s ease;
            cursor: pointer;
        }
        .card:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 30px rgba(0,0,0,0.15);
        }
        .card .icon { font-size: 40px; margin-bottom: 10px; }
        .card h3 { color: #1a3a4a; font-size: 18px; }
        .card p { color: #6b8a9a; font-size: 14px; margin-top: 5px; }
        .card .badge {
            display: inline-block;
            color: white;
            padding: 2px 12px;
            border-radius: 12px;
            font-size: 11px;
            margin-top: 10px;
        }
        .card .badge.green { background: #2ecc71; }
        .card .badge.orange { background: #f39c12; }
        .card .badge.blue { background: #3498db; }
        .card .badge.purple { background: #9b59b6; }
        
        .mensaje {
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 20px;
            border-left: 4px solid #28a745;
        }
        .mensaje.error { border-left-color: #dc3545; background: #f8d7da; color: #721c24; }
        .mensaje.success { border-left-color: #28a745; background: #d4edda; color: #155724; }
    </style>
</head>
<body>
    <%
        UsuarioBean usuario = (UsuarioBean) session.getAttribute("usuario");
        if (usuario == null) {
            response.sendRedirect("login.jsp");
            return;
        }
    %>

    <div class="navbar">
        <div class="logo">🏪 <span>Qhatu</span>PERU</div>
        <div class="user-info">
            <span class="role-badge">👑 Administrador</span>
            <span>👤 <%= usuario.getNombreCompleto() %></span>
            <a href="logout" class="btn-logout">🚪 Cerrar Sesión</a>
        </div>
    </div>

    <div class="container">
        <h2 class="dashboard-title">📊 Panel de Administrador</h2>
        
        <% if (session.getAttribute("mensaje") != null) { %>
            <div class="mensaje <%= session.getAttribute("tipoMensaje") %>">
                <%= session.getAttribute("mensaje") %>
            </div>
            <% session.removeAttribute("mensaje"); session.removeAttribute("tipoMensaje"); %>
        <% } %>
        
        <div class="grid">
            <div class="card" onclick="location.href='admin?accion=usuarios'">
                <div class="icon">👥</div>
                <h3>Gestión de Usuarios</h3>
                <p>Crear, editar y eliminar usuarios del sistema</p>
                <span class="badge green">✅ Activo</span>
            </div>
            <div class="card" onclick="location.href='admin?accion=inventario'">
                <div class="icon">📦</div>
                <h3>Gestión de Inventario</h3>
                <p>Control de stock y productos</p>
                <span class="badge orange">📊 Ver Stock</span>
            </div>
            <div class="card" onclick="location.href='admin?accion=reportes'">
                <div class="icon">📊</div>
                <h3>Reportes de Ventas</h3>
                <p>Estadísticas y análisis de ventas</p>
                <span class="badge purple">📈 Ver Reportes</span>
            </div>
            <div class="card" onclick="location.href='index.jsp'">
                <div class="icon">🛒</div>
                <h3>Sistema de Ofertas</h3>
                <p>Calcula descuentos y obsequios</p>
                <span class="badge blue">🚀 Ir</span>
            </div>
        </div>
    </div>
</body>
</html>