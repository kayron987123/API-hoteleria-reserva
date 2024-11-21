package pe.com.hotel_api.hotel.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import pe.com.hotel_api.hotel.persistence.enums.EstadoReserva;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Table(name = "reservas")
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva")
    private Long id;

    @Column(name = "fecha_entrada")
    private LocalDateTime fechaEntrada;

    @Column(name = "fecha_salida")
    private LocalDateTime fechaSalida;

    @Column(name = "cantidad_huespedes")
    private Integer cantidadHuespedes;

    @Column(name = "precio_total")
    private BigDecimal precioTotal;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoReserva estado;

    @Column(name = "codigo_reserva")
    private String codigoReserva;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_habitacion")
    private Habitacion habitacion;

    @ManyToOne
    @JoinColumn(name = "id_sede")
    private Sede sede;
}
