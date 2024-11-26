package pe.com.hotel_api.hotel.presentation.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DataDni(
        @JsonAlias("name")
        String nombre,
        @JsonAlias("surname")
        String apellido,
        @JsonAlias("date_of_birth")
        @JsonFormat(pattern="yyyy-MM-dd")
        LocalDate fechaNacimiento,
        @JsonAlias("department")
        String departmento,
        @JsonAlias("province")
        String provincia,
        @JsonAlias("district")
        String distrito
) {
}
