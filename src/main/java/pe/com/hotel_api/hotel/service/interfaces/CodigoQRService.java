package pe.com.hotel_api.hotel.service.interfaces;

import com.google.zxing.WriterException;

import java.io.IOException;
import java.time.LocalDateTime;

public interface CodigoQRService {
    String generarCodigoQR(String codigoReserva, String email, LocalDateTime fechaEntrada, LocalDateTime fechaSalida) throws WriterException, IOException;
}
