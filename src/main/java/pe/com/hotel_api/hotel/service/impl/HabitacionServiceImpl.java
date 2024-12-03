package pe.com.hotel_api.hotel.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.com.hotel_api.hotel.persistence.entity.Habitacion;
import pe.com.hotel_api.hotel.persistence.enums.EstadoHabitacion;
import pe.com.hotel_api.hotel.persistence.repository.HabitacionRepository;
import pe.com.hotel_api.hotel.persistence.repository.ReservaRepository;
import pe.com.hotel_api.hotel.persistence.repository.SedeRepository;
import pe.com.hotel_api.hotel.presentation.advice.HabitacionNotFoundException;
import pe.com.hotel_api.hotel.presentation.advice.SedeNotFoundException;
import pe.com.hotel_api.hotel.presentation.dto.HabitacionDto;
import pe.com.hotel_api.hotel.service.interfaces.HabitacionService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HabitacionServiceImpl implements HabitacionService {
    private final HabitacionRepository habitacionRepository;
    private final SedeRepository sedeRepository;

    @Override
    public List<HabitacionDto> buscarHabitacionPorNombre(String nombre, Long idSede) {
        validarSiExisteElIdDeLaSede(idSede);
        List<Habitacion> habitaciones = habitacionRepository.findByNombreContainingIgnoreCaseAndSedeId(nombre, idSede);
        if (habitaciones.isEmpty()) {
            throw new HabitacionNotFoundException("No se encontraron habitaciones con el nombre: " + nombre);
        }
        return devolverListaHabitacionesDto(habitaciones);
    }

    @Override
    public List<HabitacionDto> listarHabitacionPorSede(Long idSede) {
        validarSiExisteElIdDeLaSede(idSede);
        List<Habitacion> habitaciones = habitacionRepository.getHabitacionBySedeId(idSede);
        if (habitaciones.isEmpty()) {
            throw new HabitacionNotFoundException("No se encontraron habitaciones en la sede: " + idSede);
        }

        return devolverListaHabitacionesDto(habitaciones);
    }

    @Override
    public List<HabitacionDto> buscarHabitacionPorTipoCama(String tipoCama, Long idSede) {
        validarSiExisteElIdDeLaSede(idSede);
        List<Habitacion> habitaciones = habitacionRepository.getHabitacionByTipoCamaNombreAndSedeId(tipoCama, idSede);
        if (habitaciones.isEmpty()) {
            throw new HabitacionNotFoundException("No se encontraron habitaciones con el tipo de cama: " + tipoCama);
        }

        return devolverListaHabitacionesDto(habitaciones);
    }

    @Override
    public List<HabitacionDto> buscarHabitacionPorTipoHabitacion(String habitacion, Long idSede) {
        validarSiExisteElIdDeLaSede(idSede);
        List<Habitacion> habitaciones = habitacionRepository.getHabitacionByTipoHabitacionNombreAndSedeId(habitacion, idSede);
        if (habitaciones.isEmpty()) {
            throw new HabitacionNotFoundException("No se encontraron habitaciones con el tipo de habitacion: " + habitacion);
        }

        return devolverListaHabitacionesDto(habitaciones);
    }

    @Override
    public List<HabitacionDto> buscarHabitacionPorRangoPrecio(BigDecimal minPrecio, BigDecimal maxPrecio, Long idSede) {
        validarSiExisteElIdDeLaSede(idSede);
        List<Habitacion> habitaciones = habitacionRepository.getHabitacionByRangoBetweenPrecioAndSedeId(minPrecio, maxPrecio, idSede);
        if (habitaciones.isEmpty()) {
            throw new HabitacionNotFoundException("No se encontraron habitaciones con el rango de precio: " + minPrecio + " - " + maxPrecio);
        }
        return devolverListaHabitacionesDto(habitaciones);
    }

    @Override
    public List<HabitacionDto> buscarHabitacionPorFecha(LocalDateTime fechaEntrada, LocalDateTime fechaSalida, Long idSede) {
        validarSiExisteElIdDeLaSede(idSede);
        List<Habitacion> habitaciones = habitacionRepository.findAvailableRoomsByDatesAndSede(idSede, fechaEntrada, fechaSalida);
        if (habitaciones.isEmpty()) {
            throw new HabitacionNotFoundException("No se encontraron habitaciones disponibles en las fechas: " + fechaEntrada + " - " + fechaSalida);
        }
        return devolverListaHabitacionesDto(habitaciones);
    }

    @Override
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
    public void actualizarEstado(Long idHabitacion) {
        Habitacion habitacion = habitacionRepository.findById(idHabitacion)
                .orElseThrow(() -> new HabitacionNotFoundException("No se encontró la habitación con el id: " + idHabitacion));
        habitacion.setEstado(EstadoHabitacion.RESERVADA);
        habitacionRepository.save(habitacion);
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
        if (!sedeRepository.existsById(idSede)) {
            throw new SedeNotFoundException("No se encontró la sede con el id: " + idSede);
        }
    }
}
