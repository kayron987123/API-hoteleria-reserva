package pe.com.hotel_api.hotel.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import pe.com.hotel_api.hotel.persistence.enums.EstadoHabitacion;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Table(name = "habitaciones")
public class Habitacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_habitacion")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "precio_noche")
    private BigDecimal precioNoche;

    @Column(name = "capacidad_max")
    private Integer capacidadMax;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoHabitacion estado;

    @Column(name = "imagen_url")
    private String imagenUrl;

    @ManyToOne
    @JoinColumn(name = "id_tipo_cama")
    private TipoCama tipoCama;

    @ManyToOne
    @JoinColumn(name = "id_tipo_habitacion")
    private TipoHabitacion tipoHabitacion;
}
