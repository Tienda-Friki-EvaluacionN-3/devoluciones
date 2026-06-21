package com.tiendafriki.devoluciones;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.tiendafriki.devoluciones.dto.ErrorDTO;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ManejadorErrores {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> manejarErroresValidacion(

           MethodArgumentNotValidException ex, 
           HttpServletRequest request) { 

        Map<String, String> errores = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> { 

            errores.put(error.getField(), error.getDefaultMessage()); 

        });

        ErrorDTO errorDTO = new ErrorDTO(

            LocalDateTime.now(),                
            400,                        
            "Error de validación",     
            errores,                            
            request.getRequestURI()             

        );

        return ResponseEntity.badRequest().body(errorDTO);

    }


    @ExceptionHandler(
            org.springframework.http.converter.HttpMessageNotReadableException.class
    )
    public ResponseEntity<ErrorDTO> manejarErrorEnum(
            Exception ex,
            HttpServletRequest request
    ){

        ErrorDTO error = new ErrorDTO(

                LocalDateTime.now(),
                400,
                "[ERROR] Valores inválidos: "

                        + "Si desea actualizar estado, los valores son: "

                        + "PENDIENTE, ACEPTADA, RECHAZADA, "
                        + "EN_PROCESO, COMPLETADA. "

                        + "Si desea crear solicitud, debe ingresa id de pedido y motivo, los cuales son: "

                        + "PRODUCTO_DANADO, PEDIDO_EQUIVOCADO, "
                        + "ERROR_FABRICA, PRODUCTO_INCOMPLETO. ",
                null,
                request.getRequestURI()

        );

        return ResponseEntity.badRequest().body(error);
    }

    

    @ExceptionHandler(IllegalArgumentException.class)

    public ResponseEntity<ErrorDTO> ErrorSolicitud(
            IllegalArgumentException ex,
            HttpServletRequest request) {

        ErrorDTO error = new ErrorDTO(

                LocalDateTime.now(),

                400,

                ex.getMessage(),

                null,

                request.getRequestURI());

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorDTO> manejarErroresNoEncontrado(
            NoSuchElementException ex,
            HttpServletRequest request) {

        Map<String, String> errores = new HashMap<>();
        errores.put("error", ex.getMessage());

        ErrorDTO errorDTO = new ErrorDTO(
                LocalDateTime.now(),
                404,
                "[ERROR] Recurso No Encontrado [X_X]",
                errores,
                request.getRequestURI());

        return ResponseEntity.status(404).body(errorDTO);

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> manejarErroresGenerales(
            Exception ex,
            HttpServletRequest request) {

        ErrorDTO error = new ErrorDTO(

                LocalDateTime.now(),                               
                500,                                      
                "[-] Error interno del servidor [x_x]",  
                null,                                    
                request.getRequestURI()                          

        );

        return ResponseEntity.status(500).body(error);
    }

}
