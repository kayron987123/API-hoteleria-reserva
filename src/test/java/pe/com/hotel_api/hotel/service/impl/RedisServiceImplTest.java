package pe.com.hotel_api.hotel.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import pe.com.hotel_api.hotel.presentation.dto.UsuarioApiDniResponse;

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RedisServiceImplTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @Mock
    private OtpServiceImpl otpService;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private RedisServiceImpl redisService;

    private String otp = null;

    @BeforeEach
    void setUp() {
        otp = "1234567890";
    }

    @Test
    void guardarUsuarioTemporal() {

        var usuario = new UsuarioApiDniResponse("nombre", "apellido", "telefono", "email", "contrasena", LocalDate.now(), "dni", "departamento", "probivncia", "distrito", "image.png");

        when(otpService.generarOtp()).thenReturn(otp);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        String resultOtp = redisService.guardarUsuarioTemporal(usuario);

        assertNotNull(resultOtp);
        assertEquals(otp, resultOtp);
        verify(otpService, times(1)).generarOtp();
        verify(valueOperations, times(1)).set(eq(otp), eq(usuario), eq(5L), eq(TimeUnit.MINUTES));
    }

    @Test
    void obtenerUsuarioTemporal() {
        var usuario = new UsuarioApiDniResponse("nombre", "apellido", "telefono", "email", "contrasena", LocalDate.now(), "dni", "departamento", "probivncia", "distrito", "image.png");

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(redisTemplate.opsForValue().get(otp)).thenReturn(usuario);
        when(objectMapper.convertValue(usuario, UsuarioApiDniResponse.class)).thenReturn(usuario);

        var usuarioTemporal = redisService.obtenerUsuarioTemporal(otp);

        assertNotNull(usuarioTemporal);
        assertEquals(usuario, usuarioTemporal);
        assertEquals("nombre", usuarioTemporal.nombre());
        verify(valueOperations, times(1)).get(otp);
    }

    @Test
    void deleteUsuarioTemporal() {
        when(redisTemplate.delete(otp)).thenReturn(true);

        redisService.deleteUsuarioTemporal(otp);

        verify(redisTemplate, times(1)).delete(otp);
    }
}