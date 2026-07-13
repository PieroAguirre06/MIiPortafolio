<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.tienda.model.UsuarioBean" %>
<%@ page import="com.tienda.model.ProductoBean" %>
<%@ page import="com.tienda.model.VentaBean" %>
<%@ page import="com.tienda.dao.ProductoDAO" %>
<%@ page import="com.tienda.dao.VentaDAO" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Vendedor - QhatuPERU</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: #e8f4f8;
            min-height: 100vh;
        }
        .navbar {
            background: #2c3e50;
            padding: 15px 30px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            color: white;
            flex-wrap: wrap;
        }
        .navbar .logo { font-size: 24px; font-weight: 700; }
        .navbar .logo span { color: #3498db; }
        .navbar .user-info { display: flex; align-items: center; gap: 15px; flex-wrap: wrap; }
        .navbar .role-badge {
            background: #3498db;
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
        }
        .card:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 30px rgba(0,0,0,0.15);
        }
        .card .icon { font-size: 40px; margin-bottom: 10px; }
        .card h3 { color: #1a3a4a; font-size: 18px; margin-bottom: 10px; }
        .card p { color: #6b8a9a; font-size: 14px; margin-bottom: 15px; }
        
        .btn-primary {
            background: #3498db;
            color: white;
            padding: 10px 25px;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            font-weight: 600;
            transition: all 0.3s ease;
            text-decoration: none;
            display: inline-block;
        }
        .btn-primary:hover { background: #2980b9; transform: translateY(-2px); }
        .btn-success {
            background: #2ecc71;
            color: white;
            padding: 10px 25px;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            font-weight: 600;
            transition: all 0.3s ease;
        }
        .btn-success:hover { background: #27ae60; transform: translateY(-2px); }
        .btn-purple {
            background: #9b59b6;
            color: white;
            padding: 10px 25px;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            font-weight: 600;
            transition: all 0.3s ease;
        }
        .btn-purple:hover { background: #8e44ad; transform: translateY(-2px); }
        
        .mensaje {
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 20px;
            border-left: 4px solid #28a745;
        }
        .mensaje.error { border-left-color: #dc3545; background: #f8d7da; color: #721c24; }
        .mensaje.success { border-left-color: #28a745; background: #d4edda; color: #155724; }
        
        .stock-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 15px;
        }
        .stock-table th, .stock-table td {
            padding: 10px;
            text-align: left;
            border-bottom: 1px solid #e8f0f5;
        }
        .stock-table th {
            background: #f0f6fa;
            color: #1a3a4a;
            font-weight: 600;
        }
        .stock-bajo { color: #e74c3c; font-weight: 700; }
        .stock-normal { color: #2ecc71; font-weight: 700; }
        
        .modal {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0,0,0,0.5);
            z-index: 1000;
            justify-content: center;
            align-items: center;
        }
        .modal.active { display: flex; }
        .modal-content {
            background: white;
            border-radius: 16px;
            padding: 30px;
            max-width: 500px;
            width: 100%;
            max-height: 90vh;
            overflow-y: auto;
        }
        .modal-content h2 { color: #1a3a4a; margin-bottom: 20px; }
        .modal-content .form-group { margin-bottom: 15px; }
        .modal-content .form-group label {
            display: block;
            color: #1a3a4a;
            font-weight: 600;
            margin-bottom: 5px;
        }
        .modal-content .form-group input,
        .modal-content .form-group select {
            width: 100%;
            padding: 10px;
            border: 2px solid #dce8ed;
            border-radius: 8px;
            font-size: 14px;
        }
        .modal-actions {
            display: flex;
            gap: 10px;
            margin-top: 20px;
        }
        .modal-actions button {
            padding: 10px 25px;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            font-weight: 600;
        }
        .btn-cancel {
            background: #e8f0f5;
            color: #1a3a4a;
        }
        .btn-cancel:hover { background: #d5e1e8; }
        
        .venta-item {
            background: #f8fafc;
            border-radius: 8px;
            padding: 15px;
            margin-bottom: 10px;
            border-left: 4px solid #3498db;
        }
        .venta-item .fecha { color: #6b8a9a; font-size: 12px; }
        .venta-item .total { font-weight: 700; color: #1a3a4a; }
        .venta-item .guia { font-weight: 700; color: #3498db; }
        
        .seccion-ventas { margin-top: 30px; display: none; }
        .seccion-ventas.active { display: block; }
        .seccion-productos { margin-top: 30px; display: none; }
        .seccion-productos.active { display: block; }
    </style>
</head>
<body>
    <%
        UsuarioBean usuario = (UsuarioBean) session.getAttribute("usuario");
        if (usuario == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        ProductoDAO productoDAO = new ProductoDAO();
        VentaDAO ventaDAO = new VentaDAO();
        List<ProductoBean> productos = productoDAO.listarProductos();
        List<VentaBean> ventas = ventaDAO.listarVentas();
    %>

    <div class="navbar">
        <div class="logo">🏪 <span>Qhatu</span>PERU</div>
        <div class="user-info">
            <span class="role-badge">🛒 VENDEDOR</span>
            <span>👤 <%= usuario.getNombreCompleto() %></span>
            <a href="logout" class="btn-logout">🚪 Cerrar Sesión</a>
        </div>
    </div>

    <div class="container">
        <h2 class="dashboard-title">🛒 Panel de Vendedor</h2>
        
        <% if (session.getAttribute("mensaje") != null) { %>
            <div class="mensaje <%= session.getAttribute("tipoMensaje") %>">
                <%= session.getAttribute("mensaje") %>
            </div>
            <% session.removeAttribute("mensaje"); session.removeAttribute("tipoMensaje"); %>
        <% } %>
        
        <div class="grid">
            <div class="card">
                <div class="icon">🛍️</div>
                <h3>Registrar Venta</h3>
                <p>Crea una nueva venta para tus clientes</p>
                <button class="btn-success" onclick="abrirModalVenta()">📝 Nueva Venta</button>
            </div>
            <div class="card">
                <div class="icon">🔍</div>
                <h3>Buscar Productos</h3>
                <p>Consulta el stock y precios de productos</p>
                <button class="btn-primary" onclick="mostrarProductos()">📋 Ver Productos</button>
            </div>
            <div class="card">
                <div class="icon">📊</div>
                <h3>Mis Ventas</h3>
                <p>Historial de ventas realizadas</p>
                <button class="btn-purple" onclick="mostrarMisVentas()">📈 Ver Ventas</button>
            </div>
            <div class="card">
                <div class="icon">🏷️</div>
                <h3>Sistema de Ofertas</h3>
                <p>Calcula descuentos y obsequios</p>
                <a href="index.jsp" class="btn-primary">🚀 Ir a Ofertas</a>
            </div>
        </div>
        
        <!-- Productos -->
        <div id="productosSection" class="seccion-productos">
            <div class="card">
                <h3>📋 Lista de Productos</h3>
                <table class="stock-table">
                    <thead><tr><th>Código</th><th>Producto</th><th>Presentación</th><th>Stock</th><th>Estado</th></tr></thead>
                    <tbody>
                        <% for (ProductoBean p : productos) { %>
                            <tr>
                                <td><%= p.getCodArticulo() %></td>
                                <td><strong><%= p.getDescripcion() %></strong></td>
                                <td><%= p.getPresentacion() %></td>
                                <td><%= p.getStockActual() %></td>
                                <td class="<%= p.isStockBajo() ? "stock-bajo" : "stock-normal" %>">
                                    <%= p.isStockBajo() ? "⚠️ Stock Bajo" : "✅ Stock Normal" %>
                                </td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
                <button class="btn-cancel" onclick="ocultarProductos()">Cerrar</button>
            </div>
        </div>
        
        <!-- Mis Ventas -->
        <div id="ventasSection" class="seccion-ventas">
            <div class="card">
                <h3>📊 Mis Ventas</h3>
                <% if (ventas.isEmpty()) { %>
                    <p style="color: #6b8a9a; text-align:center; padding:20px;">📋 No hay ventas registradas aún.</p>
                <% } else { %>
                    <% for (VentaBean v : ventas) { %>
                        <div class="venta-item">
                            <div class="guia">📄 Guía N° <%= v.getNumGuia() %></div>
                            <div><strong><%= v.getArticulo() %></strong> - <%= v.getCantidad() %> und</div>
                            <div>💰 S/ <%= String.format("%.2f", v.getPrecioVenta()) %> c/u</div>
                            <div>🏪 <%= v.getNombreTienda() %></div>
                            <div class="fecha">📅 <%= v.getFechaSalida() %></div>
                            <div class="total">Total: S/ <%= String.format("%.2f", v.getTotal()) %></div>
                        </div>
                    <% } %>
                <% } %>
                <button class="btn-cancel" onclick="ocultarVentas()">Cerrar</button>
            </div>
        </div>
    </div>

    <!-- MODAL: Registrar Venta -->
    <div id="modalVenta" class="modal">
        <div class="modal-content">
            <h2>🛍️ Registrar Nueva Venta</h2>
            <form action="venta" method="POST">
                <div class="form-group">
                    <label>📦 Producto</label>
                    <select name="codArticulo" required>
                        <option value="">Seleccione...</option>
                        <% for (ProductoBean p : productos) { %>
                            <option value="<%= p.getCodArticulo() %>"><%= p.getDescripcion() %> - Stock: <%= p.getStockActual() %></option>
                        <% } %>
                    </select>
                </div>
                <div class="form-group">
                    <label>🔢 Cantidad</label>
                    <input type="number" name="cantidad" min="1" required placeholder="Ingrese cantidad">
                </div>
                <div class="form-group">
                    <label>💰 Precio Venta</label>
                    <input type="number" name="precioVenta" step="0.01" required placeholder="0.00">
                </div>
                <div class="form-group">
                    <label>🏪 Tienda</label>
                    <select name="codTienda" required>
                        <option value="1">Tienda Principal - El Tambo</option>
                        <option value="2">Tienda Huancayo</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>🚚 Transportista</label>
                    <select name="codTransportista" required>
                        <option value="1">Transportes Salazar</option>
                        <option value="2">Logística Centro</option>
                    </select>
                </div>
                <p style="color:#6b8a9a; font-size:13px;">ℹ️ El número de guía se generará automáticamente.</p>
                <div class="modal-actions">
                    <button type="submit" class="btn-success">✅ Registrar</button>
                    <button type="button" class="btn-cancel" onclick="cerrarModalVenta()">❌ Cancelar</button>
                </div>
            </form>
        </div>
    </div>

    <script>
        function abrirModalVenta() { document.getElementById('modalVenta').classList.add('active'); }
        function cerrarModalVenta() { document.getElementById('modalVenta').classList.remove('active'); }
        function mostrarProductos() { document.getElementById('productosSection').classList.add('active'); }
        function ocultarProductos() { document.getElementById('productosSection').classList.remove('active'); }
        function mostrarMisVentas() { document.getElementById('ventasSection').classList.add('active'); }
        function ocultarVentas() { document.getElementById('ventasSection').classList.remove('active'); }
        window.onclick = function(e) { let m = document.getElementById('modalVenta'); if (e.target == m) m.classList.remove('active'); }
    </script>
</body>
</html>