package com.tiendafriki.devoluciones.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tiendafriki.devoluciones.dto.*;
import com.tiendafriki.devoluciones.service.DevolucionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/devoluciones")
public class DevolucionController {

    private DevolucionService service;

    public DevolucionController(DevolucionService service){

        this.service = service;
    }

    @Operation(
        summary = "Listar devoluciones",
        description = "Obtiene una lista con todos las devoluciones registradas en el sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos o Error de logica de negocio")  ,
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })

    @GetMapping("/listar")
    public List<DevolucionResponseDTO> listar(){

        return service.listar();
    }

    @Operation(
        summary = "Buscar devolucion por id",
        description = "Obtiene una devolucion por su id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Devolucion obtenida correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos o Error de logica de negocio"),
            @ApiResponse(responseCode = "404", description = "Devolucion no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })

    @GetMapping("/buscarId/{id}")
    public DevolucionResponseDTO buscarPorId(
            @PathVariable Integer id) {

        return service.buscarPorId(id);
    }

    @Operation(
        summary = "Buscar devolucion por id de pedido",
        description = "Obtiene una devolucion por la id del pedido al que pertenece"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Devolucion obtenida correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos o Error de logica de negocio"),
            @ApiResponse(responseCode = "404", description = "Devolucion no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor o en la comunicación entre microservicios")
    })

    @GetMapping("/buscarPorIdPedido/{id}")
    public DevolucionResponseDTO buscarPorIdPedido(
            @PathVariable Integer id) {

        return service.buscarPorIdPedido(id);
    }

    @Operation(
        summary = "Registrar una devolucion",
        description = "Permite registrar una nueva devolucion en el sistema. Motivos: PRODUCTO_DANADO, PEDIDO_EQUIVOCADO, ERROR_FABRICA, PRODUCTO_INCOMPLETO"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Devolucion creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos o Error de lógica de negocio")  ,
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })

    @PostMapping("/crear")
    public ResponseEntity<String> crear(
            @Valid @RequestBody DevolucionRequestDTO dto){

        return ResponseEntity.status(201)
                .body(service.crear(dto));
    }

    @Operation(
        summary = "Actualizar estado de devolucion",
        description = "Permite actualizar el estado de una devolucion existente por medio de su id. Estados: PENDIENTE, ACEPTADA, RECHAZADA, EN_PROCESO, COMPLETADA"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos o Error de lógica de negocio")  ,
            @ApiResponse(responseCode = "404", description = "Devolucion no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })

    @PutMapping("/estado/{id}")
    public String actualizarEstado(
            @PathVariable Integer id,
            @Valid @RequestBody ActualizarEstadoDTO dto){

        return service.actualizarEstado(id, dto);
    }

    @Operation(
        summary = "Eliminar devolucion",
        description = "Permite eliminar una devolucion existente por medio de su id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Devolucion eliminada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos o Error de lógica de negocio")  ,
            @ApiResponse(responseCode = "404", description = "Devolucion no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })

    @DeleteMapping("/eliminar/{id}")
    public String eliminar(
            @PathVariable Integer id) {

        return service.eliminar(id);
    }

}