package pe.com.hotel_api.hotel.service.interfaces;

import org.springframework.web.multipart.MultipartFile;
import pe.com.hotel_api.hotel.presentation.dto.CrearUsuarioImagenRequest;
import pe.com.hotel_api.hotel.presentation.dto.UsuarioDto;

import java.io.IOException;

public interface UsuarioService {
    UsuarioDto obtenerUsuarioAutenticado();
    UsuarioDto crearUsuarioImagen(CrearUsuarioImagenRequest crearUsuarioImagenRequest);
    String procesarImagen(MultipartFile imagen) throws IOException;
    boolean tipoArchivo(String nombreImagen);
}
