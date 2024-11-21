package pe.com.hotel_api.hotel.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pe.com.hotel_api.hotel.configuration.security.UsuarioDetalle;
import pe.com.hotel_api.hotel.persistence.entity.Usuario;
import pe.com.hotel_api.hotel.persistence.repository.UsuarioRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = Optional.ofNullable(usuarioRepository.findByEmail(email))
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        return UsuarioDetalle.buildUserDetails(usuario);
    }
}
