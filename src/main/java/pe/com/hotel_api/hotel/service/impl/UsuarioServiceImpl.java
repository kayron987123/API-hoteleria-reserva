package pe.com.hotel_api.hotel.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pe.com.hotel_api.hotel.persistence.entity.Usuario;
import pe.com.hotel_api.hotel.persistence.enums.EstadoUsuario;
import pe.com.hotel_api.hotel.persistence.enums.RolUsuario;
import pe.com.hotel_api.hotel.persistence.repository.UsuarioRepository;
import pe.com.hotel_api.hotel.presentation.advice.AlreadyExistsException;
import pe.com.hotel_api.hotel.presentation.dto.CrearUsuarioImagenRequest;
import pe.com.hotel_api.hotel.presentation.dto.UsuarioDto;
import pe.com.hotel_api.hotel.service.interfaces.UsuarioService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {
    private static final String RUTA_ARCHIVO_IMAGENES = "src/main/resources/static/images/";
    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;

    private static final List<String> EXTENSIONES_VALIDAS_iMAGENES = Arrays.asList("jpg", "jpeg", "png");

    @Override
    public UsuarioDto obtenerUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Usuario usuarioEncontrado = usuarioRepository.findByEmail(email);
        return new UsuarioDto(usuarioEncontrado.getId(),
                usuarioEncontrado.getNombre(),
                usuarioEncontrado.getApellido(),
                usuarioEncontrado.getTelefono(),
                usuarioEncontrado.getEmail(),
                usuarioEncontrado.getFechaNacimiento(),
                usuarioEncontrado.getDepartamento(),
                usuarioEncontrado.getProvincia(),
                usuarioEncontrado.getDistrito(),
                usuarioEncontrado.getImageUrl());
    }

    @Override
    public UsuarioDto crearUsuarioImagen(CrearUsuarioImagenRequest crearUsuarioImagenRequest) {
        return Optional.of(crearUsuarioImagenRequest)
                .filter(user -> !usuarioRepository.existsByEmail(user.email()))
                .map(req -> {
                    Usuario usuario = new Usuario();
                    usuario.setNombre(req.nombre());
                    usuario.setApellido(req.apellido());
                    usuario.setTelefono(req.telefono());
                    usuario.setEmail(req.email());
                    usuario.setContrasena(passwordEncoder.encode(req.contrasena()));
                    usuario.setFechaNacimiento(req.fechaNacimiento());
                    usuario.setDni(req.dni());
                    usuario.setDepartamento(req.departamento());
                    usuario.setProvincia(req.provincia());
                    usuario.setDistrito(req.distrito());
                    usuario.setRol(RolUsuario.HUESPED);
                    usuario.setEstado(EstadoUsuario.ACTIVO);
                    usuario.setEmailVerificado(true);
                    usuario.setImageUrl(req.imageUrl());
                    Usuario usuarioGuardado = usuarioRepository.save(usuario);

                    return new UsuarioDto(usuarioGuardado.getId(),
                            usuarioGuardado.getNombre(),
                            usuarioGuardado.getApellido(),
                            usuarioGuardado.getTelefono(),
                            usuarioGuardado.getEmail(),
                            usuarioGuardado.getFechaNacimiento(),
                            usuarioGuardado.getDepartamento(),
                            usuarioGuardado.getProvincia(),
                            usuarioGuardado.getDistrito(),
                            usuarioGuardado.getImageUrl());
                }).orElseThrow(() -> new AlreadyExistsException(crearUsuarioImagenRequest.email() + "ya se encuentra registrado"));
    }

    @Override
    public String procesarImagen(MultipartFile imagen) throws IOException {
            String nombreImagen = imagen.getOriginalFilename();
            Path path = Paths.get(RUTA_ARCHIVO_IMAGENES + nombreImagen);
            Files.copy(imagen.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            return nombreImagen;

    }

    @Override
    public boolean tipoArchivo(String nombreImagen) {
        String extensionArchivo = nombreImagen.substring(nombreImagen.lastIndexOf(".")+1).toLowerCase();
        return EXTENSIONES_VALIDAS_iMAGENES.contains(extensionArchivo);
    }
}
