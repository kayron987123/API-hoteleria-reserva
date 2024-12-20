package pe.com.hotel_api.hotel.persistence.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pe.com.hotel_api.hotel.persistence.entity.Usuario;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeAll
    static void setUp(@Autowired UsuarioRepository usuarioRepository) {
        Usuario usuario = new Usuario(null, "jesus", "antares", "123456789", "jesus@gmail.com", "123456",
                LocalDate.of(2002,1,1), "87654321", null, null, null, null, null, null, LocalDateTime.now(), null);
        usuarioRepository.save(usuario);
    }

    @Test
    void findByEmail() {
        Usuario usuario = usuarioRepository.findByEmail("jesus@gmail.com");
        assertNotNull(usuario);
        assertEquals(1L, usuario.getId());
        assertEquals("87654321", usuario.getDni());
        assertEquals(LocalDate.now(), usuario.getFechaRegistro().toLocalDate());
    }

    @Test
    void findByEmailNotFound() {
        Usuario usuario = usuarioRepository.findByEmail("noexists@hotmail.com");
        assertNull(usuario);
    }

    @Test
    void existsByEmail() {
        boolean exists = usuarioRepository.existsByEmail("jesus@gmail.com");
        assertTrue(exists);
    }

    @Test
    void existsByEmailNotFound() {
        boolean exists = usuarioRepository.existsByEmail("noexists@gmail.com");
        assertFalse(exists);
    }

    @Test
    void existsByDni() {
        boolean exists = usuarioRepository.existsByDni("87654321");
        assertTrue(exists);
    }

    @Test
    void exitsByDniNotFound() {
        boolean exists = usuarioRepository.existsByDni("00000000");
        assertFalse(exists);
    }
}