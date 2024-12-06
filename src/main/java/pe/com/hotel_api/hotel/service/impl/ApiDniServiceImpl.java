package pe.com.hotel_api.hotel.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pe.com.hotel_api.hotel.service.interfaces.ApiDniService;

@Service
@RequiredArgsConstructor
public class ApiDniServiceImpl implements ApiDniService {
    private static final String URL_REQUEST = "https://api.consultasperu.com/api/v1/query";
    private static final String TOKEN_REQUEST_DNI = "015eaf4a8e509038c44b96b67bae1712d8ba3a65ac991e57b00fdbbdccd041cb";
    private static final String TYPE_DOCUMENT = "dni";
    private final RestTemplate restTemplate;

    @Override
    public String enviarPeticionApiDni(String dni) throws HttpClientErrorException {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("token", TOKEN_REQUEST_DNI);
        body.add("type_document", TYPE_DOCUMENT);
        body.add("document_number", dni);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(URL_REQUEST, request, String.class);
        return response.getBody();
    }

}
