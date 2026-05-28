-- ==============================================================================
-- DESACTIVAR SAFE UPDATE MODE TEMPORALMENTE
-- ==============================================================================
SET SQL_SAFE_UPDATES = 0;

-- ==============================================================================
-- ELIMINAR BASE DE DATOS SI EXISTE Y CREAR UNA NUEVA
-- ==============================================================================
DROP DATABASE IF EXISTS `bd_ibercap_upla`;
CREATE DATABASE IF NOT EXISTS `bd_ibercap_upla` 
DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `bd_ibercap_upla`;

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
-- LIMPIAR DATOS EXISTENTES (CON SAFE MODE DESACTIVADO)
-- ==============================================================================
DELETE FROM `sustentacion`;
DELETE FROM `evaluacion_detalle`;
DELETE FROM `evaluacion_cabecera`;
DELETE FROM `rubrica_item`;
DELETE FROM `rubrica_catalogo`;
DELETE FROM `proyecto_jurado`;
DELETE FROM `proyecto_asesor`;
DELETE FROM `proyecto_autor`;
DELETE FROM `proyecto`;
DELETE FROM `documento_requisito`;
DELETE FROM `tramite`;
DELETE FROM `historial_academico`;
DELETE FROM `programa_estudios`;
DELETE FROM `facultad`;
DELETE FROM `usuario`;

-- Resetear auto-incrementos
ALTER TABLE `facultad` AUTO_INCREMENT = 1;
ALTER TABLE `programa_estudios` AUTO_INCREMENT = 1;
ALTER TABLE `usuario` AUTO_INCREMENT = 1;
ALTER TABLE `tramite` AUTO_INCREMENT = 1;
ALTER TABLE `proyecto` AUTO_INCREMENT = 1;
ALTER TABLE `sustentacion` AUTO_INCREMENT = 1;

-- ==============================================================================
-- INSERTAR FACULTADES
-- ==============================================================================
INSERT INTO `facultad` (`nombre`) VALUES 
('Facultad de Ingeniería'),
('Facultad de Ciencias de la Educación'),
('Facultad de Ciencias Empresariales'),
('Facultad de Ciencias de la Salud'),
('Facultad de Derecho y Ciencias Políticas');

-- ==============================================================================
-- INSERTAR PROGRAMAS DE ESTUDIOS
-- ==============================================================================
INSERT INTO `programa_estudios` (`id_facultad`, `nombre`, `grado_academico`, `titulo_profesional`, `modalidad_estudio`) VALUES 
(1, 'Ingeniería de Sistemas y Computación', 'Bachiller en Ingeniería', 'Ingeniero de Sistemas y Computación', 'Presencial'),
(1, 'Ingeniería Civil', 'Bachiller en Ingeniería', 'Ingeniero Civil', 'Presencial'),
(2, 'Educación Primaria', 'Bachiller en Educación', 'Licenciado en Educación Primaria', 'Presencial'),
(3, 'Administración de Empresas', 'Bachiller en Administración', 'Licenciado en Administración', 'Semipresencial');

-- ==============================================================================
-- INSERTAR USUARIOS (DATOS REALES DE LA TESIS)
-- ==============================================================================
INSERT INTO `usuario` (`codigo`, `dni`, `nombres`, `apellidos`, `email_institucional`, `password_hash`, `rol`, `codigo_orcid`, `estado`) VALUES 

-- ESTUDIANTE AUTOR DE LA TESIS
('DAVID', '42061791', 'David Israel', 'Marmolejo Barbaran', 'david.marmolejo@upla.edu.pe', SHA2('david', 256), 'Estudiante', NULL, 'Activo'),

-- ASESOR DE TESIS
('WALTER', '12345678', 'Walter David', 'Estares Ventocilla', 'walter.estares@upla.edu.pe', SHA2('walter', 256), 'Docente', '0000-0002-5921-4367', 'Activo'),

-- ADMINISTRATIVO
('ADMIN', '11111111', 'Carlos', 'Administrador', 'carlos.admin@upla.edu.pe', SHA2('admin', 256), 'Administrativo', NULL, 'Activo'),

-- DECANO
('DECANO', '22222222', 'Roberto', 'Decano', 'roberto.decano@upla.edu.pe', SHA2('decano', 256), 'Decano', NULL, 'Activo'),

-- JURADOS REALES DE LA TESIS
('RUBEN', '33333331', 'Ruben', 'Tapia Silguera', 'ruben.tapia@upla.edu.pe', SHA2('ruben', 256), 'Docente', NULL, 'Activo'),
('LEONEL', '33333332', 'Leonel', 'Untiveros Peñaloza', 'leonel.untiveros@upla.edu.pe', SHA2('leonel', 256), 'Docente', NULL, 'Activo'),
('ALEX', '33333333', 'Alex Albert', 'Zuñiga Manrique', 'alex.zuniga@upla.edu.pe', SHA2('alex', 256), 'Docente', NULL, 'Activo'),
('SANTIAGO', '33333334', 'Santiago', 'Sevallos Salinas', 'santiago.sevallos@upla.edu.pe', SHA2('santiago', 256), 'Docente', NULL, 'Activo'),
('EDWARD', '33333335', 'Edward Eddie', 'Bustinza Zuasnabar', 'edward.bustinza@upla.edu.pe', SHA2('edward', 256), 'Docente', NULL, 'Activo'),
('CAROL', '33333336', 'Carol Josefina', 'Fabian Coronel', 'carol.fabian@upla.edu.pe', SHA2('carol', 256), 'Docente', NULL, 'Activo');

-- ==============================================================================
-- INSERTAR TRÁMITE DEL ESTUDIANTE
-- ==============================================================================
INSERT INTO `tramite` (`codigo_estudiante`, `tipo_tramite`, `estado_actual`) VALUES 
('DAVID', 'Obtencion_Titulo_Tesis', 'Iniciado');

-- ==============================================================================
-- INSERTAR PROYECTO DE TESIS REAL
-- ==============================================================================
INSERT INTO `proyecto` (`id_tramite`, `titulo`, `modalidad`, `enfoque`, `porcentaje_similitud`, `estado`, `url_repositorio`) VALUES 
(1, 'IMPLEMENTACIÓN DE SISTEMA DE INFORMACIÓN PARA LA AUTOMATIZACIÓN DE PROCESOS EMPRESARIALES Y ESTADÍSTICOS EN EL INSTITUTO IBERCAP - AYACUCHO 2019', 'Tesis', 'Mixta', 19.00, 'Registrado', 'https://repositorio.upla.edu.pe/handle/123456789/1');

-- ==============================================================================
-- INSERTAR ASESOR DEL PROYECTO
-- ==============================================================================
INSERT INTO `proyecto_asesor` (`id_proyecto`, `codigo_docente`, `resolucion_asignacion`, `estado`) VALUES 
(1, 'WALTER', 'RES-2024-001', 'Activo');

-- ==============================================================================
-- INSERTAR JURADOS DEL PROYECTO
-- ==============================================================================
INSERT INTO `proyecto_jurado` (`id_proyecto`, `codigo_docente`, `rol`, `resolucion_asignacion`) VALUES 
(1, 'RUBEN', 'Presidente', 'RES-2024-002'),
(1, 'LEONEL', 'Secretario', 'RES-2024-002'),
(1, 'ALEX', 'Vocal', 'RES-2024-002'),
(1, 'SANTIAGO', 'Vocal', 'RES-2024-002'),
(1, 'EDWARD', 'Vocal', 'RES-2024-002'),
(1, 'CAROL', 'Suplente', 'RES-2024-002');

-- ==============================================================================
-- INSERTAR RÚBRICAS
-- ==============================================================================
INSERT INTO `rubrica_catalogo` (`nombre`, `tipo`, `puntaje_maximo_total`) VALUES 
('Rúbrica Tesis Ingeniería de Sistemas', 'Tesis_Cuantitativa', 100.00),
('Rúbrica Evaluación de Sustentación', 'Sustentacion', 50.00);

-- ==============================================================================
-- INSERTAR EVALUACIÓN DEL ASESOR
-- ==============================================================================
INSERT INTO `evaluacion_cabecera` (`id_proyecto`, `codigo_evaluador`, `id_rubrica`, `etapa`, `comentarios_generales`, `puntaje_obtenido`, `condicion_final`) VALUES 
(1, 'WALTER', 1, 'Revision_Plan', 'El trabajo de investigación cumple con los objetivos planteados. La metodología es adecuada y los resultados son satisfactorios.', 85.50, 'Aprobado');

-- ==============================================================================
-- INSERTAR SUSTENTACIÓN CON NOTA FINAL
-- ==============================================================================
INSERT INTO `sustentacion` (`id_proyecto`, `resolucion_expedito`, `fecha_hora_programada`, `modalidad_sustentacion`, `lugar_enlace`, `nota_final_numerica`, `nota_final_letras`, `condicion_acta`, `aprobacion_tipo`, `observaciones_acta`) VALUES 
(1, 'RES-2024-003', '2024-12-20 10:00:00', 'Presencial', 'Auditorio Principal - Facultad de Ingeniería', 16.50, 'Dieciseis punto cincuenta', 'Excelente', 'Unanimidad', 'El sustentante demostró dominio del tema. Metodología clara y resultados relevantes para el Instituto IBERCAP.');

-- ==============================================================================
-- ACTUALIZAR ESTADOS FINALES (TESIS APROBADA)
-- ==============================================================================
UPDATE `proyecto` SET `estado` = 'Sustentado' WHERE `id_proyecto` = 1;
UPDATE `tramite` SET `estado_actual` = 'Culminado' WHERE `id_tramite` = 1;

-- ==============================================================================
-- REACTIVAR SAFE UPDATE MODE
-- ==============================================================================
SET SQL_SAFE_UPDATES = 1;

-- ==============================================================================
-- VERIFICAR DATOS INSERTADOS
-- ==============================================================================
SELECT '========================================' AS '=== RESULTADOS ===';
SELECT '========================================' AS '';

SELECT '=== USUARIOS CREADOS ===' AS '';
SELECT codigo, nombres, apellidos, rol, CONCAT('Contraseña: ', LOWER(codigo)) as password FROM usuario;

SELECT '========================================' AS '';
SELECT '=== PROYECTO DE TESIS ===' AS '';
SELECT id_proyecto, titulo, porcentaje_similitud, estado FROM proyecto;

SELECT '========================================' AS '';
SELECT '=== ASESOR ASIGNADO ===' AS '';
SELECT a.id_asignacion, p.titulo, u.nombres, u.apellidos, u.codigo_orcid 
FROM proyecto_asesor a 
JOIN proyecto p ON a.id_proyecto = p.id_proyecto 
JOIN usuario u ON a.codigo_docente = u.codigo;

SELECT '========================================' AS '';
SELECT '=== JURADOS ASIGNADOS ===' AS '';
SELECT j.rol, u.nombres, u.apellidos 
FROM proyecto_jurado j 
JOIN usuario u ON j.codigo_docente = u.codigo 
WHERE j.id_proyecto = 1
ORDER BY FIELD(j.rol, 'Presidente', 'Secretario', 'Vocal', 'Suplente');

SELECT '========================================' AS '';
SELECT '=== SUSTENTACIÓN ===' AS '';
SELECT s.id_sustentacion, p.titulo, s.fecha_hora_programada, s.nota_final_numerica, s.condicion_acta 
FROM sustentacion s 
JOIN proyecto p ON s.id_proyecto = p.id_proyecto;

SELECT '========================================' AS '';
SELECT '=== RESUMEN FINAL ===' AS '';
SELECT '✅ BASE DE DATOS CREADA CORRECTAMENTE' as mensaje;
SELECT '========================================' AS '';
SELECT '📋 TESIS REGISTRADA:' as mensaje;
SELECT 'IMPLEMENTACIÓN DE SISTEMA DE INFORMACIÓN PARA LA AUTOMATIZACIÓN' as mensaje;
SELECT 'DE PROCESOS EMPRESARIALES Y ESTADÍSTICOS EN EL INSTITUTO IBERCAP' as mensaje;
SELECT '========================================' AS '';
SELECT '👨‍🎓 AUTOR: DAVID ISRAEL MARMOLEJO BARBARAN' as mensaje;
SELECT '👨‍🏫 ASESOR: MG. WALTER DAVID ESTARES VENTOCILLA' as mensaje;
SELECT '⭐ NOTA OBTENIDA: 16.50 (EXCELENTE)' as mensaje;
SELECT '🏆 CONDICIÓN: APROBADO POR UNANIMIDAD' as mensaje;
SELECT '📊 PORCENTAJE SIMILITUD: 19%' as mensaje;
SELECT '========================================' AS '';
SELECT '🔑 CREDENCIALES DE ACCESO:' as mensaje;
SELECT '   DAVID / david     → Estudiante (Autor de la tesis)' as mensaje;
SELECT '   WALTER / walter   → Docente (Asesor de tesis)' as mensaje;
SELECT '   ADMIN / admin     → Administrativo' as mensaje;
SELECT '   DECANO / decano   → Decano' as mensaje;
SELECT '   RUBEN / ruben     → Jurado Presidente' as mensaje;
SELECT '   LEONEL / leonel   → Jurado Secretario' as mensaje;
SELECT '========================================' AS '';