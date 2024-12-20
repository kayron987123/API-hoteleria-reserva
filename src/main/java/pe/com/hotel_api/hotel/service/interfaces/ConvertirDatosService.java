package pe.com.hotel_api.hotel.service.interfaces;

public interface ConvertirDatosService {
    <T> T convertirAObjeto(String json, Class<T> clase);
}
