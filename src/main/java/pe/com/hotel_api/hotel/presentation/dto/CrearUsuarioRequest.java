package pe.com.hotel_api.hotel.presentation.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CrearUsuarioRequest(
        @NotBlank
        @Size(min = 9, max = 9, message = "El telefono solo debe tener 9 caracteres")
        String telefono,

        @NotBlank
        @Email
        String email,

        @NotBlank
        @Size(min = 5, max = 20, message = "La contraseña debe tener entre 5 y 20 caracteres")
        @Pattern(regexp = "^[A-Za-z0-9]+$", message = "El código solo puede contener letras mayúsculas, minúsculas y números")
        String contrasena,

        @NotBlank
        @Size(min = 8, max = 8, message = "El dni solo debe tener 8 caracteres")
        String dni
) {
}
