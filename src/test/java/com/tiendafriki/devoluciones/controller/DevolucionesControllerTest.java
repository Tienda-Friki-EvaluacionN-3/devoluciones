package com.tiendafriki.devoluciones.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.tiendafriki.devoluciones.Enum.EstadoDevolucion;
import com.tiendafriki.devoluciones.Enum.MotivoDevolucion;

import com.tiendafriki.devoluciones.controller.DevolucionController;
import com.tiendafriki.devoluciones.dto.ActualizarEstadoDTO;
import com.tiendafriki.devoluciones.dto.DevolucionRequestDTO;
import com.tiendafriki.devoluciones.dto.DevolucionResponseDTO;
import com.tiendafriki.devoluciones.service.DevolucionService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;

import org.springframework.test.context.bean.override.mockito.MockitoBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DevolucionController.class)
class DevolucionesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DevolucionService service;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private DevolucionRequestDTO crearRequestDTO() {

        DevolucionRequestDTO dto = new DevolucionRequestDTO();

        dto.setPedidoId(1);
        dto.setMotivo(MotivoDevolucion.ERROR_FABRICA);

        return dto;
    }

    private DevolucionResponseDTO crearResponseDTO() {

        return new DevolucionResponseDTO(
                1,
                1,
                MotivoDevolucion.ERROR_FABRICA,
                EstadoDevolucion.PENDIENTE,
                LocalDate.now()
        );
    }

    private ActualizarEstadoDTO crearEstadoDTO() {

        ActualizarEstadoDTO dto = new ActualizarEstadoDTO();

        dto.setEstado(EstadoDevolucion.ACEPTADA);

        return dto;
    }

    @Test
    void listarDevoluciones() throws Exception {

        when(service.listar())
                .thenReturn(List.of(crearResponseDTO()));

        mockMvc.perform(get("/devoluciones/listar"))
                .andExpect(status().isOk());
    }

    @Test
    void buscarPorId() throws Exception {

        when(service.buscarPorId(1))
                .thenReturn(crearResponseDTO());

        mockMvc.perform(get("/devoluciones/buscarId/1"))
                .andExpect(status().isOk());
    }

    @Test
    void buscarPorIdPedido() throws Exception {

        when(service.buscarPorIdPedido(1))
                .thenReturn(crearResponseDTO());

        mockMvc.perform(get("/devoluciones/buscarPorIdPedido/1"))
                .andExpect(status().isOk());
    }

    @Test
    void crearDevolucion() throws Exception {

        when(service.crear(any()))
                .thenReturn("[+] Solicitud de devolución registrada correctamente");

        mockMvc.perform(post("/devoluciones/crear")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(crearRequestDTO())))
                .andExpect(status().isCreated());
    }

    @Test
    void actualizarEstado() throws Exception {

        when(service.actualizarEstado(any(), any()))
                .thenReturn("[+] Estado actualizado correctamente");

        mockMvc.perform(put("/devoluciones/estado/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(crearEstadoDTO())))
                .andExpect(status().isOk());
    }

    @Test
    void eliminarDevolucion() throws Exception {

        when(service.eliminar(1))
                .thenReturn("[+] Devolución eliminada correctamente");

        mockMvc.perform(delete("/devoluciones/eliminar/1"))
                .andExpect(status().isOk());
    }
}