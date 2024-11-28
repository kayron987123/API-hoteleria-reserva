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
import pe.com.hotel_api.hotel.presentation.advice.AlreadyExistsException;
import pe.com.hotel_api.hotel.presentation.dto.CrearReservaRequest;
import pe.com.hotel_api.hotel.service.interfaces.CodigoQRService;
import pe.com.hotel_api.hotel.service.interfaces.ReservaService;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReservaServiceImpl implements ReservaService {
    private final ReservaRepository reservaRepository;
    private final UsuarioRepository usuarioRepository;
    private final SedeRepository sedeRepository;
    private final HabitacionRepository habitacionRepository;
    private final CodigoQRService codigoQRService;

    @Override
    public String crearReserva(CrearReservaRequest crearReservaRequest, String email) throws IOException, WriterException {

        if (existeReserva(crearReservaRequest.fechaEntrada(), crearReservaRequest.fechaSalida())) {
            throw new AlreadyExistsException("Ya existe una reserva en esas fechas");
        }

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
        Long noches = calcularNoches(fechaEntrada, fechaSalida);
        return precioNoche.multiply(BigDecimal.valueOf(noches));
    }

    private String generarCodigoReserva(){
        SecureRandom random = new SecureRandom();
        int randomSixDigits;
        String codigoReserva;

        do {
            randomSixDigits = 100000 + random.nextInt(900000);
            codigoReserva = "R" + randomSixDigits;
        } while (reservaRepository.existsByCodigoReserva(codigoReserva));

        return codigoReserva;
    }

    private boolean existeReserva(LocalDateTime fechaEntrada, LocalDateTime fechaSalida){
        return reservaRepository.existeReservaSolapada(fechaEntrada, fechaSalida);
    }

    private Long calcularNoches(LocalDateTime fechaEntrada, LocalDateTime fechaSalida){
        LocalDateTime inicioEntrada = fechaEntrada.withHour(14).withMinute(0).withSecond(0);
        LocalDateTime inicioSalida = fechaSalida.withHour(12).withMinute(0).withSecond(0);

        if (fechaEntrada.isBefore(inicioEntrada)){
            fechaEntrada = inicioEntrada;
        }
        if (fechaSalida.isAfter(inicioSalida)){
            fechaSalida = inicioSalida;
        }

        Long noches = Duration.between(fechaEntrada.toLocalDate().atStartOfDay(), fechaSalida.toLocalDate().atStartOfDay()).toDays();

        if (fechaSalida.isAfter(fechaSalida.toLocalDate().atTime(12, 0))){
            noches++;
        }
        return noches;
    }
}
