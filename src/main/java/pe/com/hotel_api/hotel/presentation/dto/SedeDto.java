package pe.com.hotel_api.hotel.presentation.dto;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import pe.com.hotel_api.hotel.persistence.enums.EstadoSede;

public record SedeDto(Long id,
                      String nombre,
                      String ciudad,
                      String pais,
                      String direccion,
                      String estado
) {
}
