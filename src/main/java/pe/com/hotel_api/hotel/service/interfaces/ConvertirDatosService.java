package pe.com.hotel_api.hotel.service.interfaces;

import pe.com.hotel_api.hotel.presentation.dto.CrearUsuarioRequest;

public interface ConvertirDatosService {
    <T> T convertirAObjeto(String json, Class<T> clase);
    String conertirAJson(CrearUsuarioRequest crearUsuarioRequest);
}
