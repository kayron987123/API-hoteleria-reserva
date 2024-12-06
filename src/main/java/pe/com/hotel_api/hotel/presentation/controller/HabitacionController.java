package pe.com.hotel_api.hotel.presentation.controller;

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
public class HabitacionController {
    private final HabitacionService habitacionService;
    private static final String MESSAGE_SUCCESS = "Habitaciones encontradas";

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

    @GetMapping("/sede/{idSede}/buscar-nombre")
    public ResponseEntity<ApiResponse> buscarHabitacionesPorFiltroFechasYHoraYNombreCiudad(@RequestParam String nombreHabitacion,
                                                                                           @PathVariable Long idSede) {
        if (nombreHabitacion == null || nombreHabitacion.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse("Debe ingresar un nombre para poder realizar el filtro", null));
        }

        if (idSede == null || idSede < 0) {
            return ResponseEntity.badRequest().body(new ApiResponse("Debe ingresar un id de sede y que sea mayor que 0 para poder realizar el filtro", null));
        }

        try {
            return ResponseEntity.ok(new ApiResponse(MESSAGE_SUCCESS, habitacionService.buscarHabitacionesPorNombreYIdSede(nombreHabitacion, idSede)));
        }catch (HabitacionNotFoundException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), null));
        }
    }
}
