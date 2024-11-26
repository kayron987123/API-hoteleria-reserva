package pe.com.hotel_api.hotel.presentation.dto;

import java.time.LocalDate;

public record UsuarioApiDniResponse(
        String nombre,
        String apellido,
        String telefono,
        String email,
        String contrasena,
        LocalDate fechaNacimiento,
        String dni,
        String departamento,
        String provincia,
        String distrito,
        String imageUrl
) {
}
