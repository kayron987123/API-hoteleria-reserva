package pe.com.hotel_api.hotel.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.com.hotel_api.hotel.persistence.entity.Usuario;
import pe.com.hotel_api.hotel.persistence.enums.EstadoUsuario;
import pe.com.hotel_api.hotel.persistence.enums.RolUsuario;
import pe.com.hotel_api.hotel.persistence.repository.UsuarioRepository;
import pe.com.hotel_api.hotel.presentation.advice.AlreadyExistsException;
import pe.com.hotel_api.hotel.presentation.dto.UsuarioApiDniResponse;
import pe.com.hotel_api.hotel.presentation.dto.UsuarioDto;
import pe.com.hotel_api.hotel.service.interfaces.UsuarioService;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {
    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;

    @Override
    public UsuarioDto obtenerUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Usuario usuarioEncontrado = usuarioRepository.findByEmail(email);
        return new UsuarioDto(usuarioEncontrado.getId(),
                usuarioEncontrado.getNombre(),
                usuarioEncontrado.getApellido(),
                usuarioEncontrado.getEmail());
    }

    @Transactional
    @Override
    public UsuarioDto crearUsuario(UsuarioApiDniResponse usuarioApiDniResponse) {
        return Optional.of(usuarioApiDniResponse)
                .filter(user -> !usuarioRepository.existsByEmail(user.email()))
                .map(req -> {
                    Usuario usuario = new Usuario();
                    usuario.setNombre(capitalizarStringsCompleto(req.nombre()));
                    usuario.setApellido(capitalizarStringsCompleto(req.apellido()));
                    usuario.setTelefono(req.telefono());
                    usuario.setEmail(req.email());
                    usuario.setContrasena(passwordEncoder.encode(req.contrasena()));
                    usuario.setFechaNacimiento(req.fechaNacimiento());
                    usuario.setDni(req.dni());
                    usuario.setDepartamento(capitalizarStringsCompleto(req.departamento()));
                    usuario.setProvincia(capitalizarStringsCompleto(req.provincia()));
                    usuario.setDistrito(capitalizarStringsCompleto(req.distrito()));
                    usuario.setRol(RolUsuario.HUESPED);
                    usuario.setEstado(EstadoUsuario.ACTIVO);
                    usuario.setEmailVerificado(false);
                    usuario.setImageUrl(req.imageUrl());
                    Usuario usuarioGuardado = usuarioRepository.save(usuario);

                    return new UsuarioDto(usuarioGuardado.getId(),
                            usuarioGuardado.getNombre(),
                            usuarioGuardado.getApellido(),
                            usuarioGuardado.getEmail());
                }).orElseThrow(() -> new AlreadyExistsException(usuarioApiDniResponse.email() + "ya se encuentra registrado"));
    }

    private String capitalizarStringsCompleto(String texto) {
        if (texto == null || texto.isEmpty()) {
            return texto;
        }

        // Dividir el texto en partes por los espacios
        String[] palabras = texto.split(" ");

        // Capitalizar cada palabra y luego unirlas
        StringBuilder resultado = new StringBuilder();
        for (String palabra : palabras) {
            if (!palabra.isEmpty()) {
                // Capitaliza la primera letra y pone el resto en minúsculas
                resultado.append(palabra.substring(0, 1).toUpperCase())
                        .append(palabra.substring(1).toLowerCase())
                        .append(" ");
            }
        }
        return resultado.toString().trim();
    }
}
