package pe.com.hotel_api.hotel.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.com.hotel_api.hotel.configuration.security.JwtUtils;
import pe.com.hotel_api.hotel.configuration.security.UsuarioDetalle;
import pe.com.hotel_api.hotel.presentation.dto.ApiResponse;
import pe.com.hotel_api.hotel.presentation.dto.JwtResponse;
import pe.com.hotel_api.hotel.presentation.dto.LoginRequest;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> logueo(@RequestBody LoginRequest loginRequest){
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.contrasena()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateTokenForUser(authentication);
            UsuarioDetalle usuarioDetalle = (UsuarioDetalle) authentication.getPrincipal();
            JwtResponse jwtResponse = new JwtResponse(usuarioDetalle.getId(), jwt);
            return ResponseEntity.ok(new ApiResponse("Login exitoso", jwtResponse));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
