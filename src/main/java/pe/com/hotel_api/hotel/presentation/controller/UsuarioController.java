package pe.com.hotel_api.hotel.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.com.hotel_api.hotel.presentation.dto.ApiResponse;
import pe.com.hotel_api.hotel.presentation.dto.CrearUsuarioRequest;
import pe.com.hotel_api.hotel.service.interfaces.UsuarioService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;

    @PostMapping("/crear")
    public ResponseEntity<ApiResponse> crearUsuario(@RequestBody CrearUsuarioRequest crearUsuarioRequest){
        try {
            return ResponseEntity.ok(new ApiResponse("Usuario creado correctamente", usuarioService.crearUsuario(crearUsuarioRequest)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), null));
        }
    }
}
