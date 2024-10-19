package uff.redes.tictactoeserver.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import uff.redes.tictactoeserver.dto.Session;
import uff.redes.tictactoeserver.service.SessionService;

@RestController
public class SessionController {
    private static final Logger log = LoggerFactory.getLogger(SessionController.class);
    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addNewSession(@RequestBody Session session) {
        log.info("Trying to start a new session: {}", session.id());
        sessionService.startSession(session);
        log.info("Player {} connected successfully! ✅", session.id());
    }
}
