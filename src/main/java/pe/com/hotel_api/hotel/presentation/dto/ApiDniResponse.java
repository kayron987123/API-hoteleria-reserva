package pe.com.hotel_api.hotel.presentation.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ApiDniResponse(
        @JsonAlias("data")
        DataDni dataDni
) {
}
