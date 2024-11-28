package pe.com.hotel_api.hotel.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.com.hotel_api.hotel.persistence.entity.Reserva;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    boolean existsByCodigoReserva(String codigoReserva);
}
