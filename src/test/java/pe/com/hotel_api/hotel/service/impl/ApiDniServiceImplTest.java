package pe.com.hotel_api.hotel.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ApiDniServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    ApiDniServiceImpl apiDniService;

    private static final String MOCK_RESPONSE = "{\"success\":true,\"message\":\"Successful response\",\"data\":{\"number\":\"76930830\"," +
            "\"full_name\":\"GAD JOSUE ALVA CASTROMONTE\",\"name\":\"GAD JOSUE\",\"surname\":\"ALVA CASTROMONTE\"," +
            "\"verification_code\":null,\"date_of_birth\":\"2002-01-02\",\"department\":\"LIMA\",\"province\":\"LIMA\"," +
            "\"district\":\"SAN JUAN DE LURIGANCHO\",\"address\":\"ASOC LOS PINOS MZ. K LT. 01\",\"ubigeo\":\"140137\"}}";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void enviarPeticionApiDni() {
        ResponseEntity<String> mockResponse = new ResponseEntity<>(MOCK_RESPONSE, HttpStatus.OK);
        when(restTemplate.postForEntity(any(String.class), any(HttpEntity.class), eq(String.class))).thenReturn(mockResponse);

        String response = apiDniService.enviarPeticionApiDni("76930830");

        assertNotNull(response);
        assertEquals(MOCK_RESPONSE, response);

        verify(restTemplate, times(1)).postForEntity(any(String.class), any(HttpEntity.class), eq(String.class));
    }
}