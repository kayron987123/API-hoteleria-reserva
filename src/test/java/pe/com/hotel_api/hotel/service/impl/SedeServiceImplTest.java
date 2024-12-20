package pe.com.hotel_api.hotel.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.hotel_api.hotel.persistence.entity.Sede;
import pe.com.hotel_api.hotel.persistence.enums.EstadoSede;
import pe.com.hotel_api.hotel.persistence.repository.SedeRepository;
import pe.com.hotel_api.hotel.presentation.advice.IllegalArgumentException;
import pe.com.hotel_api.hotel.presentation.advice.SedeNotFoundException;
import pe.com.hotel_api.hotel.presentation.dto.SedeDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SedeServiceImplTest {

    @Mock
    private SedeRepository sedeRepository;

    @InjectMocks
    private SedeServiceImpl sedeService;

    private final List<Sede> sedes = new ArrayList<>();

    @BeforeEach
    void setUp() {
        Sede sede = new Sede(1L, "Sede 1", "Lima", "Peru", "Direccion 1", EstadoSede.DISPONIBLE);
        sedes.add(sede);
    }

    @Test
    void buscarSedePorFiltroDeFechasYHoraYNombre() {
        when(sedeRepository.buscarSedePorFechaYHoraYNombreDondeHabitacionesNoEstenReservadas(any(), any(), anyString())).thenReturn(sedes);

        List<SedeDto> sedesResponse = sedeService.buscarSedePorFiltroDeFechasYHoraYNombre(LocalDateTime.now().plusHours(2), LocalDateTime.now().plusDays(2), "Lima");

        assertNotNull(sedesResponse);
        assertEquals(1, sedesResponse.size());
        verify(sedeRepository, times(1)).buscarSedePorFechaYHoraYNombreDondeHabitacionesNoEstenReservadas(any(), any(), anyString());
    }

    @Test
    void buscarSedePorFiltroDeFechasYHoraYNombreEmpty() {
        when(sedeRepository.buscarSedePorFechaYHoraYNombreDondeHabitacionesNoEstenReservadas(any(), any(), anyString())).thenReturn(Collections.emptyList());

        assertThrows(SedeNotFoundException.class,
                () -> sedeService.buscarSedePorFiltroDeFechasYHoraYNombre(LocalDateTime.now().plusHours(2), LocalDateTime.now().plusDays(2), "Lima"));

        verify(sedeRepository, times(1)).buscarSedePorFechaYHoraYNombreDondeHabitacionesNoEstenReservadas(any(), any(), anyString());
    }

    @Test
    void validacionesSedeFechaYnombre() {
        when(sedeRepository.existsByCiudadContainingIgnoreCase(anyString())).thenReturn(true);

        assertDoesNotThrow(() -> sedeService.validacionesSedeFechaYnombre(LocalDateTime.now().plusHours(2), LocalDateTime.now().plusDays(2), "Lima"));

        verify(sedeRepository, times(1)).existsByCiudadContainingIgnoreCase(anyString());
    }

    @Test
    void validacionesSedeFechaYNombreException() {
        when(sedeRepository.existsByCiudadContainingIgnoreCase(anyString())).thenReturn(false);

        SedeNotFoundException exception = assertThrows(SedeNotFoundException.class,
                () -> sedeService.validacionesSedeFechaYnombre(LocalDateTime.now().plusHours(2), LocalDateTime.now().plusDays(2), "Lima"));

        verify(sedeRepository, times(1)).existsByCiudadContainingIgnoreCase(anyString());
        assertEquals("No se encontró la sede con el nombre: Lima", exception.getMessage());
    }

    @Test
    void validacionesSedeFechaYNombreExceptionFechaEntradaEsPasada() {
        when(sedeRepository.existsByCiudadContainingIgnoreCase(anyString())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> sedeService.validacionesSedeFechaYnombre(LocalDateTime.now().minusDays(2), LocalDateTime.now().plusDays(2), "Lima"));

        verify(sedeRepository, times(1)).existsByCiudadContainingIgnoreCase(anyString());
        assertEquals("La fecha de entrada debe ser futura.", exception.getMessage());
    }

    @Test
    void validacionesSedeFechaYNombreExceptionFechaSalidaEsPasada() {
        when(sedeRepository.existsByCiudadContainingIgnoreCase(anyString())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> sedeService.validacionesSedeFechaYnombre(LocalDateTime.now().plusHours(2), LocalDateTime.now().minusDays(2), "Lima"));

        verify(sedeRepository, times(1)).existsByCiudadContainingIgnoreCase(anyString());
        assertEquals("La fecha de salida debe ser futura.", exception.getMessage());
    }

    @Test
    void validacionesSedeFechaYNombreExceptionFechaEntradaEsFuturaAFechaSalida() {
        when(sedeRepository.existsByCiudadContainingIgnoreCase(anyString())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> sedeService.validacionesSedeFechaYnombre(LocalDateTime.now().plusHours(2), LocalDateTime.now().plusHours(1), "Lima"));

        verify(sedeRepository, times(1)).existsByCiudadContainingIgnoreCase(anyString());
        assertEquals("La fecha de entrada no puede ser posterior a la fecha de salida.", exception.getMessage());
    }

    @Test
    void validacionesSedeFechaYNombreExceptionNombreCiudadLenghtMenor1OMayor20() {
        when(sedeRepository.existsByCiudadContainingIgnoreCase(anyString())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> sedeService.validacionesSedeFechaYnombre(LocalDateTime.now().plusHours(2), LocalDateTime.now().plusDays(2), "1234567891011121314151aa6"));

        verify(sedeRepository, times(1)).existsByCiudadContainingIgnoreCase(anyString());
        assertEquals("El nombre de la sede debe tener entre 1 y 20 caracteres.", exception.getMessage());
    }
}