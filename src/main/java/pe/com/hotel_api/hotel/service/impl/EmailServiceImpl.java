package pe.com.hotel_api.hotel.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import pe.com.hotel_api.hotel.service.interfaces.EmailService;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    @Value("${app.mail.user}")
    private String user;

    private final JavaMailSender javaMailSender;
    private final TemplateEngine mailTemplateEngine;

    @Override
    public void sendEmail(String toUser) throws MessagingException {
        Context context = new Context();

        String mailContent = mailTemplateEngine.process("email-template", context);
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(user);
        helper.setTo(toUser);
        helper.setSubject("Bienvenido a Hotel API");
        helper.setText(mailContent, true);
        javaMailSender.send(message);
    }
}
