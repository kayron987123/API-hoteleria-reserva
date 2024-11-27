package pe.com.hotel_api.hotel.service.interfaces;

import pe.com.hotel_api.hotel.presentation.dto.UsuarioApiDniResponse;

public interface RedisService {
    String guardarUsuarioTemporal(UsuarioApiDniResponse usuarioTemporal);
    UsuarioApiDniResponse obtenerUsuarioTemporal(String key);
    void deleteUsuarioTemporal(String key);
}
