package pe.com.hotel_api.hotel.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.com.hotel_api.hotel.persistence.entity.Habitacion;
import pe.com.hotel_api.hotel.persistence.enums.EstadoHabitacion;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HabitacionRepository extends JpaRepository<Habitacion, Long> {
    Habitacion getHabitacionById(Long id);

    @Query("SELECT h.precioNoche FROM Habitacion h WHERE h.id = ?1")
    BigDecimal getPrecioById(Long id);

    @Query("""
                SELECT h.capacidadMax
                FROM Habitacion h
                WHERE h.id = :idHabitacion
            """)
    Integer getCapacidadById(@Param("idHabitacion") Long idHabitacion);

    @Query("""
                SELECT h
                FROM Habitacion h
                WHERE (:idSede = 0 OR h.sede.id = :idSede)
                  AND (:nombre IS NULL OR LOWER(h.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')))
                  AND (:tipoCama IS NULL OR LOWER(h.tipoCama.nombre) LIKE LOWER(CONCAT('%', :tipoCama, '%')))
                  AND (:tipoHabitacion IS NULL OR LOWER(h.tipoHabitacion.nombre) LIKE LOWER(CONCAT('%', :tipoHabitacion, '%')))
                  AND (:minPrecio IS NULL OR h.precioNoche >= :minPrecio)
                  AND (:maxPrecio IS NULL OR h.precioNoche <= :maxPrecio)
                  AND NOT EXISTS (
                            SELECT r
                            FROM Reserva r
                            WHERE r.habitacion.id = h.id
                              AND r.fechaEntrada < :fechaSalida
                              AND r.fechaSalida > :fechaEntrada
                        )
            """)
    List<Habitacion> buscarHabitacionesCombinadas(
            @Param("nombre") String nombre,
            @Param("tipoCama") String tipoCama,
            @Param("tipoHabitacion") String tipoHabitacion,
            @Param("minPrecio") BigDecimal minPrecio,
            @Param("maxPrecio") BigDecimal maxPrecio,
            @Param("fechaEntrada") LocalDateTime fechaEntrada,
            @Param("fechaSalida") LocalDateTime fechaSalida,
            @Param("idSede") Long idSede
    );

    List<Habitacion> findByNombreContainingIgnoreCaseAndSedeIdAndEstado(String nombre, Long idSede, EstadoHabitacion estado);
}
