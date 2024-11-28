package pe.com.hotel_api.hotel.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ValidarOtpRequest (
        @NotBlank
        @Size(min = 10, max = 10, message = "El código de verificación debe tener 10 caracteres")
        @Pattern(regexp = "^[A-Za-z0-9!@#$%^&*-]+$", message = "El código solo puede contener letras, números y los caracteres especiales !@#$%^&*-")
        String otp
){
}
