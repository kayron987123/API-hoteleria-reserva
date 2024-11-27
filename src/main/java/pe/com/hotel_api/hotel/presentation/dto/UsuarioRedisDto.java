package pe.com.hotel_api.hotel.presentation.dto;

public record UsuarioRedisDto(
        String key,
        String nombre,
        String apellido,
        String email
) {
}
