package pe.com.hotel_api.hotel.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pe.com.hotel_api.hotel.persistence.entity.Usuario;
import pe.com.hotel_api.hotel.persistence.repository.UsuarioRepository;
import pe.com.hotel_api.hotel.presentation.advice.AlreadyExistsException;
import pe.com.hotel_api.hotel.presentation.dto.UsuarioApiDniResponse;
import pe.com.hotel_api.hotel.presentation.dto.UsuarioDto;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private UsuarioApiDniResponse usuarioApiDniResponse;
    private Usuario usuario;
    private String email;

    @BeforeEach
    void setUp() {
        usuarioApiDniResponse = new UsuarioApiDniResponse("nombre", "apellido", "987654321", "email@gmail.com", "password", LocalDate.now(),
                "dni", "departamento", "provincia", "distrito", "img.png");
        usuario = new Usuario(1L, "nombre", "apellido", "987654321", "email@gmail.com", "password", LocalDate.now(),
                "dni", "departamento", "provincia", "distrito", null, null, null, null, null);
        email = "email@gmail.com";
    }

    @Test
    void obtenerUsuarioAutenticado() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(email);
        when(usuarioRepository.findByEmail(anyString())).thenReturn(usuario);
        SecurityContextHolder.setContext(securityContext);

        UsuarioDto usuarioAutenticado = usuarioService.obtenerUsuarioAutenticado();

        assertNotNull(usuarioAutenticado);
        assertEquals(usuario.getId(), usuarioAutenticado.id());
        assertEquals(usuario.getNombre(), usuarioAutenticado.nombre());
    }

    @Test
    void crearUsuario() {


        when(passwordEncoder.encode(anyString())).thenReturn("passwordencoded");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        UsuarioDto usuarioGuardado = usuarioService.crearUsuario(usuarioApiDniResponse);

        assertNotNull(usuarioGuardado);
        assertEquals(usuario.getId(), usuarioGuardado.id());
        assertEquals(usuario.getNombre(), usuarioGuardado.nombre());
        verify(passwordEncoder, times(1)).encode(anyString());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void correoVerificado() {
        when(usuarioRepository.findByEmail(anyString())).thenReturn(usuario);

        usuarioService.correoVerificado(email);

        verify(usuarioRepository, times(1)).findByEmail(anyString());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void existeUsuarioByEmail() {
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);

        boolean existeUsuario = usuarioService.existeUsuarioByEmail(email);

        assertFalse(existeUsuario);
        verify(usuarioRepository, times(1)).existsByEmail(anyString());
    }

    @Test
    void existeUsuarioByEmailException() {
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(true);

        AlreadyExistsException exception = assertThrows(AlreadyExistsException.class,
                () -> usuarioService.existeUsuarioByEmail(email));

        assertEquals("El email email@gmail.com ya se encuentra registrado", exception.getMessage());
        verify(usuarioRepository, times(1)).existsByEmail(anyString());
    }

    @Test
    void existeUsuarioByDni() {
        when(usuarioRepository.existsByDni(anyString())).thenReturn(false);

        boolean existeUsuario = usuarioService.existeUsuarioByDni("dni");

        assertFalse(existeUsuario);
        verify(usuarioRepository, times(1)).existsByDni(anyString());
    }

    @Test
    void existeUsuarioByDniException() {
        when(usuarioRepository.existsByDni(anyString())).thenReturn(true);

        AlreadyExistsException exception = assertThrows(AlreadyExistsException.class,
                () -> usuarioService.existeUsuarioByDni("dni"));

        assertEquals("El dni dni ya se encuentra registrado", exception.getMessage());
        verify(usuarioRepository, times(1)).existsByDni(anyString());
    }
}