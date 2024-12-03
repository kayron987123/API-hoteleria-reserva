package pe.com.hotel_api.hotel.presentation.controller;

import com.google.zxing.WriterException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.com.hotel_api.hotel.presentation.advice.AlreadyExistsException;
import pe.com.hotel_api.hotel.presentation.advice.HabitacionNotFoundException;
import pe.com.hotel_api.hotel.presentation.dto.ApiResponse;
import pe.com.hotel_api.hotel.presentation.dto.CrearReservaRequest;
import pe.com.hotel_api.hotel.service.interfaces.HabitacionService;
import pe.com.hotel_api.hotel.service.interfaces.ReservaService;
import pe.com.hotel_api.hotel.service.interfaces.UsuarioService;

import java.io.IOException;

@RestController
@RequestMapping("/reservas")
@RequiredArgsConstructor
public class ReservaController {
    private final ReservaService reservaService;
    private final UsuarioService usuarioService;
    private final HabitacionService habitacionService;

    @PostMapping("/crear")
    public ResponseEntity<ApiResponse> crearReserva(@RequestBody @Valid CrearReservaRequest crearReservaRequest){
        var usuarioAutenticado = usuarioService.obtenerUsuarioAutenticado();

        if (reservaService.verificarExcedeCantidadHuespedes(crearReservaRequest.cantidadHuespedes(), crearReservaRequest.habitacion())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("La cantidad de huespedes excede la capacidad de la habitación", null));
        }
        try {
            var codeQr = reservaService.crearReserva(crearReservaRequest, usuarioAutenticado.email());
            habitacionService.actualizarEstado(crearReservaRequest.habitacion());
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Reserva creada con éxito", codeQr));
        }catch (AlreadyExistsException | HabitacionNotFoundException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(e.getMessage(), null));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(), null));
        } catch (WriterException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), null));
        }
    }
}
