<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - QhatuPERU</title>
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
            flex-direction: column;
        }
        .header-container {
            display: flex;
            align-items: center;
            justify-content: space-between;
            background: linear-gradient(135deg, #1a3a4a 0%, #2c5a6e 100%);
            padding: 12px 30px;
            border-radius: 16px;
            margin-bottom: 30px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.15);
            width: 100%;
            max-width: 500px;
        }
        .header-logo { height: 50px; width: auto; max-width: 70px; object-fit: contain; }
        .header-center { text-align: center; flex: 1; }
        .header-title { font-size: 24px; font-weight: 700; color: #2ecc71; letter-spacing: 2px; }
        .header-subtitle { font-size: 12px; color: rgba(255,255,255,0.7); }
        .header-student { margin-top: 4px; display: flex; justify-content: center; gap: 10px; align-items: center; }
        .student-label { background: rgba(255,255,255,0.15); padding: 3px 12px; border-radius: 12px; font-size: 11px; color: rgba(255,255,255,0.6); font-weight: 600; letter-spacing: 1px; }
        .student-name { font-size: 15px; font-weight: 700; color: #ffffff; background: rgba(46, 204, 113, 0.2); padding: 3px 15px; border-radius: 12px; border: 1px solid rgba(46, 204, 113, 0.3); }
        .login-container {
            background: white;
            border-radius: 20px;
            box-shadow: 0 20px 60px rgba(0, 0, 0, 0.15);
            padding: 40px;
            max-width: 420px;
            width: 100%;
        }
        .error-message {
            background: #fde8e8;
            color: #c0392b;
            padding: 12px 16px;
            border-radius: 10px;
            margin-bottom: 20px;
            font-size: 14px;
            text-align: center;
            border-left: 4px solid #c0392b;
        }
        .form-group { margin-bottom: 22px; }
        .form-group label { display: block; color: #1a3a4a; font-weight: 600; margin-bottom: 8px; font-size: 14px; }
        .form-group input {
            width: 100%;
            padding: 13px 16px;
            border: 2px solid #dce8ed;
            border-radius: 10px;
            font-size: 16px;
            transition: all 0.3s ease;
            background: #f8fafc;
        }
        .form-group input:focus { outline: none; border-color: #2ecc71; background: white; box-shadow: 0 0 0 3px rgba(46, 204, 113, 0.15); }
        .btn-login {
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
        .btn-login:hover { background: #27ae60; transform: translateY(-2px); box-shadow: 0 8px 25px rgba(46, 204, 113, 0.35); }
        .input-icon { position: relative; }
        .input-icon input { padding-left: 45px; }
        .input-icon .icon-input { position: absolute; left: 15px; top: 50%; transform: translateY(-50%); font-size: 18px; color: #a0b8c5; }
        .footer { text-align: center; margin-top: 25px; color: #a0b8c5; font-size: 12px; }
        .footer span { color: #2ecc71; }
    </style>
</head>
<body>
    <div class="header-container">
        <div class="header-left"><img src="${pageContext.request.contextPath}/imagenes/logo-upla.png" alt="UPLA" class="header-logo"></div>
        <div class="header-center">
            <div class="header-title">QhatuPERU</div>
            <div class="header-subtitle">Sistema de Planilla</div>
            <div class="header-student">
                <span class="student-label">ESTUDIANTE</span>
                <span class="student-name">AGUIRRE OSORES PIERO</span>
            </div>
        </div>
        <div class="header-right"><img src="${pageContext.request.contextPath}/imagenes/logo-facultad.png" alt="Facultad" class="header-logo"></div>
    </div>

    <div class="login-container">
        <% if (request.getAttribute("error") != null) { %>
            <div class="error-message">⚠️ <%= request.getAttribute("error") %></div>
        <% } %>
        <form action="login" method="POST">
            <div class="form-group">
                <label for="username">👤 Usuario</label>
                <div class="input-icon">
                    <span class="icon-input">👤</span>
                    <input type="text" id="username" name="username" placeholder="Ingresa tu usuario" required autofocus>
                </div>
            </div>
            <div class="form-group">
                <label for="clave">🔑 Contraseña</label>
                <div class="input-icon">
                    <span class="icon-input">🔒</span>
                    <input type="password" id="clave" name="clave" placeholder="Ingresa tu contraseña" required>
                </div>
            </div>
            <button type="submit" class="btn-login">🚀 Iniciar Sesión</button>
        </form>
        <div class="footer">© 2026 · <span>QhatuPERU</span> · Sistema de Planilla</div>
    </div>
</body>
</html>