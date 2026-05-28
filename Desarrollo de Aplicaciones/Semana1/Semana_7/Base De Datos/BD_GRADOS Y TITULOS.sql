-- ==============================================================================
-- ELIMINAR BASE DE DATOS SI EXISTE Y CREAR UNA NUEVA
-- ==============================================================================
DROP DATABASE IF EXISTS `bd_grados_titulos_upla`;
CREATE DATABASE IF NOT EXISTS `bd_grados_titulos_upla` 
DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `bd_grados_titulos_upla`;

-- ==============================================================================
-- MÓDULO 1: CATÁLOGOS ACADÉMICOS
-- ==============================================================================
CREATE TABLE IF NOT EXISTS `facultad` (
  `id_facultad` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(150) NOT NULL,
  PRIMARY KEY (`id_facultad`)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `programa_estudios` (
  `id_programa` INT NOT NULL AUTO_INCREMENT,
  `id_facultad` INT NOT NULL,
  `nombre` VARCHAR(150) NOT NULL,
  `grado_academico` VARCHAR(150) NOT NULL,
  `titulo_profesional` VARCHAR(150) NOT NULL,
  `modalidad_estudio` ENUM('Presencial', 'Semipresencial', 'A_Distancia') NOT NULL,
  PRIMARY KEY (`id_programa`),
  FOREIGN KEY (`id_facultad`) REFERENCES `facultad`(`id_facultad`) ON DELETE RESTRICT
) ENGINE=InnoDB;

-- ==============================================================================
-- MÓDULO 2: USUARIOS Y ROLES
-- ==============================================================================
CREATE TABLE IF NOT EXISTS `usuario` (
  `codigo` VARCHAR(20) NOT NULL,
  `dni` VARCHAR(15) NOT NULL UNIQUE,
  `nombres` VARCHAR(150) NOT NULL,
  `apellidos` VARCHAR(150) NOT NULL,
  `email_institucional` VARCHAR(150) NOT NULL UNIQUE,
  `password_hash` VARCHAR(255) NOT NULL,
  `rol` ENUM('Estudiante', 'Docente', 'Administrativo', 'Decano') NOT NULL,
  `codigo_orcid` VARCHAR(20) DEFAULT NULL,
  `estado` ENUM('Activo', 'Inactivo') DEFAULT 'Activo',
  PRIMARY KEY (`codigo`)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `historial_academico` (
  `id_historial` INT NOT NULL AUTO_INCREMENT,
  `codigo_estudiante` VARCHAR(20) NOT NULL,
  `id_programa` INT NOT NULL,
  `condicion_actual` ENUM('Egresado', 'Bachiller', 'Titulado') DEFAULT 'Egresado',
  `fecha_egreso` DATE DEFAULT NULL,
  `fecha_bachiller` DATE DEFAULT NULL,
  PRIMARY KEY (`id_historial`),
  FOREIGN KEY (`codigo_estudiante`) REFERENCES `usuario`(`codigo`) ON DELETE CASCADE,
  FOREIGN KEY (`id_programa`) REFERENCES `programa_estudios`(`id_programa`) ON DELETE RESTRICT
) ENGINE=InnoDB;

-- ==============================================================================
-- MÓDULO 3: GESTIÓN DE TRÁMITES
-- ==============================================================================
CREATE TABLE IF NOT EXISTS `tramite` (
  `id_tramite` INT NOT NULL AUTO_INCREMENT,
  `codigo_estudiante` VARCHAR(20) NOT NULL,
  `tipo_tramite` ENUM('Obtencion_Bachiller', 'Obtencion_Titulo_Tesis', 'Obtencion_Titulo_TSP') NOT NULL,
  `estado_actual` ENUM(
    'Iniciado', 
    'Revision_Requisitos', 
    'Aprobacion_Plan', 
    'Desarrollo_Investigacion', 
    'Revision_Similitud', 
    'Revision_Jurado', 
    'Expedito', 
    'Sustentacion_Programada', 
    'Culminado', 
    'Rechazado'
  ) DEFAULT 'Iniciado',
  `fecha_inicio` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `fecha_actualizacion` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_tramite`),
  FOREIGN KEY (`codigo_estudiante`) REFERENCES `usuario`(`codigo`) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `documento_requisito` (
  `id_documento` INT NOT NULL AUTO_INCREMENT,
  `id_tramite` INT NOT NULL,
  `tipo_documento` ENUM('Solicitud_FUT', 'DNI_Copia', 'Foto_Pasaporte', 'Constancia_Matricula', 'Certificado_Idiomas', 'Recibo_Pago', 'Certificado_Trabajo_TSP', 'Plan_Tesis', 'Borrador_Tesis', 'Informe_Similitud') NOT NULL,
  `ruta_archivo` VARCHAR(255) NOT NULL,
  `estado_validacion` ENUM('Pendiente', 'Validado', 'Observado') DEFAULT 'Pendiente',
  `observacion` TEXT,
  `fecha_subida` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_documento`),
  FOREIGN KEY (`id_tramite`) REFERENCES `tramite`(`id_tramite`) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ==============================================================================
-- MÓDULO 4: PROYECTO DE INVESTIGACIÓN
-- ==============================================================================
CREATE TABLE IF NOT EXISTS `proyecto` (
  `id_proyecto` INT NOT NULL AUTO_INCREMENT,
  `id_tramite` INT NOT NULL,
  `titulo` VARCHAR(500) NOT NULL,
  `modalidad` ENUM('Tesis', 'Trabajo_Suficiencia_Profesional') NOT NULL,
  `enfoque` ENUM('Cuantitativa', 'Cualitativa', 'Mixta', 'No_Aplica') NOT NULL,
  `porcentaje_similitud` DECIMAL(5,2) DEFAULT NULL,
  `estado` ENUM('Registrado', 'Plan_Aprobado', 'En_Ejecucion', 'Aprobado_Por_Asesor', 'Aprobado_Por_Jurado', 'Sustentado') DEFAULT 'Registrado',
  `url_repositorio` VARCHAR(255) DEFAULT NULL,
  `fecha_registro` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_proyecto`),
  FOREIGN KEY (`id_tramite`) REFERENCES `tramite`(`id_tramite`) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `proyecto_autor` (
  `id_proyecto` INT NOT NULL,
  `codigo_estudiante` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`id_proyecto`, `codigo_estudiante`),
  FOREIGN KEY (`id_proyecto`) REFERENCES `proyecto`(`id_proyecto`) ON DELETE CASCADE,
  FOREIGN KEY (`codigo_estudiante`) REFERENCES `usuario`(`codigo`) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `proyecto_asesor` (
  `id_asignacion` INT NOT NULL AUTO_INCREMENT,
  `id_proyecto` INT NOT NULL,
  `codigo_docente` VARCHAR(20) NOT NULL,
  `resolucion_asignacion` VARCHAR(100) DEFAULT NULL,
  `estado` ENUM('Activo', 'Renuncia', 'Cambiado') DEFAULT 'Activo',
  PRIMARY KEY (`id_asignacion`),
  FOREIGN KEY (`id_proyecto`) REFERENCES `proyecto`(`id_proyecto`) ON DELETE CASCADE,
  FOREIGN KEY (`codigo_docente`) REFERENCES `usuario`(`codigo`) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `proyecto_jurado` (
  `id_asignacion` INT NOT NULL AUTO_INCREMENT,
  `id_proyecto` INT NOT NULL,
  `codigo_docente` VARCHAR(20) NOT NULL,
  `rol` ENUM('Presidente', 'Secretario', 'Vocal', 'Suplente') NOT NULL,
  `resolucion_asignacion` VARCHAR(100) DEFAULT NULL,
  PRIMARY KEY (`id_asignacion`),
  FOREIGN KEY (`id_proyecto`) REFERENCES `proyecto`(`id_proyecto`) ON DELETE CASCADE,
  FOREIGN KEY (`codigo_docente`) REFERENCES `usuario`(`codigo`) ON DELETE CASCADE,
  UNIQUE KEY `idx_proyecto_docente` (`id_proyecto`, `codigo_docente`)
) ENGINE=InnoDB;

-- ==============================================================================
-- MÓDULO 5: EVALUACIONES Y RÚBRICAS
-- ==============================================================================
CREATE TABLE IF NOT EXISTS `rubrica_catalogo` (
  `id_rubrica` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(150) NOT NULL,
  `tipo` ENUM('Plan_Cuantitativo', 'Plan_Cualitativo', 'Tesis_Cuantitativa', 'Tesis_Cualitativa', 'TSP', 'Sustentacion') NOT NULL,
  `puntaje_maximo_total` DECIMAL(5,2) NOT NULL,
  PRIMARY KEY (`id_rubrica`)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `rubrica_item` (
  `id_item` INT NOT NULL AUTO_INCREMENT,
  `id_rubrica` INT NOT NULL,
  `numero_pregunta` INT NOT NULL,
  `criterio_evaluacion` TEXT NOT NULL,
  `puntaje_maximo` DECIMAL(4,2) NOT NULL,
  PRIMARY KEY (`id_item`),
  FOREIGN KEY (`id_rubrica`) REFERENCES `rubrica_catalogo`(`id_rubrica`) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `evaluacion_cabecera` (
  `id_evaluacion` INT NOT NULL AUTO_INCREMENT,
  `id_proyecto` INT NOT NULL,
  `codigo_evaluador` VARCHAR(20) NOT NULL,
  `id_rubrica` INT NOT NULL,
  `etapa` ENUM('Revision_Plan', 'Revision_Borrador_Final', 'Sustentacion') NOT NULL,
  `comentarios_generales` TEXT,
  `puntaje_obtenido` DECIMAL(5,2) NOT NULL DEFAULT 0.0,
  `condicion_final` ENUM('Aprobado', 'Aprobado_Observaciones_Menores', 'Desaprobado_Observaciones_Mayores', 'Desaprobado') NOT NULL,
  `fecha_evaluacion` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_evaluacion`),
  FOREIGN KEY (`id_proyecto`) REFERENCES `proyecto`(`id_proyecto`) ON DELETE CASCADE,
  FOREIGN KEY (`codigo_evaluador`) REFERENCES `usuario`(`codigo`) ON DELETE CASCADE,
  FOREIGN KEY (`id_rubrica`) REFERENCES `rubrica_catalogo`(`id_rubrica`) ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `evaluacion_detalle` (
  `id_detalle` INT NOT NULL AUTO_INCREMENT,
  `id_evaluacion` INT NOT NULL,
  `id_item` INT NOT NULL,
  `puntaje_asignado` DECIMAL(4,2) NOT NULL,
  PRIMARY KEY (`id_detalle`),
  FOREIGN KEY (`id_evaluacion`) REFERENCES `evaluacion_cabecera`(`id_evaluacion`) ON DELETE CASCADE,
  FOREIGN KEY (`id_item`) REFERENCES `rubrica_item`(`id_item`) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ==============================================================================
-- MÓDULO 6: SUSTENTACIÓN
-- ==============================================================================
CREATE TABLE IF NOT EXISTS `sustentacion` (
  `id_sustentacion` INT NOT NULL AUTO_INCREMENT,
  `id_proyecto` INT NOT NULL,
  `resolucion_expedito` VARCHAR(100) NOT NULL,
  `fecha_hora_programada` DATETIME NOT NULL,
  `modalidad_sustentacion` ENUM('Presencial', 'No_Presencial') DEFAULT 'Presencial',
  `lugar_enlace` VARCHAR(255) NOT NULL,
  `nota_final_numerica` DECIMAL(4,2) DEFAULT NULL,
  `nota_final_letras` VARCHAR(50) DEFAULT NULL,
  `condicion_acta` ENUM('Excelente', 'Muy_Bueno', 'Bueno', 'Regular', 'Desaprobado', 'Pendiente_De_Sustentar') DEFAULT 'Pendiente_De_Sustentar',
  `aprobacion_tipo` ENUM('Unanimidad', 'Mayoria', 'No_Aplica') DEFAULT 'No_Aplica',
  `observaciones_acta` TEXT,
  PRIMARY KEY (`id_sustentacion`),
  FOREIGN KEY (`id_proyecto`) REFERENCES `proyecto`(`id_proyecto`) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ==============================================================================
-- INSERTAR DATOS - FACULTADES
-- ==============================================================================
INSERT INTO `facultad` (`nombre`) VALUES 
('Facultad de Ciencias de la Educación'),
('Facultad de Ingeniería'),
('Facultad de Ciencias Empresariales'),
('Facultad de Ciencias de la Salud'),
('Facultad de Derecho y Ciencias Políticas');

-- ==============================================================================
-- INSERTAR DATOS - PROGRAMAS DE ESTUDIOS
-- ==============================================================================
INSERT INTO `programa_estudios` (`id_facultad`, `nombre`, `grado_academico`, `titulo_profesional`, `modalidad_estudio`) VALUES 
(1, 'Educación Primaria', 'Bachiller en Educación', 'Licenciado en Educación Primaria', 'Presencial'),
(1, 'Educación Secundaria', 'Bachiller en Educación', 'Licenciado en Educación Secundaria', 'Presencial'),
(2, 'Ingeniería de Sistemas', 'Bachiller en Ingeniería', 'Ingeniero de Sistemas', 'Presencial'),
(3, 'Administración de Empresas', 'Bachiller en Administración', 'Licenciado en Administración', 'Semipresencial'),
(4, 'Enfermería', 'Bachiller en Enfermería', 'Licenciado en Enfermería', 'Presencial'),
(5, 'Derecho', 'Bachiller en Derecho', 'Abogado', 'Presencial');

-- ==============================================================================
-- INSERTAR DATOS - USUARIOS (SOLO UNO POR ROL, CONTRASEÑAS SIMPLES)
-- ==============================================================================
-- Las contraseñas son: admin, decano, docente, estudiante (TODO EN MINÚSCULAS)
INSERT INTO `usuario` (`codigo`, `dni`, `nombres`, `apellidos`, `email_institucional`, `password_hash`, `rol`, `codigo_orcid`, `estado`) VALUES 
('ADMIN', '11111111', 'Carlos', 'Administrador', 'carlos.admin@upla.edu.pe', SHA2('admin', 256), 'Administrativo', NULL, 'Activo'),
('DECANO', '22222222', 'Roberto', 'Decano', 'roberto.decano@upla.edu.pe', SHA2('decano', 256), 'Decano', '0000-0001-1111-1111', 'Activo'),
('DOCENTE', '33333333', 'Juan', 'Docente', 'juan.docente@upla.edu.pe', SHA2('docente', 256), 'Docente', '0000-0002-2222-2222', 'Activo'),
('ESTUDIANTE', '44444444', 'Luis', 'Estudiante', 'luis.estudiante@upla.edu.pe', SHA2('estudiante', 256), 'Estudiante', NULL, 'Activo');

-- ==============================================================================
-- INSERTAR DATOS - TRÁMITES
-- ==============================================================================
INSERT INTO `tramite` (`codigo_estudiante`, `tipo_tramite`, `estado_actual`) VALUES 
('ESTUDIANTE', 'Obtencion_Titulo_Tesis', 'Iniciado');

-- ==============================================================================
-- INSERTAR DATOS - PROYECTOS
-- ==============================================================================
INSERT INTO `proyecto` (`id_tramite`, `titulo`, `modalidad`, `enfoque`, `porcentaje_similitud`, `estado`, `url_repositorio`) VALUES 
(1, 'Influencia de la inteligencia artificial en la educación superior', 'Tesis', 'Mixta', 15.50, 'Registrado', 'https://repositorio.upla.edu.pe/handle/123456789/1');

-- ==============================================================================
-- INSERTAR DATOS - RÚBRICAS DE EJEMPLO
-- ==============================================================================
INSERT INTO `rubrica_catalogo` (`nombre`, `tipo`, `puntaje_maximo_total`) VALUES 
('Rúbrica Tesis Cuantitativa', 'Tesis_Cuantitativa', 100.00),
('Rúbrica Tesis Cualitativa', 'Tesis_Cualitativa', 100.00),
('Rúbrica Sustentación', 'Sustentacion', 50.00);

-- ==============================================================================
-- VERIFICAR DATOS INSERTADOS
-- ==============================================================================
SELECT '========================================' AS '=== RESULTADOS ===';
SELECT '========================================' AS '';

SELECT '=== USUARIOS CREADOS ===' AS '';
SELECT codigo, nombres, apellidos, rol, 'Contraseña: ' + LOWER(codigo) as password FROM usuario;

SELECT '========================================' AS '';
SELECT '=== FACULTADES ===' AS '';
SELECT * FROM facultad;

SELECT '========================================' AS '';
SELECT '=== PROGRAMAS ===' AS '';
SELECT p.id_programa, f.nombre as facultad, p.nombre as programa, p.modalidad_estudio 
FROM programa_estudios p 
JOIN facultad f ON p.id_facultad = f.id_facultad;

SELECT '========================================' AS '';
SELECT '=== TRÁMITES ===' AS '';
SELECT t.id_tramite, u.nombres, u.apellidos, t.tipo_tramite, t.estado_actual, t.fecha_inicio 
FROM tramite t 
JOIN usuario u ON t.codigo_estudiante = u.codigo;

SELECT '========================================' AS '';
SELECT '=== PROYECTOS ===' AS '';
SELECT id_proyecto, titulo, modalidad, enfoque, estado FROM proyecto;

SELECT '========================================' AS '';
SELECT '=== RÚBRICAS ===' AS '';
SELECT id_rubrica, nombre, tipo, puntaje_maximo_total FROM rubrica_catalogo;

SELECT '========================================' AS '';
SELECT '=== VERIFICACIÓN DE CONTRASEÑAS ===' AS '';
SELECT 
    codigo, 
    rol,
    CASE 
        WHEN codigo = 'ADMIN' AND password_hash = SHA2('admin', 256) THEN '✅ CONTRASEÑA CORRECTA (admin)'
        WHEN codigo = 'DECANO' AND password_hash = SHA2('decano', 256) THEN '✅ CONTRASEÑA CORRECTA (decano)'
        WHEN codigo = 'DOCENTE' AND password_hash = SHA2('docente', 256) THEN '✅ CONTRASEÑA CORRECTA (docente)'
        WHEN codigo = 'ESTUDIANTE' AND password_hash = SHA2('estudiante', 256) THEN '✅ CONTRASEÑA CORRECTA (estudiante)'
        ELSE '❌ ERROR EN CONTRASEÑA'
    END as verificacion
FROM usuario;

SELECT '========================================' AS '';
SELECT '=== RESUMEN FINAL ===' AS '';
SELECT '✅ BASE DE DATOS CREADA CORRECTAMENTE' as mensaje;
SELECT '📋 USUARIOS DISPONIBLES:' as mensaje;
SELECT '   ADMIN     → contraseña: admin' as mensaje;
SELECT '   DECANO    → contraseña: decano' as mensaje;
SELECT '   DOCENTE   → contraseña: docente' as mensaje;
SELECT '   ESTUDIANTE → contraseña: estudiante' as mensaje;


USE bd_grados_titulos_upla;

-- Ver el estado del proyecto
SELECT p.id_proyecto, p.titulo, p.estado 
FROM proyecto p 
JOIN tramite t ON p.id_tramite = t.id_tramite 
WHERE t.codigo_estudiante = 'ESTUDIANTE';

-- Ver la sustentación
SELECT s.id_sustentacion, s.id_proyecto, s.nota_final_numerica, s.condicion_acta 
FROM sustentacion s 
JOIN proyecto p ON s.id_proyecto = p.id_proyecto 
JOIN tramite t ON p.id_tramite = t.id_tramite 
WHERE t.codigo_estudiante = 'ESTUDIANTE';

-- Ver el trámite
SELECT * FROM tramite WHERE codigo_estudiante = 'ESTUDIANTE';

USE bd_grados_titulos_upla;

-- Actualizar el proyecto del estudiante
UPDATE proyecto SET estado = 'Sustentado' WHERE id_proyecto = 1;

-- Actualizar o insertar sustentación con nota aprobatoria
INSERT INTO sustentacion (id_proyecto, resolucion_expedito, fecha_hora_programada, modalidad_sustentacion, lugar_enlace, nota_final_numerica, nota_final_letras, condicion_acta, aprobacion_tipo, observaciones_acta) 
VALUES (1, 'RES-2024-003', NOW(), 'Presencial', 'Auditorio Principal', 16.50, 'Dieciseis punto cincuenta', 'Excelente', 'Unanimidad', 'Excelente sustentación')
ON DUPLICATE KEY UPDATE 
    nota_final_numerica = 16.50,
    nota_final_letras = 'Dieciseis punto cincuenta',
    condicion_acta = 'Excelente',
    aprobacion_tipo = 'Unanimidad';

-- Actualizar el trámite
UPDATE tramite SET estado_actual = 'Culminado' WHERE codigo_estudiante = 'ESTUDIANTE';

-- Verificar los datos
SELECT '=== DATOS ACTUALIZADOS ===' as '';
SELECT '--- PROYECTO ---' as '';
SELECT id_proyecto, titulo, estado FROM proyecto WHERE id_proyecto = 1;

SELECT '--- SUSTENTACIÓN ---' as '';
SELECT id_sustentacion, id_proyecto, nota_final_numerica, condicion_acta FROM sustentacion WHERE id_proyecto = 1;

SELECT '--- TRÁMITE ---' as '';
SELECT id_tramite, codigo_estudiante, estado_actual FROM tramite WHERE codigo_estudiante = 'ESTUDIANTE';