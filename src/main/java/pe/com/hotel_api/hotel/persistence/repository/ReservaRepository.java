package pe.com.hotel_api.hotel.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.com.hotel_api.hotel.persistence.entity.Reserva;

import java.time.LocalDateTime;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    boolean existsByCodigoReserva(String codigoReserva);
    @Query("""
    SELECT COUNT(r) > 0
    FROM Reserva r
    WHERE 
        (r.fechaEntrada <= :fechaSalida AND r.fechaSalida >= :fechaEntrada)
    """)
    boolean existeReservaSolapada(@Param("fechaEntrada") LocalDateTime fechaEntrada, @Param("fechaSalida") LocalDateTime fechaSalida);

}
