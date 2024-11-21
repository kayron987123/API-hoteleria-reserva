package pe.com.hotel_api.hotel.presentation.dto;

public record ApiResponse(
        String message,
        Object data
) {
}
