package pe.com.hotel_api.hotel.service.interfaces;

import pe.com.hotel_api.hotel.presentation.dto.HabitacionDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface HabitacionService {
    List<HabitacionDto> buscarHabitacionPorNombre(String nombre, Long idSede);

    List<HabitacionDto> listarHabitacionPorSede(Long idSede);

    List<HabitacionDto> buscarHabitacionPorTipoCama(String tipoCama, Long idSede);

    List<HabitacionDto> buscarHabitacionPorTipoHabitacion(String habitacion, Long idSede);

    List<HabitacionDto> buscarHabitacionPorRangoPrecio(BigDecimal minPrecio, BigDecimal maxPrecio, Long idSede);

    List<HabitacionDto> buscarHabitacionPorFecha(LocalDateTime fechaEntrada, LocalDateTime fechaSalida, Long idSede);

    List<HabitacionDto> buscarHabitaciones(String nombre, String tipoCama, String tipoHabitacion,
                                           BigDecimal minPrecio, BigDecimal maxPrecio,
                                           LocalDateTime fechaEntrada, LocalDateTime fechaSalida, Long idSede);

    void actualizarEstado(Long idHabitacion);
}
