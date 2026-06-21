package com.tiendafriki.devoluciones.dto;

import lombok.*;
import java.time.LocalDate;

import com.tiendafriki.devoluciones.Enum.EstadoDevolucion;
import com.tiendafriki.devoluciones.Enum.MotivoDevolucion;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class DevolucionResponseDTO {

    private Integer id;

    private Integer pedidoId;

    private MotivoDevolucion motivo;

    private EstadoDevolucion estado;

    private LocalDate fechaCreacion;

}
