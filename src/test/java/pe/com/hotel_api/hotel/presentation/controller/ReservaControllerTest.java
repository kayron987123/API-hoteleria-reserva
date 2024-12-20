package pe.com.hotel_api.hotel.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.WriterException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import pe.com.hotel_api.hotel.presentation.advice.AlreadyExistsException;
import pe.com.hotel_api.hotel.presentation.advice.HabitacionNotFoundException;
import pe.com.hotel_api.hotel.presentation.dto.CrearReservaRequest;
import pe.com.hotel_api.hotel.presentation.dto.UsuarioDto;
import pe.com.hotel_api.hotel.service.interfaces.HabitacionService;
import pe.com.hotel_api.hotel.service.interfaces.ReservaService;
import pe.com.hotel_api.hotel.service.interfaces.UsuarioService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReservaController.class)
class ReservaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservaService reservaService;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private HabitacionService habitacionService;

    @Autowired
    private ObjectMapper objectMapper;

    private UsuarioDto usuario;
    private String codigoQr;
    private CrearReservaRequest crearReservaRequest;

    @BeforeEach
    void setUp() {
        usuario = new UsuarioDto(1L, "gad", "alva", "josuealva920@gmail.com");
        codigoQr = "codigoQr-mock";
        crearReservaRequest = new CrearReservaRequest(LocalDateTime.now().plusHours(1), LocalDateTime.now().plusDays(2), 5, 1L);
    }

    private String generateJwtToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, "12345678910QPWLEOAIMZNCHDJSKLQIWURHT102OAPZLFMAKQIWU12049586J23MAMZJ18234")
                .compact();
    }

    @Test
    @WithMockUser(username = "josuealva920@gmail.com")
    void crearReserva() throws Exception {
        var token = generateJwtToken(usuario.email(), "ROLE_HUESPED");
        when(usuarioService.obtenerUsuarioAutenticado()).thenReturn(usuario);
        when(reservaService.verificarExcedeCantidadHuespedes(any(), anyLong())).thenReturn(false);
        when(reservaService.crearReserva(any(CrearReservaRequest.class), anyString())).thenReturn(codigoQr);
        doNothing().when(habitacionService).actualizarEstado(anyLong());

        mockMvc.perform(post("/reservas/crear")
                        .header("Authorization", "Bearer " + token)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(crearReservaRequest)))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Reserva creada con exito"))
                .andExpect(jsonPath("$.data").value(codigoQr));

    }

    @Test
    @WithMockUser(username = "josuealva920@gmail.com")
    void crearReservaExcedeCantidadHuespedes() throws Exception {
        var token = generateJwtToken(usuario.email(), "ROLE_HUESPED");
        when(usuarioService.obtenerUsuarioAutenticado()).thenReturn(usuario);
        when(reservaService.verificarExcedeCantidadHuespedes(any(), anyLong())).thenReturn(true);

        mockMvc.perform(post("/reservas/crear")
                        .header("Authorization", "Bearer " + token)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(crearReservaRequest)))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("La cantidad de huespedes excede la capacidad de la habitacion"))
                .andExpect(jsonPath("$.data").doesNotExist());

    }

    @Test
    @WithMockUser(username = "josuealva920@gmail.com")
    void crearReservaAlreadyExistsException() throws Exception {
        var token = generateJwtToken(usuario.email(), "ROLE_HUESPED");
        when(usuarioService.obtenerUsuarioAutenticado()).thenReturn(usuario);
        when(reservaService.verificarExcedeCantidadHuespedes(any(), anyLong())).thenReturn(false);
        when(reservaService.crearReserva(any(CrearReservaRequest.class), anyString()))
                .thenThrow(new AlreadyExistsException("La reserva ya existe"));

        mockMvc.perform(post("/reservas/crear")
                        .header("Authorization", "Bearer " + token)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(crearReservaRequest)))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("La reserva ya existe"))
                .andExpect(jsonPath("$.data").doesNotExist());

    }

    @Test
    @WithMockUser(username = "josuealva920@gmail.com")
    void crearReservaHabitacionNotFoundException() throws Exception {
        var token = generateJwtToken(usuario.email(), "ROLE_HUESPED");
        when(usuarioService.obtenerUsuarioAutenticado()).thenReturn(usuario);
        when(reservaService.verificarExcedeCantidadHuespedes(any(), anyLong())).thenReturn(false);
        when(reservaService.crearReserva(any(CrearReservaRequest.class), anyString()))
                .thenThrow(new HabitacionNotFoundException("La habitacion no existe"));

        mockMvc.perform(post("/reservas/crear")
                        .header("Authorization", "Bearer " + token)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(crearReservaRequest)))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("La habitacion no existe"))
                .andExpect(jsonPath("$.data").doesNotExist());

    }

    @Test
    @WithMockUser(username = "josuealva920@gmail.com")
    void crearReservaIOException() throws Exception {
        var token = generateJwtToken(usuario.email(), "ROLE_HUESPED");
        when(usuarioService.obtenerUsuarioAutenticado()).thenReturn(usuario);
        when(reservaService.verificarExcedeCantidadHuespedes(any(), anyLong())).thenReturn(false);
        when(reservaService.crearReserva(any(CrearReservaRequest.class), anyString()))
                .thenThrow(new IOException("Error de E/S al generar QR"));

        mockMvc.perform(post("/reservas/crear")
                        .header("Authorization", "Bearer " + token)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(crearReservaRequest)))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Error de E/S al generar QR"))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    @WithMockUser(username = "josuealva920@gmail.com")
    void crearReservaWriterException() throws Exception {
        var token = generateJwtToken(usuario.email(), "ROLE_HUESPED");
        when(usuarioService.obtenerUsuarioAutenticado()).thenReturn(usuario);
        when(reservaService.verificarExcedeCantidadHuespedes(any(), anyLong())).thenReturn(false);
        when(reservaService.crearReserva(any(CrearReservaRequest.class), anyString()))
                .thenThrow(new WriterException("Error al escribir el código QR"));

        mockMvc.perform(post("/reservas/crear")
                        .header("Authorization", "Bearer " + token)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(crearReservaRequest)))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Error al escribir el código QR"))
                .andExpect(jsonPath("$.data").doesNotExist());
    }
}