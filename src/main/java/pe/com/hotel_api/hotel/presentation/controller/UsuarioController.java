package pe.com.hotel_api.hotel.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pe.com.hotel_api.hotel.presentation.dto.ApiResponse;
import pe.com.hotel_api.hotel.presentation.dto.CrearUsuarioImagenRequest;
import pe.com.hotel_api.hotel.presentation.dto.UsuarioDto;
import pe.com.hotel_api.hotel.service.interfaces.UsuarioService;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;

    @PostMapping("/crear")
    public ResponseEntity<ApiResponse> crearUsuario(@RequestPart(value = "datos") CrearUsuarioImagenRequest crearUsuarioRequest,
                                                    @RequestPart(value = "imagen", required = false) MultipartFile imagen){
        UsuarioDto usuarioGuardado;

        try {
            if (imagen != null && !imagen.isEmpty()) {
                String nombreImagen = imagen.getOriginalFilename();
                if (!usuarioService.tipoArchivo(nombreImagen)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(new ApiResponse("Formato de Imagen no válida", null));
                }
                String imagenGuardada = usuarioService.procesarImagen(imagen);
                if (imagenGuardada == null) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new ApiResponse("Error al guardar la imagen", null));
                }
                var usuarioNuevo = new CrearUsuarioImagenRequest(crearUsuarioRequest.nombre(), crearUsuarioRequest.apellido(),
                        crearUsuarioRequest.telefono(), crearUsuarioRequest.email(), crearUsuarioRequest.contrasena(),
                        crearUsuarioRequest.fechaNacimiento(), crearUsuarioRequest.dni(), crearUsuarioRequest.departamento(),
                        crearUsuarioRequest.provincia(), crearUsuarioRequest.distrito(), imagenGuardada);
                usuarioGuardado = usuarioService.crearUsuarioImagen(usuarioNuevo);
                return ResponseEntity.ok(new ApiResponse("Usuario creado correctamente", usuarioGuardado));
            } else {
                var usuarioNuevo = new CrearUsuarioImagenRequest(
                        crearUsuarioRequest.nombre(),
                        crearUsuarioRequest.apellido(),
                        crearUsuarioRequest.telefono(),
                        crearUsuarioRequest.email(),
                        crearUsuarioRequest.contrasena(),
                        crearUsuarioRequest.fechaNacimiento(),
                        crearUsuarioRequest.dni(),
                        crearUsuarioRequest.departamento(),
                        crearUsuarioRequest.provincia(),
                        crearUsuarioRequest.distrito(),
                        "default.jpg"
                );
                usuarioGuardado = usuarioService.crearUsuarioImagen(usuarioNuevo);
            }
            return ResponseEntity.ok(new ApiResponse("Usuario creado correctamente", usuarioGuardado));
        }catch (IOException e) {
            return ResponseEntity.badRequest().body(new ApiResponse("Error al recuperar nombre de la imagen: " + e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse("Error al crear el usuario: " + e.getMessage(), null));
        }
    }
}
