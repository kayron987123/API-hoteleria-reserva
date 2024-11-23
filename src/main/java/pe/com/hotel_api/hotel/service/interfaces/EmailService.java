package pe.com.hotel_api.hotel.service.interfaces;

import jakarta.mail.MessagingException;

public interface EmailService {
    void sendEmail(String toUser) throws MessagingException;
}
