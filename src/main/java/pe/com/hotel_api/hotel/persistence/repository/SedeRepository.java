package pe.com.hotel_api.hotel.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.com.hotel_api.hotel.persistence.entity.Sede;

public interface SedeRepository extends JpaRepository<Sede, Long> {
    Sede getSedeById(Long id);
}
