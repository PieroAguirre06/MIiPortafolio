<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.tienda.model.OfertaBean" %>
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
    <title>Resultados - QhatuPERU</title>
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
        }
        .header .subtitle {
            color: #6b8a9a;
            font-size: 14px;
            font-weight: 500;
        }
        .header .result-icon { font-size: 40px; margin-bottom: 8px; }
        
        .result-card {
            background: #f8fafc;
            border-radius: 12px;
            padding: 20px;
            margin-bottom: 15px;
            border: 1px solid #e8f0f5;
        }
        .result-item {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 12px 0;
            border-bottom: 1px solid #e8f0f5;
        }
        .result-item:last-child { border-bottom: none; }
        .result-label { color: #6b8a9a; font-size: 14px; font-weight: 500; }
        .result-value { font-size: 18px; font-weight: 700; color: #1a3a4a; }
        .result-value.highlight { color: #2ecc71; }
        .result-value.green { color: #2ecc71; }
        .result-value.blue { color: #3498db; }
        
        .total-card {
            background: linear-gradient(135deg, #1a3a4a 0%, #2c5a6e 100%);
            border-radius: 12px;
            padding: 20px;
            color: white;
            margin-top: 10px;
        }
        .total-card .result-item { border-bottom-color: rgba(255,255,255,0.15); }
        .total-card .result-label { color: rgba(255,255,255,0.75); }
        .total-card .result-value { color: white; }
        
        .btn-back {
            display: block;
            width: 100%;
            padding: 14px;
            background: #e8f0f5;
            color: #1a3a4a;
            text-align: center;
            text-decoration: none;
            border-radius: 10px;
            font-weight: 700;
            margin-top: 20px;
            transition: all 0.3s ease;
            border: none;
            cursor: pointer;
            font-size: 16px;
        }
        .btn-back:hover { background: #d5e1e8; transform: translateY(-2px); }
        .badge {
            display: inline-block;
            background: #2ecc71;
            color: white;
            padding: 3px 14px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: 700;
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
            <div class="result-icon">📊</div>
            <h1>Resultados</h1>
            <p class="subtitle">Resumen de tu compra</p>
        </div>

        <%
            OfertaBean oferta = (OfertaBean) session.getAttribute("oferta");
            if (oferta != null) {
        %>
            <div class="result-card">
                <div class="result-item">
                    <span class="result-label">💰 Importe de compra</span>
                    <span class="result-value">S/ <%= String.format("%.2f", oferta.getImporteCompra()) %></span>
                </div>
                <div class="result-item">
                    <span class="result-label">📉 Descuento aplicado</span>
                    <span class="result-value green">S/ <%= String.format("%.2f", oferta.getDescuento()) %></span>
                </div>
                <div class="result-item">
                    <span class="result-label">💳 Importe a pagar</span>
                    <span class="result-value highlight">S/ <%= String.format("%.2f", oferta.getImportePagar()) %></span>
                </div>
            </div>
            <div class="result-card">
                <div class="result-item">
                    <span class="result-label">🖊️ Lapiceros de obsequio</span>
                    <span class="result-value blue"><%= oferta.getLapicerosObsequio() %> unidades</span>
                </div>
                <div class="result-item">
                    <span class="result-label">💰 Ahorro total</span>
                    <span class="result-value green">S/ <%= String.format("%.2f", oferta.getAhorroTotal()) %></span>
                </div>
                <div class="result-item">
                    <span class="result-label">🎁 Obsequio incluido</span>
                    <span class="result-value"><span class="badge"><%= oferta.getLapicerosObsequio() %> lapiceros</span></span>
                </div>
            </div>
            <div class="total-card">
                <div class="result-item">
                    <span class="result-label">📦 Docenas adquiridas</span>
                    <span class="result-value"><%= oferta.getCantidadDocenas() %></span>
                </div>
                <div class="result-item">
                    <span class="result-label">💰 Precio por docena</span>
                    <span class="result-value">S/ <%= String.format("%.2f", oferta.getPrecioDocena()) %></span>
                </div>
            </div>
        <% } else { %>
            <div class="result-card">
                <p style="text-align:center; color:#6b8a9a; padding:10px 0;">No hay datos disponibles. Realice una consulta primero.</p>
            </div>
        <% } %>

        <a href="index.jsp" class="btn-back">← Nueva consulta</a>
        <div class="footer">
            © 2026 · <span>QhatuPERU</span> · Sistema de Ofertas
        </div>
        <a href="dashboard.jsp" class="back-link">← Volver al Dashboard</a>
    </div>
</body>
</html>