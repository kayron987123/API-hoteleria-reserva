package pe.com.hotel_api.hotel.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.com.hotel_api.hotel.service.interfaces.ConvertirDatosService;

@Service
@RequiredArgsConstructor
public class ConvertirDatosServiceImpl implements ConvertirDatosService {

    private final ObjectMapper objectMapper;

    @Override
    public <T> T convertirAObjeto(String json, Class<T> clase) {
        try {
            return objectMapper.readValue(json, clase);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al convertir el json :" + e.getMessage());
        }
    }
}
