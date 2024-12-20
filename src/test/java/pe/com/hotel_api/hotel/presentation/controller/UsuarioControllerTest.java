package pe.com.hotel_api.hotel.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import pe.com.hotel_api.hotel.presentation.advice.AlreadyExistsException;
import pe.com.hotel_api.hotel.presentation.dto.*;
import pe.com.hotel_api.hotel.service.interfaces.*;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private EmailService emailService;

    @MockBean
    private ApiDniService apiDniService;

    @MockBean
    private ConvertirDatosService convertirDatosService;

    @MockBean
    private RedisService redisService;

    @MockBean
    private OtpService otpService;

    private CrearUsuarioRequest crearUsuarioRequest;
    private ApiDniResponse apiDniResponse;
    private ApiDniResponse apiDniResponseBad;
    private String json;
    private UsuarioApiDniResponse usuarioApiDniResponse;
    private UsuarioDto usuarioDto;
    private ValidarOtpRequest validarOtpRequest;

    @BeforeEach
    void setUp() {
        crearUsuarioRequest = new CrearUsuarioRequest("987654321", "josuealva920@gmail.com", "123456", "98765432");
        apiDniResponse = new ApiDniResponse("", new DataDni("Pedro", "Julian", LocalDate.of(1990, 10, 10), "1", "LIMA", "LIMA"));
        apiDniResponseBad = new ApiDniResponse("",null);
        json = """
                {
                     "message": "",
                     "data": {
                         "name": "PEDRO",
                         "surname": "JULIAN",
                         "date_of_birth": "1990-10-10",
                         "department": "LIMA",
                         "province": "LIMA",
                         "district": "LIMA"
                     }
                }
                """;
        usuarioApiDniResponse = new UsuarioApiDniResponse("PEDRO", "JULIAN", null, null, null, null, null, null, null, null, null);
        usuarioDto = new UsuarioDto(1L, "Pedro", "Julian", "asd@gmail.com");
        validarOtpRequest = new ValidarOtpRequest("1234567890");
    }

    @Test
    @WithMockUser
    void crearUsuario() throws Exception {
        when(apiDniService.enviarPeticionApiDni(anyString())).thenReturn(json);
        when(convertirDatosService.convertirAObjeto(anyString(), any())).thenReturn(apiDniResponse);

        when(usuarioService.existeUsuarioByDni(anyString())).thenReturn(false);
        when(usuarioService.existeUsuarioByEmail(anyString())).thenReturn(false);

        when(redisService.guardarUsuarioTemporal(any())).thenReturn("mockkey");
        doNothing().when(emailService).sendEmailForVerifiUser(anyString(), anyString());

        mockMvc.perform(post("/usuarios/crear")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(crearUsuarioRequest)))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Usuario guardado temporalmente"))
                .andExpect(jsonPath("$.data.key").value("mockkey"))
                .andExpect(jsonPath("$.data.nombre").value("Pedro"))
                .andExpect(jsonPath("$.data.apellido").value("Julian"))
                .andExpect(jsonPath("$.data.email").value("josuealva920@gmail.com"));
    }

    @Test
    @WithMockUser
    void crearUsuarioJsonNullOrEmptyException() throws Exception {
        when(apiDniService.enviarPeticionApiDni(anyString())).thenReturn(null);

        mockMvc.perform(post("/usuarios/crear")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(crearUsuarioRequest)))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Colocar un dni existente"))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    @WithMockUser
    void crearUsuarioDataDniIsNull() throws Exception {
        when(apiDniService.enviarPeticionApiDni(anyString())).thenReturn(json);
        when(convertirDatosService.convertirAObjeto(anyString(), any())).thenReturn(apiDniResponseBad);

        mockMvc.perform(post("/usuarios/crear")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(crearUsuarioRequest)))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Dni no existente"))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    @WithMockUser
    void crearUsuarioDniAlreadyExistsException() throws Exception {
        when(apiDniService.enviarPeticionApiDni(anyString())).thenReturn(json);
        when(convertirDatosService.convertirAObjeto(anyString(), any())).thenReturn(apiDniResponse);

        when(usuarioService.existeUsuarioByDni(anyString()))
                .thenThrow(new AlreadyExistsException("El dni " + crearUsuarioRequest.dni() + " ya se encuentra registrado"));

        mockMvc.perform(post("/usuarios/crear")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(crearUsuarioRequest)))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("El dni " + crearUsuarioRequest.dni() + " ya se encuentra registrado"))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    @WithMockUser
    void crearUsuarioEmailAlreadyExistsException() throws Exception {
        when(apiDniService.enviarPeticionApiDni(anyString())).thenReturn(json);
        when(convertirDatosService.convertirAObjeto(anyString(), any())).thenReturn(apiDniResponse);

        when(usuarioService.existeUsuarioByDni(anyString())).thenReturn(false);
        when(usuarioService.existeUsuarioByEmail(anyString()))
                .thenThrow(new AlreadyExistsException("El email " + crearUsuarioRequest.email() + " ya se encuentra registrado"));


        mockMvc.perform(post("/usuarios/crear")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(crearUsuarioRequest)))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("El email " + crearUsuarioRequest.email() + " ya se encuentra registrado"))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    @WithMockUser
    void crearUsuarioMessagingException() throws Exception {
        when(apiDniService.enviarPeticionApiDni(anyString())).thenReturn(json);
        when(convertirDatosService.convertirAObjeto(anyString(), any())).thenReturn(apiDniResponse);

        when(usuarioService.existeUsuarioByDni(anyString())).thenReturn(false);
        when(usuarioService.existeUsuarioByEmail(anyString())).thenReturn(false);

        when(redisService.guardarUsuarioTemporal(any())).thenReturn("mockkey");
        doThrow(new MessagingException("Simulated email error")).when(emailService).sendEmailForVerifiUser(anyString(), anyString());


        mockMvc.perform(post("/usuarios/crear")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(crearUsuarioRequest)))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Error al enviar el correo: Simulated email error"))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    @WithMockUser
    void validarOtp() throws Exception {
        when(otpService.esValidoOtp(anyString())).thenReturn(true);
        when(redisService.obtenerUsuarioTemporal(anyString())).thenReturn(usuarioApiDniResponse);
        when(usuarioService.crearUsuario(any())).thenReturn(usuarioDto);

        doNothing().when(usuarioService).correoVerificado(anyString());
        doNothing().when(redisService).deleteUsuarioTemporal(anyString());

        mockMvc.perform(post("/usuarios/validar-otp")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validarOtpRequest)))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Otp validado y usuario creado correctamente"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.email").value("asd@gmail.com"));
    }

    @Test
    @WithMockUser
    void validarOtpInvalidOtpException() throws Exception {
        when(otpService.esValidoOtp(anyString())).thenReturn(false);

        mockMvc.perform(post("/usuarios/validar-otp")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validarOtpRequest)))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Otp invalido"))
                .andExpect(jsonPath("$.data").doesNotExist());
    }
}