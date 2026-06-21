package com.tiendafriki.devoluciones.service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.tiendafriki.devoluciones.Enum.EstadoDevolucion;
import com.tiendafriki.devoluciones.dto.*;
import com.tiendafriki.devoluciones.model.Devolucion;
import com.tiendafriki.devoluciones.model.Pedido;
import com.tiendafriki.devoluciones.repository.DevolucionRepository;

@Service
public class DevolucionService {

    private DevolucionRepository repository;

    public DevolucionService(DevolucionRepository repository) {

        this.repository = repository;
    }

    private DevolucionResponseDTO convertirADTO(
            Devolucion devolucion){

        return new DevolucionResponseDTO(

                devolucion.getId(),
                devolucion.getPedidoId(),
                devolucion.getMotivo(),
                devolucion.getEstado(),
                devolucion.getFechaCreacion()

        );
    }

    public List<DevolucionResponseDTO> listar(){

        return repository.findAll()
                .stream()
                .map(this::convertirADTO)
                .toList();

    }

    public DevolucionResponseDTO buscarPorId(Integer id) {

        return repository.findById(id)

                .map(this::convertirADTO)

                .orElseThrow(() ->

                        new NoSuchElementException(
                                "[ERROR] Devolución no encontrada [X_X] ..."
                        )
                );
    }


    public DevolucionResponseDTO buscarPorIdPedido(Integer id){

        return repository.findByPedidoId(id)

                .map(this::convertirADTO)

                .orElseThrow(() ->

                        new NoSuchElementException(
                                "[ERROR] Devolución del pedido no encontrada [X_X] ..."
                        )
                );
    }


    public String crear(DevolucionRequestDTO dto){

        Optional<Devolucion> existente =
                repository.findByPedidoId(dto.getPedidoId());

        if(existente.isPresent()){

            throw new IllegalArgumentException(
                    "[ERROR] Ya existe una devolución para este pedido [X_X] ..."
            );
        }

        RestTemplate restTemplate = new RestTemplate();

        String url =
                "http://localhost:8085/pedidos/buscarId/"
                + dto.getPedidoId();

        try{

            Pedido pedido =
                    restTemplate.getForObject(
                            url,
                            Pedido.class
                    );

            if(pedido == null){

                throw new NoSuchElementException(
                        "[ERROR] El pedido no existe [X_X] ..."
                );
            }

            if(!pedido.getEstado()
                    .equalsIgnoreCase("PAGADO")){

                throw new IllegalArgumentException(
                        "[ERROR] Solo se pueden devolver pedidos PAGADOS [X_X] ..."
                );
            }

        }catch(NoSuchElementException | IllegalArgumentException e){

            throw e;

        }
        
        catch (HttpClientErrorException.NotFound e) {

            throw new IllegalArgumentException(
        "[ERROR] El pedido ingresado es invalido o no existe [X_X] ...");

        }    
        
        catch(Exception e){

            throw new RuntimeException(
                    "[ERROR] Error al consultar el microservicio pedidos [X_X] ..."
            );
        }

        Devolucion devolucion = new Devolucion();

        devolucion.setPedidoId(dto.getPedidoId());

        devolucion.setMotivo(dto.getMotivo());

        devolucion.setEstado(EstadoDevolucion.PENDIENTE);

        devolucion.setFechaCreacion(LocalDate.now());

        repository.save(devolucion);

        return "[+] Solicitud de devolución registrada correctamente";
    }

    public String actualizarEstado(
            Integer id,
            ActualizarEstadoDTO nuevoEstadoDTO){

        Optional<Devolucion> devolucionOpt =
                repository.findById(id);

        if(devolucionOpt.isEmpty()){

            throw new NoSuchElementException(
                    "[ERROR] Devolución no encontrada [X_X] ..."
            );
        }

        Devolucion devolucion =
                devolucionOpt.get();

        EstadoDevolucion estadoActual =
                devolucion.getEstado();

        EstadoDevolucion nuevoEstado =
                nuevoEstadoDTO.getEstado();

        if(estadoActual == EstadoDevolucion.PENDIENTE){

            if(nuevoEstado != EstadoDevolucion.ACEPTADA &&
               nuevoEstado != EstadoDevolucion.RECHAZADA){

                throw new IllegalArgumentException(
                        "[ERROR] Una devolución PENDIENTE solo puede pasar a ACEPTADA o RECHAZADA [X_X] ..."
                );
            }
        }


        if(estadoActual == EstadoDevolucion.ACEPTADA){

            if(nuevoEstado != EstadoDevolucion.EN_PROCESO){

                throw new IllegalArgumentException(
                        "[ERROR] Una devolución ACEPTADA solo puede pasar a EN_PROCESO [X_X] ..."
                );
            }
        }

        if(estadoActual == EstadoDevolucion.EN_PROCESO){

            if(nuevoEstado != EstadoDevolucion.COMPLETADA){

                throw new IllegalArgumentException(
                        "[ERROR] Una devolución EN_PROCESO solo puede pasar a COMPLETADA [X_X] ..."
                );
            }
        }

        if(estadoActual == EstadoDevolucion.RECHAZADA){

            if(nuevoEstado != EstadoDevolucion.ACEPTADA){

                throw new IllegalArgumentException(
                        "[ERROR] Una devolución RECHAZADA solo puede cambiar a ACEPTADA [X_X] ..."
                );
            }
        }

        if(estadoActual == EstadoDevolucion.COMPLETADA){

            throw new IllegalArgumentException(
                    "[ERROR] La devolución ya fue COMPLETADA [X_X] ..."
            );
        }

        devolucion.setEstado(nuevoEstado);

        repository.save(devolucion);

        if(nuevoEstado == EstadoDevolucion.COMPLETADA){

            try{

                String urlActualizar =

                        "http://localhost:8085/pedidos/"
                        + devolucion.getPedidoId()
                        + "/reembolsado";

                RestTemplate restTemplate =
                        new RestTemplate();

                restTemplate.put(urlActualizar, null);

            }catch(Exception e){

                throw new RuntimeException(
                        "[ERROR] No se pudo actualizar el pedido a REEMBOLSADO [X_X] ..."
                );
            }
        }

        return "[+] Estado actualizado correctamente";
    }

    public String eliminar(Integer id) {

        Optional<Devolucion> devolucionOpt =
                repository.findById(id);

        if(devolucionOpt.isEmpty()){

            throw new NoSuchElementException(
                    "[ERROR] Devolución No Encontrada [X_X] ..."
            );
        }

        repository.deleteById(id);

        return "[+] Devolución eliminada correctamente";
    }

}