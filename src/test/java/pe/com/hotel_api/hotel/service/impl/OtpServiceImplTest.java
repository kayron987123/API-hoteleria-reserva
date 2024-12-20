package pe.com.hotel_api.hotel.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OtpServiceImplTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @InjectMocks
    private OtpServiceImpl otpService;

    @Test
    void esValidoOtp() {
        var otp = "1234567890";
        var usuario = "usuario";

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(otp)).thenReturn(usuario);

        var result = otpService.esValidoOtp(otp);

        assertTrue(result);
    }

    @Test
    void esValidoOtpInvalido() {
        var otp = "1234567890";

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(otp)).thenReturn(null);

        var result = otpService.esValidoOtp(otp);

        assertFalse(result);
    }

    @Test
    void generarOtp() {
        var otp = otpService.generarOtp();

        assertNotNull(otp);
        assertEquals(10, otp.length());
    }
}