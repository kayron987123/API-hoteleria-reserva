package pe.com.hotel_api.hotel.presentation.controller;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.com.hotel_api.hotel.presentation.advice.AlreadyExistsException;
import pe.com.hotel_api.hotel.presentation.dto.*;
import pe.com.hotel_api.hotel.service.interfaces.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;
    private final AzureBlobService azureBlobService;
    private final EmailService emailService;
    private final ApiDniService apiDniService;
    private final ConvertirDatosService convertirDatosService;
    private final RedisService redisService;
    private final OtpService otpService;

    private static final String URL_IMAGE_DEFAULT = "https://imageneshoteleria.blob.core.windows.net/imagenes-usuarios/default.png";

    @PostMapping("/crear")
    public ResponseEntity<ApiResponse> crearUsuario(@RequestBody @Valid CrearUsuarioRequest crearUsuarioRequest){
        UsuarioApiDniResponse nuevoUsarioTemporal;
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
            usuarioService.existeUsuarioByEmail(crearUsuarioRequest.email());
            usuarioService.existeUsuarioByDni(crearUsuarioRequest.dni());

            nuevoUsarioTemporal = new UsuarioApiDniResponse(
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
                    URL_IMAGE_DEFAULT);

            String key = redisService.guardarUsuarioTemporal(nuevoUsarioTemporal);
            emailService.sendEmailForVerifiUser(crearUsuarioRequest.email(), key);
            return ResponseEntity.ok(new ApiResponse("Usuario guardado temporalmente", new UsuarioRedisDto(key, nuevoUsarioTemporal.nombre(), nuevoUsarioTemporal.apellido(), nuevoUsarioTemporal.email())));
        }catch (AlreadyExistsException | IllegalArgumentException e){
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), null));
        } catch (MessagingException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error al enviar el correo: " + e.getMessage(), null));
        }
    }

    @PostMapping("/validar-otp")
    public ResponseEntity<ApiResponse> validarOtp(@RequestBody @Valid ValidarOtpRequest validarOtpRequest){
        if (!otpService.esValidoOtp(validarOtpRequest.otp())){
            return ResponseEntity.badRequest().body(new ApiResponse("Otp invalido", null));
        }

            UsuarioApiDniResponse usuarioGuardado = redisService.obtenerUsuarioTemporal(validarOtpRequest.otp());
            var usuarioResponse = usuarioService.crearUsuario(usuarioGuardado);
            usuarioService.correoVerificado(usuarioGuardado.email());
            redisService.deleteUsuarioTemporal(validarOtpRequest.otp());
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Otp validado y usuario creado correctamente", usuarioResponse));

    }
}
