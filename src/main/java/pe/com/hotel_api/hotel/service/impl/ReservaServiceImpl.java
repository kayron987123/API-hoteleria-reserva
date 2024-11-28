package pe.com.hotel_api.hotel.service.impl;

import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.com.hotel_api.hotel.persistence.entity.Reserva;
import pe.com.hotel_api.hotel.persistence.enums.EstadoReserva;
import pe.com.hotel_api.hotel.persistence.repository.HabitacionRepository;
import pe.com.hotel_api.hotel.persistence.repository.ReservaRepository;
import pe.com.hotel_api.hotel.persistence.repository.SedeRepository;
import pe.com.hotel_api.hotel.persistence.repository.UsuarioRepository;
import pe.com.hotel_api.hotel.presentation.dto.CrearReservaRequest;
import pe.com.hotel_api.hotel.service.interfaces.CodigoQRService;
import pe.com.hotel_api.hotel.service.interfaces.ReservaService;
import pe.com.hotel_api.hotel.service.interfaces.UsuarioService;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class ReservaServiceImpl implements ReservaService {
    private final ReservaRepository reservaRepository;
    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;
    private final SedeRepository sedeRepository;
    private final HabitacionRepository habitacionRepository;
    private final CodigoQRService codigoQRService;
    private static final AtomicInteger counter = new AtomicInteger(1);

    @Override
    public String crearReserva(CrearReservaRequest crearReservaRequest, String email) throws IOException, WriterException {
        var codigoReserva = generarCodigoReserva();

        var reserva = new Reserva();
        reserva.setFechaEntrada(crearReservaRequest.fechaEntrada());
        reserva.setFechaSalida(crearReservaRequest.fechaSalida());
        reserva.setCantidadHuespedes(crearReservaRequest.cantidadHuespedes());
        reserva.setEstado(EstadoReserva.RESERVADA);
        reserva.setUsuario(usuarioRepository.findByEmail(email));
        reserva.setHabitacion(habitacionRepository.getHabitacionById(crearReservaRequest.habitacion()));
        reserva.setSede(sedeRepository.getSedeById(crearReservaRequest.sede()));
        reserva.setPrecioTotal(calcularPrecioTotal(crearReservaRequest.habitacion(), crearReservaRequest.fechaEntrada(), crearReservaRequest.fechaSalida()));
        reserva.setCodigoReserva(codigoReserva);
        String qrCode = codigoQRService.generarCodigoQR(codigoReserva, email, crearReservaRequest.fechaEntrada(), crearReservaRequest.fechaSalida());

        reserva.setCodigoQrUrl(qrCode);
        reservaRepository.save(reserva);
        return qrCode;
    }

    private BigDecimal calcularPrecioTotal(Long id, LocalDateTime fechaEntrada, LocalDateTime fechaSalida) {
        BigDecimal precioNoche = habitacionRepository.getPrecioById(id);
        Duration duration = Duration.between(fechaEntrada, fechaSalida);
        long horas = duration.toDays();
        return precioNoche.multiply(BigDecimal.valueOf(horas));
    }

    private String generarCodigoReserva(){
        // Use SecureRandom for more cryptographically strong random number generation
        SecureRandom random = new SecureRandom();
        // Generate a unique 6-digit number
        int randomSixDigits;
        String codigoReserva;

        do {
            // Generate a random 6-digit number between 100000 and 999999
            randomSixDigits = 100000 + random.nextInt(900000);
            codigoReserva = "R" + randomSixDigits;
        } while (reservaRepository.existsByCodigoReserva(codigoReserva)); // Ensure uniqueness

        return codigoReserva;
    }
}
