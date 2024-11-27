package pe.com.hotel_api.hotel.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import pe.com.hotel_api.hotel.service.interfaces.OtpService;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final int TOKEN_LENGTH = 10;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*-";

    @Override
    public boolean esValidoOtp(String otp) {
        var usuario = redisTemplate.opsForValue().get(otp);

        if (usuario != null){
            return true;
        }
        return false;
    }

    @Override
    public String generarOtp() {
        SecureRandom random = new SecureRandom();
        StringBuilder key = new StringBuilder(TOKEN_LENGTH);

        for (int i = 0; i < TOKEN_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            key.append(CHARACTERS.charAt(index));
        }

        return key.toString();
    }
}
