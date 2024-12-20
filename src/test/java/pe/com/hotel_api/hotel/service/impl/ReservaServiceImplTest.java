package pe.com.hotel_api.hotel.service.impl;

import com.google.zxing.WriterException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.hotel_api.hotel.persistence.entity.Habitacion;
import pe.com.hotel_api.hotel.persistence.entity.Reserva;
import pe.com.hotel_api.hotel.persistence.entity.Usuario;
import pe.com.hotel_api.hotel.persistence.repository.HabitacionRepository;
import pe.com.hotel_api.hotel.persistence.repository.ReservaRepository;
import pe.com.hotel_api.hotel.persistence.repository.UsuarioRepository;
import pe.com.hotel_api.hotel.presentation.advice.AlreadyExistsException;
import pe.com.hotel_api.hotel.presentation.dto.CrearReservaRequest;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservaServiceImplTest {
    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private HabitacionRepository habitacionRepository;

    @Mock
    private CodigoQRServiceImpl codigoQRService;

    @InjectMocks
    private ReservaServiceImpl reservaService;

    @Test
    void crearReserva() throws IOException, WriterException {
        var usuario = new Usuario(1L, "nombre", "apellido", "987654321", "email@gmail.com", "password", LocalDate.now(),
                "dni", "departamento", "provincia", "distrito", null, null, null, null, null);
        var habitacion = new Habitacion(1L, "habitacion", BigDecimal.valueOf(50.5), 2, null, null, null, null, null);
        var reservaRequest = new CrearReservaRequest(LocalDateTime.now().plusHours(1), LocalDateTime.now().plusDays(2), 2, 1L);
        var expectedQrCode = "http://url";

        when(reservaRepository.existeReservaSolapada(any(), any())).thenReturn(false);
        when(usuarioRepository.findByEmail(anyString())).thenReturn(usuario);
        when(habitacionRepository.getHabitacionById(anyLong())).thenReturn(habitacion);
        when(habitacionRepository.getPrecioById(anyLong())).thenReturn(BigDecimal.valueOf(50.5));
        when(codigoQRService.generarCodigoQR(anyString(), anyString(), any(), any())).thenReturn("http://url");
        when(reservaRepository.existsByCodigoReserva(anyString())).thenReturn(false);

        var qrCode = reservaService.crearReserva(reservaRequest, "email@gmail.com");

        assertNotNull(qrCode);
        assertEquals(expectedQrCode, qrCode);
        verify(reservaRepository, times(1)).save(any(Reserva.class));
        verify(reservaRepository, times(1)).existeReservaSolapada(any(), any());
        verify(usuarioRepository, times(1)).findByEmail(anyString());
        verify(habitacionRepository, times(1)).getHabitacionById(anyLong());
        verify(habitacionRepository, times(1)).getPrecioById(anyLong());
        verify(codigoQRService, times(1)).generarCodigoQR(anyString(), anyString(), any(), any());
        verify(reservaRepository, times(1)).existsByCodigoReserva(anyString());
    }

    @Test
    void crearReservaException() {
        var reservaRequest = new CrearReservaRequest(LocalDateTime.now().plusHours(1), LocalDateTime.now().plusDays(2), 2, 1L);

        when(reservaRepository.existeReservaSolapada(any(), any())).thenReturn(true);

        AlreadyExistsException exception = assertThrows(AlreadyExistsException.class, () ->
            reservaService.crearReserva(reservaRequest, "email@gmail.com"));

        assertEquals("Ya existe una reserva en esas fechas", exception.getMessage());
        verify(reservaRepository, times(1)).existeReservaSolapada(any(), any());
        verify(reservaRepository, never()).save(any(Reserva.class));
    }

    @Test
    void verificarExcedeCantidadHuespedes() {
        when(habitacionRepository.getCapacidadById(anyLong())).thenReturn(3);

        boolean result = reservaService.verificarExcedeCantidadHuespedes(3, 1L);

        assertFalse(result);
        verify(habitacionRepository, times(1)).getCapacidadById(anyLong());
    }

    @Test
    void verificarExcedeCantidadHuespedesSiExcede() {
        when(habitacionRepository.getCapacidadById(anyLong())).thenReturn(3);

        boolean result = reservaService.verificarExcedeCantidadHuespedes(4, 1L);

        assertTrue(result);
        verify(habitacionRepository, times(1)).getCapacidadById(anyLong());
    }
}