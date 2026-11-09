package io.github.polobernardoscarlos.fichaje.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ProblemDetail manejarNoEncontrado(RecursoNoEncontradoException ex) {
        log.warn(ex.getMessage());
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problema.setTitle("Recurso no encontrado");
        return problema;
    }


    @ExceptionHandler(ReglaNegocioException.class)
    public ProblemDetail manejarReglaNegocio(ReglaNegocioException ex) {
        log.warn(ex.getMessage());
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT,ex.getMessage());
        problema.setTitle("Conflicto reglas negocio");
        return problema;

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail manejarValidacion(MethodArgumentNotValidException ex) {
        log.warn("Hay campos con valores incorrectos", ex);
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Hay campos con valores incorrectos");
        problema.setTitle("Error de validacion");
        return problema;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail manejarGenerico(Exception ex) {
        log.error("Error inesperado", ex);
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Error inesperado");
        problema.setTitle("Error inesperado");
        return problema;

    }

}

