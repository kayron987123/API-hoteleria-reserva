package pe.com.hotel_api.hotel.presentation.dto;

import java.time.LocalDate;

public record UsuarioDto(
        Long id,
        String nombre,
        String apellido,
        String telefono,
        String email,
        LocalDate fechaNacimiento,
        String departamento,
        String provincia,
        String distrito,
        String imageUrl) {
}
