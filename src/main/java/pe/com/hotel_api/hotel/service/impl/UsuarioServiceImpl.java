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

        Usuario usuario = new Usuario();
        usuario.setNombre(capitalizarStringsCompleto(usuarioApiDniResponse.nombre()));
        usuario.setApellido(capitalizarStringsCompleto(usuarioApiDniResponse.apellido()));
        usuario.setTelefono(usuarioApiDniResponse.telefono());
        usuario.setEmail(usuarioApiDniResponse.email());
        usuario.setContrasena(passwordEncoder.encode(usuarioApiDniResponse.contrasena()));
        usuario.setFechaNacimiento(usuarioApiDniResponse.fechaNacimiento());
        usuario.setDni(usuarioApiDniResponse.dni());
        usuario.setDepartamento(capitalizarStringsCompleto(usuarioApiDniResponse.departamento()));
        usuario.setProvincia(capitalizarStringsCompleto(usuarioApiDniResponse.provincia()));
        usuario.setDistrito(capitalizarStringsCompleto(usuarioApiDniResponse.distrito()));
        usuario.setRol(RolUsuario.HUESPED);
        usuario.setEstado(EstadoUsuario.ACTIVO);
        usuario.setEmailVerificado(false);
        usuario.setImageUrl(usuarioApiDniResponse.imageUrl());
        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        return new UsuarioDto(usuarioGuardado.getId(),
                usuarioGuardado.getNombre(),
                usuarioGuardado.getApellido(),
                usuarioGuardado.getEmail());
    }

    @Transactional
    @Override
    public void correoVerificado(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email);
        usuario.setEmailVerificado(true);
        usuarioRepository.save(usuario);
    }

    private String capitalizarStringsCompleto(String texto) {
        if (texto == null || texto.isEmpty()) {
            return texto;
        }

        String[] palabras = texto.split(" ");

        StringBuilder resultado = new StringBuilder();
        for (String palabra : palabras) {
            if (!palabra.isEmpty()) {
                resultado.append(palabra.substring(0, 1).toUpperCase())
                        .append(palabra.substring(1).toLowerCase())
                        .append(" ");
            }
        }
        return resultado.toString().trim();
    }

    @Override
    public boolean existeUsuarioByEmail(String email) {
        if (usuarioRepository.existsByEmail(email)) {
            throw new AlreadyExistsException("El email" + email + " ya se encuentra registrado");
        }
        return false;
    }

    @Override
    public boolean existeUsuarioByDni(String dni) {
        if (usuarioRepository.existsByDni(dni)) {
            throw new AlreadyExistsException("El dni" + dni + " ya se encuentra registrado");
        }
        return false;
    }
}
