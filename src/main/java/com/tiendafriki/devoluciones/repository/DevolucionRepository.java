package com.tiendafriki.devoluciones.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tiendafriki.devoluciones.model.Devolucion;

public interface DevolucionRepository extends JpaRepository <Devolucion, Integer>{

    Optional<Devolucion> findByPedidoId(Integer pedidoId);

}
