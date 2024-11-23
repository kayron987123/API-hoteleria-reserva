package pe.com.hotel_api.hotel.presentation.controller;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pe.com.hotel_api.hotel.presentation.dto.ApiResponse;
import pe.com.hotel_api.hotel.presentation.dto.CrearUsuarioRequest;
import pe.com.hotel_api.hotel.presentation.dto.UsuarioDto;
import pe.com.hotel_api.hotel.service.interfaces.AzureBlobService;
import pe.com.hotel_api.hotel.service.interfaces.EmailService;
import pe.com.hotel_api.hotel.service.interfaces.UsuarioService;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;
    private final AzureBlobService azureBlobService;
    private final EmailService emailService;

    @PostMapping("/crear")
    public ResponseEntity<ApiResponse> crearUsuario(@RequestPart(value = "usuario") CrearUsuarioRequest crearUsuarioRequest,
                                                    @RequestPart(value = "imagen", required = false) MultipartFile imagen){
        UsuarioDto usuarioGuardado;

        try {
            if (imagen != null && !imagen.isEmpty()) {
                var imagenGuardada = azureBlobService.cargarImagen(imagen);
                var usuarioNuevo = new CrearUsuarioRequest(crearUsuarioRequest.nombre(),
                        crearUsuarioRequest.apellido(),
                        crearUsuarioRequest.email(),
                        crearUsuarioRequest.contrasena(),
                        crearUsuarioRequest.dni(),
                        imagenGuardada);
                emailService.sendEmail(crearUsuarioRequest.email());
                usuarioGuardado = usuarioService.crearUsuario(usuarioNuevo);
            } else {
                var usuarioNuevo = new CrearUsuarioRequest(
                        crearUsuarioRequest.nombre(),
                        crearUsuarioRequest.apellido(),
                        crearUsuarioRequest.email(),
                        crearUsuarioRequest.contrasena(),
                        crearUsuarioRequest.dni(),
                        "default.jpg"
                );
                usuarioGuardado = usuarioService.crearUsuario(usuarioNuevo);
            }
            return ResponseEntity.ok(new ApiResponse("Usuario creado correctamente", usuarioGuardado));
        }catch (IOException e) {
            return ResponseEntity.badRequest().body(new ApiResponse("Error al recuperar nombre de la imagen: " + e.getMessage(), null));
        }catch (MessagingException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error al enviar el correo: " + e.getMessage(), null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse("Archivo invalido o vacio" + e.getMessage(), null));
        }
    }
}
