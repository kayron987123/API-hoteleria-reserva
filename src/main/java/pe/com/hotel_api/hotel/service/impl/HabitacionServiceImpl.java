package pe.com.hotel_api.hotel.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.com.hotel_api.hotel.persistence.entity.Habitacion;
import pe.com.hotel_api.hotel.persistence.enums.EstadoHabitacion;
import pe.com.hotel_api.hotel.persistence.repository.HabitacionRepository;
import pe.com.hotel_api.hotel.persistence.repository.SedeRepository;
import pe.com.hotel_api.hotel.presentation.advice.HabitacionNotFoundException;
import pe.com.hotel_api.hotel.presentation.advice.SedeNotFoundException;
import pe.com.hotel_api.hotel.presentation.dto.HabitacionDto;
import pe.com.hotel_api.hotel.service.interfaces.HabitacionService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HabitacionServiceImpl implements HabitacionService {
    private final HabitacionRepository habitacionRepository;
    private final SedeRepository sedeRepository;

    @Override
    @Cacheable(value = "listaHabitacionesCache8Parametros", key = "{#nombre, #tipoCama, #tipoHabitacion, #minPrecio, #maxPrecio, #fechaEntrada, #fechaSalida, #idSede}")
    public List<HabitacionDto> buscarHabitaciones(String nombre, String tipoCama, String tipoHabitacion, BigDecimal minPrecio, BigDecimal maxPrecio, LocalDateTime fechaEntrada, LocalDateTime fechaSalida, Long idSede) {
        validarSiExisteElIdDeLaSede(idSede);

        List<Habitacion> habitaciones = habitacionRepository.buscarHabitacionesCombinadas(
                nombre, tipoCama, tipoHabitacion, minPrecio, maxPrecio, fechaEntrada, fechaSalida, idSede
        );
        if (habitaciones.isEmpty()) {
            throw new HabitacionNotFoundException("No se encontraron habitaciones con los filtros ingresados");
        }

        return devolverListaHabitacionesDto(habitaciones);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"listaHabitacionesCache8Parametros", "listaHabitacionesCache2Parametros"}, allEntries = true)
    public void actualizarEstado(Long idHabitacion) {
        Habitacion habitacion = habitacionRepository.findById(idHabitacion)
                .orElseThrow(() -> new HabitacionNotFoundException("No se encontró la habitación con el id: " + idHabitacion));
        habitacion.setEstado(EstadoHabitacion.RESERVADA);
        habitacionRepository.save(habitacion);
    }

    @Override
    @Cacheable(value = "listaHabitacionesCache2Parametros", key = "{#nombreHabitacion, #idSede}")
    public List<HabitacionDto> buscarHabitacionesPorNombreYIdSede(String nombreHabitacion, Long idSede) {
        List<Habitacion> habitaciones = habitacionRepository.findByNombreContainingIgnoreCaseAndSedeIdAndEstado(nombreHabitacion, idSede, EstadoHabitacion.DISPONIBLE);
        if (habitaciones.isEmpty()) {
            throw new HabitacionNotFoundException("No se encontraron habitaciones con el nombre: " + nombreHabitacion);
        }

        return devolverListaHabitacionesDto(habitaciones);
    }

    private List<HabitacionDto> devolverListaHabitacionesDto(List<Habitacion> habitaciones) {
        return habitaciones.stream()
                .map(habitacion -> new HabitacionDto(
                        habitacion.getId(),
                        habitacion.getNombre(),
                        habitacion.getPrecioNoche(),
                        habitacion.getCapacidadMax(),
                        habitacion.getEstado(),
                        habitacion.getImagenUrl(),
                        habitacion.getTipoCama(),
                        habitacion.getTipoHabitacion(),
                        habitacion.getSede()
                )).toList();
    }

    private void validarSiExisteElIdDeLaSede(Long idSede) {
        if (idSede == 0) {
            return;
        }
        if (!sedeRepository.existsById(idSede)) {
            throw new SedeNotFoundException("No se encontró la sede con el id: " + idSede);
        }
    }
}
