package pe.com.hotel_api.hotel.presentation.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record CrearReservaRequest(
        @Future(message = "La fecha de entrada debe ser futura")
        LocalDateTime fechaEntrada,

        @Future(message = "La fecha de salida debe ser futura")
        LocalDateTime fechaSalida,

        @NotNull
        @Min(value = 1, message = "La cantidad de huespedes debe ser mayor a 0")
        @Positive(message = "La cantidad de huespedes debe ser mayor a 0")
        Integer cantidadHuespedes,

        @NotNull
        @Min(value = 1, message = "El id de la habitación debe ser mayor a 0")
        @Positive(message = "El id de la habitación debe ser mayor a 0")
        Long habitacion
) {
}
