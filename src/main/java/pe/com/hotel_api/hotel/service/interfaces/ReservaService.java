package pe.com.hotel_api.hotel.service.interfaces;

import com.google.zxing.WriterException;
import pe.com.hotel_api.hotel.presentation.dto.CrearReservaRequest;

import java.io.IOException;

public interface ReservaService {
    String crearReserva(CrearReservaRequest crearReservaRequest, String email) throws IOException, WriterException;
    boolean verificarExcedeCantidadHuespedes(Integer cantidadHuespedes, Long idHabitacion);
}
