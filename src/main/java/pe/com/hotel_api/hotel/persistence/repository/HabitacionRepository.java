package pe.com.hotel_api.hotel.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pe.com.hotel_api.hotel.persistence.entity.Habitacion;

import java.math.BigDecimal;

public interface HabitacionRepository extends JpaRepository<Habitacion, Long> {
    Habitacion getHabitacionById(Long id);

    @Query("SELECT h.precioNoche FROM Habitacion h WHERE h.id = ?1")
    BigDecimal getPrecioById(Long id);
}
