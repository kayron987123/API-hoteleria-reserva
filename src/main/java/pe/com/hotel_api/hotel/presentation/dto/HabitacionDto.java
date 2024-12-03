package pe.com.hotel_api.hotel.presentation.dto;

import pe.com.hotel_api.hotel.persistence.entity.Sede;
import pe.com.hotel_api.hotel.persistence.entity.TipoCama;
import pe.com.hotel_api.hotel.persistence.entity.TipoHabitacion;
import pe.com.hotel_api.hotel.persistence.enums.EstadoHabitacion;

import java.math.BigDecimal;

public record HabitacionDto(
        Long id,
        String nombre,
        BigDecimal precioNoche,
        Integer capacidadMax,
        EstadoHabitacion estado,
        String imagenUrl,
        TipoCama tipoCama,
        TipoHabitacion tipoHabitacion,
        Sede sede
) {
}
