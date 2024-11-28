package pe.com.hotel_api.hotel.presentation.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record CrearReservaRequest(
        LocalDateTime fechaEntrada,
        LocalDateTime fechaSalida,
        Integer cantidadHuespedes,
        Long habitacion,
        Long sede
) {
}
