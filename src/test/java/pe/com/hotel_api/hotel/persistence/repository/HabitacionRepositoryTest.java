package pe.com.hotel_api.hotel.persistence.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pe.com.hotel_api.hotel.persistence.entity.*;
import pe.com.hotel_api.hotel.persistence.enums.EstadoHabitacion;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@Slf4j
class HabitacionRepositoryTest {
    @Autowired
    private HabitacionRepository habitacionRepository;

    @BeforeAll
    static void setUp(@Autowired HabitacionRepository habitacionRepository) {
        Habitacion habitacion = new Habitacion(null, "Habitacion imperial", BigDecimal.valueOf(12.50), 3, EstadoHabitacion.DISPONIBLE, "url.jpg", null, null, null);
        habitacionRepository.save(habitacion);
    }

    @Test
    void getHabitacionById() {
        Habitacion habitacion = habitacionRepository.getHabitacionById(1L);
        assertNotNull(habitacion);
        assertEquals(1, habitacion.getId());
    }

    @Test
    void getHabitacionByIdNotFound() {
        Habitacion habitacion = habitacionRepository.getHabitacionById(2L);
        assertNull(habitacion);
    }

    @Test
    void getPrecioById() {
        BigDecimal precio = habitacionRepository.getPrecioById(1L);
        assertNotNull(precio);
        assertEquals(0, precio.compareTo(BigDecimal.valueOf(12.50)));
    }

    @Test
    void getPrecioByIdNotFound() {
        BigDecimal precio = habitacionRepository.getPrecioById(2L);
        assertNull(precio);
    }

    @Test
    void getCapacidadById() {
        Integer capacidad = habitacionRepository.getCapacidadById(1L);
        assertNotNull(capacidad);
        assertEquals(3, capacidad);
    }

    @Test
    void getCapacidadByIdNotFound() {
        Integer capacidad = habitacionRepository.getCapacidadById(2L);
        assertNull(capacidad);
    }
}
