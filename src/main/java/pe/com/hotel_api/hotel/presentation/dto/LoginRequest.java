package pe.com.hotel_api.hotel.presentation.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank(message = "El email no debe estar vacío")
        @Email(message = "El email debe ser válido")
        String email,

        @NotBlank(message = "La contraseña no debe estar vacía")
        @Size(min = 5, max = 20, message = "La contraseña debe tener entre 5 y 20 caracteres")
        @Pattern(regexp = "^[A-Za-z0-9]+$", message = "El código solo puede contener letras mayúsculas, minúsculas y números")
        String contrasena
) {
}
