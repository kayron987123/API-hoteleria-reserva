package pe.com.hotel_api.hotel.service.interfaces;

import pe.com.hotel_api.hotel.presentation.dto.CrearUsuarioRequest;
import pe.com.hotel_api.hotel.presentation.dto.UsuarioApiDniResponse;
import pe.com.hotel_api.hotel.presentation.dto.UsuarioDto;

public interface UsuarioService {
    UsuarioDto obtenerUsuarioAutenticado();
    UsuarioDto crearUsuario(UsuarioApiDniResponse UsuarioApiDniResponse);
}
