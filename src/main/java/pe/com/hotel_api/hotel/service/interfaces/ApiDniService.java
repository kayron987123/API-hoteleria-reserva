package pe.com.hotel_api.hotel.service.interfaces;

import org.springframework.web.client.HttpClientErrorException;

public interface ApiDniService {
    String enviarPeticionApiDni(String dni) throws HttpClientErrorException;
}
