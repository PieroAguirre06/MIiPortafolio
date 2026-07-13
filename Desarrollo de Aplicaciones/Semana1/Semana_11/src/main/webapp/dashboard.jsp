<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.tienda.model.UsuarioBean" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - QhatuPERU</title>
    <style>
        /* ========== ENCABEZADO COMÚN ========== */
.header-container {
    display: flex;
    align-items: center;
    justify-content: space-between;
    background: linear-gradient(135deg, #1a3a4a 0%, #2c5a6e 100%);
    padding: 15px 30px;
    border-radius: 16px;
    margin-bottom: 25px;
    box-shadow: 0 4px 15px rgba(0,0,0,0.15);
}

.header-left, .header-right {
    display: flex;
    align-items: center;
}

.header-logo {
    height: 60px;
    width: auto;
    max-width: 80px;
    object-fit: contain;
}

.header-center {
    text-align: center;
    flex: 1;
}

.header-title {
    font-size: 28px;
    font-weight: 700;
    color: #2ecc71;
    letter-spacing: 2px;
    text-shadow: 0 2px 4px rgba(0,0,0,0.2);
}

.header-subtitle {
    font-size: 14px;
    color: rgba(255,255,255,0.7);
    margin-top: 2px;
}

.header-student {
    margin-top: 6px;
    display: flex;
    justify-content: center;
    gap: 10px;
    align-items: center;
}

.student-label {
    background: rgba(255,255,255,0.15);
    padding: 3px 12px;
    border-radius: 12px;
    font-size: 12px;
    color: rgba(255,255,255,0.6);
    font-weight: 600;
    letter-spacing: 1px;
}

.student-name {
    font-size: 16px;
    font-weight: 700;
    color: #ffffff;
    letter-spacing: 1px;
    background: rgba(46, 204, 113, 0.2);
    padding: 3px 15px;
    border-radius: 12px;
    border: 1px solid rgba(46, 204, 113, 0.3);
}
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: #e8f4f8;
            min-height: 100vh;
            padding: 20px;
        }
        .container {
            max-width: 1200px;
            margin: 0 auto;
        }
        .navbar {
            background: #1a3a4a;
            padding: 20px 30px;
            border-radius: 16px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            color: white;
            flex-wrap: wrap;
            margin-bottom: 30px;
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
        .grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
            gap: 20px;
            margin-top: 20px;
        }
        .card {
            background: white;
            border-radius: 16px;
            padding: 25px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.08);
        }
        .card h3 { color: #1a3a4a; margin-bottom: 10px; }
        .card p { color: #6b8a9a; margin-bottom: 15px; }
        .btn-primary {
            background: #2ecc71;
            color: white;
            padding: 10px 25px;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            font-weight: 600;
        }
        .btn-primary:hover { background: #27ae60; }
        .info-item {
            display: flex;
            justify-content: space-between;
            padding: 8px 0;
            border-bottom: 1px solid #e8f0f5;
        }
        .info-item .label { color: #6b8a9a; }
        .info-item .value { font-weight: 600; color: #1a3a4a; }
        .info-item .value.highlight { color: #2ecc71; }
    </style>
</head>
<body>
    <%
        UsuarioBean usuario = (UsuarioBean) session.getAttribute("usuario");
        if (usuario == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        // Redirigir según el rol automáticamente
        String rol = usuario.getNombreRol();
        if (rol != null) {
            if (rol.contains("Administrador")) {
                response.sendRedirect("dashboard_admin.jsp");
                return;
            } else if (rol.contains("Vendedor")) {
                response.sendRedirect("dashboard_vendedor.jsp");
                return;
            } else if (rol.contains("Almacenero")) {
                response.sendRedirect("dashboard_almacenero.jsp");
                return;
            } else if (rol.contains("Cajero")) {
                response.sendRedirect("dashboard_cajero.jsp");
                return;
            }
        }
    %>
    
    <div class="container">
        <div class="navbar">
            <div class="logo">🏪 <span>Qhatu</span>PERU</div>
            <div class="user-info">
                <span class="role-badge"><%= usuario.getNombreRol() %></span>
                <span>👤 <%= usuario.getNombreCompleto() %></span>
                <a href="logout" class="btn-logout">Cerrar Sesión</a>
            </div>
        </div>
        
        <div class="grid">
            <div class="card">
                <h3>🛒 Sistema de Ofertas</h3>
                <p>Calcula descuentos y obsequios</p>
                <a href="index.jsp" class="btn-primary">Ir a Ofertas</a>
            </div>
            <div class="card">
                <h3>👤 Mi Perfil</h3>
                <div class="info-item">
                    <span class="label">Usuario</span>
                    <span class="value"><%= usuario.getUsername() %></span>
                </div>
                <div class="info-item">
                    <span class="label">Nombre</span>
                    <span class="value"><%= usuario.getNombreCompleto() %></span>
                </div>
                <div class="info-item">
                    <span class="label">Correo</span>
                    <span class="value"><%= usuario.getCorreo() != null ? usuario.getCorreo() : "No registrado" %></span>
                </div>
                <div class="info-item">
                    <span class="label">Rol</span>
                    <span class="value highlight"><%= usuario.getNombreRol() %></span>
                </div>
                <div class="info-item">
                    <span class="label">Estado</span>
                    <span class="value highlight"><%= usuario.getEstado() == 1 ? "✅ Activo" : "❌ Inactivo" %></span>
                </div>
            </div>
        </div>
    </div>
</body>
</html>