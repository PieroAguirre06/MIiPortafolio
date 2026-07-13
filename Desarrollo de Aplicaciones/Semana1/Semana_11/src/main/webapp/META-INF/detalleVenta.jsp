<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.tienda.model.VentaBean" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Detalle de Venta</title>
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
            padding: 40px;
            max-width: 600px;
            width: 100%;
            box-shadow: 0 4px 15px rgba(0,0,0,0.08);
        }
        h1 { color: #1a3a4a; margin-bottom: 20px; }
        .detalle-item {
            display: flex;
            justify-content: space-between;
            padding: 12px 0;
            border-bottom: 1px solid #e8f0f5;
        }
        .detalle-item .label { color: #6b8a9a; }
        .detalle-item .value { font-weight: 600; color: #1a3a4a; }
        .btn-back {
            display: inline-block;
            margin-top: 20px;
            padding: 10px 25px;
            background: #6b8a9a;
            color: white;
            text-decoration: none;
            border-radius: 8px;
        }
        .btn-back:hover { background: #5a7a8a; }
        .total { font-size: 24px; color: #2ecc71; }
    </style>
</head>
<body>
    <div class="container">
        <h1>📄 Detalle de Venta</h1>
        
        <%
            VentaBean venta = (VentaBean) request.getAttribute("venta");
            if (venta != null) {
        %>
            <div class="detalle-item">
                <span class="label">📄 N° Guía</span>
                <span class="value"><%= venta.getNumGuia() %></span>
            </div>
            <div class="detalle-item">
                <span class="label">📦 Producto</span>
                <span class="value"><%= venta.getArticulo() %></span>
            </div>
            <div class="detalle-item">
                <span class="label">🔢 Cantidad</span>
                <span class="value"><%= venta.getCantidad() %></span>
            </div>
            <div class="detalle-item">
                <span class="label">💰 Precio Unitario</span>
                <span class="value">S/ <%= String.format("%.2f", venta.getPrecioVenta()) %></span>
            </div>
            <div class="detalle-item">
                <span class="label">💵 Total</span>
                <span class="value total">S/ <%= String.format("%.2f", venta.getTotal()) %></span>
            </div>
            <div class="detalle-item">
                <span class="label">🏪 Tienda</span>
                <span class="value"><%= venta.getNombreTienda() %></span>
            </div>
            <div class="detalle-item">
                <span class="label">🚚 Transportista</span>
                <span class="value"><%= venta.getTransportista() %></span>
            </div>
            <div class="detalle-item">
                <span class="label">📅 Fecha</span>
                <span class="value"><%= venta.getFechaSalida() %></span>
            </div>
        <% } else { %>
            <p style="color: #6b8a9a;">No se encontró la venta.</p>
        <% } %>
        
        <a href="dashboard.jsp" class="btn-back">← Volver al Dashboard</a>
    </div>
</body>
</html>