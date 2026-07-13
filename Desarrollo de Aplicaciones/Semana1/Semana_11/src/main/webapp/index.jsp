<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.tienda.model.UsuarioBean" %>
<%
    UsuarioBean usuario = (UsuarioBean) session.getAttribute("usuario");
    if (usuario == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sistema de Ofertas - QhatuPERU</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: #e8f4f8;
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 20px;
        }
        .container {
            background: white;
            border-radius: 16px;
            box-shadow: 0 10px 40px rgba(0, 0, 0, 0.1);
            padding: 30px;
            max-width: 480px;
            width: 100%;
        }
        .header {
            text-align: center;
            margin-bottom: 25px;
        }
        .header h1 {
            color: #1a3a4a;
            font-size: 28px;
            font-weight: 700;
            margin-bottom: 5px;
        }
        .header .subtitle {
            color: #6b8a9a;
            font-size: 14px;
            font-weight: 500;
        }
        .header .icon { font-size: 40px; margin-bottom: 8px; }
        
        .discount-table {
            background: #f0f6fa;
            border-radius: 12px;
            padding: 15px 20px;
            margin: 20px 0 25px;
        }
        .discount-table h3 {
            color: #1a3a4a;
            font-size: 13px;
            font-weight: 700;
            margin-bottom: 12px;
            text-align: center;
            text-transform: uppercase;
            letter-spacing: 1px;
        }
        .discount-table table {
            width: 100%;
            border-collapse: collapse;
            text-align: center;
        }
        .discount-table td {
            padding: 8px 5px;
            border-bottom: 1px solid #dce8ed;
            color: #1a3a4a;
            font-size: 14px;
        }
        .discount-table tr:last-child td { border-bottom: none; }
        .discount-table .highlight { font-weight: 700; color: #2ecc71; font-size: 15px; }
        
        .form-group {
            margin-bottom: 20px;
        }
        .form-group label {
            display: block;
            color: #1a3a4a;
            font-weight: 600;
            margin-bottom: 8px;
            font-size: 14px;
        }
        .form-group input {
            width: 100%;
            padding: 12px 16px;
            border: 2px solid #dce8ed;
            border-radius: 10px;
            font-size: 16px;
            transition: all 0.3s ease;
            background: #f8fafc;
        }
        .form-group input:focus {
            outline: none;
            border-color: #2ecc71;
            background: white;
            box-shadow: 0 0 0 3px rgba(46, 204, 113, 0.15);
        }
        .form-group input::placeholder { color: #b0c8d5; font-size: 14px; }
        
        .input-group {
            display: flex;
            align-items: center;
        }
        .input-group span {
            background: #e8f0f5;
            padding: 12px 16px;
            border-radius: 10px 0 0 10px;
            border: 2px solid #dce8ed;
            border-right: none;
            font-weight: 700;
            color: #1a3a4a;
            font-size: 14px;
        }
        .input-group input {
            border-radius: 0 10px 10px 0;
            border-left: none;
        }
        .hint-text {
            font-size: 12px;
            color: #8aacbb;
            margin-top: 5px;
            font-weight: 400;
        }
        
        .btn-calculate {
            width: 100%;
            padding: 14px;
            background: #2ecc71;
            color: white;
            border: none;
            border-radius: 10px;
            font-size: 18px;
            font-weight: 700;
            cursor: pointer;
            transition: all 0.3s ease;
            margin-top: 10px;
        }
        .btn-calculate:hover {
            background: #27ae60;
            transform: translateY(-2px);
            box-shadow: 0 8px 25px rgba(46, 204, 113, 0.35);
        }
        
        .error-message {
            background: #fde8e8;
            color: #c0392b;
            padding: 12px;
            border-radius: 10px;
            margin-bottom: 18px;
            font-size: 14px;
            text-align: center;
            border-left: 4px solid #c0392b;
        }
        .footer {
            text-align: center;
            margin-top: 25px;
            color: #a0b8c5;
            font-size: 12px;
            font-weight: 500;
        }
        .footer span { color: #2ecc71; }
        
        .back-link {
            display: block;
            text-align: center;
            margin-top: 15px;
            color: #3498db;
            text-decoration: none;
            font-weight: 600;
        }
        .back-link:hover { text-decoration: underline; }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <div class="icon">🏪</div>
            <h1>Sistema de Ofertas</h1>
            <p class="subtitle">Tienda · Lapiceros</p>
        </div>

        <% if (request.getAttribute("error") != null) { %>
            <div class="error-message">⚠️ <%= request.getAttribute("error") %></div>
        <% } %>

        <div class="discount-table">
            <h3>📊 Tabla de Descuentos</h3>
            <table>
                <tr><td>&lt; 10 docenas</td><td><span class="highlight">10%</span></td></tr>
                <tr><td>≥ 10 docenas</td><td><span class="highlight">20%</span></td></tr>
            </table>
        </div>

        <form action="oferta" method="POST">
            <div class="form-group">
                <label for="precioDocena">💰 Precio por docena (S/)</label>
                <div class="input-group">
                    <span>S/</span>
                    <input type="number" id="precioDocena" name="precioDocena" step="0.01" min="0" required placeholder="Ej: 50.00">
                </div>
            </div>
            <div class="form-group">
                <label for="cantidadDocenas">📦 Cantidad de docenas</label>
                <input type="number" id="cantidadDocenas" name="cantidadDocenas" min="0" required placeholder="Ej: 6">
                <div class="hint-text">Ingresa la cantidad de docenas que deseas comprar</div>
            </div>
            <button type="submit" class="btn-calculate">Calcular</button>
        </form>

        <div class="footer">
            © 2026 · <span>QhatuPERU</span> · Sistema de Ofertas
        </div>
        <a href="dashboard.jsp" class="back-link">← Volver al Dashboard</a>
    </div>
</body>
</html>