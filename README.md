# API de Gestión de Hoteles

Lo siguiente es una lista de endpoints disponibles y recursos necesarios para consumir esta API.

**URL de la aplicación:**  
`https://hoteleria-cuh2gzacd2fkg5f9.mexicocentral-01.azurewebsites.net`

## Requisitos previos

- Algunas rutas requieren un **Bearer Token** proporcionado al usuario en el encabezado de autorización. Obtén este token al iniciar sesión en el endpoint `/auth/login`.
- Utiliza herramientas como Postman, curl o bibliotecas de cliente HTTP (Axios, Fetch, etc.).

## Endpoints disponibles

| Funcionalidad            | Método | Endpoint           |
|--------------------------|--------|--------------------|
| Registrar usuario        | POST   | `/usuarios/crear`  |
| Validar OTP y guardar    | POST   | `/usuarios/validar-otp`     |
| Login usuario            | POST   | `/auth/login`      |
| Registrar Reserva        | POST   | `/reservas/crear`      |
| Filtrar sedes por nombre de ciudad y fechas    | GET    | `/sedes/filtrar/fecha-nombre`|
| Filtrar sedes por nombre de ciudad y fecha con horas    | GET    | `/sedes/filtrar/fecha-hora-nombre`|
| Filtrar Habitaciones por nombre    | GET    | `/habitaciones/sede/{idSede}/buscar-nombre`|

---

## ENDPOINTS

### 1. LOGIN
- **Loguear usuario**
  - **Request URL:** `POST auth/login`
  - **Descripción:** Este endpoint permite a un usuario iniciar sesión y recibir un token JWT para autenticarse en futuros requests.
  - **Request Body:**
    ```json
    {
        "email": "ivanrual61@gmail.com",
        "contrasena": "test123"
    }
    ```
  - **Response:**
    ```json
    {
        "message": "Login exitoso",
        "data": {
            "id": 22,
            "token": "[Token generado automáticamente]"
        }
    }
    ```

### 2. USUARIO

- **Registrar usuario**
  - **Request URL:** `POST /usuarios/crear`
  - **Descripción:** Este endpoint permite registrar un nuevo usuario de manera temporal y enviara un OTP al correo del usuario.
  - **Request Body:**
    ```json
    {
        "telefono": "933777024",
        "email": "ivanrual61@gmail.com",
        "contrasena": "test123",
        "dni": 74231760
    }
    ```
  - **Response:**
    ```json
    {
        "message": "Usuario guarda temporalmente",
        "data": {
            "key": "NE&iaPWCz%",
            "nombre": "IVAN GUILLERMO",
            "apellido": "RUPAY ALVAREZ",
            "email": "ivanrual61@gmail.com"
        }
    }
    ```

- **Validar Otp y Guardar Usuario**
  - **Request URL:** `POST /usuarios/validar-otp`
  - **Descripción:** Este endpoint valida el OTP enviado por correo y crea el usuario permanentemente en el sistema.
  - **Request Body:**
    ```json
    {
      "otp": "NE&iaPWCz%"
    }
    ```
  - **Response:**
    ```json
    {
        "message": "Otp validado y usuario creado correctamente",
        "data": {
            "id": 24,
            "nombre": "Ivan Guillermo",
            "apellido": "Rupay Alvarez",
            "email": "josuealva920@gmail.com"
        }
    }
    ```

### 3. Sedes 

- **Buscar Sedes por Filtros**
  - **Request URL:** `GET /sedes/filtrar/fecha-nombre`
  - **Descripción:** Este endpoint permite filtrar Sedes por Ciudad de la Sede y Fechas de Entrada y Salida donde se mostrara una lista de Sedes que no tengan habitaciones ocupadas en esas fechas.
    ### Parámetros de Consulta

    | Parámetro | Tipo | Descripción | Ejemplo |
    |-----------|------|-------------|---------|
    | `fechaEntrada` | LocalDate | Fecha de entrada | `2024-12-30` |
    | `fechaSalida` | LocalDate | Fecha de salida | `2024-12-31` |
    | `nombreCiudad` | String | Nombre de la Ciudad | `iqui` |
  - **Response:**
    ```json
    {
        "message": "Sede encontradas",
        "data": [
            {
                "id": 2,
                "nombre": "Hotel Sol",
                "ciudad": "Cusco",
                "pais": "Perú",
                "direccion": "Calle del Sol 456",
                "estado": "DISPONIBLE"
            }
        ]
    }
    ```

- **Buscar Sedes por Filtros 2**
  - **Request URL:** `GET /sedes/filtrar/fecha-hora-nombre`
  - **Descripción:** Este endpoint permite filtrar Sedes por Ciudad de la Sede y Fechas de Entrada y Salida donde se mostrara una lista de Sedes que no tengan habitaciones ocupadas en esas fechas.
    ### Parámetros de Consulta

    | Parámetro | Tipo | Descripción | Ejemplo |
    |-----------|------|-------------|---------|
    | `fechaEntrada` | LocalDateTime | Fecha de entrada | `2024-12-30T22:03:00` |
    | `fechaSalida` | LocalDateTime | Fecha de salida | `2024-12-31T21:10:00` |
    | `nombreCiudad` | String | Nombre de la Ciudad | `cusco` |
  - **Response:**
    ```json
    {
        "message": "Sede encontradas",
        "data": [
            {
                "id": 2,
                "nombre": "Hotel Sol",
                "ciudad": "Cusco",
                "pais": "Perú",
                "direccion": "Calle del Sol 456",
                "estado": "DISPONIBLE"
            }
        ]
    }
    ```

### 4. Habitaciones 
- **Buscar Habitaciones por Filtros**
  - **Request URL:** `GET /habitaciones/sede/{idSede}/buscar-nombre`
  - **Descripción:** Este endpoint permite filtrar habitaciones por el nombre de habitaciones.
    ### Parámetros de Consulta

    | Parámetro | Tipo | Descripción | Ejemplo |
    |-----------|------|-------------|---------|
    | `nombreHabitacion` | String | Nombre de la Habitacion | `paraiso` |
  - **Response:**
    ```json
    {
        "message": "Habitaciones encontradas",
        "data": [
            {
                "id": 9,
                "nombre": "Villa Paraíso",
                "precioNoche": 600.00,
                "capacidadMax": 6,
                "estado": "DISPONIBLE",
                "imagenUrl": "villa_paraiso.jpg",
                "tipoCama": {
                    "id": 5,
                    "nombre": "Super King Size",
                    "descripcion": "Cama de lujo con dimensiones superiores, proporcionando la máxima comodidad y amplitud."
                },
                "tipoHabitacion": {
                    "id": 8,
                    "nombre": "Junior Suite",
                    "descripcion": "Habitación espaciosa con un área de descanso adicional, perfecta para quienes buscan comodidad extra."
                },
                "sede": {
                    "id": 1,
                    "nombre": "Hotel Centro",
                    "ciudad": "Cusco",
                    "pais": "Perú",
                    "direccion": "Av. Central 123",
                    "estado": "DISPONIBLE"
                }
            }
        ]
    }
    ```

### 5. RESERVA 

- **Registrar reserva**
  - **Request URL:** `POST /reservas/crear`
  - **Obligatorio Authentication: Bearer Token del Usuario**
  - **Descripción:** Este endpoint permite registrar una reserva y generar un codigo QR como identificacion del huesped.
  - **Request Body:**
    ```json
    {
        "fechaEntrada": "2024-11-28T22:03:00",
        "fechaSalida": "2024-11-29T21:10:00",
        "cantidadHuespedes": "4",
        "habitacion": 3,
    }
    ```
  - **Response:**
    ```json
    {
        "message": "Reserva creada con éxito",
        "data": "https://imageneshoteleria.blob.core.windows.net/imagenes-usuarios/codQR_2024-11-28T13%3A34%3A11.305075100_R911440.png"
    }
    ```
---
