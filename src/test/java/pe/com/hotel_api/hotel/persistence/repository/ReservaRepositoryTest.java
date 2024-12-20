package pe.com.hotel_api.hotel.persistence.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pe.com.hotel_api.hotel.persistence.entity.Reserva;
import pe.com.hotel_api.hotel.persistence.enums.EstadoReserva;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
class ReservaRepositoryTest {

    @Autowired
    private ReservaRepository reservaRepository;

    @BeforeAll
    static void setUp(@Autowired ReservaRepository reservaRepository) {
        Reserva reserva = new Reserva(null, LocalDateTime.now(), LocalDateTime.now().plusDays(2), 3, BigDecimal.valueOf(12.50),
                EstadoReserva.RESERVADA, "R001", "image.png", null, null);
        reservaRepository.save(reserva);
    }

    @Test
    void existsByCodigoReserva() {
        boolean exists = reservaRepository.existsByCodigoReserva("R001");
        Assertions.assertTrue(exists);
    }

    @Test
    void existsByCodigoReservaNotFound() {
        boolean exists = reservaRepository.existsByCodigoReserva("R002");
        Assertions.assertFalse(exists);
    }

    @Test
    void existeReservaSolapada() {
        boolean existeReservaSolapada = reservaRepository.existeReservaSolapada(LocalDateTime.now().plusMinutes(4), LocalDateTime.now().plusDays(2).plusMinutes(4));
        Assertions.assertTrue(existeReservaSolapada);
    }

    @Test
    void existeReservaSolapadaNotFound() {
        boolean existeReservaSolapada = reservaRepository.existeReservaSolapada(LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(4));
        Assertions.assertFalse(existeReservaSolapada);
    }
}