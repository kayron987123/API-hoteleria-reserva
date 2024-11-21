package pe.com.hotel_api.hotel.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import pe.com.hotel_api.hotel.persistence.enums.EstadoUsuario;
import pe.com.hotel_api.hotel.persistence.enums.RolUsuario;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Table(name = "usuarios")
public class Usuario{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellido")
    private String apellido;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "email")
    private String email;

    @Column(name = "contrasena")
    private String contrasena;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column(name = "dni")
    private String dni;

    @Column(name = "departamento")
    private String departamento;

    @Column(name = "provincia")
    private String provincia;

    @Column(name = "distrito")
    private String distrito;

    @Column(name = "rol", columnDefinition = "rol_usuario")
    @Enumerated(EnumType.STRING)
    private RolUsuario rol;

    @Column(name = "estado", columnDefinition = "estado_usuario")
    @Enumerated(EnumType.STRING)
    private EstadoUsuario estado;

    @Column(name = "imagen_url")
    private String imageUrl;

    @CreationTimestamp
    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    @Column(name = "email_verificado")
    private Boolean emailVerificado;
}
