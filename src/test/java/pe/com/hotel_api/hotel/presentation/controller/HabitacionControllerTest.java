package pe.com.hotel_api.hotel.presentation.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import pe.com.hotel_api.hotel.persistence.enums.EstadoHabitacion;
import pe.com.hotel_api.hotel.presentation.advice.HabitacionNotFoundException;
import pe.com.hotel_api.hotel.presentation.advice.SedeNotFoundException;
import pe.com.hotel_api.hotel.presentation.dto.HabitacionDto;
import pe.com.hotel_api.hotel.service.interfaces.HabitacionService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HabitacionController.class)
class HabitacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HabitacionService habitacionService;

    private final List<HabitacionDto> habitacionesDto = new ArrayList<>();

    @BeforeEach
    void setUp() {
        habitacionesDto.add(new HabitacionDto(1L, "Habitacion 1", new BigDecimal(150), 1, EstadoHabitacion.DISPONIBLE, "a.jpg",
                null, null, null));
        habitacionesDto.add(new HabitacionDto(2L, "Habitacion 2", new BigDecimal(200), 1, EstadoHabitacion.DISPONIBLE, "a.jpg",
                null, null, null));
    }

    @Test
    @WithMockUser
    void buscarHabitaciones() throws Exception {
        when(habitacionService.buscarHabitaciones(any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(habitacionesDto);

        mockMvc.perform(get("/habitaciones/sede/1/buscar")
                .param("nombreHabitacion", "Habitacion")
                .param("tipoCama", "Cama")
                .param("tipoHabitacion", "Habitacion")
                .param("minPrecio", "100")
                .param("maxPrecio", "10000")
                .param("fechaEntrada", LocalDateTime.now().toString())
                .param("fechaSalida", LocalDateTime.now().plusDays(2).toString())
                .param("idSede", "1"))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").exists())
                .andExpect(jsonPath("$.data[1].id").exists())
                .andExpect(jsonPath("$.message").value("Habitaciones encontradas"));
    }

    @Test
    @WithMockUser
    void buscarHabitacionesFechasException() throws Exception {
        when(habitacionService.buscarHabitaciones(any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(habitacionesDto);

        mockMvc.perform(get("/habitaciones/sede/1/buscar")
                        .param("nombreHabitacion", "Habitacion")
                        .param("tipoCama", "Cama")
                        .param("tipoHabitacion", "Habitacion")
                        .param("minPrecio", "100")
                        .param("maxPrecio", "10000")
                        .param("fechaEntrada", LocalDateTime.now().toString())
                        .param("idSede", "1"))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Debe ingresar ambas fechas para poder realizar el filtro"))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    @WithMockUser
    void buscarHabitacionesSedeNotFoundException() throws Exception {
        when(habitacionService.buscarHabitaciones(any(), any(), any(), any(), any(), any(), any(), any()))
                .thenThrow(new SedeNotFoundException("Sede no encontrada"));

        mockMvc.perform(get("/habitaciones/sede/20/buscar")
                        .param("nombreHabitacion", "Habitacion")
                        .param("tipoCama", "Cama")
                        .param("tipoHabitacion", "Habitacion")
                        .param("minPrecio", "100")
                        .param("maxPrecio", "10000")
                        .param("fechaEntrada", LocalDateTime.now().toString())
                        .param("fechaSalida", LocalDateTime.now().plusDays(2).toString())
                        .param("idSede", "1"))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Sede no encontrada"))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    @WithMockUser
    void buscarHabitacionesHabitacionNotFoundException() throws Exception {
        when(habitacionService.buscarHabitaciones(any(), any(), any(), any(), any(), any(), any(), any()))
                .thenThrow(new HabitacionNotFoundException("Habitacion no encontrada"));

        mockMvc.perform(get("/habitaciones/sede/1/buscar")
                        .param("nombreHabitacion", "Haabiitaacioon")
                        .param("tipoCama", "Cama")
                        .param("tipoHabitacion", "Habitacion")
                        .param("minPrecio", "100")
                        .param("maxPrecio", "10000")
                        .param("fechaEntrada", LocalDateTime.now().toString())
                        .param("fechaSalida", LocalDateTime.now().plusDays(2).toString())
                        .param("idSede", "1"))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Habitacion no encontrada"))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    @WithMockUser
    void buscarHabitacionesPorFiltroFechasYHoraYNombreCiudad() throws Exception {
        when(habitacionService.buscarHabitacionesPorNombreYIdSede(any(), any()))
                .thenReturn(habitacionesDto);

        mockMvc.perform(get("/habitaciones/sede/1/buscar-nombre")
                        .param("nombreHabitacion", "Habitacion")
                        .param("idSede", "1"))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").exists())
                .andExpect(jsonPath("$.data[1].id").exists())
                .andExpect(jsonPath("$.message").value("Habitaciones encontradas"));
    }

    @Test
    @WithMockUser
    void buscarHabitacionesPorFiltroFechasYHoraYNombreCiudadNombreHabitacionException() throws Exception {
        when(habitacionService.buscarHabitacionesPorNombreYIdSede(any(), any()))
                .thenReturn(habitacionesDto);

        mockMvc.perform(get("/habitaciones/sede/1/buscar-nombre")
                        .param("nombreHabitacion", "")
                        .param("idSede", "1"))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Debe ingresar un nombre para poder realizar el filtro"))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    @WithMockUser
    void buscarHabitacionesPorFiltroFechasYHoraYNombreCiudadIdSedeNullOrLessThan0() throws Exception {
        when(habitacionService.buscarHabitacionesPorNombreYIdSede(any(), any()))
                .thenReturn(habitacionesDto);

        mockMvc.perform(get("/habitaciones/sede/0/buscar-nombre")
                        .param("nombreHabitacion", "Habitacion")
                        .param("idSede", "0"))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Debe ingresar un id de sede y que sea mayor que 0 para poder realizar el filtro"))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    @WithMockUser
    void buscarHabitacionesPorFiltroFechasYHoraYNombreCiudadSedeNotFoundException() throws Exception {
        when(habitacionService.buscarHabitacionesPorNombreYIdSede(any(), any()))
                .thenThrow(new SedeNotFoundException("Sede no encontrada"));

        mockMvc.perform(get("/habitaciones/sede/20/buscar-nombre")
                        .param("nombreHabitacion", "Habitacion")
                        .param("idSede", "20"))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Sede no encontrada"))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    @WithMockUser
    void buscarHabitacionesPorFiltroFechasYHoraYNombreCiudadHabitacionNotFoundException() throws Exception {
        when(habitacionService.buscarHabitacionesPorNombreYIdSede(any(), any()))
                .thenThrow(new HabitacionNotFoundException("Habitacion no encontrada"));

        mockMvc.perform(get("/habitaciones/sede/1/buscar-nombre")
                        .param("nombreHabitacion", "Pequena")
                        .param("idSede", "1"))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Habitacion no encontrada"))
                .andExpect(jsonPath("$.data").doesNotExist());
    }
}