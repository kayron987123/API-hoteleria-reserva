package pe.com.hotel_api.hotel.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private TemplateEngine templateEngine;

    @Mock
    private MimeMessage mimeMessage;

    @InjectMocks
    private EmailServiceImpl emailService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(emailService, "user", "josue@gmail.com");
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
    }

    @Test
    void sendEmailForVerifiUser() throws MessagingException {
        String toUser = "tu@gmail.com";
        String token = "abcd123";
        String template = "<html><body><h1>Token: " + token + "</h1></body></html>";

        when(templateEngine.process(eq("email-template"), any(Context.class))).thenReturn(template);

        emailService.sendEmailForVerifiUser(toUser, token);

        verify(javaMailSender, times(1)).send(any(MimeMessage.class));
        verify(templateEngine, times(1)).process(eq("email-template"), any(Context.class));
        verify(javaMailSender, times(1)).send(mimeMessage);
    }

    @Test
    void sendEmailForVerifiUserException() {
        String toUser = "tu@gmail.com";
        String token = "abcd123";
        String template = "<html><body><h1>Token: " + token + "</h1></body></html>";

        when(templateEngine.process(eq("email-template"), any(Context.class))).thenReturn(template);

        doAnswer(invocation -> {
            throw new MessagingException("Error al enviar el correo");
        }).when(javaMailSender).send(any(MimeMessage.class));

        assertThrows(MessagingException.class, () ->
            emailService.sendEmailForVerifiUser(toUser, token));

        verify(templateEngine, times(1)).process(eq("email-template"), any(Context.class));
        verify(javaMailSender, times(1)).createMimeMessage();
        verify(javaMailSender, times(1)).send(any(MimeMessage.class));
    }
}