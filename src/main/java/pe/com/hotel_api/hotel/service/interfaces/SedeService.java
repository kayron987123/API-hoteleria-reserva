package pe.com.hotel_api.hotel.service.interfaces;

import pe.com.hotel_api.hotel.presentation.dto.SedeDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface SedeService {
    List<SedeDto> buscarSedePorFiltroDeFechasYHoraYNombre(LocalDateTime fechaEntrada, LocalDateTime fechaSalida, String nombreCiudad);
    void validacionesSedeFechaYnombre(LocalDateTime fechaEntrada, LocalDateTime fechaSalida, String nombreCiudad);
}
