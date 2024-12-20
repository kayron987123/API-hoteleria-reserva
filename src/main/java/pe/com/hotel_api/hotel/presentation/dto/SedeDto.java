package pe.com.hotel_api.hotel.presentation.dto;

public record SedeDto(Long id,
                      String nombre,
                      String ciudad,
                      String pais,
                      String direccion,
                      String estado
) {
}
