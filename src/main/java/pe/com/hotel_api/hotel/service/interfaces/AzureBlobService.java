package pe.com.hotel_api.hotel.service.interfaces;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AzureBlobService {
    String cargarImagen(MultipartFile file) throws IOException;
}
