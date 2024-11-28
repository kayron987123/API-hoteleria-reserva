package pe.com.hotel_api.hotel.presentation.controller;

import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.com.hotel_api.hotel.presentation.dto.ApiResponse;
import pe.com.hotel_api.hotel.presentation.dto.CrearReservaRequest;
import pe.com.hotel_api.hotel.service.interfaces.ReservaService;
import pe.com.hotel_api.hotel.service.interfaces.UsuarioService;

import java.io.IOException;

@RestController
@RequestMapping("/reservas")
@RequiredArgsConstructor
public class ReservaController {
    private final ReservaService reservaService;
    private final UsuarioService usuarioService;

    @PostMapping("/crear")
    public ResponseEntity<ApiResponse> crearReserva(@RequestBody CrearReservaRequest crearReservaRequest){
        var usuarioAutenticado = usuarioService.obtenerUsuarioAutenticado();
        try {
            var codeQr = reservaService.crearReserva(crearReservaRequest, usuarioAutenticado.email());
            return ResponseEntity.ok(new ApiResponse("Reserva creada con éxito", codeQr));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(), null));
        } catch (WriterException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), null));
        }
    }
}
