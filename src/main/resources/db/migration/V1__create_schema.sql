-- Creación de tipos ENUM
CREATE TABLE usuarios (
                          id_usuario INT AUTO_INCREMENT PRIMARY KEY,
                          nombre VARCHAR(100) NOT NULL,
                          apellido VARCHAR(100) NOT NULL,
                          telefono VARCHAR(9) NOT NULL,
                          email VARCHAR(100) UNIQUE NOT NULL,
                          contrasena VARCHAR(100) NOT NULL,
                          fecha_nacimiento DATE NOT NULL,
                          dni VARCHAR(8) NOT NULL,
                          departamento VARCHAR(100) NOT NULL,
                          provincia VARCHAR(100) NOT NULL,
                          distrito VARCHAR(100) NOT NULL,
                          rol ENUM('HUESPED', 'ADMIN') NOT NULL DEFAULT 'HUESPED',
                          estado ENUM('ACTIVO', 'INACTIVO') NOT NULL DEFAULT 'ACTIVO',
                          imagen_url VARCHAR(255),
                          fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                          email_verificado BOOLEAN DEFAULT FALSE NOT NULL
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
                              FOREIGN KEY (id_tipo_cama) REFERENCES tipos_camas(id_tipo_cama) ON DELETE CASCADE,
                              FOREIGN KEY (id_tipo_habitacion) REFERENCES tipos_habitaciones(id_tipo_habitacion) ON DELETE CASCADE
);

CREATE TABLE reservas (
                          id_reserva INT AUTO_INCREMENT PRIMARY KEY,
                          fecha_entrada TIMESTAMP NOT NULL,
                          fecha_salida TIMESTAMP NOT NULL,
                          cantidad_huespedes INT NOT NULL,
                          precio_total DECIMAL(10, 2) NOT NULL,
                          estado ENUM('PENDIENTE', 'CONFIRMADA', 'RESERVADA', 'CANCELADA', 'COMPLETA') NOT NULL DEFAULT 'PENDIENTE',
                          codigo_reserva VARCHAR(255) UNIQUE NOT NULL,
                          id_usuario INT NOT NULL,
                          id_habitacion INT NOT NULL,
                          id_sede INT NOT NULL,
                          FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE,
                          FOREIGN KEY (id_habitacion) REFERENCES habitaciones(id_habitacion) ON DELETE CASCADE,
                          FOREIGN KEY (id_sede) REFERENCES sedes(id_sede) ON DELETE CASCADE
);
