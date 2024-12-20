package pe.com.hotel_api.hotel.presentation.controller;

import com.google.zxing.WriterException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Do reservations", description = "Controller for Reservations")
public class ReservaController {
    private final ReservaService reservaService;
    private final UsuarioService usuarioService;
    private final HabitacionService habitacionService;

    @Operation(
            summary = "Create Reservation",
            description = "Create a reservation with the data of the reservation and the authenticated user",
            tags = {"Do reservations"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Reservation request with the data of the reservation",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CrearReservaRequest.class)
                    )
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "201",
                            description = "Reservation created",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponse.class)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "400",
                            description = "Bad request",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponse.class)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "409",
                            description = "Conflict",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponse.class)
                            )
                    )
            }
    )
    @PostMapping("/crear")
    public ResponseEntity<ApiResponse> crearReserva(@RequestBody @Valid CrearReservaRequest crearReservaRequest){
        var usuarioAutenticado = usuarioService.obtenerUsuarioAutenticado();

        if (reservaService.verificarExcedeCantidadHuespedes(crearReservaRequest.cantidadHuespedes(), crearReservaRequest.habitacion())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("La cantidad de huespedes excede la capacidad de la habitacion", null));
        }
        try {
            var codeQr = reservaService.crearReserva(crearReservaRequest, usuarioAutenticado.email());
            habitacionService.actualizarEstado(crearReservaRequest.habitacion());
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Reserva creada con exito", codeQr));
        }catch (AlreadyExistsException | HabitacionNotFoundException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(e.getMessage(), null));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(), null));
        } catch (WriterException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), null));
        }
    }


}
