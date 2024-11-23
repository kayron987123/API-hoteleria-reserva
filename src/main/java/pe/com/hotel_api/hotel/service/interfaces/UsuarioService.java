package pe.com.hotel_api.hotel.service.interfaces;

import org.springframework.web.multipart.MultipartFile;
import pe.com.hotel_api.hotel.presentation.dto.CrearUsuarioRequest;
import pe.com.hotel_api.hotel.presentation.dto.UsuarioDto;

import java.io.IOException;

public interface UsuarioService {
    UsuarioDto obtenerUsuarioAutenticado();
    UsuarioDto crearUsuario(CrearUsuarioRequest crearUsuarioRequest);
}
