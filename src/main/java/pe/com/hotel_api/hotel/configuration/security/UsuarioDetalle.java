package pe.com.hotel_api.hotel.configuration.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pe.com.hotel_api.hotel.persistence.entity.Usuario;
import pe.com.hotel_api.hotel.persistence.enums.RolUsuario;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UsuarioDetalle implements UserDetails {

    private Long id;
    private String email;
    private String contrasena;
    private RolUsuario rol;

    public static UsuarioDetalle buildUserDetails(Usuario usuario) {
        return new UsuarioDetalle(usuario.getId(), usuario.getEmail(), usuario.getContrasena(), usuario.getRol());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + rol.name()));
    }

    @Override
    public String getPassword() {
        return contrasena;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
