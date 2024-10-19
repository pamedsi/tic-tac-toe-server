package uff.redes.tictactoeserver.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import uff.redes.tictactoeserver.dto.ExceptionMessage;

@RestControllerAdvice
public class Handler {

    @ExceptionHandler
    public ResponseEntity<ExceptionMessage> handleException(ServerException ex){
        return ResponseEntity.status(ex.getStatus()).body(new ExceptionMessage(ex.getMessage()));
    }
}
