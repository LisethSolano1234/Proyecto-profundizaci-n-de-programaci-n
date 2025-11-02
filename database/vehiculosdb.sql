DROP DATABASE IF EXISTS vehiculosdb;
CREATE DATABASE vehiculosdb;
USE vehiculosdb;

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

CREATE TABLE trayecto (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  codigo_ruta VARCHAR(100) NOT NULL,
  id_persona BIGINT NOT NULL,
  id_vehiculo BIGINT NOT NULL,
  ubicacion VARCHAR(255) NOT NULL,
  orden_parada INT NOT NULL,
  latitud DOUBLE,
  longitud DOUBLE,
  login_registro VARCHAR(100) NOT NULL,
  fecha_inicio DATE,
  fecha_fin DATE,
  CONSTRAINT fk_trayecto_persona FOREIGN KEY (id_persona)
    REFERENCES persona(id)
    ON DELETE CASCADE,
  CONSTRAINT fk_trayecto_vehiculo FOREIGN KEY (id_vehiculo)
    REFERENCES vehiculo(id)
    ON DELETE CASCADE
);

--  Persona administrativa
INSERT INTO persona (nombre, apellido, tipo_identificacion, numero_identificacion, correo, telefono, tipo_persona)
VALUES ('Ana', 'Ramírez', 'CC', '1001', 'ana.ramirez@example.com', '3101112233', 'A');

--  Conductor
INSERT INTO persona (nombre, apellido, tipo_identificacion, numero_identificacion, correo, telefono, tipo_persona, vigencia_licencia)
VALUES ('Carlos', 'Pérez', 'CC', '2002', 'carlos.perez@example.com', '3102223344', 'C', '2026-12-31');

--  Vehículo
INSERT INTO vehiculo (placa, tipo_vehiculo, tipo_servicio, tipo_combustible, capacidad_pasajeros, color, modelo, marca, linea)
VALUES ('ABC123', 'Automóvil', 'Privado', 'Gasolina', 5, 'Rojo', 2023, 'Toyota', 'Corolla');

--  Documento habilitado
INSERT INTO documento (codigo, nombre, tipo_documento, tipo_vehiculo, requerido, descripcion, fecha_emision, fecha_vencimiento, estado_documento, vehiculo_id)
VALUES ('DOC-001', 'SOAT', 'Seguro Obligatorio', 'A', 'RA', 'Documento obligatorio de tránsito', '2024-01-01', '2025-01-01', 'HABILITADO', 1);

--  Asociación vehículo-persona
INSERT INTO vehiculo_persona (fecha_asociacion, estado, vehiculo_id, persona_id)
VALUES (CURDATE(), 'EA', 1, 2);

INSERT INTO persona (
    tipo_identificacion, tipo_persona, vigencia_licencia,
    apellido, correo, nombre, numero_identificacion, telefono
) VALUES (
    'CC', 'C', '2026-12-31',
    'Gómez', 'conductor@correo.com', 'Juan', '123456789', '3001234567'
);

INSERT INTO vehiculo (
    capacidad_pasajeros, modelo, placa, color, linea,
    marca, tipo_combustible, tipo_servicio, tipo_vehiculo
) VALUES (
    4, 2020, 'ABC123', 'Blanco', 'Sedán',
    'Toyota', 'Gasolina', 'Particular', 'Automóvil'
);

INSERT INTO ruta (codigo, descripcion)
VALUES ('RUTA-001', 'Ruta principal de prueba');

INSERT INTO trayecto (
    fecha_inicio, fecha_fin, latitud, longitud, orden_parada,
    id_persona, id_vehiculo, id_ruta, login_registro, ubicacion
) VALUES 
('2025-11-02', '2025-11-02', 4.44530452335443, -75.24342910353239, 1, 1, 1, 1, 'admin', 'Alcaldía de ibagué'),
('2025-11-02', '2025-11-02', 4.444210947630705, -75.24209427654772, 2, 1, 1, 1, 'admin', 'gobernación del tolima'),
('2025-11-02', '2025-11-02', 4.44347096137032, -75.24030950101361, 3, 1, 1, 1, 'admin', 'Centro Comercial combeima'),
('2025-11-02', '2025-11-02', 4.446755939154615, -75.2418519157435, 4, 1, 1, 1, 'admin', 'concha acustica'),
('2025-11-02', '2025-11-02', 4.4470, -75.2430, 5, 1, 1, 1, 'admin', 'Parque Deportivo de Ibagué');

SELECT * FROM persona;
SELECT * FROM vehiculo;
SELECT * FROM trayecto;

SELECT id, codigo_ruta FROM trayecto;

INSERT INTO persona (nombre, apellido, tipo_identificacion, tipo_persona, numero_identificacion)
VALUES ('Juan', 'Pérez', 'CC', 'C', '123456789');

INSERT INTO vehiculo (placa, marca, modelo, color, tipo_vehiculo, tipo_combustible, tipo_servicio, capacidad_pasajeros)
VALUES ('ABC123', 'Toyota', 2022, 'Rojo', 'Automóvil', 'Gasolina', 'Particular', 5);

INSERT INTO ruta (codigo, descripcion)
VALUES ('RUTA01', 'Ruta de prueba por Ibagué');

INSERT INTO trayecto (fecha_inicio, fecha_fin, latitud, longitud, orden_parada, id_persona, id_vehiculo, id_ruta, login_registro, ubicacion)
VALUES 
('2025-11-02', '2025-11-02', 4.443680844325873, -75.24457767469653, 1, 1, 1, 1, 'admin', 'Conservatorio del Tolima'),
('2025-11-02', '2025-11-02', 4.448165243345001, -75.24071539004012, 2, 1, 1, 1, 'admin', 'Museo Panóptico de Ibagué'),
('2025-11-02', '2025-11-02', 4.445571315202558, -75.20480063712604, 3, 1, 1, 1, 'admin', 'Centro Comercial La Estación'),
('2025-11-02', '2025-11-02', 4.4400276249670325, -75.20426654771187, 4, 1, 1, 1, 'admin', 'Acqua Power Center'),
('2025-11-02', '2025-11-02', 4.427193932054335, -75.18318927469652, 5, 1, 1, 1, 'admin', 'Parque Deportivo de Ibagué');





