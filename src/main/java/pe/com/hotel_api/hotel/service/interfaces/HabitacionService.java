package pe.com.hotel_api.hotel.service.interfaces;

import pe.com.hotel_api.hotel.presentation.dto.HabitacionDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface HabitacionService {
    List<HabitacionDto> buscarHabitaciones(String nombre, String tipoCama, String tipoHabitacion,
                                           BigDecimal minPrecio, BigDecimal maxPrecio,
                                           LocalDateTime fechaEntrada, LocalDateTime fechaSalida, Long idSede);

    void actualizarEstado(Long idHabitacion);

    List<HabitacionDto> buscarHabitacionesPorNombreYIdSede(String nombreHabitacion, Long idSede);
}
