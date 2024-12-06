package pe.com.hotel_api.hotel.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pe.com.hotel_api.hotel.persistence.entity.Sede;

import java.time.LocalDateTime;
import java.util.List;

public interface SedeRepository extends JpaRepository<Sede, Long> {
    Sede getSedeById(Long id);
    boolean existsById(Long idSede);

    @Query("""
                SELECT s 
                FROM Sede s 
                WHERE LOWER(s.ciudad) LIKE LOWER(CONCAT('%', :nombreCiudad, '%'))
                  AND NOT EXISTS (
                    SELECT r
                    FROM Reserva r
                    WHERE r.habitacion.sede.id = s.id
                      AND r.fechaEntrada <= :fechaSalida
                      AND r.fechaSalida >= :fechaEntrada
                  )
            """)
    List<Sede> buscarSedePorFechaYHoraYNombreDondeHabitacionesNoEstenReservadas(LocalDateTime fechaEntrada, LocalDateTime fechaSalida, String nombreCiudad);

    boolean existsByCiudadContainingIgnoreCase(String nombre);
}
