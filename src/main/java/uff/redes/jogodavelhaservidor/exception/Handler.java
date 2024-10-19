package uff.redes.jogodavelhaservidor.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import uff.redes.jogodavelhaservidor.dto.ExceptionMessage;

@RestControllerAdvice
public class Handler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionMessage handleException(Exception ex){
        return new ExceptionMessage(ex.getMessage());
    }
}
