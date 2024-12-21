package pe.com.hotel_api.hotel.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import pe.com.hotel_api.hotel.persistence.entity.Sede;
import pe.com.hotel_api.hotel.persistence.repository.SedeRepository;
import pe.com.hotel_api.hotel.presentation.advice.IllegalArgumentException;
import pe.com.hotel_api.hotel.presentation.advice.SedeNotFoundException;
import pe.com.hotel_api.hotel.presentation.dto.SedeDto;
import pe.com.hotel_api.hotel.service.interfaces.SedeService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SedeServiceImpl implements SedeService {
    private final SedeRepository sedeRepository;

    @Override
    @Cacheable(value = "listaSedesCache", key = "{#fechaEntrada, #fechaSalida, #nombreCiudad}")
    public List<SedeDto> buscarSedePorFiltroDeFechasYHoraYNombre(LocalDateTime fechaEntrada, LocalDateTime fechaSalida, String nombreCiudad) {
        List<Sede> sedeResponse = sedeRepository.buscarSedePorFechaYHoraYNombreDondeHabitacionesNoEstenReservadas(fechaEntrada, fechaSalida, nombreCiudad);
        if (sedeResponse.isEmpty()){
            throw new SedeNotFoundException("No se encontraron sedes con el nombre : " + nombreCiudad + " entre las fecha y hora indicadas: " + fechaEntrada + " - " + fechaSalida);
        }
        return devolverListaSedeDto(sedeResponse);
    }

    @Override
    public void validacionesSedeFechaYnombre(LocalDateTime fechaEntrada, LocalDateTime fechaSalida, String nombreCiudad) {
        if (!sedeRepository.existsByCiudadContainingIgnoreCase(nombreCiudad)) {
            throw new SedeNotFoundException("No se encontró la sede con el nombre: " + nombreCiudad);
        }
        if (fechaEntrada.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha de entrada debe ser futura.");
        }
        if (fechaSalida.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha de salida debe ser futura.");
        }
        if (fechaEntrada.isAfter(fechaSalida)) {
            throw new IllegalArgumentException("La fecha de entrada no puede ser posterior a la fecha de salida.");
        }
        if (nombreCiudad.isEmpty() || nombreCiudad.length() > 20) {
            throw new IllegalArgumentException("El nombre de la sede debe tener entre 1 y 20 caracteres.");
        }
    }

    private List<SedeDto> devolverListaSedeDto(List<Sede> sedes) {
        return sedes.stream()
                .map(sede -> new SedeDto(sede.getId(), sede.getNombre(), sede.getCiudad(), sede.getPais(), sede.getDireccion(), sede.getEstado().name()))
                .toList();
    }
}
