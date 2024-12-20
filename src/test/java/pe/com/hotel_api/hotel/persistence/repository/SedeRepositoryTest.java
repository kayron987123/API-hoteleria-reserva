package pe.com.hotel_api.hotel.persistence.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pe.com.hotel_api.hotel.persistence.entity.Sede;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class SedeRepositoryTest {

    @Autowired
    private SedeRepository sedeRepository;

    @BeforeEach
    void setUp() {
        Sede sede = new Sede(null, "Sede 1", "Direccion 1", "arequipa", "Wiesse", null);
        sedeRepository.save(sede);
    }

    @Test
    void getSedeById() {
        Sede sede = sedeRepository.getSedeById(1L);
        assertNotNull(sede);
        assertEquals("arequipa", sede.getPais());
    }

    @Test
    void getSedeByIdNotFound(){
        Sede sede = sedeRepository.getSedeById(2L);
        assertNull(sede);
    }

    @Test
    void existsById() {
        boolean exists = sedeRepository.existsById(2L);
        assertTrue(exists);
    }

    @Test
    void existsByIdNotFound() {
           boolean exists = sedeRepository.existsById(2L);
        assertFalse(exists);
    }
}