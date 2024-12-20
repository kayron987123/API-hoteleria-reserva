package pe.com.hotel_api.hotel.presentation.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ApiDniResponse(
        @JsonAlias("message")
        String mensaje,
        @JsonAlias("data")
        DataDni dataDni
) {
}
