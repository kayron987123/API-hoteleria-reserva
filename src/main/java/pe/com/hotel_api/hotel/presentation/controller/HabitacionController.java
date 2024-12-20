package pe.com.hotel_api.hotel.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.com.hotel_api.hotel.presentation.advice.HabitacionNotFoundException;
import pe.com.hotel_api.hotel.presentation.advice.SedeNotFoundException;
import pe.com.hotel_api.hotel.presentation.dto.ApiResponse;
import pe.com.hotel_api.hotel.presentation.dto.HabitacionDto;
import pe.com.hotel_api.hotel.service.interfaces.HabitacionService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/habitaciones")
@RequiredArgsConstructor
@Tag(name = "Filter Rooms", description = "Controller for Rooms")
public class HabitacionController {
    private final HabitacionService habitacionService;
    private static final String MESSAGE_SUCCESS = "Habitaciones encontradas";

    @Operation(
            summary = "Search Rooms",
            description = "Filter rooms by name, bed type, room type, price, entry date, exit date and headquarters",
            tags = {"Filter Rooms"},
            parameters = {
                    @Parameter(name = "nombreHabitacion", description = "Nombre de la habitación", required = false),
                    @Parameter(name = "tipoCama", description = "Tipo de cama", required = false),
                    @Parameter(name = "tipoHabitacion", description = "Tipo de habitación", required = false),
                    @Parameter(name = "minPrecio", description = "Precio mínimo", required = false, schema = @Schema(type = "number", format = "bigdecimal", defaultValue = "100")),
                    @Parameter(name = "maxPrecio", description = "Precio máximo", required = false, schema = @Schema(type = "number", format = "bigdecimal", defaultValue = "10000")),
                    @Parameter(name = "fechaEntrada", description = "Fecha de entrada", required = false, schema = @Schema(type = "string", format = "date-time")),
                    @Parameter(name = "fechaSalida", description = "Fecha de salida", required = false, schema = @Schema(type = "string", format = "date-time")),
                    @Parameter(name = "idSede", description = "ID de la sede", required = true, schema = @Schema(type = "integer", format = "int64"))
            },
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Rooms found",
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
                    )
            }
    )
    @GetMapping("/sede/{idSede}/buscar")
    public ResponseEntity<ApiResponse> buscarHabitaciones(@RequestParam(required = false) String nombreHabitacion,
                                                          @RequestParam(required = false) String tipoCama,
                                                          @RequestParam(required = false) String tipoHabitacion,
                                                          @RequestParam(required = false, defaultValue = "100") BigDecimal minPrecio,
                                                          @RequestParam(required = false, defaultValue = "10000") BigDecimal maxPrecio,
                                                          @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaEntrada,
                                                          @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaSalida,
                                                          @PathVariable Long idSede) {
        if ((fechaEntrada != null && fechaSalida == null) || (fechaEntrada == null && fechaSalida != null)) {
            return ResponseEntity.badRequest().body(new ApiResponse("Debe ingresar ambas fechas para poder realizar el filtro", null));
        }

        try {
            List<HabitacionDto> habitacionesResponse = habitacionService.buscarHabitaciones(
                    nombreHabitacion, tipoCama, tipoHabitacion, minPrecio, maxPrecio, fechaEntrada, fechaSalida, idSede);
            return ResponseEntity.ok(new ApiResponse(MESSAGE_SUCCESS, habitacionesResponse));
        } catch (SedeNotFoundException | HabitacionNotFoundException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), null));
        }
    }

    @Operation(
            summary = "Search Rooms",
            description = "Filter rooms by name and headquarters",
            tags = {"Filter Rooms"},
            parameters = {
                    @Parameter(name = "nombreHabitacion", description = "Nombre de la habitación", required = false),
                    @Parameter(name = "idSede", description = "ID de la sede", required = true, schema = @Schema(type = "integer", format = "int64"))
            },
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Rooms found",
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
                    )
            }
    )
    @GetMapping("/sede/{idSede}/buscar-nombre")
    public ResponseEntity<ApiResponse> buscarHabitacionesPorFiltroFechasYHoraYNombreCiudad(@RequestParam String nombreHabitacion,
                                                                                           @PathVariable Long idSede) {
        if (nombreHabitacion == null || nombreHabitacion.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse("Debe ingresar un nombre para poder realizar el filtro", null));
        }

        if (idSede == null || idSede <= 0) {
            return ResponseEntity.badRequest().body(new ApiResponse("Debe ingresar un id de sede y que sea mayor que 0 para poder realizar el filtro", null));
        }

        try {
            return ResponseEntity.ok(new ApiResponse(MESSAGE_SUCCESS, habitacionService.buscarHabitacionesPorNombreYIdSede(nombreHabitacion, idSede)));
        }catch (SedeNotFoundException | HabitacionNotFoundException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), null));
        }
    }
}
