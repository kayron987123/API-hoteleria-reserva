package pe.com.hotel_api.hotel.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import pe.com.hotel_api.hotel.presentation.dto.UsuarioApiDniResponse;
import pe.com.hotel_api.hotel.service.interfaces.OtpService;
import pe.com.hotel_api.hotel.service.interfaces.RedisService;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final int REDIS_EXPIRATION_MINUTES = 5;
    private final OtpService otpService;
    private final ObjectMapper objectMapper;

    @Override
    public String guardarUsuarioTemporal(UsuarioApiDniResponse usuarioTemporal) {
        String otp = otpService.generarOtp();
        redisTemplate.opsForValue().set(otp, usuarioTemporal, REDIS_EXPIRATION_MINUTES, TimeUnit.MINUTES);
        return otp;
    }

    @Override
    public UsuarioApiDniResponse obtenerUsuarioTemporal(String key) {
        return objectMapper.convertValue(redisTemplate.opsForValue().get(key), UsuarioApiDniResponse.class);
    }

    @Override
    public void deleteUsuarioTemporal(String key) {
        redisTemplate.delete(key);
    }
}
