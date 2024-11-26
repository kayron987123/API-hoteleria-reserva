package pe.com.hotel_api.hotel.presentation.dto;

public record CrearUsuarioRequest(
        String telefono,
        String email,
        String contrasena,
        String dni
) {
}
