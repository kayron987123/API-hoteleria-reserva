package pe.com.hotel_api.hotel.presentation.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import pe.com.hotel_api.hotel.presentation.advice.IllegalArgumentException;
import pe.com.hotel_api.hotel.presentation.advice.SedeNotFoundException;
import pe.com.hotel_api.hotel.presentation.dto.SedeDto;
import pe.com.hotel_api.hotel.service.interfaces.SedeService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SedeController.class)
class SedeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SedeService sedeService;

    private final List<SedeDto> sedeList= new ArrayList<>();

    @BeforeEach
    void setUp() {
        sedeList.add(new SedeDto(1L, "Sede 1", "Lima", null, null, null));
        sedeList.add(new SedeDto(2L, "Sede 2", "Lima", null, null, null));
    }

    @Test
    @WithMockUser
    void buscarSedePorFiltroDeFechasYHoraYNombre() throws Exception {
        doNothing().when(sedeService).validacionesSedeFechaYnombre(any(), any(), anyString());
        when(sedeService.buscarSedePorFiltroDeFechasYHoraYNombre(any(), any(), anyString()))
                .thenReturn(sedeList);

        mockMvc.perform(get("/sedes/filtrar/fecha-hora-nombre")
                .param("fechaEntrada", "2021-08-01T00:00:00")
                .param("fechaSalida", "2021-08-02T00:00:00")
                .param("nombreCiudad", "Lima"))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").exists())
                .andExpect(jsonPath("$.data[1].id").exists())
                .andExpect(jsonPath("$.message").value("Sede encontradas"));
    }

    @Test
    @WithMockUser
    void buscarSedePorFiltroDeFechasYHoraYNombreFechasVaciasException() throws Exception {
        when(sedeService.buscarSedePorFiltroDeFechasYHoraYNombre(any(), any(), anyString()))
                .thenReturn(sedeList);

        mockMvc.perform(get("/sedes/filtrar/fecha-hora-nombre")
                        .param("fechaEntrada", "")
                        .param("fechaSalida", "")
                        .param("nombreCiudad", "Lima"))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Los 2 filtros de fecha no deben estar vacías."))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    @WithMockUser
    void buscarSedePorFiltroDeFechasYHoraYNombreNombreCiudadIsNullOrIsBlankException() throws Exception {
        when(sedeService.buscarSedePorFiltroDeFechasYHoraYNombre(any(), any(), anyString()))
                .thenReturn(sedeList);

        mockMvc.perform(get("/sedes/filtrar/fecha-hora-nombre")
                        .param("fechaEntrada", "2021-08-01T00:00:00")
                        .param("fechaSalida", "2021-08-02T00:00:00")
                        .param("nombreCiudad", ""))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("El nombre de la sede no debe estar vacío."))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    @WithMockUser
    void buscarSedePorFiltroDeFechasYHoraYNombreSedeNotFoundException() throws Exception {
        when(sedeService.buscarSedePorFiltroDeFechasYHoraYNombre(any(), any(), anyString()))
                .thenThrow(new SedeNotFoundException("Sede no encontrada"));

        mockMvc.perform(get("/sedes/filtrar/fecha-hora-nombre")
                        .param("fechaEntrada", "2021-08-01T00:00:00")
                        .param("fechaSalida", "2021-08-02T00:00:00")
                        .param("nombreCiudad", "Arequipa"))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Sede no encontrada"))
                .andExpect(jsonPath("$.data").doesNotExist());

    }

    @Test
    @WithMockUser
    void buscarSedePorFiltroDeFechasYHoraYNombreIllegalArgumentException() throws Exception {
        when(sedeService.buscarSedePorFiltroDeFechasYHoraYNombre(any(), any(), anyString()))
                .thenThrow(new IllegalArgumentException("Parametros no validos"));

        mockMvc.perform(get("/sedes/filtrar/fecha-hora-nombre")
                        .param("fechaEntrada", "2021-08-01T00:00:00")
                        .param("fechaSalida", "2021-08-02T00:00:00")
                        .param("nombreCiudad", "Arequipa"))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Parametros no validos"))
                .andExpect(jsonPath("$.data").doesNotExist());

    }

    @Test
    @WithMockUser
    void buscarSedePorFiltroDeFechasYNombre() throws Exception {
        doNothing().when(sedeService).validacionesSedeFechaYnombre(any(), any(), anyString());
        when(sedeService.buscarSedePorFiltroDeFechasYHoraYNombre(any(), any(), anyString()))
                .thenReturn(sedeList);

        mockMvc.perform(get("/sedes/filtrar/fecha-nombre")
                        .param("fechaEntrada", "2021-08-01")
                        .param("fechaSalida", "2021-08-02")
                        .param("nombreCiudad", "Lima"))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").exists())
                .andExpect(jsonPath("$.data[1].id").exists())
                .andExpect(jsonPath("$.message").value("Sede encontradas"));
    }

    //aaa

    @Test
    @WithMockUser
    void buscarSedePorFiltroDeFechasYNombreFechasVaciasException() throws Exception {
        when(sedeService.buscarSedePorFiltroDeFechasYHoraYNombre(any(), any(), anyString()))
                .thenReturn(sedeList);

        mockMvc.perform(get("/sedes/filtrar/fecha-nombre")
                        .param("fechaEntrada", "")
                        .param("fechaSalida", "")
                        .param("nombreCiudad", "Lima"))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Los 2 filtros de fecha no deben estar vacías."))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    @WithMockUser
    void buscarSedePorFiltroDeFechasYNombreNombreCiudadIsNullOrIsBlankException() throws Exception {
        when(sedeService.buscarSedePorFiltroDeFechasYHoraYNombre(any(), any(), anyString()))
                .thenReturn(sedeList);

        mockMvc.perform(get("/sedes/filtrar/fecha-nombre")
                        .param("fechaEntrada", "2021-08-01")
                        .param("fechaSalida", "2021-08-02")
                        .param("nombreCiudad", ""))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("El nombre de la sede no debe estar vacío."))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    @WithMockUser
    void buscarSedePorFiltroDeFechasYNombreSedeNotFoundException() throws Exception {
        when(sedeService.buscarSedePorFiltroDeFechasYHoraYNombre(any(), any(), anyString()))
                .thenThrow(new SedeNotFoundException("Sede no encontrada"));

        mockMvc.perform(get("/sedes/filtrar/fecha-nombre")
                        .param("fechaEntrada", "2021-08-01")
                        .param("fechaSalida", "2021-08-02")
                        .param("nombreCiudad", "Arequipa"))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Sede no encontrada"))
                .andExpect(jsonPath("$.data").doesNotExist());

    }

    @Test
    @WithMockUser
    void buscarSedePorFiltroDeFechasYNombreIllegalArgumentException() throws Exception {
        when(sedeService.buscarSedePorFiltroDeFechasYHoraYNombre(any(), any(), anyString()))
                .thenThrow(new IllegalArgumentException("Parametros no validos"));

        mockMvc.perform(get("/sedes/filtrar/fecha-nombre")
                        .param("fechaEntrada", "2021-08-01")
                        .param("fechaSalida", "2021-08-02")
                        .param("nombreCiudad", "Arequipa"))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Parametros no validos"))
                .andExpect(jsonPath("$.data").doesNotExist());

    }


}