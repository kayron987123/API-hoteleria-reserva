package pe.com.hotel_api.hotel.presentation.controller;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pe.com.hotel_api.hotel.presentation.dto.*;
import pe.com.hotel_api.hotel.service.impl.ApiDniServiceImpl;
import pe.com.hotel_api.hotel.service.interfaces.*;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;
    private final AzureBlobService azureBlobService;
    private final EmailService emailService;
    private final ApiDniService apiDniService;
    private final ConvertirDatosService convertirDatosService;

    private static final String URL_IMAGE_DEFAULT = "https://imageneshoteleria.blob.core.windows.net/imagenes-usuarios/default.png";

    @PostMapping("/crear")
    public ResponseEntity<ApiResponse> crearUsuario(@RequestPart(value = "usuario") CrearUsuarioRequest crearUsuarioRequest,
                                                    @RequestPart(value = "imagen", required = false) MultipartFile imagen){
        UsuarioDto usuarioGuardado;

        //consumir api dni
        var jsonReponse = apiDniService.enviarPeticionApiDni(crearUsuarioRequest.dni());
        if (jsonReponse == null || jsonReponse.isEmpty()){
            return ResponseEntity.badRequest().body(new ApiResponse("Colocar un dni existente", null));
        }
        ApiDniResponse response = convertirDatosService.convertirAObjeto(jsonReponse, ApiDniResponse.class);
        DataDni dataDni = response.dataDni();
        if (dataDni == null){
            return ResponseEntity.badRequest().body(new ApiResponse("Dni no existente", null));
        }

        try {
            if (imagen != null && !imagen.isEmpty()) {
                var imagenGuardada = azureBlobService.cargarImagen(imagen);
                var usuarioNuevo = new UsuarioApiDniResponse(
                        dataDni.nombre(),
                        dataDni.apellido(),
                        crearUsuarioRequest.telefono(),
                        crearUsuarioRequest.email(),
                        crearUsuarioRequest.contrasena(),
                        dataDni.fechaNacimiento(),
                        crearUsuarioRequest.dni(),
                        dataDni.departmento(),
                        dataDni.provincia(),
                        dataDni.distrito(),
                        imagenGuardada
                );
                usuarioGuardado = usuarioService.crearUsuario(usuarioNuevo);
            } else {
                var usuarioNuevo = new UsuarioApiDniResponse(
                        dataDni.nombre(),
                        dataDni.apellido(),
                        crearUsuarioRequest.telefono(),
                        crearUsuarioRequest.email(),
                        crearUsuarioRequest.contrasena(),
                        dataDni.fechaNacimiento(),
                        crearUsuarioRequest.dni(),
                        dataDni.departmento(),
                        dataDni.provincia(),
                        dataDni.distrito(),
                        URL_IMAGE_DEFAULT
                );
                usuarioGuardado = usuarioService.crearUsuario(usuarioNuevo);
            }
            emailService.sendEmail(crearUsuarioRequest.email());
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
