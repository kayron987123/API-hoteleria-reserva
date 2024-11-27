package pe.com.hotel_api.hotel.service.interfaces;

public interface OtpService {
    boolean esValidoOtp(String otp);
    String generarOtp();
}
