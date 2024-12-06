package pe.com.hotel_api.hotel.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.com.hotel_api.hotel.presentation.advice.IllegalArgumentException;
import pe.com.hotel_api.hotel.presentation.advice.SedeNotFoundException;
import pe.com.hotel_api.hotel.presentation.dto.ApiResponse;
import pe.com.hotel_api.hotel.service.interfaces.SedeService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RestController
@RequestMapping("/sedes")
@RequiredArgsConstructor
public class SedeController {
    private final SedeService sedeService;
    private static final String MESSAGE_SUCCESS = "Sede encontradas";

    @GetMapping("/filtrar/fecha-hora-nombre")
    public ResponseEntity<ApiResponse> buscarSedePorFiltroDeFechasYHoraYNombre(@RequestParam(required = false) LocalDateTime fechaEntrada,
                                                                               @RequestParam(required = false) LocalDateTime fechaSalida,
                                                                               @RequestParam(required = false) String nombreCiudad) {
        if (fechaEntrada == null || fechaSalida == null) {
            return ResponseEntity.badRequest().body(new ApiResponse("Los 2 filtros de fecha no deben estar vacías.", null));
        }
        if (nombreCiudad == null || nombreCiudad.isBlank()) {
            return ResponseEntity.badRequest().body(new ApiResponse("El nombre de la sede no debe estar vacío.", null));
        }

        try {
            sedeService.validacionesSedeFechaYnombre(fechaEntrada, fechaSalida, nombreCiudad);
            return ResponseEntity.ok(new ApiResponse(MESSAGE_SUCCESS, sedeService.buscarSedePorFiltroDeFechasYHoraYNombre(fechaEntrada, fechaSalida, nombreCiudad)));
        } catch (SedeNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), null));
        }

    }

    @GetMapping("/filtrar/fecha-nombre")
    public ResponseEntity<ApiResponse> buscarSedePorFiltroDeFechasYNombre(@RequestParam(required = false) LocalDate fechaEntrada,
                                                                          @RequestParam(required = false) LocalDate fechaSalida,
                                                                          @RequestParam(required = false) String nombreCiudad) {
        if (fechaEntrada == null || fechaSalida == null) {
            return ResponseEntity.badRequest().body(new ApiResponse("Los 2 filtros de fecha no deben estar vacías.", null));
        }
        if (nombreCiudad == null || nombreCiudad.isBlank()) {
            return ResponseEntity.badRequest().body(new ApiResponse("El nombre de la sede no debe estar vacío.", null));
        }
        try {
            LocalDateTime fechaEntradaNueva = fechaEntrada.atTime(LocalTime.MIDNIGHT);
            LocalDateTime fechaSalidaNueva = fechaSalida.atTime(LocalTime.MIDNIGHT);
            sedeService.validacionesSedeFechaYnombre(fechaEntradaNueva, fechaSalidaNueva, nombreCiudad);
            return ResponseEntity.ok(new ApiResponse(MESSAGE_SUCCESS, sedeService.buscarSedePorFiltroDeFechasYHoraYNombre(fechaEntradaNueva, fechaSalidaNueva, nombreCiudad)));
        } catch (SedeNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), null));
        }

    }
}
