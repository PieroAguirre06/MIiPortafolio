-- Crear la base de datos 
CREATE DATABASE QhatuPERU
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- Seleccionar la base de datos para comenzar a usarla

USE QhatuPERU;


-- ========================================================
-- 1. Tabla ROL
-- Almacena los perfiles o funciones dentro de la empresa
-- ========================================================
CREATE TABLE ROL (
    CodRol INT AUTO_INCREMENT PRIMARY KEY,
    NomRol VARCHAR(30) NOT NULL,
    Descripcion VARCHAR(100),
    CONSTRAINT U_Rol_NomRol UNIQUE(NomRol)
);

-- ========================================================
-- 2. Tabla USUARIO
-- Almacena las credenciales y datos del personal
-- ========================================================
CREATE TABLE USUARIO (
    CodUsuario INT AUTO_INCREMENT PRIMARY KEY,
    Username VARCHAR(50) NOT NULL,
    Clave VARCHAR(255) NOT NULL, -- Longitud adecuada para hashes seguros (ej. bcrypt o argon2)
    Nombres VARCHAR(50) NOT NULL,
    Apellidos VARCHAR(50),
    Correo VARCHAR(60),
    Estado TINYINT(1) DEFAULT 1, -- 1 = Activo, 0 = Inactivo (Uso estándar en MySQL para booleanos)
    CodRol INT NOT NULL,
    CONSTRAINT U_Usuario_Username UNIQUE(Username),
    CONSTRAINT FK_Usuario_Rol FOREIGN KEY (CodRol) REFERENCES ROL(CodRol)
);

-- Tabla TIENDA
CREATE TABLE TIENDA (
    CodTienda INT NOT NULL PRIMARY KEY,
    Direccion VARCHAR(60),
    Distrito VARCHAR(20),
    Telefono VARCHAR(15),
    Fax VARCHAR(15)
);

-- Tabla LINEA
CREATE TABLE LINEA (
    CodLinea INT AUTO_INCREMENT PRIMARY KEY,
    NomLinea VARCHAR(20) NOT NULL,
    Descripcion VARCHAR(40),
    CONSTRAINT U_Linea_NomLinea UNIQUE(NomLinea)
);

-- Tabla PROVEEDOR
CREATE TABLE PROVEEDOR (
    CodProveedor INT AUTO_INCREMENT PRIMARY KEY,
    NomProveedor VARCHAR(40) NOT NULL,
    Representante VARCHAR(30),
    Direccion VARCHAR(60),
    Ciudad VARCHAR(15),
    Departamento VARCHAR(15),
    CodigoPostal VARCHAR(15),
    Telefono VARCHAR(15),
    Fax VARCHAR(15)
);

-- Tabla ARTICULO
CREATE TABLE ARTICULO (
    CodArticulo INT AUTO_INCREMENT PRIMARY KEY,
    CodLinea INT NOT NULL,
    CodProveedor INT NOT NULL,
    DescripcionArticulo VARCHAR(40) NOT NULL,
    Presentacion VARCHAR(30),
    PrecioProveedor DECIMAL(10,2), -- Reemplazo de MONEY
    StockActual SMALLINT,
    StockMinimo SMALLINT,
    Descontinuado TINYINT(1) DEFAULT 0, -- Reemplazo de BIT
    CONSTRAINT CK_Articulo_PrecioProveedor CHECK (PrecioProveedor >= 0),
    CONSTRAINT FK_Articulo_Linea FOREIGN KEY (CodLinea) REFERENCES LINEA(CodLinea) ON DELETE CASCADE,
    CONSTRAINT FK_Articulo_Proveedor FOREIGN KEY (CodProveedor) REFERENCES PROVEEDOR(CodProveedor)
);

-- Tabla ORDEN_COMPRA
CREATE TABLE ORDEN_COMPRA (
    NumOrden INT NOT NULL PRIMARY KEY,
    FechaOrden DATETIME NOT NULL,
    FechaIngreso DATETIME
);

-- Tabla ORDEN_DETALLE
CREATE TABLE ORDEN_DETALLE (
    NumOrden INT NOT NULL,
    CodArticulo INT NOT NULL,
    PrecioCompra DECIMAL(10,2) NOT NULL, -- Reemplazo de MONEY
    CantidadSolicitada SMALLINT NOT NULL,
    CantidadRecibida SMALLINT,
    Estado VARCHAR(10),
    CONSTRAINT PK_ORDEN_DETALLE PRIMARY KEY (NumOrden, CodArticulo),
    CONSTRAINT FK_OrdenDetalle_Orden FOREIGN KEY (NumOrden) REFERENCES ORDEN_COMPRA(NumOrden),
    CONSTRAINT FK_OrdenDetalle_Articulo FOREIGN KEY (CodArticulo) REFERENCES ARTICULO(CodArticulo)
);

-- Tabla TRANSPORTISTA
CREATE TABLE TRANSPORTISTA (
    CodTransportista INT NOT NULL PRIMARY KEY,
    NomTransportista VARCHAR(30) NOT NULL,
    Direccion VARCHAR(60),
    Telefono VARCHAR(15)
);

-- Tabla GUIA_ENVIO
CREATE TABLE GUIA_ENVIO (
    NumGuia INT NOT NULL PRIMARY KEY,
    CodTienda INT NOT NULL,
    FechaSalida DATETIME NOT NULL,
    CodTransportista INT NOT NULL,
    CONSTRAINT FK_GuiaEnvio_Tienda FOREIGN KEY (CodTienda) REFERENCES TIENDA(CodTienda),
    CONSTRAINT FK_GuiaEnvio_Transportista FOREIGN KEY (CodTransportista) REFERENCES TRANSPORTISTA(CodTransportista)
);

-- Tabla GUIA_DETALLE
CREATE TABLE GUIA_DETALLE (
    NumGuia INT NOT NULL,
    CodArticulo INT NOT NULL,
    PrecioVenta DECIMAL(10,2) NOT NULL, -- Reemplazo de MONEY
    CantidadEnviada SMALLINT NOT NULL,
    CONSTRAINT PK_GUIA_DETALLE PRIMARY KEY (NumGuia, CodArticulo),
    CONSTRAINT FK_GuiaDetalle_Guia FOREIGN KEY (NumGuia) REFERENCES GUIA_ENVIO(NumGuia),
    CONSTRAINT FK_GuiaDetalle_Articulo FOREIGN KEY (CodArticulo) REFERENCES ARTICULO(CodArticulo)
);


-- ========================================================
-- 1. Tabla ROL (20 registros)
-- ========================================================
INSERT INTO ROL (NomRol, Descripcion) VALUES 
('Administrador', 'Control total del sistema'),
('Vendedor Tienda 1', 'Atención en punto de venta principal'),
('Almacenero Principal', 'Gestión de almacén central'),
('Cajero', 'Cobros y facturación'),
('Supervisor de Ventas', 'Control de vendedores y metas'),
('Gerente de Operaciones', 'Gestión de toda la cadena de suministros'),
('Auditor Interno', 'Revisión de movimientos y cuadres'),
('Asistente de Logística', 'Apoyo en recepción de mercadería'),
('Jefe de Compras', 'Negociación con proveedores'),
('Repartidor', 'Distribución local'),
('Encargado de Limpieza', 'Mantenimiento de tienda'),
('Soporte de Sistemas', 'Mantenimiento de hardware y software'),
('Analista de Datos', 'Generación de reportes de ventas'),
('Gerente Comercial', 'Estrategias de precios y promociones'),
('Guardia de Seguridad', 'Prevención de pérdidas'),
('Atención al Cliente', 'Resolución de reclamos'),
('Vendedor Tienda 2', 'Atención en sucursal secundaria'),
('Almacenero Secundario', 'Gestión de mermas y devoluciones'),
('Contador', 'Gestión de libros contables'),
('Recursos Humanos', 'Control de personal y turnos');

-- ========================================================
-- 2. Tabla USUARIO (20 registros)
-- Se asume una contraseña hasheada genérica para el ejemplo
-- ========================================================
INSERT INTO USUARIO (Username, Clave, Nombres, Apellidos, Correo, Estado, CodRol) VALUES 
('admin_gral', '$2y$10$DemoHash...', 'Carlos', 'Pérez', 'carlos@qhatu.pe', 1, 1),
('vendedor_01', '$2y$10$DemoHash...', 'Ana', 'Gómez', 'ana@qhatu.pe', 1, 2),
('almacen_01', '$2y$10$DemoHash...', 'Luis', 'Torres', 'luis@qhatu.pe', 1, 3),
('caja_01', '$2y$10$DemoHash...', 'María', 'López', 'maria@qhatu.pe', 1, 4),
('super_vtas', '$2y$10$DemoHash...', 'Jorge', 'Rojas', 'jorge@qhatu.pe', 1, 5),
('gerente_op', '$2y$10$DemoHash...', 'Pedro', 'Salinas', 'pedro@qhatu.pe', 1, 6),
('auditor_1', '$2y$10$DemoHash...', 'Elena', 'Vargas', 'elena@qhatu.pe', 1, 7),
('logistica_1', '$2y$10$DemoHash...', 'Diego', 'Castro', 'diego@qhatu.pe', 1, 8),
('compras_jefe', '$2y$10$DemoHash...', 'Sofía', 'Ruiz', 'sofia@qhatu.pe', 1, 9),
('reparto_01', '$2y$10$DemoHash...', 'Juan', 'Mendoza', 'juan@qhatu.pe', 1, 10),
('limpieza_01', '$2y$10$DemoHash...', 'Carmen', 'Díaz', 'carmen@qhatu.pe', 1, 11),
('it_soporte', '$2y$10$DemoHash...', 'Miguel', 'Flores', 'miguel@qhatu.pe', 1, 12),
('data_analyst', '$2y$10$DemoHash...', 'Lucía', 'Ramírez', 'lucia@qhatu.pe', 1, 13),
('gerencia_com', '$2y$10$DemoHash...', 'Raúl', 'Fernández', 'raul@qhatu.pe', 1, 14),
('seguridad_1', '$2y$10$DemoHash...', 'Víctor', 'Cruz', 'victor@qhatu.pe', 1, 15),
('atencion_cl', '$2y$10$DemoHash...', 'Paola', 'Morales', 'paola@qhatu.pe', 1, 16),
('vendedor_02', '$2y$10$DemoHash...', 'José', 'Ortiz', 'jose@qhatu.pe', 1, 17),
('almacen_02', '$2y$10$DemoHash...', 'Rosa', 'Ríos', 'rosa@qhatu.pe', 1, 18),
('contabilidad', '$2y$10$DemoHash...', 'Julio', 'Navarro', 'julio@qhatu.pe', 1, 19),
('rrhh_jefe', '$2y$10$DemoHash...', 'Marta', 'Silva', 'marta@qhatu.pe', 1, 20);

-- ========================================================
-- 3. Tabla TIENDA (20 registros)
-- (CodTienda no es auto_increment, se ingresa manualmente)
-- ========================================================
INSERT INTO TIENDA (CodTienda, Direccion, Distrito, Telefono, Fax) VALUES 
(1, 'Av. Mariscal Castilla 1500', 'El Tambo', '064-123456', '064-123456'),
(2, 'Calle Real 800', 'Huancayo', '064-654321', '064-654321'),
(3, 'Jr. Progreso 320', 'San Agustín', '064-112233', '064-112233'),
(4, 'Av. 9 de Diciembre 450', 'Chilca', '064-445566', '064-445566'),
(5, 'Jr. Arequipa 120', 'San Jerónimo', '064-778899', '064-778899'),
(6, 'Carretera Central Km 5', 'Pilcomayo', '064-998877', '064-998877'),
(7, 'Plaza Principal', 'Sicaya', '064-665544', '064-665544'),
(8, 'Av. Leoncio Prado 200', 'Sapallanga', '064-332211', '064-332211'),
(9, 'Jr. Tarapacá 500', 'Jauja', '064-159753', '064-159753'),
(10, 'Av. Francisco Carle 100', 'Concepción', '064-357159', '064-357159'),
(11, 'Jr. Lima 300', 'Tarma', '064-258456', '064-258456'),
(12, 'Calle Amazonas 400', 'Chupaca', '064-753159', '064-753159'),
(13, 'Av. Independencia 600', 'Hualhuas', '064-951357', '064-951357'),
(14, 'Jr. Junín 210', 'Viques', '064-147258', '064-147258'),
(15, 'Calle Los Héroes 330', 'Huayucachi', '064-369258', '064-369258'),
(16, 'Av. San Martín 800', 'Cullhuas', '064-123789', '064-123789'),
(17, 'Jr. Bolognesi 150', 'Pucará', '064-987123', '064-987123'),
(18, 'Calle Sucre 420', 'Chupuro', '064-456123', '064-456123'),
(19, 'Av. Grau 180', 'Quichuay', '064-789456', '064-789456'),
(20, 'Jr. Cahuide 260', 'Chongos Bajo', '064-321654', '064-321654');

-- ========================================================
-- 4. Tabla LINEA (20 registros)
-- ========================================================
INSERT INTO LINEA (NomLinea, Descripcion) VALUES 
('Abarrotes', 'Productos básicos de despensa'),
('Lácteos', 'Leche, quesos y derivados'),
('Panadería', 'Panes y bollería envasada'),
('Cárnicos', 'Carnes y aves'),
('Verduras', 'Vegetales frescos'),
('Frutas', 'Frutas de estación'),
('Limpieza del Hogar', 'Detergentes y desinfectantes'),
('Aseo Personal', 'Jabones, champús y pastas dentales'),
('Bebidas', 'Agua, jugos y gaseosas'),
('Cereales', 'Avenas y cereales para desayuno'),
('Conservas', 'Atún, duraznos y vegetales en lata'),
('Embutidos', 'Salchichas, jamones y chorizos'),
('Congelados', 'Papas, nuggets y verduras congeladas'),
('Especias', 'Condimentos y sazonadores'),
('Mascotas', 'Alimentos para perros y gatos'),
('Bebés', 'Fórmulas, pañales y papillas'),
('Snacks', 'Galletas y piqueos'),
('Salsas', 'Kétchup, mayonesa y aderezos'),
('Harinas', 'Harinas de trigo y maíz'),
('Papelería Básica', 'Útiles escolares y de oficina básicos');

-- ========================================================
-- 5. Tabla PROVEEDOR (20 registros)
-- ========================================================
INSERT INTO PROVEEDOR (NomProveedor, Representante, Direccion, Ciudad, Departamento, CodigoPostal, Telefono, Fax) VALUES 
('Alicorp S.A.A.', 'Jorge G.', 'Av. Argentina 4793', 'Callao', 'Lima', '07001', '01-3150800', '01-3150801'),
('Gloria S.A.', 'Rosa V.', 'Av. República de Panamá 2461', 'Lima', 'Lima', '15046', '01-4707170', '01-4707171'),
('Nestlé Perú', 'Luis M.', 'Av. Los Castillos 340', 'Ate', 'Lima', '15012', '01-2083000', '01-2083001'),
('San Fernando S.A.', 'Carlos B.', 'Av. República de Panamá 4295', 'Surquillo', 'Lima', '15036', '01-2135300', '01-2135301'),
('Costeño Alimentos', 'Ana F.', 'Av. Nicolás Ayllón 2634', 'Ate', 'Lima', '15012', '01-4952000', '01-4952001'),
('Procter & Gamble', 'Mario R.', 'Av. Canaval y Moreyra 480', 'San Isidro', 'Lima', '15047', '01-2212222', '01-2212223'),
('Kimberly-Clark Perú', 'Diana T.', 'Calle Las Orquídeas 585', 'San Isidro', 'Lima', '15046', '01-6156300', '01-6156301'),
('Unilever Perú', 'Julio C.', 'Av. Paseo de la República 3195', 'San Isidro', 'Lima', '15047', '01-6148200', '01-6148201'),
('Corporación Lindley', 'Marta S.', 'Av. República de Panamá 3055', 'San Isidro', 'Lima', '15046', '01-2136000', '01-2136001'),
('Ajeper S.A.', 'Víctor L.', 'Av. La Paz 131', 'Huachipa', 'Lima', '15461', '01-3136000', '01-3136001'),
('Molitalia S.A.', 'Elena P.', 'Av. Venezuela 2850', 'Lima', 'Lima', '15081', '01-5136200', '01-5136201'),
('Laive S.A.', 'Pedro Q.', 'Av. Nicolás de Piérola 600', 'Ate', 'Lima', '15012', '01-6187600', '01-6187601'),
('Avícola Sofía', 'Raúl M.', 'Jr. Mantaro 450', 'Huancayo', 'Junín', '12001', '064-213456', '064-213457'),
('Mercado Mayorista', 'Juan D.', 'Av. Ferrocarril s/n', 'El Tambo', 'Junín', '12002', '064-223344', '064-223345'),
('Agroindustrias del Sur', 'Sofía A.', 'Carretera Central Km 10', 'Concepción', 'Junín', '12101', '064-556677', '064-556678'),
('Distribuidora El Sol', 'Luis C.', 'Calle Real 1050', 'Huancayo', 'Junín', '12001', '064-334455', '064-334456'),
('Comercializadora Centro', 'María H.', 'Av. Huancavelica 800', 'El Tambo', 'Junín', '12002', '064-778899', '064-778890'),
('Granos Andinos', 'José W.', 'Jr. Cajamarca 210', 'Huancayo', 'Junín', '12001', '064-112233', '064-112234'),
('Empacadora del Centro', 'Carmen N.', 'Parque Industrial s/n', 'El Tambo', 'Junín', '12002', '064-990011', '064-990012'),
('Importaciones Global', 'Miguel R.', 'Av. Giráldez 600', 'Huancayo', 'Junín', '12001', '064-445566', '064-445567');

-- ========================================================
-- 6. Tabla TRANSPORTISTA (20 registros)
-- ========================================================
INSERT INTO TRANSPORTISTA (CodTransportista, NomTransportista, Direccion, Telefono) VALUES 
(1, 'Transportes Salazar', 'Av. Ferrocarril 1200, Huancayo', '987654321'),
(2, 'Logística Centro', 'Calle Arequipa 340, El Tambo', '987654322'),
(3, 'Carga Rápida SAC', 'Jr. Junín 560, Chilca', '987654323'),
(4, 'Distribuciones Andes', 'Av. Mariscal Castilla 800, El Tambo', '987654324'),
(5, 'Fletes Huanca', 'Calle Real 1500, Huancayo', '987654325'),
(6, 'Transportes El Sol', 'Av. Huancavelica 450, El Tambo', '987654326'),
(7, 'Envios Seguros', 'Jr. Ayacucho 220, Huancayo', '987654327'),
(8, 'Rutas del Valle', 'Carretera Central Km 4, Pilcomayo', '987654328'),
(9, 'Cargas y Fletes Wanka', 'Av. Leoncio Prado 780, Chilca', '987654329'),
(10, 'Expreso Mantaro', 'Jr. Amazonas 130, Huancayo', '987654330'),
(11, 'Movilidad Junín', 'Av. Los Próceres 600, San Agustín', '987654331'),
(12, 'Transportes Estrella', 'Calle Lima 440, Jauja', '987654332'),
(13, 'Logística San Jerónimo', 'Av. 28 de Julio 300, San Jerónimo', '987654333'),
(14, 'Carga Pesada Perú', 'Av. 9 de Diciembre 500, Chilca', '987654334'),
(15, 'Transportes Veloz', 'Jr. Piura 250, Huancayo', '987654335'),
(16, 'Distribuidora Vial', 'Calle Cusco 180, El Tambo', '987654336'),
(17, 'Ruta 66 Cargas', 'Av. Giráldez 900, Huancayo', '987654337'),
(18, 'Fletes del Centro', 'Jr. Puno 320, Huancayo', '987654338'),
(19, 'Expreso Andino', 'Av. Ferrocarril 2000, El Tambo', '987654339'),
(20, 'Transportes Globales', 'Calle Tarapacá 150, Huancayo', '987654340');

-- ========================================================
-- 7. Tabla ARTICULO (20 registros de primera necesidad)
-- Vincula CodLinea (1-20) y CodProveedor (1-20)
-- ========================================================
INSERT INTO ARTICULO (CodLinea, CodProveedor, DescripcionArticulo, Presentacion, PrecioProveedor, StockActual, StockMinimo, Descontinuado) VALUES 
(1, 5, 'Arroz Extra Costeño', 'Bolsa 5 Kg', 18.50, 150, 50, 0),
(1, 1, 'Azúcar Blanca', 'Bolsa 1 Kg', 3.80, 200, 50, 0),
(2, 2, 'Leche Evaporada Gloria', 'Lata 400g', 3.50, 300, 100, 0),
(1, 1, 'Aceite Vegetal Primor', 'Botella 1 Litro', 8.20, 120, 30, 0),
(1, 11, 'Fideos Espagueti Molitalia', 'Paquete 500g', 2.50, 250, 80, 0),
(10, 1, 'Avena Clásica', 'Bolsa 300g', 1.80, 100, 20, 0),
(11, 1, 'Atún en Aceite', 'Lata 170g', 4.50, 180, 60, 0),
(1, 18, 'Lentejas Bebé', 'Bolsa 500g', 3.00, 90, 20, 0),
(14, 1, 'Sal Yodada', 'Bolsa 1 Kg', 1.20, 150, 40, 0),
(19, 11, 'Harina Preparada', 'Bolsa 1 Kg', 4.00, 100, 30, 0),
(4, 13, 'Huevos Rosados', 'Plancha 30 und', 16.00, 80, 20, 0),
(4, 4, 'Pollo Entero Fresco', 'Kilo', 9.50, 60, 15, 0),
(5, 14, 'Papa Blanca', 'Kilo', 2.00, 400, 100, 0),
(5, 14, 'Cebolla Roja', 'Kilo', 2.50, 200, 50, 0),
(14, 14, 'Ajo Pelado', 'Malla 250g', 3.50, 70, 15, 0),
(8, 6, 'Jabón de Tocador', 'Paquete 3 und', 4.80, 110, 30, 0),
(7, 6, 'Detergente en Polvo', 'Bolsa 1.2 Kg', 11.50, 85, 25, 0),
(8, 7, 'Papel Higiénico Doble Hoja', 'Paquete 4 und', 5.00, 220, 60, 0),
(8, 6, 'Pasta Dental', 'Tubo 90g', 3.20, 140, 40, 0),
(9, 9, 'Agua Mineral Sin Gas', 'Botella 2.5 Litros', 3.00, 300, 100, 0);

-- ========================================================
-- 8. Tabla ORDEN_COMPRA (20 registros)
-- (NumOrden no es auto_increment)
-- ========================================================
INSERT INTO ORDEN_COMPRA (NumOrden, FechaOrden, FechaIngreso) VALUES 
(1, '2026-05-01 08:30:00', '2026-05-02 10:00:00'),
(2, '2026-05-02 09:15:00', '2026-05-03 11:30:00'),
(3, '2026-05-03 10:45:00', '2026-05-04 09:20:00'),
(4, '2026-05-04 11:00:00', '2026-05-05 14:00:00'),
(5, '2026-05-05 14:30:00', '2026-05-06 16:15:00'),
(6, '2026-05-06 08:00:00', '2026-05-07 09:45:00'),
(7, '2026-05-07 15:20:00', '2026-05-08 11:10:00'),
(8, '2026-05-08 09:10:00', '2026-05-09 10:30:00'),
(9, '2026-05-09 16:40:00', '2026-05-10 12:00:00'),
(10, '2026-05-10 10:05:00', '2026-05-11 08:50:00'),
(11, '2026-05-11 11:30:00', '2026-05-12 14:20:00'),
(12, '2026-05-12 13:15:00', '2026-05-13 15:40:00'),
(13, '2026-05-13 09:50:00', '2026-05-14 10:15:00'),
(14, '2026-05-14 14:25:00', '2026-05-15 11:30:00'),
(15, '2026-05-15 08:45:00', '2026-05-16 09:10:00'),
(16, '2026-05-16 10:10:00', '2026-05-17 12:45:00'),
(17, '2026-05-16 15:30:00', NULL),
(18, '2026-05-17 09:00:00', NULL),
(19, '2026-05-17 11:20:00', NULL),
(20, '2026-05-17 16:00:00', NULL);

-- ========================================================
-- 9. Tabla ORDEN_DETALLE (20 registros)
-- Vincula NumOrden (1-20) y CodArticulo (1-20)
-- ========================================================
INSERT INTO ORDEN_DETALLE (NumOrden, CodArticulo, PrecioCompra, CantidadSolicitada, CantidadRecibida, Estado) VALUES 
(1, 1, 18.50, 50, 50, 'Completo'),
(2, 2, 3.80, 100, 100, 'Completo'),
(3, 3, 3.50, 150, 150, 'Completo'),
(4, 4, 8.20, 60, 60, 'Completo'),
(5, 5, 2.50, 120, 120, 'Completo'),
(6, 6, 1.80, 50, 50, 'Completo'),
(7, 7, 4.50, 80, 80, 'Completo'),
(8, 8, 3.00, 40, 40, 'Completo'),
(9, 9, 1.20, 100, 100, 'Completo'),
(10, 10, 4.00, 50, 50, 'Completo'),
(11, 11, 16.00, 40, 40, 'Completo'),
(12, 12, 9.50, 30, 30, 'Completo'),
(13, 13, 2.00, 200, 200, 'Completo'),
(14, 14, 2.50, 100, 100, 'Completo'),
(15, 15, 3.50, 30, 30, 'Completo'),
(16, 16, 4.80, 50, 50, 'Completo'),
(17, 17, 11.50, 40, 0, 'Pendiente'),
(18, 18, 5.00, 100, 0, 'Pendiente'),
(19, 19, 3.20, 70, 0, 'Pendiente'),
(20, 20, 3.00, 150, 0, 'Pendiente');

-- ========================================================
-- 10. Tabla GUIA_ENVIO (20 registros)
-- (NumGuia no es auto_increment). Vincula Tienda y Transportista
-- ========================================================
INSERT INTO GUIA_ENVIO (NumGuia, CodTienda, FechaSalida, CodTransportista) VALUES 
(1, 1, '2026-05-10 07:00:00', 1),
(2, 2, '2026-05-10 08:30:00', 2),
(3, 3, '2026-05-11 09:15:00', 3),
(4, 4, '2026-05-11 10:45:00', 4),
(5, 5, '2026-05-12 07:30:00', 5),
(6, 6, '2026-05-12 11:00:00', 6),
(7, 7, '2026-05-13 08:20:00', 7),
(8, 8, '2026-05-13 14:10:00', 8),
(9, 9, '2026-05-14 09:50:00', 9),
(10, 10, '2026-05-14 15:30:00', 10),
(11, 11, '2026-05-15 07:45:00', 11),
(12, 12, '2026-05-15 13:20:00', 12),
(13, 13, '2026-05-16 08:10:00', 13),
(14, 14, '2026-05-16 11:40:00', 14),
(15, 15, '2026-05-16 16:00:00', 15),
(16, 16, '2026-05-17 07:30:00', 16),
(17, 17, '2026-05-17 09:15:00', 17),
(18, 18, '2026-05-17 10:45:00', 18),
(19, 19, '2026-05-17 14:00:00', 19),
(20, 20, '2026-05-17 16:30:00', 20);

-- ========================================================
-- 11. Tabla GUIA_DETALLE (20 registros)
-- Vincula NumGuia (1-20) y CodArticulo (1-20)
-- ========================================================
INSERT INTO GUIA_DETALLE (NumGuia, CodArticulo, PrecioVenta, CantidadEnviada) VALUES 
(1, 1, 20.00, 10),
(2, 2, 4.50, 20),
(3, 3, 4.20, 30),
(4, 4, 9.50, 15),
(5, 5, 3.20, 25),
(6, 6, 2.50, 10),
(7, 7, 5.50, 20),
(8, 8, 3.80, 10),
(9, 9, 1.80, 25),
(10, 10, 4.80, 15),
(11, 11, 18.00, 10),
(12, 12, 11.00, 10),
(13, 13, 2.80, 50),
(14, 14, 3.50, 25),
(15, 15, 4.50, 10),
(16, 16, 6.00, 15),
(17, 17, 13.50, 10),
(18, 18, 6.50, 20),
(19, 19, 4.00, 15),
(20, 20, 4.00, 40);




UPDATE USUARIO SET Clave = '123456' WHERE Username IN ('admin_gral', 'vendedor_01', 'almacen_01', 'caja_01');

-- ============================================
-- PASO 1: Desactivar modo seguro
-- ============================================
SET SQL_SAFE_UPDATES = 0;

-- ============================================
-- PASO 2: Actualizar contraseñas de usuarios de prueba
-- ============================================
USE QhatuPERU;

UPDATE USUARIO SET Clave = '123456' 
WHERE Username IN ('admin_gral', 'vendedor_01', 'almacen_01', 'caja_01');

-- ============================================
-- PASO 3: Verificar que se actualizó correctamente
-- ============================================
SELECT Username, Clave, Nombres, Apellidos, CodRol 
FROM USUARIO 
WHERE Username IN ('admin_gral', 'vendedor_01', 'almacen_01', 'caja_01');

-- ============================================
-- PASO 4: Reactivar modo seguro (OPCIONAL)
-- ============================================
SET SQL_SAFE_UPDATES = 1;



USE QhatuPERU;

-- Desactivar la verificación de claves foráneas
SET FOREIGN_KEY_CHECKS = 0;

-- Eliminar todos los usuarios que no sean admin o rrhh
DELETE FROM USUARIO WHERE Username NOT IN ('admin', 'rrhh');

-- Eliminar todos los roles que no sean 1 o 2
DELETE FROM ROL WHERE CodRol NOT IN (1, 2);

-- Si el rol 2 no existe, lo creamos; si existe, lo actualizamos
INSERT INTO ROL (CodRol, NomRol, Descripcion) VALUES (1, 'Administrador', 'Control total del sistema')
ON DUPLICATE KEY UPDATE NomRol = 'Administrador', Descripcion = 'Control total del sistema';

INSERT INTO ROL (CodRol, NomRol, Descripcion) VALUES (2, 'RRHH', 'Cálculo de sueldos y planilla')
ON DUPLICATE KEY UPDATE NomRol = 'RRHH', Descripcion = 'Cálculo de sueldos y planilla';

-- Ahora insertar o actualizar los usuarios
INSERT INTO USUARIO (CodUsuario, Username, Clave, Nombres, Apellidos, Correo, Estado, CodRol)
VALUES
(1, 'admin', '123456', 'Carlos', 'Pérez', 'admin@qhatu.pe', 1, 1),
(2, 'rrhh', '123456', 'Ana', 'Gómez', 'rrhh@qhatu.pe', 1, 2)
ON DUPLICATE KEY UPDATE
    Clave = '123456',
    Nombres = VALUES(Nombres),
    Apellidos = VALUES(Apellidos),
    Correo = VALUES(Correo),
    Estado = 1,
    CodRol = VALUES(CodRol);

-- Reactivar la verificación de claves foráneas
SET FOREIGN_KEY_CHECKS = 1;

-- Verificar el resultado
SELECT u.Username, u.Nombres, u.Clave, r.NomRol
FROM USUARIO u
JOIN ROL r ON u.CodRol = r.CodRol
ORDER BY u.CodUsuario;