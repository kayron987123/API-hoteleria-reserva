package pe.com.hotel_api.hotel.presentation.dto;


public record LoginRequest(
        String email,
        String contrasena
) {
}
