package pe.com.hotel_api.hotel.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pe.com.hotel_api.hotel.persistence.entity.Usuario;
import pe.com.hotel_api.hotel.persistence.repository.UsuarioRepository;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioDetailsServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioDetailsService usuarioDetailsService;

    private Usuario usuario;
    private String email;

    @BeforeEach
    void setUp() {
        usuario = new Usuario(1L, "nombre", "apellido", "987654321", "email@gmail.com", "password", LocalDate.now(),
                "dni", "departamento", "provincia", "distrito", null, null, null, null, null);
        email = "email@gmail.com";
    }

    @Test
    void loadUserByUsername() {
        when(usuarioRepository.findByEmail(anyString())).thenReturn(usuario);

        UserDetails usuarioDetalle = usuarioDetailsService.loadUserByUsername(email);

        assertNotNull(usuarioDetalle);
        assertEquals(email, usuarioDetalle.getUsername());
        verify(usuarioRepository, times(1)).findByEmail(anyString());
    }

    @Test
    void loadUserByUsenameException() {
        when(usuarioRepository.findByEmail(anyString())).thenReturn(null);

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> usuarioDetailsService.loadUserByUsername(email));

        assertEquals("Usuario no encontrado", exception.getMessage());
        verify(usuarioRepository, times(1)).findByEmail(anyString());
    }
}