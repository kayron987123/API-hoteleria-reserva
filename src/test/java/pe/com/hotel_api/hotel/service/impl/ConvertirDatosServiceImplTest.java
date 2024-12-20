package pe.com.hotel_api.hotel.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConvertirDatosServiceImplTest {

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private ConvertirDatosServiceImpl convertirDatosService;

    @Test
    void convertirAObjeto() throws JsonProcessingException {
        String json = "{\"success\":true,\"message\":\"Successful response\",\"data\":{\"number\":\"76930830\"," +
                "\"full_name\":\"GAD JOSUE ALVA CASTROMONTE\",\"name\":\"GAD JOSUE\",\"surname\":\"ALVA CASTROMONTE\"," +
                "\"verification_code\":null,\"date_of_birth\":\"2002-01-02\",\"department\":\"LIMA\",\"province\":\"LIMA\"," +
                "\"district\":\"SAN JUAN DE LURIGANCHO\",\"address\":\"ASOC LOS PINOS MZ. K LT. 01\",\"ubigeo\":\"140137\"}}";
        when(objectMapper.readValue(json, Object.class)).thenReturn(new Object());

        Object response = convertirDatosService.convertirAObjeto(json, Object.class);

        assertNotNull(response);
        verify(objectMapper, times(1)).readValue(json, Object.class);
    }

    @Test
    void convertirAObjetoException() throws JsonProcessingException {
        String json = "{\"success\":true,\"message\":\"Successful response\",\"data\":{\"number\":\"76930830\"," +
                "\"full_name\":\"GAD JOSUE ALVA CASTROMONTE\",\"name\":\"GAD JOSUE\",\"surname\":\"ALVA CASTROMONTE\"," +
                "\"verification_code\":null,\"date_of_birth\":\"2002-01-02\",\"department\":\"LIMA\",\"province\":\"LIMA\"," +
                "\"district\":\"SAN JUAN DE LURIGANCHO\",\"address\":\"ASOC LOS PINOS MZ. K LT. 01\",\"ubigeo\":\"140137\"}}";

        when(objectMapper.readValue(json, Object.class)).thenThrow(new JsonProcessingException("Error de formato") {
        });

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
            convertirDatosService.convertirAObjeto(json, Object.class));

        assertEquals("Error al convertir el json :Error de formato", exception.getMessage());
        verify(objectMapper, times(1)).readValue(json, Object.class);
    }
}