package pe.com.hotel_api.hotel.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.hotel_api.hotel.persistence.entity.Habitacion;
import pe.com.hotel_api.hotel.persistence.entity.Sede;
import pe.com.hotel_api.hotel.persistence.entity.TipoCama;
import pe.com.hotel_api.hotel.persistence.entity.TipoHabitacion;
import pe.com.hotel_api.hotel.persistence.enums.EstadoHabitacion;
import pe.com.hotel_api.hotel.persistence.enums.EstadoSede;
import pe.com.hotel_api.hotel.persistence.repository.HabitacionRepository;
import pe.com.hotel_api.hotel.persistence.repository.SedeRepository;
import pe.com.hotel_api.hotel.presentation.advice.HabitacionNotFoundException;
import pe.com.hotel_api.hotel.presentation.advice.SedeNotFoundException;
import pe.com.hotel_api.hotel.presentation.dto.HabitacionDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HabitacionServiceImplTest {

    @Mock
    private HabitacionRepository habitacionRepository;

    @Mock
    private SedeRepository sedeRepository;

    @InjectMocks
    private HabitacionServiceImpl habitacionService;

    private Habitacion habitacion;

    private final List<Habitacion> habitaciones = new ArrayList<>();

    @BeforeEach
    void setUp() {
        Sede sede = new Sede(1L, "Sede 1", "Lima", "Peru", "Los Ciruelos", EstadoSede.DISPONIBLE);
        TipoHabitacion tipoHabitacion = new TipoHabitacion(1L, "Tipo 1", "Descripcion 1");
        TipoCama tipoCama = new TipoCama(1L, "Tipo 1", "Descripcion 1");
        habitacion = new Habitacion(1L, "Habitacion 1", BigDecimal.valueOf(12.5), 1, EstadoHabitacion.DISPONIBLE, "imagen.png", tipoCama, tipoHabitacion, sede);
        habitaciones.add(habitacion);
        habitacion = new Habitacion(1L, "Habitacion 1", BigDecimal.valueOf(12.5), 1, EstadoHabitacion.DISPONIBLE, "imagen.png", tipoCama, tipoHabitacion, sede);
        habitaciones.add(habitacion);
    }

    @Test
    void buscarHabitaciones() {
        when(sedeRepository.existsById(anyLong())).thenReturn(true);
        when(habitacionRepository.buscarHabitacionesCombinadas(any(), any(), any(), any(), any(), any(), any(), anyLong())).thenReturn(habitaciones);

        List<HabitacionDto> result = habitacionService.buscarHabitaciones("Habitacion 1", "Tipo1", "Tipo 1", new BigDecimal(10), new BigDecimal(20), LocalDateTime.now(), LocalDateTime.now(), 1L);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
        var nombre = result.getFirst().nombre();
        assertEquals(habitacion.getNombre(), nombre);
    }

    @Test
    void buscarHabitacionesEmpty() {
        when(sedeRepository.existsById(anyLong())).thenReturn(true);
        when(habitacionRepository.buscarHabitacionesCombinadas(any(), any(), any(), any(), any(), any(), any(), anyLong())).thenReturn(Collections.emptyList());

        assertThrows(HabitacionNotFoundException.class,
                () -> habitacionService.buscarHabitaciones("Inexistente", "Tipo2",
                        "Tipo 2", BigDecimal.valueOf(10), BigDecimal.valueOf(20), LocalDateTime.now(), LocalDateTime.now(), 1L));
    }

    @Test
    void buscarHabitacionesException() {
        when(sedeRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(SedeNotFoundException.class,
                () -> habitacionService.buscarHabitaciones("Habitacion 1", "Tipo1",
                        "Tipo 1", BigDecimal.valueOf(10), BigDecimal.valueOf(20), LocalDateTime.now(), LocalDateTime.now(), 2L));
    }

    @Test
    void actualizarEstado() {
        when(habitacionRepository.findById(anyLong())).thenReturn(Optional.of(habitacion));

        habitacionService.actualizarEstado(1L);

        assertEquals(EstadoHabitacion.RESERVADA, habitacion.getEstado());
        verify(habitacionRepository, times(1)).save(any(Habitacion.class));
    }

    @Test
    void actualizarEstadoException() {
        when(habitacionRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(HabitacionNotFoundException.class, () -> habitacionService.actualizarEstado(5L));
    }

    @Test
    void buscarHabitacionesPorNombreYIdSede() {
        when(habitacionRepository.findByNombreContainingIgnoreCaseAndSedeIdAndEstado(any(), anyLong(), any())).thenReturn(habitaciones);

        List<HabitacionDto> result = habitacionService.buscarHabitacionesPorNombreYIdSede("Habitacion 1", 1L);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
        var nombre = result.getFirst().nombre();
        assertEquals(habitacion.getNombre(), nombre);
    }

    @Test
    void buscarHabitacionesPorNombreYIdSedeException() {
        when(habitacionRepository.findByNombreContainingIgnoreCaseAndSedeIdAndEstado(any(), anyLong(), any())).thenReturn(Collections.emptyList());

        assertThrows(HabitacionNotFoundException.class, () -> habitacionService.buscarHabitacionesPorNombreYIdSede("Habitacion 5", 1L));
    }
}