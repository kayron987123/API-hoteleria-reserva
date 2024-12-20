package pe.com.hotel_api.hotel.presentation.dto;

public record UsuarioDto(
        Long id,
        String nombre,
        String apellido,
        String email) {
}
