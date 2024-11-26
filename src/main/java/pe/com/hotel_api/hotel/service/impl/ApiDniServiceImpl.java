package pe.com.hotel_api.hotel.service.impl;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import pe.com.hotel_api.hotel.service.interfaces.ApiDniService;

@Service
public class ApiDniServiceImpl implements ApiDniService {

    private static final String URL_REQUEST = "https://api.consultasperu.com/api/v1/query";
    private static final String TOKEN_REQUEST_DNI = "7a37c6d81c6a67b8c65ac0ee9b1df7440cfb1f614260fc7060e3abe01b17704d";
    private static final String TYPE_DOCUMENT = "dni";

    @Override
    public String enviarPeticionApiDni(String dni) {
        // Crear los datos en formato MultiValueMap
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("token", TOKEN_REQUEST_DNI);
        body.add("type_document", TYPE_DOCUMENT);
        body.add("document_number", dni);

        // Configurar los encabezados
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Crear la entidad HTTP
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        // Enviar la solicitud POST con RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.postForEntity(URL_REQUEST, request, String.class);
        return response.getBody();
    }

}
