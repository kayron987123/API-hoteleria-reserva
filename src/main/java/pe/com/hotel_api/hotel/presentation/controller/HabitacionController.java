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

    //check
    @GetMapping("/listar-por-sede/{idSede}")
    public ResponseEntity<ApiResponse> listarHabitacionesPorSede(@PathVariable Long idSede) {
        try {
            List<HabitacionDto> habitacionesResponse = habitacionService.listarHabitacionPorSede(idSede);
            return ResponseEntity.ok(new ApiResponse(MESSAGE_SUCCESS, habitacionesResponse));
        } catch (SedeNotFoundException | HabitacionNotFoundException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), null));
        }
    }

    //check
    @GetMapping("/sede/{idSede}/buscar-disponibles-por-fechas")
    public ResponseEntity<ApiResponse> buscarHabitacionesPorFecha(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaEntrada,
                                                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaSalida,
                                                                  @PathVariable Long idSede) {
        try {
            List<HabitacionDto> habitacionesResponse = habitacionService.buscarHabitacionPorFecha(fechaEntrada, fechaSalida, idSede);
            return ResponseEntity.ok(new ApiResponse(MESSAGE_SUCCESS, habitacionesResponse));
        } catch (SedeNotFoundException | HabitacionNotFoundException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/sede/{idSede}/buscar-por-nombre")
    public ResponseEntity<ApiResponse> buscarHabitacionPorNombre(@RequestParam String nombre,
                                                                 @PathVariable Long idSede) {
        try {
            List<HabitacionDto> habitacionesResponse = habitacionService.buscarHabitacionPorNombre(nombre, idSede);
            return ResponseEntity.ok(new ApiResponse(MESSAGE_SUCCESS, habitacionesResponse));
        } catch (SedeNotFoundException | HabitacionNotFoundException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/sede/{idSede}/buscar-por-tipo-cama")
    public ResponseEntity<ApiResponse> buscarHabitacionPorTipoCama(@RequestParam String cama,
                                                                   @PathVariable Long idSede) {
        try {
            List<HabitacionDto> habitacionesResponse = habitacionService.buscarHabitacionPorTipoCama(cama, idSede);
            return ResponseEntity.ok(new ApiResponse(MESSAGE_SUCCESS, habitacionesResponse));
        } catch (SedeNotFoundException | HabitacionNotFoundException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/sede/{idSede}/buscar-por-tipo-habitacion")
    public ResponseEntity<ApiResponse> buscarHabitacionPorTipoHabitacion(@RequestParam String habitacion,
                                                                         @PathVariable Long idSede) {
        try {
            List<HabitacionDto> habitacionesResponse = habitacionService.buscarHabitacionPorTipoHabitacion(habitacion, idSede);
            return ResponseEntity.ok(new ApiResponse(MESSAGE_SUCCESS, habitacionesResponse));
        } catch (SedeNotFoundException | HabitacionNotFoundException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), null));
        }
    }

    //check
    @GetMapping("/sede/{idSede}/buscar-por-precio")
    public ResponseEntity<ApiResponse> buscarHabitacionPorPrecio(@RequestParam BigDecimal minPrecio,
                                                                 @RequestParam(required = false, defaultValue = "1000") BigDecimal maxPrecio,
                                                                 @PathVariable Long idSede) {
        try {
            List<HabitacionDto> habitacionesResponse = habitacionService.buscarHabitacionPorRangoPrecio(minPrecio, maxPrecio, idSede);
            return ResponseEntity.ok(new ApiResponse(MESSAGE_SUCCESS, habitacionesResponse));
        } catch (SedeNotFoundException | HabitacionNotFoundException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), null));
        }
    }

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
}
