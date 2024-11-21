package pe.com.hotel_api.hotel.presentation.dto;


import java.time.LocalDate;

public record CrearUsuarioRequest(
        String nombre,
        String apellido,
        String telefono,
        String email,
        String contrasena,
        LocalDate fechaNacimiento,
        String dni,
        String departamento,
        String provincia,
        String distrito
) {
}
