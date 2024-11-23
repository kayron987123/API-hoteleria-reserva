package pe.com.hotel_api.hotel.presentation.dto;

public record CrearUsuarioRequest(
        String nombre,
        String apellido,
        String email,
        String contrasena,
        String dni,
        String imageUrl
) {
}
