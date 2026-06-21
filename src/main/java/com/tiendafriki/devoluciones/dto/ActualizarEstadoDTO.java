package com.tiendafriki.devoluciones.dto;

import com.tiendafriki.devoluciones.Enum.EstadoDevolucion;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ActualizarEstadoDTO {

    @NotNull(message = "[ERROR] Debe indicar un estado")
    private EstadoDevolucion estado;

}
