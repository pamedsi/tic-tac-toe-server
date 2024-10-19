package uff.redes.tictactoeserver.exception;

import org.springframework.http.HttpStatus;

import java.io.Serial;

public class ServerException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 5980610244414521227L;
    private final HttpStatus status;

    public ServerException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
