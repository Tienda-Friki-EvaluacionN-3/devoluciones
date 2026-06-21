package com.tiendafriki.devoluciones.dto;

import com.tiendafriki.devoluciones.Enum.MotivoDevolucion;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class DevolucionRequestDTO {

    @NotNull(message = "[ERROR] El ID del pedido es obligatorio [X_X]")
    @Positive(message = "[ERROR] El ID del pedido debe ser positivo [X_X]")
    private Integer pedidoId;

    @NotNull(message = "[ERROR] Debe indicar un motivo [X_X]")
    private MotivoDevolucion motivo;

}
