<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Calculadora de Hipotenusa - Euclidiana</title>

    <style>
        /* ====== ESTILOS ====== */
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        body {
            background: #f4f7fc;
            display: flex;
            justify-content: center;
            padding: 2rem 1rem;
        }
        .container {
            max-width: 800px;
            width: 100%;
            background: white;
            border-radius: 20px;
            box-shadow: 0 8px 30px rgba(0,0,0,0.12);
            padding: 2rem 2rem 2.5rem;
        }
        header {
            text-align: center;
            margin-bottom: 2rem;
        }
        header h1 {
            font-size: 1.8rem;
            color: #1a3b5d;
            letter-spacing: 2px;
        }
        header h2 {
            font-size: 1.5rem;
            color: #2c3e50;
            font-weight: 400;
            margin-top: 0.2rem;
        }
        .teorema {
            font-size: 1.1rem;
            color: #e67e22;
            background: #fef9e7;
            display: inline-block;
            padding: 0.3rem 1.5rem;
            border-radius: 30px;
            margin-top: 0.5rem;
        }
        /* Nuevo bloque para el estudiante */
        .estudiante {
            margin-top: 0.8rem;
            font-size: 1.1rem;
            font-weight: 600;
            color: #2c3e50;
            background: #ecf0f1;
            display: inline-block;
            padding: 0.3rem 2rem;
            border-radius: 30px;
            letter-spacing: 1px;
        }
        .estudiante span {
            font-weight: 300;
            color: #1a3b5d;
        }
        .estudiante .upla {
            font-weight: 700;
            color: #c0392b;
        }
        .logos {
            display: flex;
            justify-content: center;
            gap: 20px;
            margin-bottom: 1rem;
        }
        .logos img {
            height: 50px;
            width: auto;
        }
        .formulario {
            background: #f8faff;
            padding: 1.5rem;
            border-radius: 16px;
            margin-bottom: 1.5rem;
        }
        .campo {
            display: flex;
            align-items: center;
            gap: 0.8rem;
            margin-bottom: 1rem;
            flex-wrap: wrap;
        }
        .campo label {
            width: 100px;
            font-weight: 600;
            color: #1a3b5d;
        }
        .campo input {
            flex: 1;
            min-width: 100px;
            padding: 0.6rem 1rem;
            border: 1px solid #ccd9e9;
            border-radius: 8px;
            font-size: 1rem;
            transition: 0.2s;
        }
        .campo input:focus {
            border-color: #3498db;
            outline: none;
            box-shadow: 0 0 0 3px rgba(52,152,219,0.2);
        }
        .unidad {
            font-weight: 600;
            color: #2c3e50;
            margin-left: -0.5rem;
        }
        .resultado {
            margin: 1rem 0;
            font-size: 1.1rem;
            min-height: 2.5rem;
        }
        .formula {
            background: #eaf4fc;
            padding: 0.5rem 1rem;
            border-radius: 8px;
            color: #1a3b5d;
        }
        .error {
            color: #e74c3c;
            background: #fde8e8;
            padding: 0.5rem 1rem;
            border-radius: 8px;
        }
        .acciones {
            display: flex;
            gap: 1rem;
            margin-top: 1rem;
            flex-wrap: wrap;
        }
        .btn {
            padding: 0.7rem 2rem;
            border: none;
            border-radius: 30px;
            font-size: 1rem;
            font-weight: 600;
            cursor: pointer;
            transition: 0.2s;
        }
        .btn.calcular {
            background: #2ecc71;
            color: white;
        }
        .btn.calcular:hover {
            background: #27ae60;
            transform: scale(1.02);
        }
        .btn.reset {
            background: #ecf0f1;
            color: #2c3e50;
        }
        .btn.reset:hover {
            background: #d5dbdb;
        }
        .visualizacion {
            text-align: center;
            margin: 2rem 0;
        }
        .visualizacion h3 {
            margin-bottom: 0.8rem;
            color: #1a3b5d;
        }
        #trianguloCanvas {
            border: 1px solid #ccd9e9;
            border-radius: 12px;
            background: #ffffff;
            max-width: 100%;
            height: auto;
        }
        .tripletas {
            margin-top: 2rem;
            padding-top: 1.5rem;
            border-top: 2px dashed #d5dbdb;
        }
        .tripletas h3 {
            color: #1a3b5d;
            text-align: center;
            margin-bottom: 1.2rem;
        }
        .tripleta {
            display: flex;
            justify-content: space-between;
            align-items: center;
            background: #f0f5fb;
            padding: 0.6rem 1.2rem;
            border-radius: 30px;
            margin-bottom: 0.6rem;
            max-width: 300px;
            margin-left: auto;
            margin-right: auto;
        }
        .numeros {
            font-weight: 700;
            color: #1a3b5d;
        }
        .descripcion {
            font-size: 0.9rem;
            color: #7f8c8d;
            font-style: italic;
        }
        .angulo {
            text-align: center;
            margin-top: 1.5rem;
            color: #2c3e50;
            font-size: 0.95rem;
            background: #ecf9f9;
            padding: 0.5rem;
            border-radius: 30px;
        }
        @media (max-width: 600px) {
            .container { padding: 1rem; }
            .campo { flex-direction: column; align-items: stretch; }
            .campo label { width: auto; }
            .acciones { flex-direction: column; }
            .btn { width: 100%; }
        }
    </style>
</head>
<body>

<div class="container">
    <!-- Logos (opcional) -->
    <div class="logos">
        <img src="${pageContext.request.contextPath}/imagenes/logo-upla.png" alt="UPLA" style="height:60px;">
        <img src="${pageContext.request.contextPath}/imagenes/logo-facultad.png" alt="Facultad" style="height:60px;">
    </div>

    <header>
        <h1>GEOMETRÍA EUCLIDIANA</h1>
        <h2>Calculadora de Hipotenusa</h2>
        <p class="teorema">c = √(a² + b²) — Teorema de Pitágoras</p>

        <!-- NOMBRE DEL ESTUDIANTE Y UPLA -->
        <div class="estudiante">
            ESTUDIANTE <span>AGUIRRE OSORES PIERO</span> &nbsp;·&nbsp; <span class="upla">UPLA</span>
        </div>
    </header>

    <section class="formulario">
        <form action="${pageContext.request.contextPath}/calcular" method="post">
            <div class="campo">
                <label for="catetoA">CATETO A</label>
                <input type="text" id="catetoA" name="catetoA" placeholder="ej. 3" 
                       value="${param.catetoA}">
                <span class="unidad">a</span>
            </div>
            <div class="campo">
                <label for="catetoB">CATETO B</label>
                <input type="text" id="catetoB" name="catetoB" placeholder="ej. 4" 
                       value="${param.catetoB}">
                <span class="unidad">b</span>
            </div>

            <div class="resultado">
                <%
                    com.ejemplo.model.Triangulo triangulo = 
                        (com.ejemplo.model.Triangulo) request.getAttribute("triangulo");
                    String error = (String) request.getAttribute("error");

                    if (triangulo != null) {
                %>
                    <p class="formula">
                        c = √(<%= triangulo.getCatetoA() %>² + <%= triangulo.getCatetoB() %>²) = 
                        <strong><%= String.format("%.4f", triangulo.getHipotenusa()) %></strong>
                    </p>
                <%
                    } else if (error != null) {
                %>
                    <p class="error"><%= error %></p>
                <%
                    }
                %>
            </div>

            <div class="acciones">
                <button type="submit" class="btn calcular">Calcular</button>
                <button type="reset" class="btn reset" onclick="limpiarCanvas()">Reset</button>
            </div>
        </form>
    </section>

    <section class="visualizacion">
        <h3>Visualización a escala</h3>
        <canvas id="trianguloCanvas" width="400" height="300"></canvas>
    </section>

    <section class="tripletas">
        <h3>TRIPLETAS PITAGÓRICAS</h3>
        <div class="tripleta">
            <span class="numeros">3 - 4 - 5</span>
            <span class="descripcion">tripleta clásica</span>
        </div>
        <div class="tripleta">
            <span class="numeros">5 - 12 - 13</span>
            <span class="descripcion">tripleta clásica</span>
        </div>
        <div class="tripleta">
            <span class="numeros">8 - 15 - 17</span>
            <span class="descripcion">tripleta clásica</span>
        </div>
        <p class="angulo">Ángulo recto: 90° &nbsp;|&nbsp; Fórmula: c = √(a² + b²) &nbsp;|&nbsp; Teorema: a² + b² = c²</p>
    </section>
</div>

<!-- ===== JAVASCRIPT ===== -->
<script>
    window.catetoA = ${not empty triangulo ? triangulo.catetoA : 0};
    window.catetoB = ${not empty triangulo ? triangulo.catetoB : 0};
    window.hipotenusa = ${not empty triangulo ? triangulo.hipotenusa : 0};

    window.addEventListener('load', function() {
        dibujarTriangulo(window.catetoA, window.catetoB, window.hipotenusa);
    });

    function dibujarTriangulo(a, b, c) {
        var canvas = document.getElementById('trianguloCanvas');
        var ctx = canvas.getContext('2d');
        ctx.clearRect(0, 0, canvas.width, canvas.height);

        if (a <= 0 || b <= 0 || c <= 0) {
            ctx.font = "16px 'Segoe UI', sans-serif";
            ctx.fillStyle = "#7f8c8d";
            ctx.textAlign = "center";
            ctx.fillText("Ingrese valores y presione Calcular", canvas.width/2, canvas.height/2 - 10);
            ctx.font = "14px 'Segoe UI', sans-serif";
            ctx.fillText("para visualizar el triángulo", canvas.width/2, canvas.height/2 + 20);
            return;
        }

        var margen = 50;
        var anchoUtil = canvas.width - 2 * margen;
        var altoUtil = canvas.height - 2 * margen;
        var maxLado = Math.max(a, b, c);
        var escala = Math.min(anchoUtil / maxLado, altoUtil / maxLado) * 0.8;

        var origenX = margen;
        var origenY = canvas.height - margen;

        var xA = origenX + a * escala;
        var yA = origenY;
        var xB = origenX;
        var yB = origenY - b * escala;
        var xC = origenX;
        var yC = origenY;

        ctx.beginPath();
        ctx.moveTo(xC, yC);
        ctx.lineTo(xA, yA);
        ctx.lineTo(xB, yB);
        ctx.closePath();
        ctx.fillStyle = 'rgba(52, 152, 219, 0.15)';
        ctx.fill();
        ctx.strokeStyle = '#2c3e50';
        ctx.lineWidth = 2.5;
        ctx.stroke();

        var lado = 12;
        ctx.strokeStyle = '#e67e22';
        ctx.lineWidth = 2;
        ctx.strokeRect(xC, yC - lado, lado, lado);

        ctx.font = 'bold 16px "Segoe UI", sans-serif';
        ctx.fillStyle = '#1a3b5d';
        ctx.textAlign = 'center';
        ctx.textBaseline = 'bottom';
        var midX = (xC + xA) / 2;
        var midY = yC + 20;
        ctx.fillText('a = ' + a.toFixed(2), midX, midY);

        var midXb = xC - 25;
        var midYb = (yC + yB) / 2;
        ctx.textAlign = 'right';
        ctx.textBaseline = 'middle';
        ctx.fillText('b = ' + b.toFixed(2), midXb, midYb);

        var midXh = (xA + xB) / 2;
        var midYh = (yA + yB) / 2 - 15;
        ctx.textAlign = 'left';
        ctx.textBaseline = 'bottom';
        ctx.fillStyle = '#c0392b';
        ctx.fillText('c = ' + c.toFixed(2), midXh, midYh);

        ctx.fillStyle = '#2c3e50';
        ctx.beginPath();
        ctx.arc(xC, yC, 4, 0, 2 * Math.PI);
        ctx.fill();
        ctx.beginPath();
        ctx.arc(xA, yA, 4, 0, 2 * Math.PI);
        ctx.fill();
        ctx.beginPath();
        ctx.arc(xB, yB, 4, 0, 2 * Math.PI);
        ctx.fill();

        ctx.font = '12px "Segoe UI", sans-serif';
        ctx.fillStyle = '#95a5a6';
        ctx.textAlign = 'right';
        ctx.textBaseline = 'bottom';
        ctx.fillText('Escala ajustada', canvas.width - 10, canvas.height - 10);
    }

    function limpiarCanvas() {
        var canvas = document.getElementById('trianguloCanvas');
        var ctx = canvas.getContext('2d');
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        ctx.font = "16px 'Segoe UI', sans-serif";
        ctx.fillStyle = "#7f8c8d";
        ctx.textAlign = "center";
        ctx.fillText("Ingrese valores y presione Calcular", canvas.width/2, canvas.height/2 - 10);
        ctx.font = "14px 'Segoe UI', sans-serif";
        ctx.fillText("para visualizar el triángulo", canvas.width/2, canvas.height/2 + 20);
        window.catetoA = 0;
        window.catetoB = 0;
        window.hipotenusa = 0;
    }
</script>

</body>
</html>