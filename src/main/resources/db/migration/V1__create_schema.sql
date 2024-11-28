-- Desactivar foreign key checks temporalmente
SET FOREIGN_KEY_CHECKS = 0;

-- Eliminar tablas si existen para evitar conflictos
DROP TABLE IF EXISTS reservas;
DROP TABLE IF EXISTS habitaciones;
DROP TABLE IF EXISTS sedes;
DROP TABLE IF EXISTS tipos_camas;
DROP TABLE IF EXISTS tipos_habitaciones;
DROP TABLE IF EXISTS usuarios;

-- Creación de tipos ENUM y tablas
CREATE TABLE usuarios (
                          id_usuario INT AUTO_INCREMENT PRIMARY KEY,
                          nombre VARCHAR(100) NOT NULL,
                          apellido VARCHAR(100) NOT NULL,
                          telefono VARCHAR(9),
                          email VARCHAR(100) NOT NULL,
                          contrasena VARCHAR(100) NOT NULL,
                          fecha_nacimiento DATE,
                          dni VARCHAR(8) NOT NULL,
                          departamento VARCHAR(100),
                          provincia VARCHAR(100),
                          distrito VARCHAR(100),
                          rol ENUM('HUESPED', 'ADMIN') NOT NULL DEFAULT 'HUESPED',
                          estado ENUM('ACTIVO', 'INACTIVO') NOT NULL DEFAULT 'ACTIVO',
                          imagen_url VARCHAR(255),
                          fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                          email_verificado BOOLEAN DEFAULT FALSE NOT NULL,
                          CONSTRAINT uk_email UNIQUE (email),
                          CONSTRAINT uk_dni UNIQUE (dni)
);

CREATE TABLE tipos_habitaciones (
                                    id_tipo_habitacion INT AUTO_INCREMENT PRIMARY KEY,
                                    nombre VARCHAR(100) NOT NULL,
                                    descripcion VARCHAR(100) NOT NULL
);

CREATE TABLE tipos_camas (
                             id_tipo_cama INT AUTO_INCREMENT PRIMARY KEY,
                             nombre VARCHAR(100) NOT NULL,
                             descripcion VARCHAR(100) NOT NULL
);

CREATE TABLE sedes (
                       id_sede INT AUTO_INCREMENT PRIMARY KEY,
                       nombre VARCHAR(100) NOT NULL,
                       ciudad VARCHAR(100) NOT NULL,
                       pais VARCHAR(100) NOT NULL,
                       direccion VARCHAR(255) NOT NULL,
                       estado ENUM('DISPONIBLE', 'NO DISPONIBLE') NOT NULL DEFAULT 'DISPONIBLE'
);

CREATE TABLE habitaciones (
                              id_habitacion INT AUTO_INCREMENT PRIMARY KEY,
                              nombre VARCHAR(100) NOT NULL,
                              precio_noche DECIMAL(10, 2) NOT NULL,
                              capacidad_max INT NOT NULL,
                              estado ENUM('DISPONIBLE', 'OCUPADA', 'RESERVADA', 'MANTENIMIENTO') NOT NULL DEFAULT 'DISPONIBLE',
                              imagen_url VARCHAR(255) NOT NULL,
                              id_tipo_cama INT NOT NULL,
                              id_tipo_habitacion INT NOT NULL,
                              CONSTRAINT fk_habitacion_tipo_cama FOREIGN KEY (id_tipo_cama) REFERENCES tipos_camas(id_tipo_cama) ON DELETE CASCADE,
                              CONSTRAINT fk_habitacion_tipo_habitacion FOREIGN KEY (id_tipo_habitacion) REFERENCES tipos_habitaciones(id_tipo_habitacion) ON DELETE CASCADE
);

CREATE TABLE reservas (
                          id_reserva INT AUTO_INCREMENT PRIMARY KEY,
                          fecha_entrada TIMESTAMP NOT NULL,
                          fecha_salida TIMESTAMP NOT NULL,
                          cantidad_huespedes INT NOT NULL,
                          precio_total DECIMAL(10, 2) NOT NULL,
                          estado ENUM('PENDIENTE', 'CONFIRMADA', 'RESERVADA', 'CANCELADA', 'COMPLETA') NOT NULL DEFAULT 'PENDIENTE',
                          codigo_reserva VARCHAR(255) NOT NULL,
                          codigo_qr_url VARCHAR(255) NOT NULL,
                          id_usuario INT NOT NULL,
                          id_habitacion INT NOT NULL,
                          id_sede INT NOT NULL,
                          CONSTRAINT uk_codigo_reserva UNIQUE (codigo_reserva),
                          CONSTRAINT fk_reserva_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE,
                          CONSTRAINT fk_reserva_habitacion FOREIGN KEY (id_habitacion) REFERENCES habitaciones(id_habitacion) ON DELETE CASCADE,
                          CONSTRAINT fk_reserva_sede FOREIGN KEY (id_sede) REFERENCES sedes(id_sede) ON DELETE CASCADE
);

-- Reactivar foreign key checks
SET FOREIGN_KEY_CHECKS = 1;