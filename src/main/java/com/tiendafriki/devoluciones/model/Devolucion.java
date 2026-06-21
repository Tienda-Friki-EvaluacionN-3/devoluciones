package com.tiendafriki.devoluciones.model;

import java.time.LocalDate;

import com.tiendafriki.devoluciones.Enum.EstadoDevolucion;
import com.tiendafriki.devoluciones.Enum.MotivoDevolucion;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "devoluciones")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Devolucion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private Integer pedidoId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MotivoDevolucion motivo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoDevolucion estado;

    private LocalDate fechaCreacion;

}
