-- ===========================================
-- RECREACION COMPLETA DE LA BASE DE DATOS
-- COMPATIBLE CON EL CODIGO DEL PROYECTO VEHICULOS
-- ===========================================

DROP DATABASE IF EXISTS vehiculosdb;
CREATE DATABASE vehiculosdb CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE vehiculosdb;

-- ===========================================
-- TABLA PERSONA
-- ===========================================
CREATE TABLE persona (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL,
  apellido VARCHAR(100) NOT NULL,
  tipo_identificacion VARCHAR(10) NOT NULL,
  numero_identificacion VARCHAR(50) NOT NULL UNIQUE,
  correo VARCHAR(100),
  telefono VARCHAR(20),
  tipo_persona VARCHAR(1) NOT NULL, -- A = Administrativo, C = Conductor
  licencia_conduccion LONGBLOB,
  vigencia_licencia DATE
);

-- ===========================================
-- TABLA VEHÍCULO
-- ===========================================
CREATE TABLE vehiculo (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  placa VARCHAR(6) NOT NULL UNIQUE,
  tipo_vehiculo VARCHAR(50) NOT NULL,
  tipo_servicio VARCHAR(50) NOT NULL,
  tipo_combustible VARCHAR(50) NOT NULL,
  capacidad_pasajeros INT NOT NULL,
  color VARCHAR(30) NOT NULL,
  modelo INT NOT NULL,
  marca VARCHAR(50) NOT NULL,
  linea VARCHAR(50)
);

-- ===========================================
-- TABLA DOCUMENTO
-- ===========================================
CREATE TABLE documento (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  codigo VARCHAR(100) NOT NULL UNIQUE,
  nombre VARCHAR(100) NOT NULL,
  tipo_documento VARCHAR(100) NOT NULL,
  tipo_vehiculo VARCHAR(20),
  requerido VARCHAR(20),
  descripcion VARCHAR(255),
  fecha_emision DATE,
  fecha_vencimiento DATE,
  archivo LONGBLOB,
  estado_documento VARCHAR(50) NOT NULL DEFAULT 'EN VERIFICACION',
  vehiculo_id BIGINT NOT NULL,
  CONSTRAINT fk_documento_vehiculo FOREIGN KEY (vehiculo_id)
    REFERENCES vehiculo(id)
    ON DELETE CASCADE
);

-- ===========================================
-- RELACIÓN VEHÍCULO - PERSONA
-- ===========================================
CREATE TABLE vehiculo_persona (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  fecha_asociacion DATE,
  estado VARCHAR(2) NOT NULL, -- EA, PO, RO
  vehiculo_id BIGINT NOT NULL,
  persona_id BIGINT NOT NULL,
  CONSTRAINT fk_vp_vehiculo FOREIGN KEY (vehiculo_id)
    REFERENCES vehiculo(id)
    ON DELETE CASCADE,
  CONSTRAINT fk_vp_persona FOREIGN KEY (persona_id)
    REFERENCES persona(id)
    ON DELETE CASCADE
);

-- ===========================================
-- TABLA RUTA
-- ===========================================
CREATE TABLE ruta (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  codigo VARCHAR(100) NOT NULL UNIQUE,
  descripcion VARCHAR(255) NOT NULL
);

-- ===========================================
-- TABLA TRAYECTO
-- ===========================================
CREATE TABLE trayecto (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  id_ruta BIGINT NOT NULL,
  id_persona BIGINT NOT NULL,
  id_vehiculo BIGINT NOT NULL,
  ubicacion VARCHAR(255) NOT NULL,
  orden_parada INT NOT NULL,
  latitud DOUBLE,
  longitud DOUBLE,
  login_registro VARCHAR(100) NOT NULL,
  fecha_inicio DATE,
  fecha_fin DATE,
  CONSTRAINT fk_trayecto_ruta FOREIGN KEY (id_ruta)
    REFERENCES ruta(id)
    ON DELETE CASCADE,
  CONSTRAINT fk_trayecto_persona FOREIGN KEY (id_persona)
    REFERENCES persona(id)
    ON DELETE CASCADE,
  CONSTRAINT fk_trayecto_vehiculo FOREIGN KEY (id_vehiculo)
    REFERENCES vehiculo(id)
    ON DELETE CASCADE
);

-- ===========================================
-- TABLA USUARIO (FALTABA EN TU SCRIPT)
-- ===========================================
CREATE TABLE usuario (
  login VARCHAR(100) PRIMARY KEY,
  password VARCHAR(100),
  api_key VARCHAR(100),
  rol VARCHAR(50),
  persona_id BIGINT NOT NULL,
  CONSTRAINT fk_usuario_persona FOREIGN KEY (persona_id)
    REFERENCES persona(id)
    ON DELETE CASCADE
);

-- ===========================================
-- DATOS DE PRUEBA
-- ===========================================

-- Persona administrativa
INSERT INTO persona (nombre, apellido, tipo_identificacion, numero_identificacion, correo, telefono, tipo_persona)
VALUES ('Ana', 'Ramírez', 'CC', '1001', 'ana.ramirez@example.com', '3101112233', 'A');

-- Conductor
INSERT INTO persona (nombre, apellido, tipo_identificacion, numero_identificacion, correo, telefono, tipo_persona, vigencia_licencia)
VALUES ('Carlos', 'Pérez', 'CC', '2002', 'carlos.perez@example.com', '3102223344', 'C', '2026-12-31');

-- Vehículo
INSERT INTO vehiculo (placa, tipo_vehiculo, tipo_servicio, tipo_combustible, capacidad_pasajeros, color, modelo, marca, linea)
VALUES ('ABC123', 'Automóvil', 'Privado', 'Gasolina', 5, 'Rojo', 2023, 'Toyota', 'Corolla');

-- Documento
INSERT INTO documento (codigo, nombre, tipo_documento, tipo_vehiculo, requerido, descripcion, fecha_emision, fecha_vencimiento, estado_documento, vehiculo_id)
VALUES ('DOC-001', 'SOAT', 'Seguro Obligatorio', 'A', 'RA', 'Documento obligatorio de tránsito', '2024-01-01', '2025-01-01', 'HABILITADO', 1);

-- Asociación vehículo-persona
INSERT INTO vehiculo_persona (fecha_asociacion, estado, vehiculo_id, persona_id)
VALUES (CURDATE(), 'EA', 1, 2);

-- Ruta
INSERT INTO ruta (codigo, descripcion)
VALUES ('RUTA-002', 'Ruta principal de prueba');

-- Trayectos asociados
INSERT INTO trayecto (fecha_inicio, fecha_fin, latitud, longitud, orden_parada, id_persona, id_vehiculo, id_ruta, login_registro, ubicacion)
VALUES
('2025-11-02', '2025-11-02', 4.4453, -75.2434, 1, 2, 1, 1, 'admin', 'Alcaldía de Ibagué'),
('2025-11-02', '2025-11-02', 4.4442, -75.2420, 2, 2, 1, 1, 'admin', 'Gobernación del Tolima'),
('2025-11-02', '2025-11-02', 4.4434, -75.2403, 3, 2, 1, 1, 'admin', 'Centro Comercial Combeima'),
('2025-11-02', '2025-11-02', 4.4467, -75.2418, 4, 2, 1, 1, 'admin', 'Concha Acústica'),
('2025-11-02', '2025-11-02', 4.4470, -75.2430, 5, 2, 1, 1, 'admin', 'Parque Deportivo de Ibagué');

-- Usuario ADMINISTRADOR (para login)
INSERT INTO usuario (login, password, api_key, rol, persona_id)
VALUES ('admin', '1234', UUID(), 'ADMIN', 1);

-- ===========================================
-- CONSULTAS DE VERIFICACIÓN
-- ===========================================
SELECT * FROM persona;
SELECT * FROM vehiculo;
SELECT * FROM ruta;
SELECT * FROM trayecto;
SELECT * FROM usuario;
